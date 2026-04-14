package com.pfe.service;

import com.pfe.service.dto.Base64FileDto;
import org.springframework.web.multipart.MultipartFile;

public interface MinioService {

    void uploadFileMinio(MultipartFile file, String bucketName) throws Exception;

    byte[] getFileFromMinio(String fileName, String bucketName) throws Exception;

    String getFileUrl(String fileName, String bucketName) throws Exception;

    String uploadBase64File(Base64FileDto notificationFileDto) throws Exception;

    void deleteFile(String fileName, String bucketName) throws Exception;
}
