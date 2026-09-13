package com.docMind.service.impl;

import com.docMind.dto.UploadResponse;
import com.docMind.model.Document;
import com.docMind.repository.DocumentRepository;
import com.docMind.service.DocProcessingService;
import com.docMind.service.DocumentService;
import com.docMind.service.ExtensionCacheService;
import com.docMind.service.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final StorageService storageService;
    private final DocumentRepository documentRepository;
    private final DocProcessingService docProcessingService;
    private final ExtensionCacheService extensionCacheService;

    public DocumentServiceImpl(StorageService storageService,
            DocumentRepository documentRepository,
            DocProcessingService docProcessingService,
            ExtensionCacheService extensionCacheService) {
        this.storageService = storageService;
        this.documentRepository = documentRepository;
        this.docProcessingService = docProcessingService;
        this.extensionCacheService = extensionCacheService;
    }

    @Override
    public UploadResponse processDocument(MultipartFile file) throws Exception {

        String originalFilename = file.getOriginalFilename();

        // Extract extension
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        // Validate extension using cache
        if (!extensionCacheService.isAllowed(extension)) {
            return new UploadResponse(
                    originalFilename,
                    null,
                    "File type not allowed: ." + extension,
                    false);
        }

        // 1. Save original file
        String storedFileName = storageService.saveOriginalFile(file);

        // 2. Create PENDING document record
        Document document = new Document();
        document.setOriginalFileName(originalFilename);
        document.setStoredFileName(storedFileName);
        document.setContentType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setStatus("PENDING");
        document.setUploadedAt(LocalDateTime.now());

        document = documentRepository.save(document);

        // 3. Trigger background processing
        docProcessingService.processDocumentAsync(document.getId(), file);

        // 4. Return immediate response
        return new UploadResponse(
                originalFilename,
                null,
                "File received successfully. Processing started in background.",
                true);
    }
}