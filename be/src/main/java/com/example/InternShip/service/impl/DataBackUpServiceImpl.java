package com.example.InternShip.service.impl;

import java.io.File;
import java.io.IOException;
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
    @Scheduled(cron = "0 0 17 * * ?", zone = "Asia/Ho_Chi_Minh")
    public void backupDatabase() throws IOException, InterruptedException {
        deleteOldBackups(7); // xóa backup cũ > 7 ngày

        String dbName = extractDbName(dbUrl);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm"));
        String backupFile = backupFolder + File.separator + "backup_" + timestamp + ".sql";

        // Tạo thư mục nếu chưa tồn tại
        File folder = new File(backupFolder);
        if (!folder.exists())
            folder.mkdirs();

        
        // Sử dụng ProcessBuilder để xử lý khoảng trắng trong đường dẫn
        ProcessBuilder pb = new ProcessBuilder(
                "mysqldump",
                "-u" + dbUser,
                "-p" + dbPass,
                dbName,
                "-r", backupFile);

        // Thêm environment nếu cần
        pb.redirectErrorStream(true); // gom stdout + stderr

        Process process = pb.start();
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            log.info("Backup created successfully: " + backupFile);
        } else {
            log.error("Could not create backup. Exit code: " + exitCode);
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
                    } else {
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
}
