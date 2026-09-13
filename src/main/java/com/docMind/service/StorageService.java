package com.docMind.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  String saveOriginalFile(MultipartFile file) throws Exception;

  String saveMarkdownFile(String originalFileName, String markdownContent) throws Exception;
}