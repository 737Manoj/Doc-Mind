package com.docMind.model;

import jakarta.persistence.*;

@Entity
@Table(name = "allowed_extensions")
public class AllowedExtension {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 20)
  private String extension; // e.g. "pdf", "docx", "md" (without the dot)

  @Column(nullable = false)
  private boolean active = true; // so we can disable without deleting

  private String description; // optional: "PDF Document"

  // Constructors
  public AllowedExtension() {
  }

  public AllowedExtension(String extension, String description) {
    this.extension = extension;
    this.description = description;
    this.active = true;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
