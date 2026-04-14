package com.pfe.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

  @Value("${bs-app.minio.endpoint}")
  private String endpoint;

  @Value("${bs-app.minio.accessKey}")
  private String accessKey;

  @Value("${bs-app.minio.secretKey}")
  private String secretKey;

  @Bean
  public MinioClient minioClient() {
    return MinioClient.builder()
        .endpoint(this.endpoint)
        .credentials(this.accessKey, this.secretKey)
        .build();
  }

}
