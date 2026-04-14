package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service", url = "${bs-app.endpoint.gateway}/user-management/api/v1", configuration = FeignClientConfig.class)
public interface UserService {
  @GetMapping("/open-api/getRootId")
  UUID getRootId();
}
