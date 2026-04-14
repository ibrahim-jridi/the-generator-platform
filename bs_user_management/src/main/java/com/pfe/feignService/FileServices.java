package com.pfe.feignService;

import com.pfe.config.FeignClientConfig;
import com.pfe.service.dto.request.CreateFolderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "file-management", url = "${bs-app.endpoint.gateway-url:localhost:9999}"
    + "/file-management/api/v1", configuration = FeignClientConfig.class)
public interface FileServices {

  @PostMapping("/open-api/folder/create")
  ResponseEntity<String> createFolder(@RequestBody CreateFolderRequest request);
}
