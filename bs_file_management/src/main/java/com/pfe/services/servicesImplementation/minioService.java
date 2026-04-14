package com.pfe.services.servicesImplementation;

import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import java.nio.file.Path;
import org.springframework.stereotype.Service;

@Service
public class minioService {

  static MinioClient minioClient;


  public static StatObjectResponse getMetadata(Path path) throws Exception {
    try {
      StatObjectArgs args = StatObjectArgs.builder()
          .bucket("zzzzzzzzzzzzzzz")
          .object(path.toString())
          .build();
      return minioClient.statObject(args);
    } catch (Exception e) {
      throw new Exception("Error while fetching files in Minio", e);
    }
  }

  public static void remove(Path source) throws Exception {
    try {
      RemoveObjectArgs args = RemoveObjectArgs.builder()
          .bucket("zzzzzzzzzzzzzzz")
          .object(source.toString())
          .build();
      minioClient.removeObject(args);
    } catch (Exception e) {
      throw new Exception("Error while fetching files in Minio", e);
    }
  }
}
