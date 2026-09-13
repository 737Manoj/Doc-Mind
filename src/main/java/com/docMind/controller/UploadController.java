package com.docMind.controller;

import com.docMind.dto.UploadResponse;
import com.docMind.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class UploadController {

    private final DocumentService documentService;

    // Constructor injection
    public UploadController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadDocument(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new UploadResponse(null, null, "File is empty", false));
        }

        try {
            UploadResponse response = documentService.processDocument(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new UploadResponse(
                            file.getOriginalFilename(),
                            null,
                            "Failed to process file: " + e.getMessage(),
                            false));
        }
    }
}
