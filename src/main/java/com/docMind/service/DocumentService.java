package com.docMind.service;

import com.docMind.dto.UploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

  /**
   * Accepts the file, does basic validation + saves it,
   * then starts background processing.
   * Returns immediately to the user.
   */
    
  UploadResponse processDocument(MultipartFile file) throws Exception;
}
