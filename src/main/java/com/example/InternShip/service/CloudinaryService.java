package com.example.InternShip.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.InternShip.dto.cloudinary.response.FileResponse;

public interface CloudinaryService {
    FileResponse uploadFile(MultipartFile file,String folder);
    FileResponse uploadFile_Month_allowance_report(byte[] fileBytes, String fileName, String folder);

}
