package com.docMind.service;

import com.docMind.model.AllowedExtension;
import com.docMind.repository.AllowedExtensionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExtensionCacheService {

  private final AllowedExtensionRepository allowedExtensionRepository;

  // In-memory cache
  private final Set<String> allowedExtensions = new HashSet<>();

  public ExtensionCacheService(AllowedExtensionRepository allowedExtensionRepository) {
    this.allowedExtensionRepository = allowedExtensionRepository;
  }

  /**
   * Load all active extensions into memory when the application starts.
   */
  @PostConstruct
  public void loadExtensions() {
    List<AllowedExtension> extensions = allowedExtensionRepository.findByActiveTrue();
    allowedExtensions.clear();

    for (AllowedExtension ext : extensions) {
      allowedExtensions.add(ext.getExtension().toLowerCase());
    }

    System.out.println("Loaded allowed extensions: " + allowedExtensions);
  }

  /**
   * Check if an extension is allowed.
   */
  public boolean isAllowed(String extension) {
    if (extension == null)
      return false;
    return allowedExtensions.contains(extension.toLowerCase());
  }

  /**
   * Optional: refresh cache at runtime if needed later.
   */
  public void refresh() {
    loadExtensions();
  }
}
