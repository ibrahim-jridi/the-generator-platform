package com.pfe.service.impl;

import com.pfe.service.MinioReportService;
import com.pfe.service.ReportTemplateService;
import com.pfe.service.dto.ReportTemplateDTO;
import com.pfe.validator.IReportValidator;
import io.minio.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class MinioReportServiceImpl implements MinioReportService {
    private static final Logger log = LoggerFactory.getLogger(MinioReportServiceImpl.class);
    @Value("${bs-app.minio.endpoint}")
    private String minioEndpoint;

    @Value("${bs-app.minio.accessKey}")
    private String minioAccessKey;

    @Value("${bs-app.minio.secretKey}")
    private String minioSecretKey;

    @Value("${bs-app.minio.report.bucket}")
    private String report;

    private final MinioClient minioClient;

    private final ReportTemplateService reportTemplateService;

    private final IReportValidator reportValidator;

    public MinioReportServiceImpl(ReportTemplateService reportTemplateService, IReportValidator reportValidator,MinioClient minioClient) {
        this.minioClient = minioClient;
        this.reportTemplateService = reportTemplateService;
        this.reportValidator = reportValidator;
    }

    public MinioClient getMinioClient() {
        return MinioClient.builder().endpoint(minioEndpoint).credentials(minioAccessKey, minioSecretKey).build();
    }

    private void createBucketIfNotExists(MinioClient minioClient, String bucketName) throws Exception {
        boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());

        if (!bucketExists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }


    @Override
    public void uploadLogoReport(MultipartFile file, String name) throws Exception {
        MinioClient minioClient = getMinioClient();
        byte[] data = file.getBytes();
        try (InputStream inputStream = new ByteArrayInputStream(data)) {
            createBucketIfNotExists(minioClient, getReportBucketName());
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(getReportBucketName())
                .object(name)
                .stream(inputStream, data.length, -1)
                .contentType(file.getContentType())
                .build());
        }
    }

    @Override
    public void uploadFile(String processName, String instanceId, byte[] file, String reportName) throws Exception {
        // Générer un nom unique pour le fichier PDF
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        String reportPdf = reportName + "_" + timestamp + ".pdf";

        // Construire le chemin complet de l'objet
        String objectName = String.format("%s/%s/%s", processName, instanceId, reportPdf);

        // Obtenir un client Minio
        MinioClient minioClient = getMinioClient();

        try (InputStream inputStream = new ByteArrayInputStream(file)) {
            // Créer le bucket si nécessaire
            createBucketIfNotExists(minioClient, getReportBucketName());

            // Envoyer le fichier au bucket
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(getReportBucketName())
                .object(objectName)
                .stream(inputStream, file.length, -1)
                .contentType(MediaType.APPLICATION_PDF_VALUE) // Utiliser APPLICATION_PDF_VALUE
                .build());
        }
    }

    @Override
    public byte[] getLogoReport(String filename) throws Exception {
        MinioClient minioClient = getMinioClient();
        try (InputStream stream = minioClient.getObject(GetObjectArgs.builder().bucket(getReportBucketName()).object(filename).build())) {
            return stream.readAllBytes();
        }
    }

    public String getReportBucketName() {
        return report;
    }

    public String uploadFileJrxmlMinio(MultipartFile file, String type) throws Exception {
        String newFileName = type + ".jrxml";

        MinioClient minioClient = getMinioClient();
        byte[] fileData = file.getBytes();

        String fileName = file.getOriginalFilename();

        try (InputStream inputStream = new ByteArrayInputStream(fileData)) {
            createBucketIfNotExists(minioClient, getReportBucketName());
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(getReportBucketName())
                .object(newFileName)
                .stream(inputStream, fileData.length, -1)
                .contentType(file.getContentType())
                .build());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    @Override
    public ReportTemplateDTO uploadTemplate(MultipartFile file, String type) throws Exception {
        this.reportValidator.beforeSave(file, type);

        ReportTemplateDTO reportTemplate = new ReportTemplateDTO();

        reportTemplate.setType(type);
        reportTemplate.setPath(getReportBucketName() + "/" + type);

        String fileName = this.uploadFileJrxmlMinio(file, type);

        return reportTemplateService.save(reportTemplate);
    }

    @Override
    public String getTemplateReportUrl(String filename) throws Exception {
        MinioClient minioClient = getMinioClient();

        // Générer une URL signée pour l'objet
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(getReportBucketName())
                .object(filename)
                .extraQueryParams(Map.of("response-content-type", "application/octet-stream"))
                .build()
        );
    }

    @Override
    public void updateTemplate(MultipartFile file, String type, UUID id) throws Exception {
        ReportTemplateDTO reportTemplate = reportTemplateService.findOne(id).orElseThrow();

        MinioClient minioClient = getMinioClient();
        if (file == null) {
            try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(getReportBucketName())
                    .object(reportTemplate.getType() + ".jrxml")
                    .build()
            )) {
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(getReportBucketName())
                        .object(type + ".jrxml")
                        .stream(inputStream, -1, 10485760)
                        .contentType("application/xml")
                        .build()
                );

                minioClient.removeObject(
                    RemoveObjectArgs.builder()
                        .bucket(getReportBucketName())
                        .object(reportTemplate.getType() + ".jrxml")
                        .build()
                );

            } catch (Exception e) {
                throw new Exception("Erreur lors du renommage du fichier", e);
            }
        } else {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(getReportBucketName())
                    .object(reportTemplate.getType() + ".jrxml")
                    .build()
            );
            String fileName = this.uploadFileJrxmlMinio(file, type);
        }

        reportTemplate.setType(type);
        reportTemplate.setPath(getReportBucketName() + "/" + type + ".jrxml");
        reportTemplateService.save(reportTemplate);
    }

    @Override
    public void deleteTemplate(UUID id) throws Exception {
        ReportTemplateDTO reportTemplate = reportTemplateService.findOne(id).orElseThrow();
        String objectName = reportTemplate.getType() + ".jrxml";
        MinioClient minioClient = getMinioClient();
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(getReportBucketName())
                .object(objectName)
                .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erreur lors de la suppression du fichier de Minio", e);
        }
        reportTemplateService.delete(id);
    }

    @Override
    public void uploadPdfToMinio(byte[] pdfBytes, String fileName, String bucketName) throws Exception {
        MinioClient minioClient = getMinioClient();
        try (InputStream inputStream = new ByteArrayInputStream(pdfBytes)) {
            createBucketIfNotExists(minioClient, bucketName);
            // Télécharge le fichier sur MinIO
            minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .stream(inputStream, pdfBytes.length, -1)
                .contentType("application/pdf")
                .build());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Erreur lors de l'envoi du fichier à MinIO", e);
        }
    }
}
