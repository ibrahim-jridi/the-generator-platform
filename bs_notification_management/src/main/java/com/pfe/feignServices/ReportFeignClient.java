package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import com.pfe.service.dto.Base64FileDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "bsReportManagement", url = "${bs-app.endpoint.gateway-url}/report-management/api/v1", configuration = FeignClientConfig.class)
public interface ReportFeignClient {
  @PostMapping("/minio/upload-base64-file")
  ResponseEntity<String> uploadNotificationFile(@RequestBody Base64FileDTO notificationFileDto);

  @DeleteMapping("/minio/delete-file")
  void deleteOldNotificationFile(@RequestParam String fileName, @RequestParam String bucketName);
  @GetMapping("/minio/download-file")
  ResponseEntity<byte[]> getUploadedFile(@RequestParam("fileName") String fileName, @RequestParam("bucketName") String bucketName, @RequestHeader("Authorization") String authorization);

}
