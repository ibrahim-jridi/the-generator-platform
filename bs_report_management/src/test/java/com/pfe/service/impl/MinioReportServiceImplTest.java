package com.pfe.service.impl;

import com.pfe.service.ReportTemplateService;
import com.pfe.service.dto.ReportTemplateDTO;
import com.pfe.validator.IReportValidator;
import io.minio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MinioReportServiceImplTest {

    @Spy
    @InjectMocks
    private MinioReportServiceImpl minioService;

    @Mock
    private MinioClient mockMinioClient;

    @Mock
    private ReportTemplateService reportTemplateService;

    @Mock
    private GetObjectResponse mockGetObjectResponse;

    @Mock
    private IReportValidator reportValidator;

    private static final String BUCKET_NAME = "report";
    private static final String FILE_NAME = "test-file.pdf";
    private static final String FILE_CONTENT = "Hello, MinIO!";
    private static final String TEMPLATE_TYPE = "report-template";

    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadLogoReport() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();
        doReturn("report").when(minioService).getReportBucketName();

        doReturn(mockMinioClient).when(minioService).getMinioClient();

        byte[] fileData = FILE_CONTENT.getBytes();
        when(mockFile.getBytes()).thenReturn(fileData);
        when(mockFile.getOriginalFilename()).thenReturn(FILE_NAME);
        when(mockFile.getContentType()).thenReturn("text/plain");

       minioService.uploadLogoReport(mockFile, FILE_NAME);

       verify(mockMinioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testUploadFile() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();
        doReturn("report").when(minioService).getReportBucketName();

        byte[] fileData = FILE_CONTENT.getBytes();
        String processName = "process1";
        String instanceId = "instance1";
        when(mockFile.getBytes()).thenReturn(fileData);
        when(mockFile.getOriginalFilename()).thenReturn(FILE_NAME);
        when(mockFile.getContentType()).thenReturn("text/plain");

        MinioClient.Builder mockBuilder = mock(MinioClient.Builder.class);
        when(mockBuilder.endpoint(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.credentials(anyString(), anyString())).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockMinioClient);

        when(mockMinioClient.bucketExists(any(BucketExistsArgs.class))).thenReturn(false);
        doNothing().when(mockMinioClient).makeBucket(any(MakeBucketArgs.class));
        doAnswer(invocation -> null).when(mockMinioClient).putObject(any(PutObjectArgs.class));

        minioService.uploadFile(processName, instanceId, fileData, FILE_NAME);

        verify(mockMinioClient, times(1)).putObject(any(PutObjectArgs.class));
    }


    @Test
    void testGetLogoReport() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();
        doReturn("report").when(minioService).getReportBucketName();
        String filename = "logo.png";
        byte[] expectedData = FILE_CONTENT.getBytes();


        InputStream inputStream = new ByteArrayInputStream(expectedData);
        when(mockGetObjectResponse.readAllBytes()).thenReturn(FILE_CONTENT.getBytes());
        when(mockMinioClient.getObject(any(GetObjectArgs.class))).thenReturn(mockGetObjectResponse);

        byte[] result = minioService.getLogoReport(filename);

        assertArrayEquals(expectedData, result);

        verify(mockMinioClient, times(1)).getObject(any(GetObjectArgs.class));
    }

    @Test
    void testUploadTemplate() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();
        doReturn("report").when(minioService).getReportBucketName();

        String type = "templateType";
        ReportTemplateDTO mockTemplate = new ReportTemplateDTO();
        mockTemplate.setType(type);
        mockTemplate.setPath(BUCKET_NAME + "/" + type);

        byte[] fileData = FILE_CONTENT.getBytes();
        when(mockFile.getBytes()).thenReturn(fileData);
        when(mockFile.getOriginalFilename()).thenReturn(FILE_NAME);
        when(mockFile.getContentType()).thenReturn("text/plain");


        MinioClient.Builder mockBuilder = mock(MinioClient.Builder.class);
        when(mockBuilder.endpoint(anyString())).thenReturn(mockBuilder);
        when(mockBuilder.credentials(anyString(), anyString())).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockMinioClient);

        when(reportTemplateService.save(any(ReportTemplateDTO.class))).thenReturn(mockTemplate);

        ReportTemplateDTO result = minioService.uploadTemplate(mockFile, type);

        assertNotNull(result);
        assertNotNull(result);
        assertEquals(type, result.getType());
        verify(reportTemplateService, times(1)).save(any(ReportTemplateDTO.class));
    }

    @Test
    void testUpdateTemplate() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();
        doReturn("report").when(minioService).getReportBucketName();

        String type = "updatedTemplate";
        UUID templateId = UUID.randomUUID();
        ReportTemplateDTO mockTemplate = new ReportTemplateDTO();
        mockTemplate.setType(type);
        mockTemplate.setPath(BUCKET_NAME + "/" + type);

        byte[] fileData = FILE_CONTENT.getBytes();
        when(mockFile.getBytes()).thenReturn(fileData);
        when(mockFile.getOriginalFilename()).thenReturn(FILE_NAME);
        when(mockFile.getContentType()).thenReturn("text/plain");

        when(reportTemplateService.findOne(templateId)).thenReturn(java.util.Optional.of(mockTemplate));
        when(reportTemplateService.save(any(ReportTemplateDTO.class))).thenReturn(mockTemplate);

        minioService.updateTemplate(mockFile, type, templateId);

        assertEquals(type, mockTemplate.getType());
        verify(reportTemplateService, times(1)).save(any(ReportTemplateDTO.class));
    }

    @Test
    void testDeleteTemplate() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();
        doReturn("report").when(minioService).getReportBucketName();

        UUID templateId = UUID.randomUUID();
        ReportTemplateDTO mockTemplate = new ReportTemplateDTO();
        mockTemplate.setType(TEMPLATE_TYPE);

        when(reportTemplateService.findOne(templateId)).thenReturn(java.util.Optional.of(mockTemplate));

        minioService.deleteTemplate(templateId);

        verify(mockMinioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
        verify(reportTemplateService, times(1)).delete(templateId);
    }

    @Test
    void testGetTemplateReportUrl() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();
        doReturn("report").when(minioService).getReportBucketName();

        String filename = "template.jrxml";
        String expectedUrl = "https://minio-url.com/" + filename;

        doReturn(mockMinioClient).when(minioService).getMinioClient();
        when(mockMinioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class))).thenReturn(expectedUrl);

        String result = minioService.getTemplateReportUrl(filename);

        assertEquals(expectedUrl, result);
    }

    @Test
    void testUploadPdfToMinio() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();
        doReturn("report").when(minioService).getReportBucketName();

        byte[] pdfData = "PDF Content".getBytes();
        String pdfFileName = "test-report.pdf";
        String bucketName = BUCKET_NAME;

        doReturn(mockMinioClient).when(minioService).getMinioClient();

        minioService.uploadPdfToMinio(pdfData, pdfFileName, bucketName);

        verify(mockMinioClient, times(1)).putObject(any(PutObjectArgs.class));
    }
}
