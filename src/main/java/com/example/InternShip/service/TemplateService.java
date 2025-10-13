package com.example.InternShip.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface TemplateService {
    void storeContractTemplate(MultipartFile file);
    Resource loadContractTemplate();
    Path getTemplatePath();
}