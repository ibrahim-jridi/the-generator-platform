package com.pfe.service.impl;

import com.pfe.validator.IMinioValidator;
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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MinioServiceImplTest {

    @Spy
    @InjectMocks
    private MinioServiceImpl minioService;

    @Mock
    private MinioClient mockMinioClient;

    @Mock
    private GetObjectResponse mockGetObjectResponse;

    @Mock
    private IMinioValidator iMinioValidator;

    @Mock
    private MultipartFile mockFile;

    private static final String BUCKET_NAME = "report";
    private static final String FILE_NAME = "test-file.txt";
    private static final String FILE_CONTENT = "Hello, MinIO!";


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testUploadFileMinio() throws Exception {

        doReturn(mockMinioClient).when(minioService).getMinioClient();

        byte[] fileData = FILE_CONTENT.getBytes();
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

        minioService.uploadFileMinio(mockFile, "report");

        verify(mockMinioClient, times(1)).bucketExists(any(BucketExistsArgs.class));
        verify(mockMinioClient, times(1)).makeBucket(any(MakeBucketArgs.class));
        verify(mockMinioClient, times(1)).putObject(any(PutObjectArgs.class));
    }

    @Test
    void testGetFileFromMinio() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();

        InputStream fileInputStream = new ByteArrayInputStream(FILE_CONTENT.getBytes());

        when(mockGetObjectResponse.readAllBytes()).thenReturn(FILE_CONTENT.getBytes());
        when(mockMinioClient.getObject(any(GetObjectArgs.class))).thenReturn(mockGetObjectResponse);

        byte[] resultData = minioService.getFileFromMinio(FILE_NAME, BUCKET_NAME);

        verify(mockMinioClient, times(1)).getObject(any(GetObjectArgs.class));

        assertArrayEquals(FILE_CONTENT.getBytes(), resultData);
    }

    @Test
    void testGetFileUrl() throws Exception {
        doReturn(mockMinioClient).when(minioService).getMinioClient();

        String expectedUrl = "http://example.com/signed-url";
        when(mockMinioClient.getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class))).thenReturn(expectedUrl);

        String fileUrl = minioService.getFileUrl(FILE_NAME, BUCKET_NAME);

        verify(mockMinioClient, times(1)).getPresignedObjectUrl(any(GetPresignedObjectUrlArgs.class));

        assertEquals(expectedUrl, fileUrl);
    }
}
