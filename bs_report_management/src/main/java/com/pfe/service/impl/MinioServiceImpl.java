package com.pfe.service.impl;

import com.pfe.service.MinioService;
import com.pfe.service.dto.Base64FileDto;
import com.pfe.validator.IMinioValidator;
import io.minio.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
@Transactional
public class MinioServiceImpl implements MinioService {

    private static final Logger log = LoggerFactory.getLogger(MinioServiceImpl.class);
    @Value("${bs-app.minio.endpoint}")
    private String minioEndpoint;

    @Value("${bs-app.minio.accessKey}")
    private String minioAccessKey;

    @Value("${bs-app.minio.secretKey}")
    private String minioSecretKey;


    @Value("${bs-app.minio.report.bucket}")
    private String report;

    private final IMinioValidator minioValidator;

    public MinioServiceImpl(IMinioValidator minioValidator) {
        this.minioValidator = minioValidator;
    }


    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(minioEndpoint).credentials(minioAccessKey, minioSecretKey).build();
    }

    private void createBucketIfNotExists(MinioClient minioClient, String bucketName) throws Exception {
        this.minioValidator.beforeSave(bucketName);
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    @Override
    public void uploadFileMinio(MultipartFile file, String bucketName) throws Exception {
        this.minioValidator.beforeSave(bucketName);
        MinioClient minioClient = getMinioClient();
        byte[] fileData = file.getBytes();

        String fileName = file.getOriginalFilename();

        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            createBucketIfNotExists(minioClient, bucketName);
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(inputStream, fileData.length, -1)
                .contentType(file.getContentType())
                .build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getFileFromMinio(String fileName, String bucketName) throws Exception {
        MinioClient minioClient = getMinioClient();
        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(fileName).build())) {
            return stream.readAllBytes();
        }
    }

    @Override
    public String getFileUrl(String fileName, String bucketName) throws Exception {
        MinioClient minioClient = getMinioClient();


        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucketName)
                .object(fileName)
                .extraQueryParams(Map.of("response-content-type", "application/octet-stream"))
                .build()
        );
    }

    @Override
    public String uploadBase64File(Base64FileDto notificationFileDto) throws Exception {
        MinioClient minioClient = getMinioClient();

        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder()
            .bucket(notificationFileDto.getBucketName())
            .build());

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                .bucket(notificationFileDto.getBucketName())
                .build());
        }

        try (InputStream inputStream = new ByteArrayInputStream(notificationFileDto.getFileBase64())) {
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(notificationFileDto.getBucketName())
                .object(notificationFileDto.getFileName())
                .stream(inputStream, notificationFileDto.getFileBase64().length, -1)
                .build());
        } catch (IOException e) {
            e.printStackTrace();
      }

        return notificationFileDto.getFileName();
    }

    @Override
    public void deleteFile(String fileName, String bucketName) throws Exception {
        MinioClient minioClient = getMinioClient();
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .build());
        } catch (Exception e) {
            throw new Exception("Failed to delete notification file", e);
        }
    }


}
