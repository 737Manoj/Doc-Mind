# DocMind 🧠📄

DocMind is a robust, asynchronous document processing backend application built with **Spring Boot** and **Java 21**. It allows users to upload documents, validates them against allowed formats dynamically loaded from a database, stores the original documents, and parses their content into **Markdown** format asynchronously in the background.

---

## 🚀 Features

- **Document Upload API**: Simple and clean `multipart/form-data` file upload endpoint.
- **Dynamic File Validation**: Allowed file formats (extensions) are managed in a database (MySQL) and cached in-memory at application startup for high performance.
- **Asynchronous Processing**: File processing and parsing run non-blockingly using a dedicated Spring Boot asynchronous task executor (`ThreadPoolTaskExecutor`), allowing the API to respond immediately.
- **Extensible Parser Engine**:
  - **Text/Markdown (`.txt`, `.md`, `.markdown`)**: Parsed and extracted directly as Markdown.
  - **PDF (`.pdf`)**: Parsing placeholder ready for PDFBox or Apache Tika implementation.
  - **Word (`.docx`)**: Parsing placeholder ready for Apache POI implementation.
- **Secure Storage**: Files are saved on disk with uniquely generated UUIDs to prevent name collisions.
- **Database Tracking**: Relational mapping (JPA/Hibernate) stores processing states (`PENDING`, `PROCESSED`, `FAILED`), upload metadata, file size, content types, and timestamps.

---

## 🛠️ Technology Stack

- **Framework**: Spring Boot (v4.1.x)
- **Language**: Java 21
- **Database**: MySQL
- **ORM**: Spring Data JPA / Hibernate
- **Build Tool**: Maven
- **Utilities**: Lombok (for reducing boilerplate code)

---

## 📁 Project Structure

```text
DocMind/
├── src/main/java/com/docMind/
│   ├── DocMindApplication.java        # Main Spring Boot application entrypoint
│   ├── config/
│   │   └── AsyncConfig.java           # Custom ThreadPoolTaskExecutor for async processing
│   ├── controller/
│   │   └── UploadController.java      # REST Endpoints for handling document uploads
│   ├── dto/
│   │   └── UploadResponse.java        # Data transfer object for API responses
│   ├── model/
│   │   ├── AllowedExtension.java      # Database entity representing active permitted extensions
│   │   └── Document.java              # Database entity representing uploaded document details & states
│   ├── repository/
│   │   ├── AllowedExtensionRepository.java
│   │   └── DocumentRepository.java
│   └── service/
│       ├── DocProcessingService.java  # Interface for asynchronous document processing
│       ├── DocumentService.java       # Orchestrator interface for file validation & record creation
│       ├── ExtensionCacheService.java  # Service to load & cache active extensions in memory
│       ├── ParserService.java         # Interface for converting documents to Markdown
│       ├── StorageService.java        # Interface for disk reading/writing of uploads & markdowns
│       └── impl/
│           ├── DocProcessingServiceImpl.java
│           ├── DocumentServiceImpl.java
│           ├── ParserServiceImpl.java
│           └── StorageServiceImpl.java
└── src/main/resources/
    ├── application.properties         # Database configs, server port, upload directories
    ├── templates/
    └── static/
```

---

## ⚙️ Configuration

Configurations are defined in `src/main/resources/application.properties`:

- **Database Configuration**:
  By default, it uses MySQL on `localhost:3306` with the database `docmind`. Update the credentials accordingly:

  ```properties
  spring.datasource.url=jdbc:mysql://localhost:3306/docmind?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
  spring.datasource.username=root
  spring.datasource.password=your_password_here
  ```

- **File Upload & Storage Directories**:

  ```properties
  # Max limits
  spring.servlet.multipart.max-file-size=50MB
  spring.servlet.multipart.max-request-size=50MB

  # Custom storage paths
  app.upload.dir=data/uploads
  app.markdown.dir=data/markdown
  ```

---

## 🔌 API Endpoints

### 1. Upload Document

- **Endpoint**: `/api/upload`
- **Method**: `POST`
- **Content-Type**: `multipart/form-data`
- **Request Payload**:
  - `file`: (Multipart File binary)

#### Response Examples

- **Success (200 OK - Processing Started)**:

  ```json
  {
    "fileName": "example_doc.txt",
    "markdownPath": null,
    "message": "File received successfully. Processing started in background.",
    "success": true
  }
  ```

- **Unsupported Extension (200 OK / Bad Request depending on implementation)**:

  ```json
  {
    "fileName": "malicious_script.sh",
    "markdownPath": null,
    "message": "File type not allowed: .sh",
    "success": false
  }
  ```

- **Empty File (400 Bad Request)**:
  ```json
  {
    "fileName": null,
    "markdownPath": null,
    "message": "File is empty",
    "success": false
  }
  ```

---

## 🔄 Document Processing Pipeline

1. **Upload Request**: The client hits the `/api/upload` endpoint.
2. **File Validation**:
   - The file extension is extracted.
   - Checked against `ExtensionCacheService` (populated from `allowed_extensions` table where `active = true`).
3. **Save Original**: The file is stored inside the `data/uploads` directory under a randomly generated UUID filename.
4. **Persist DB Record**: A record is created in the `documents` database table with state set to `PENDING`.
5. **Immediate Response**: The server returns an immediate success response to the client.
6. **Async Parsing**:
   - The file is sent asynchronously to the custom task executor (`documentTaskExecutor`).
   - The `ParserService` processes the file into Markdown text.
   - The generated Markdown file is saved in `data/markdown/` named `<original_name>_<uuid>.md`.
   - The document's DB record status is updated to `PROCESSED` with a timestamp and the Markdown file name (or set to `FAILED` if parsing fails).

---

## 🛠️ Setting Up & Running the Application

### Prerequisites

- **Java**: JDK 21 or higher
- **Maven**: Installed locally (or use the included `./mvnw` wrapper)
- **Database**: MySQL running locally or on a server

### Steps to Run

1. **Database Setup**: Ensure MySQL is running, and configure your credentials in `application.properties`.
2. **Bootstrap Database Table**:
   Before uploading any file, make sure the `allowed_extensions` table is populated in your DB. You can insert sample allowed extensions:
   ```sql
   INSERT INTO allowed_extensions (extension, active) VALUES ('txt', true);
   INSERT INTO allowed_extensions (extension, active) VALUES ('md', true);
   INSERT INTO allowed_extensions (extension, active) VALUES ('markdown', true);
   INSERT INTO allowed_extensions (extension, active) VALUES ('pdf', true);
   INSERT INTO allowed_extensions (extension, active) VALUES ('docx', true);
   ```
3. **Run the Application**:
   Using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```
4. **Test uploading a file** using a tool like Postman or `curl`:
   ```bash
   curl -F "file=@sample.txt" http://localhost:8080/api/upload
   ```

---

## 🔮 Future Enhancements

- [ ] Implement full PDF parsing using Apache PDFBox/Tika.
- [ ] Implement full DOCX parsing using Apache POI.
- [ ] Add support for downloading processed markdown files via a dedicated endpoint.
- [ ] Create an admin UI to manage allowed extensions dynamically.
- [ ] Add user authentication and document ownership tracking.
