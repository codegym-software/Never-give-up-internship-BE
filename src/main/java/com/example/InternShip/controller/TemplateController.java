package com.example.InternShip.controller;

import com.example.InternShip.dto.response.ApiResponse;
import com.example.InternShip.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping(consumes = "multipart/form-data")
    //@PreAuthorize("hasAuthority('SCOPE_HR')")
    public ResponseEntity<Void> uploadContractTemplate(@RequestParam("file") MultipartFile file) {
        templateService.storeContractTemplate(file);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Resource> downloadContractTemplate() throws IOException {
        Resource resource = templateService.loadContractTemplate();

        String contentType = Files.probeContentType(templateService.getTemplatePath());
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}