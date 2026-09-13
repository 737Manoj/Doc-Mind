package com.docMind.dto;

public class UploadResponse {

    private String originalFileName;
    private String markdownFileName;
    private String message;
    private boolean success;

    // Constructors
    public UploadResponse() {
    }

    public UploadResponse(String originalFileName, String markdownFileName, String message, boolean success) {
        this.originalFileName = originalFileName;
        this.markdownFileName = markdownFileName;
        this.message = message;
        this.success = success;
    }

    // Getters and Setters
    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getMarkdownFileName() {
        return markdownFileName;
    }

    public void setMarkdownFileName(String markdownFileName) {
        this.markdownFileName = markdownFileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
