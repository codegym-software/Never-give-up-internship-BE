package com.example.InternShip.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.InternShip.dto.response.FileResponse;
import com.example.InternShip.entity.User;
import com.example.InternShip.exception.FileStorageException;
import com.example.InternShip.service.AuthService;
import com.example.InternShip.service.CloudinaryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryServiceImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    private final AuthService authService;

    private static final String ALLOWED_EXTENSIONS = "pdf"; // Hiện tại sẽ chỉ upload cv trước sau này sẽ hỗ trợ excel
                                                            // sau
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public FileResponse uploadFile(MultipartFile file,String folder) {
        User user = authService.getUserLogin();
        try {
            validateFile(file);

            // Tên thật của file
            String originalFilename = file.getOriginalFilename();

            // loại file
            String extension = getFileExtension(originalFilename);

            // Tạo tên file không có extension
            String fileName = removeEmailDomain(user.getEmail());

            // XÁC ĐỊNH RESOURCE TYPE
            String resourceType;
            if ("pdf".equalsIgnoreCase(extension)) {
                resourceType = "image"; // PDF upload như image theo docs
            } else {
                resourceType = "raw"; // Excel vẫn dùng raw
            }

            // Upload lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", resourceType,
                            "folder", folder,
                            "public_id", fileName, 
                            "overwrite", true, // Ghi đè nếu trùng
                            "unique_filename", false // Thêm random string (tungaris.pdf -> tungaris_456.pdf)
                    ));

            // Cấu hình nó thể đừng hỏi nó là cái gì        
            String publicId = (String) uploadResult.get("public_id");
            // Link xem pdf online (Cái này lưu vào csdl của bảng intern_program nhé)
            String secureUrl = (String) uploadResult.get("secure_url");

            // Hiện tại return để test chứ nên return cái secureUrl
            return FileResponse.builder()
                    .fileName(originalFilename) // Tên file gốc
                    .fileUrl(secureUrl) // Url xem online
                    .publicId(publicId) // Cak
                    .fileSize(file.getSize()) // Kích thước file
                    .fileType(file.getContentType()) // Loại file (pdf, xls, xlsx)
                    .uploadDate(LocalDateTime.now()) // Thời gian upload
                    .message("Upload thành công!")
                    .build();

        } catch (IOException e) {
            log.error("Error uploading file: ", e);
            throw new FileStorageException("Không thể upload file: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileStorageException("File trống!");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileStorageException("File quá lớn! Kích thước tối đa: 10MB");
        }

        String fileName = file.getOriginalFilename();
        String extension = getFileExtension(fileName);

        if (!ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new FileStorageException("Chỉ chấp nhận file PDF!");
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String removeEmailDomain(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        return email.substring(0, email.indexOf("@"));
    }
}