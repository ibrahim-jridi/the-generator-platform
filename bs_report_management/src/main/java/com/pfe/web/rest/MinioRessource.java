package com.pfe.web.rest;

import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.MinioService;
import com.pfe.service.dto.Base64FileDto;
import com.pfe.web.rest.errors.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/minio")
public class MinioRessource {

    private final Logger log = LoggerFactory.getLogger(MinioRessource.class);

    private static final String ENTITY_NAME = "BSReportManagementFile";

    private final MinioService minioService;


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public MinioRessource(MinioService minioService) {
        this.minioService = minioService;
    }


    @PostMapping("/upload-file")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""
        + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Object> uploadFile(@RequestPart("file") MultipartFile file,
        @RequestParam("bucketName") String bucketName) {
        this.log.info("REST request to upload file : {}", bucketName);
        try {
            this.minioService.uploadFileMinio(file, bucketName);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (ValidationException e) {
          throw e;
        } catch (Exception e) {
            log.error("Error uploading file", e);
            return new ResponseEntity<>("Error uploading file", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload-base64-file")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<String> uploadBase64File(@RequestBody Base64FileDto notificationFileDto) {
        try {
            return new ResponseEntity<>(this.minioService.uploadBase64File(notificationFileDto), HttpStatus.OK);
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error uploading file", e);
            return new ResponseEntity<>("Error uploading file", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<Void> deleteFile(@RequestParam("fileName") String fileName, @RequestParam("bucketName") String bucketName) throws Exception {
        this.log.info("REST request to delete  file : {}", fileName);
        this.minioService.deleteFile(fileName, bucketName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download-file")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<byte[]> getUploadedFile(@RequestParam("fileName") String fileName, @RequestParam("bucketName") String bucketName) {
        this.log.info("REST request to get file from minio : {}", fileName);
        try {
            byte[] fileData = this.minioService.getFileFromMinio(fileName, bucketName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", fileName);
            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/get-file-url")
    public ResponseEntity<String> getFileUrl(@RequestParam("fileName") String fileName, @RequestParam("bucketName") String bucketName) throws Exception {
        this.log.info("REST request to get  file Url : {}", fileName);
        String url = this.minioService.getFileUrl(fileName, bucketName);
        if (url != null && !url.isEmpty()) {
            return new ResponseEntity<>(url, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(url, HttpStatus.NOT_FOUND);
        }
    }

}






