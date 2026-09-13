package com.docMind.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocProcessingService {
  void processDocumentAsync(Long documentId, MultipartFile file);
}