package com.docMind.service;

import org.springframework.web.multipart.MultipartFile;

public interface ParserService {

  /**
   * Converts the uploaded file into clean UTF-8 Markdown.
   */
  String parseToMarkdown(MultipartFile file) throws Exception;
}
