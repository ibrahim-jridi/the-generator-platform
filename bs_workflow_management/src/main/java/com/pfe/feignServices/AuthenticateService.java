package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import com.pfe.dto.request.SignUpRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${bs-app.endpoint.gateway}"
    + "/api/v1/oauth", configuration = FeignClientConfig.class)

public interface AuthenticateService {

  @PostMapping("/register/by-email")
  ResponseEntity<?> signupByEmail(@RequestBody SignUpRequest request);

}
