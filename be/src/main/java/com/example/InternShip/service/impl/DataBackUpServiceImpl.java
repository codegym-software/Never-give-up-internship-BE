package com.example.InternShip.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;

import com.example.InternShip.service.DataBackUpService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j(topic = "DATABASE_BACKUP")
public class DataBackUpServiceImpl implements DataBackUpService {

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${backup.folder}")
    private String backupFolder;

    // Chạy mỗi ngày lúc 2 giờ sáng
    @Override
    @Scheduled(cron = "0 48 18 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void backupDatabase() throws IOException, InterruptedException {
        deleteOldBackups(7); // xóa backup cũ > 7 ngày

        String dbName = extractDbName(dbUrl);
        String host = extractHost(dbUrl);
        String port = extractPort(dbUrl);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        String backupFile = backupFolder + File.separator + "backup_" + timestamp + ".sql";

        // Tạo thư mục nếu chưa tồn tại
        File folder = new File(backupFolder);
        if (!folder.exists())
            folder.mkdirs();

        // Sử dụng ProcessBuilder với các tham số tối ưu cho RDS
        ProcessBuilder pb = new ProcessBuilder(
                "mysqldump",
                "-u" + dbUser,
                "-p" + dbPass,
                "-h" + host,
                "-P" + port,
                "--single-transaction",      // Đảm bảo tính nhất quán (consistent backup)
                "--set-gtid-purged=OFF",     // Quan trọng cho RDS/GTID
                dbName,
                "-r", backupFile);

        pb.redirectErrorStream(true); 

        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append(System.lineSeparator());
            }
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info("Backup created successfully: " + backupFile);
        } else {
            log.error("Could not create backup. Exit code: " + exitCode);
            log.error("Mysqldump error details:\n" + output.toString());
        }
    }

    private void deleteOldBackups(int daysThreshold) {
        File folder = new File(backupFolder);
        if (!folder.exists() || !folder.isDirectory())
            return;

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".sql"));
        if (files == null)
            return;

        for (File file : files) {
            try {
                Path path = file.toPath();
                BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                long fileAgeDays = Duration.between(
                        attrs.creationTime().toInstant(),
                        java.time.Instant.now()).toDays();

                if (fileAgeDays > daysThreshold) {
                    if (file.delete()) {
                        log.info("Deleted old backup: " + file.getName());
                    }
                    else {
                        log.error("Failed to delete old backup: " + file.getName());
                    }
                }
            } catch (IOException e) {
                log.error("Error reading file attributes: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    // Utils
    private String extractDbName(String url) {
        return url.substring(url.lastIndexOf("/") + 1, url.contains("?") ? url.indexOf("?") : url.length());
    }

    private String extractHost(String url) {
        try {
            // jdbc:mysql://hostname:port/dbName
            String cleanUrl = url.replace("jdbc:mysql://", "");
            int slashIndex = cleanUrl.indexOf("/");
            String hostPort = cleanUrl.substring(0, slashIndex);
            if (hostPort.contains(":")) {
                return hostPort.split(":")[0];
            }
            return hostPort;
        } catch (Exception e) {
            log.warn("Could not extract host, defaulting to localhost");
            return "localhost";
        }
    }

    private String extractPort(String url) {
        try {
            String cleanUrl = url.replace("jdbc:mysql://", "");
            int slashIndex = cleanUrl.indexOf("/");
            String hostPort = cleanUrl.substring(0, slashIndex);
            if (hostPort.contains(":")) {
                return hostPort.split(":")[1];
            }
        } catch (Exception e) {
            log.warn("Could not extract port, defaulting to 3306");
        }
        return "3306";
    }
}
