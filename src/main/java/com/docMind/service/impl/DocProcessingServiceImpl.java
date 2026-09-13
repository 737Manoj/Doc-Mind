package com.docMind.service.impl;

import com.docMind.model.Document;
import com.docMind.repository.DocumentRepository;
import com.docMind.service.DocProcessingService;
import com.docMind.service.ParserService;
import com.docMind.service.StorageService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class DocProcessingServiceImpl implements DocProcessingService {

  private final StorageService storageService;
  private final ParserService parserService;
  private final DocumentRepository documentRepository;

  public DocProcessingServiceImpl(StorageService storageService,
      ParserService parserService,
      DocumentRepository documentRepository) {
    this.storageService = storageService;
    this.parserService = parserService;
    this.documentRepository = documentRepository;
  }

  @Async("documentTaskExecutor")
  @Override
  public void processDocumentAsync(Long documentId, MultipartFile file) {
    try {
      // 1. Parse to Markdown
      String markdownContent = parserService.parseToMarkdown(file);

      // 2. Save Markdown file
      String markdownFileName = storageService.saveMarkdownFile(
          file.getOriginalFilename(), markdownContent);

      // 3. Update document in DB
      Document document = documentRepository.findById(documentId)
          .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));

      document.setMarkdownFileName(markdownFileName);
      document.setStatus("PROCESSED");
      document.setProcessedAt(LocalDateTime.now());

      documentRepository.save(document);

    } catch (Exception e) {
      // Mark as failed
      documentRepository.findById(documentId).ifPresent(doc -> {
        doc.setStatus("FAILED");
        documentRepository.save(doc);
      });
      e.printStackTrace();
    }
  }
}
