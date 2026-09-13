package com.docMind.service.impl;

import com.docMind.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageServiceImpl implements StorageService {

  @Value("${app.upload.dir}")
  private String uploadDir;

  @Value("${app.markdown.dir}")
  private String markdownDir;

  @Override
  public String saveOriginalFile(MultipartFile file) throws Exception {
    Path uploadPath = Paths.get(uploadDir);
    if (!Files.exists(uploadPath)) {
      Files.createDirectories(uploadPath);
    }

    String originalName = file.getOriginalFilename();
    String extension = "";

    if (originalName != null && originalName.contains(".")) {
      extension = originalName.substring(originalName.lastIndexOf("."));
    }

    String storedFileName = UUID.randomUUID() + extension;
    Path targetPath = uploadPath.resolve(storedFileName);

    Files.copy(file.getInputStream(), targetPath);

    return storedFileName;
  }

  @Override
  public String saveMarkdownFile(String originalFileName, String markdownContent) throws Exception {
    Path markdownPath = Paths.get(markdownDir);
    if (!Files.exists(markdownPath)) {
      Files.createDirectories(markdownPath);
    }

    // Create a clean name for the markdown file
    String baseName = originalFileName;
    if (baseName.contains(".")) {
      baseName = baseName.substring(0, baseName.lastIndexOf("."));
    }

    String markdownFileName = baseName + "_" + UUID.randomUUID() + ".md";
    Path targetPath = markdownPath.resolve(markdownFileName);

    Files.writeString(targetPath, markdownContent, StandardCharsets.UTF_8);

    return markdownFileName;
  }
}