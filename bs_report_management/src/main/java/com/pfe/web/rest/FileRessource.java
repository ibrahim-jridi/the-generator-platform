package com.pfe.web.rest;

import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.MinioReportService;
import com.pfe.service.dto.ReportTemplateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/files")
public class FileRessource {

    private final Logger log = LoggerFactory.getLogger(FileRessource.class);

    private static final String ENTITY_NAME = "BSReportManagementFile";

    private final MinioReportService minioService;


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public FileRessource(MinioReportService minioService) {
        this.minioService = minioService;
    }

    @GetMapping("/logo-report-url")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<byte[]> getImageReportUrlMinio(@RequestParam("name") String name) {
        log.info("REST request to get logo report Url from minio : {}", name);
        try {
            byte[] imageData = minioService.getLogoReport(name);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", name);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    public byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("InputStream cannot be null");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }

    @PostMapping("/upload-logo-report")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Object> uploadLogoReport(@RequestBody MultipartFile file, @RequestParam("name") String name) {
        log.info("REST request to upload logo report Url : {}", name);
        try {
            minioService.uploadLogoReport(file, name);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error uploading file", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/upload-report")
    public ResponseEntity<ReportTemplateDTO> uploadJrxmlFile(@RequestParam("type") String type, @RequestBody MultipartFile file) throws Exception {
        ReportTemplateDTO reportTemplateDTO = minioService.uploadTemplate(file, type);
        return ResponseEntity
            .created(new URI("/api/v1/files/" + reportTemplateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportTemplateDTO.getId().toString()))
            .body(reportTemplateDTO);
    }

    @GetMapping("/report-url")
    public ResponseEntity<String> getTemplateReportUrl(@RequestParam("name") String name) throws Exception {
        log.info("REST request to get template report Url : {}", name);
        String url = minioService.getTemplateReportUrl(name + ".jrxml");
        if (url != null && !url.isEmpty()) {
            return new ResponseEntity<>(url, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(url, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/update-report")
    public ResponseEntity<String> updateJrxmlFile(@RequestParam("id") UUID id, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam("type") String type) {
        log.info("REST request to update Jrxml File : {}", id);
        try {
            minioService.updateTemplate(file, type, id);
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error uploading file", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReportTemplate(@PathVariable("id") UUID id) throws Exception {
        log.info("REST request to delete ReportTemplate : {}", id);
        minioService.deleteTemplate(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}






