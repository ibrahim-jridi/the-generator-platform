package com.pfe.web.rest;

import com.pfe.IntegrationTest;
import com.pfe.service.MinioReportService;
import com.pfe.service.dto.ReportTemplateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
class FileRessourceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MinioReportService minioService;

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetImageReportUrlMinioSuccess() throws Exception {
        String name = "test-logo.png";
        byte[] imageData = "Fake Image Data".getBytes();

        when(minioService.getLogoReport(name)).thenReturn(imageData);

        mockMvc.perform(get("/api/v1/files/logo-report-url")
                .param("name", name))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetImageReportUrlMinioNotFound() throws Exception {
        String name = "nonexistent-logo.png";

        when(minioService.getLogoReport(name)).thenThrow(new RuntimeException("File not found"));

        mockMvc.perform(get("/api/v1/files/logo-report-url")
                .param("name", name))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testUploadLogoReportSuccess() throws Exception {
        String name = "test-logo.png";
        MockMultipartFile file = new MockMultipartFile(
            "file", "test-logo.png", MediaType.IMAGE_PNG_VALUE, "Fake Image Data".getBytes()
        );

        doNothing().when(minioService).uploadLogoReport(file, name);

        mockMvc.perform(multipart("/api/v1/files/upload-logo-report")
                .file(file)
                .param("name", name))
            .andExpect(status().isOk())
            .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testUploadLogoReportFailure() throws Exception {
        String name = "test-logo.png";
        MockMultipartFile file = new MockMultipartFile(
            "file", "test-logo.png", MediaType.IMAGE_PNG_VALUE, "Fake Image Data".getBytes()
        );

        doThrow(new RuntimeException("Error uploading file")).when(minioService).uploadLogoReport(file, name);

        mockMvc.perform(multipart("/api/v1/files/upload-logo-report")
                .file(file)
                .param("name", name))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Error uploading file"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testUploadJrxmlFileSuccess() throws Exception {
        String type = "report-type";
        MockMultipartFile file = new MockMultipartFile(
            "file", "template.jrxml", MediaType.TEXT_PLAIN_VALUE, "<jrxml></jrxml>".getBytes()
        );
        ReportTemplateDTO reportTemplateDTO = new ReportTemplateDTO();
        reportTemplateDTO.setId(UUID.randomUUID());

        when(minioService.uploadTemplate(file, type)).thenReturn(reportTemplateDTO);

        mockMvc.perform(multipart("/api/v1/files/upload-report")
                .file(file)
                .param("type", type))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/v1/files/" + reportTemplateDTO.getId()))
            .andExpect(content().json("{\"id\":\"" + reportTemplateDTO.getId() + "\"}"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testUpdateJrxmlFileSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        String type = "report-type";
        MockMultipartFile file = new MockMultipartFile(
            "file", "template.jrxml", MediaType.TEXT_PLAIN_VALUE, "<jrxml></jrxml>".getBytes()
        );

        doNothing().when(minioService).updateTemplate(file, type, id);

        mockMvc.perform(multipart("/api/v1/files/update-report")
                .file(file)
                .param("id", id.toString())
                .param("type", type))
            .andExpect(status().isOk())
            .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testDeleteReportTemplateSuccess() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(minioService).deleteTemplate(id);

        mockMvc.perform(delete("/api/v1/files/{id}", id))
            .andExpect(status().isNoContent());
    }
}
