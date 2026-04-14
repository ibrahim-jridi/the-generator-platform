package com.pfe.validator;


import io.minio.MinioClient;

public interface IMinioValidator extends IGenericValidator<MinioClient, MinioClient> {
    public void beforeSave(String bucketName);
}
