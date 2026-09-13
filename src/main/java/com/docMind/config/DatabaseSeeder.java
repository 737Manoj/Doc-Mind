package com.docMind.config;

import com.docMind.model.AllowedExtension;
import com.docMind.repository.AllowedExtensionRepository;
import com.docMind.service.ExtensionCacheService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeeder implements CommandLineRunner {

  private final AllowedExtensionRepository allowedExtensionRepository;
  private final ExtensionCacheService extensionCacheService;

  public DatabaseSeeder(AllowedExtensionRepository allowedExtensionRepository,
      ExtensionCacheService extensionCacheService) {
    this.allowedExtensionRepository = allowedExtensionRepository;
    this.extensionCacheService = extensionCacheService;
  }

  @Override
  public void run(String... args) throws Exception {
    if (allowedExtensionRepository.count() == 0) {
      // Seed the extensions supported by the application
      allowedExtensionRepository.save(new AllowedExtension("pdf", "PDF Document"));
      allowedExtensionRepository.save(new AllowedExtension("docx", "Word Document"));
      allowedExtensionRepository.save(new AllowedExtension("txt", "Text Document"));
      allowedExtensionRepository.save(new AllowedExtension("md", "Markdown Document"));
      allowedExtensionRepository.save(new AllowedExtension("markdown", "Markdown Document"));

      // Refresh the ExtensionCacheService memory cache immediately after seeding
      extensionCacheService.refresh();
      System.out.println("🌱 Database successfully seeded with supported allowed extensions and cache refreshed!");
    }
  }
}
