package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import com.pfe.dto.FolderDto;
import com.pfe.dto.request.CreateFolderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "file-service", url = "${bs-app.endpoint.gateway}"
    + "/file-management/api/v1", configuration = FeignClientConfig.class)
public interface FileServices {

  @PostMapping("/folder/create")
  ResponseEntity<String> createFolder(@RequestBody CreateFolderRequest request);

  @GetMapping("/user")
  ResponseEntity<FolderDto> getFolderByUserID();

  @PostMapping("/createChildFolder")
  ResponseEntity<String> createFolderUnderParent(@RequestBody CreateFolderRequest request);
}
