package com.docMind.service.impl;

import com.docMind.service.ParserService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@Service
public class ParserServiceImpl implements ParserService {

  @Override
  public String parseToMarkdown(MultipartFile file) throws Exception {
    String originalName = file.getOriginalFilename();
    String extension = getExtension(originalName).toLowerCase();

    return switch (extension) {
      case "md", "markdown", "txt" -> parseTextFile(file);
      case "pdf" -> parsePdf(file);
      case "docx" -> parseDocx(file);
      default -> throw new IllegalArgumentException("Unsupported file type: " + extension);
    };
  }

  private String parseTextFile(MultipartFile file) throws Exception {
    // Read as UTF-8 text
    return new String(file.getBytes(), StandardCharsets.UTF_8);
  }

  private String parsePdf(MultipartFile file) throws Exception {
    try (PDDocument document = PDDocument.load(file.getInputStream())) {
      PDFTextStripper pdfStripper = new PDFTextStripper();
      String text = pdfStripper.getText(document);

      StringBuilder sb = new StringBuilder();
      sb.append("# ").append(file.getOriginalFilename()).append("\n\n");
      sb.append(text);
      return sb.toString();
    }
  }

  private String parseDocx(MultipartFile file) throws Exception {
    try (XWPFDocument document = new XWPFDocument(file.getInputStream())) {
      StringBuilder sb = new StringBuilder();
      sb.append("# ").append(file.getOriginalFilename()).append("\n\n");

      for (XWPFParagraph paragraph : document.getParagraphs()) {
        String text = paragraph.getText();
        if (text == null || text.trim().isEmpty()) {
          continue;
        }
        sb.append(text).append("\n\n");
      }
      return sb.toString();
    }
  }

  private String getExtension(String filename) {
    if (filename == null || !filename.contains(".")) {
      return "";
    }
    return filename.substring(filename.lastIndexOf(".") + 1);
  }
}
