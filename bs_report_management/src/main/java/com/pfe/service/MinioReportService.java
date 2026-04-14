package com.pfe.service;

import com.pfe.service.dto.ReportTemplateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface MinioReportService {

    void uploadLogoReport(MultipartFile file, String name) throws Exception;

    void uploadFile(String processName, String instanceId, byte[] file, String reportName) throws Exception;

    byte[] getLogoReport(String filename) throws Exception;

    ReportTemplateDTO uploadTemplate(MultipartFile file, String type) throws Exception;

    String getTemplateReportUrl(String filename) throws Exception;

    void updateTemplate(MultipartFile file, String type, UUID id) throws Exception;

    void deleteTemplate(UUID id) throws Exception;

    void uploadPdfToMinio(byte[] pdfBytes, String fileName, String bucketName) throws Exception;
}
