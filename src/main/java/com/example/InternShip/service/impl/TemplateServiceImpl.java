package com.example.InternShip.service.impl;

import com.example.InternShip.exception.FileStorageException;
import com.example.InternShip.service.TemplateService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class TemplateServiceImpl implements TemplateService {

    @Value("${file.template-storage-path}")
    private String storageLocation;
    private Path rootLocation;
    private final String CONTRACT_TEMPLATE_FILENAME = "hop-dong-thuc-tap-mau.docx";

    @PostConstruct
    public void init() {
        try {
            this.rootLocation = Paths.get(storageLocation);
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new FileStorageException("Không thể khởi tạo thư mục lưu trữ", e);
        }
    }

    @Override
    public void storeContractTemplate(MultipartFile file) {
        try {
            validateDocxFile(file);
            // Xác định đường dẫn đầy đủ đến file
            Path destinationFile = this.rootLocation.resolve(CONTRACT_TEMPLATE_FILENAME).normalize().toAbsolutePath();

            // Ghi đè file cũ nếu tồn tại
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new FileStorageException("Lỗi khi lưu file.", e);
        }
    }

    @Override
    public Resource loadContractTemplate() {
        try {
            Path file = rootLocation.resolve(CONTRACT_TEMPLATE_FILENAME);
            Resource resource = new UrlResource(file.toUri());
            if (!resource.exists()) {
                throw new FileStorageException("Chưa có hợp đồng mẫu nào được tải lên.");
            }
            if (!resource.isReadable()) {
                throw new FileStorageException("Không thể đọc file: " + CONTRACT_TEMPLATE_FILENAME);
            }
            return resource;

        } catch (MalformedURLException e) {
            throw new FileStorageException("Lỗi URL không hợp lệ: " + e.getMessage(), e);
        }
    }

    @Override
    public Path getTemplatePath() {
        return rootLocation.resolve(CONTRACT_TEMPLATE_FILENAME);
    }

    private void validateDocxFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("Không thể lưu file rỗng.");
        }

        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName);

        // Chỉ chấp nhận "docx"
        if (!"docx".equalsIgnoreCase(extension)) {
            throw new FileStorageException("File không hợp lệ! Chỉ chấp nhận file có định dạng .docx");
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}