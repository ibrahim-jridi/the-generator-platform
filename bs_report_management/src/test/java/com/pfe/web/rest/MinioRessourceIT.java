package com.pfe.web.rest;

import com.pfe.IntegrationTest;
import com.pfe.service.MinioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IntegrationTest
@AutoConfigureMockMvc
class MinioRessourceIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MinioService minioService;

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testUploadFileSuccess() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
            "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()
        );

        doNothing().when(minioService).uploadFileMinio(any(), anyString());

        mockMvc.perform(multipart("/api/v1/minio/upload-file")
                .file(mockFile)
                .param("bucketName", "test-bucket"))
            .andExpect(status().isOk())
            .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testUploadFileFailure() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile(
            "file", "test.txt", MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()
        );

        doThrow(new RuntimeException("Error")).when(minioService).uploadFileMinio(any(), anyString());

        mockMvc.perform(multipart("/api/v1/minio/upload-file")
                .file(mockFile)
                .param("bucketName", "test-bucket"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Error uploading file"));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testDownloadFileSuccess() throws Exception {
        String fileName = "test.txt";
        String bucketName = "test-bucket";
        byte[] fileData = "Hello World".getBytes();

        when(minioService.getFileFromMinio(fileName, bucketName)).thenReturn(fileData);

        mockMvc.perform(get("/api/v1/minio/download-file")
                .param("fileName", fileName)
                .param("bucketName", bucketName))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM))
            .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"" + fileName + "\""))
            .andExpect(content().bytes(fileData));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testDownloadFileNotFound() throws Exception {
        String fileName = "nonexistent.txt";
        String bucketName = "test-bucket";

        doThrow(new RuntimeException("File not found")).when(minioService).getFileFromMinio(fileName, bucketName);

        mockMvc.perform(get("/api/v1/minio/download-file")
                .param("fileName", fileName)
                .param("bucketName", bucketName))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetFileUrlSuccess() throws Exception {
        String fileName = "test.txt";
        String bucketName = "test-bucket";
        String fileUrl = "http://minio.example.com/test.txt";

        when(minioService.getFileUrl(fileName, bucketName)).thenReturn(fileUrl);

        mockMvc.perform(get("/api/v1/minio/get-file-url")
                .param("fileName", fileName)
                .param("bucketName", bucketName))
            .andExpect(status().isOk())
            .andExpect(content().string(fileUrl));
    }

    @Test
    @Transactional
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetFileUrlNotFound() throws Exception {
        String fileName = "nonexistent.txt";
        String bucketName = "test-bucket";

        when(minioService.getFileUrl(fileName, bucketName)).thenReturn("");

        mockMvc.perform(get("/api/v1/minio/get-file-url")
                .param("fileName", fileName)
                .param("bucketName", bucketName))
            .andExpect(status().isNotFound());
    }
}
