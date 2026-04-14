package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import com.pfe.service.dto.TaskDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "workflow-service", url = "${bs-app.endpoint.gateway-url}"
    + "/workflow-management/api/v1/camunda", configuration = FeignClientConfig.class)
public interface WorkflowFeignClient {
    @GetMapping("/current-task/by-id/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable String id, @RequestHeader("Authorization") String authorization);

}
