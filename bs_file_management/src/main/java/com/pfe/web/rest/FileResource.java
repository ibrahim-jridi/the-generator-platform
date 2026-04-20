package com.pfe.web.rest;

import com.pfe.dto.FileDto;
import com.pfe.mappers.FileMapper;
import com.pfe.repository.FileRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.services.FileService;
import com.pfe.domain.File;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for managing {@link File}.
 */
@RestController
@RequestMapping("/api/v1/minio/file")
@Transactional
public class FileResource {

  private static final Logger log = LoggerFactory.getLogger(FileResource.class);


  private final FileService fileService;
  private final FileRepository fileRepository;
  private final FileMapper fileMapper;

  public FileResource(FileService fileService, FileRepository fileRepository,
      FileMapper fileMapper) {
    this.fileService = fileService;
    this.fileRepository = fileRepository;
    this.fileMapper = fileMapper;
  }

  /**
   * {@code POST /upload} : Upload a file.
   *
   * @param file the file to upload.
   * @param path the path to upload the file to.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the uploaded
   * file.
   * @throws Exception if an error occurs during file upload.
   */
  @PostMapping("/upload")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<FileDto> uploadFile(@RequestParam("file") MultipartFile file,
      @RequestParam("path") String path) {
    if (path == null || path.trim().isEmpty()) {
      throw new IllegalArgumentException("path cannot be null or empty:" + path);
    }
    FileDto fileSaved = this.fileService.uploadFile(file, path);
    return ResponseEntity.ok(fileSaved);
  }
  @PostMapping("/upload-from-report")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<FileDto> uploadFileFromReport(@RequestPart("file") MultipartFile file) {
      FileDto fileSaved = this.fileService.uploadFileFromReport(file);
      return ResponseEntity.ok(fileSaved);
  }

  /**
   * {@code POST /uploadMultiple} : Upload multiple files.
   *
   * @param files the files to upload.
   * @param path  the path to upload the files to.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the list of
   * uploaded files.
   * @throws Exception if an error occurs during files upload.
   */
  @PostMapping("/uploadMultiple")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<FileDto>> uploadFiles(@RequestParam("files") List<MultipartFile> files,
      @RequestParam("path") String path) throws Exception {
    try {
      if (path == null || path.trim().isEmpty()) {
        throw new IllegalArgumentException("path cannot be null or empty:" + path);

      }
      List<FileDto> fileSaved = this.fileService.uploadFiles(files, path);
      return ResponseEntity.ok(fileSaved);
    } catch (Exception e) {
      log.error("Error uploading the files", e);
      throw e;
    }
  }

  /**
   * {@code GET /{fileName}} : Get a file by its name.
   *
   * @param fileName the name of the file to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the file content, or
   * {@code 404 (Not Found)} if not found.
   */
  @GetMapping("/{fileName}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<InputStreamResource> getFile(@PathVariable String fileName) {
    try {
      InputStream file = this.fileService.getFile(fileName);
      String contentType = determineContentType(fileName);

      return ResponseEntity.ok()
          .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
          .contentType(MediaType.parseMediaType(contentType))
          .body(new InputStreamResource(file));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
  }

  private String determineContentType(String fileName) {
    String contentType = URLConnection.guessContentTypeFromName(fileName);
    if (contentType == null) {
      contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE; // Default to binary stream if type is unknown
    }
    return contentType;
  }


  /**
   * {@code GET /} : Get all files.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of files.
   */
  @GetMapping("")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<FileDto>> getFiles() {
    try {
      List<FileDto> fileList = this.fileService.getFiles();
      return ResponseEntity.ok(fileList);
    } catch (Exception e) {
      return ResponseEntity.status(500).body(null);
    }
  }

  /**
   * {@code GET /download/{fileName}} : Download a file by its name.
   *
   * @param fileName the name of the file to download.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} or
   * {@code 500 (Internal Server Error)} if download fails.
   */
  @GetMapping("/download/{fileName}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<String> downloadFile(@PathVariable String fileName) {
    try {
      this.fileService.downloadFile(fileName);
      return ResponseEntity.ok("File converted successfully to base64-encoded ");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("File download failed.");
    }
  }

  /**
   * {@code DELETE /delete/{fileName}} : Delete a file by its name.
   *
   * @param fileName the name of the file to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)} if deletion is
   * successful.
   */
  @DeleteMapping("/delete/{fileName}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<String> removeFile(@PathVariable UUID fileName) {
    try {
      this.fileService.removeFile(fileName);
      return ResponseEntity.ok("File removed successfully.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("File removal failed.");
    }
  }

  /**
   * {@code POST /rename} : Rename a file.
   *
   * @param fileId  the ID of the file to rename.
   * @param newName the new name for the file.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} if successful.
   */
  @PostMapping("/rename")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<String> renameFile(@RequestParam UUID fileId,
      @RequestParam String newName) {
    try {
      this.fileService.renameFile(fileId, newName);
      return ResponseEntity.ok("File renamed successfully.");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("File rename failed: " + e.getMessage());
    }
  }

  @GetMapping("/{fileId}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public FileDto getFileById(@PathVariable UUID fileId) {
      FileDto filedto = this.fileService.getFileById(fileId);
      return filedto;
  }
  /**
   * {@code GET /folder/{folderId}} : Get all files in a folder.
   */
  @GetMapping("/folder/{folderId}")
  public ResponseEntity<List<FileDto>> getFilesByFolderId(@PathVariable UUID folderId) {
    try {
      List<FileDto> files = this.fileService.getFilesByFolderId(folderId);
      return ResponseEntity.ok(files);
    } catch (Exception e) {
      log.error("Error getting files by folder ID", e);
      return ResponseEntity.status(500).body(null);
    }
  }
}
