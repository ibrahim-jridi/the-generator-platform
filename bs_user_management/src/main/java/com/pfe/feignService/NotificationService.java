package com.pfe.feignService;

import com.pfe.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-management", url = "${bs-app.endpoint.gateway-url:localhost:9999}"
    + "/notification-management/api/v1", configuration = FeignClientConfig.class)
public interface NotificationService {

  @PostMapping("/emails/email-verification")
  void sendEmailVerification(@RequestParam("email") String email,
      @RequestParam("username") String username,
      @RequestParam("activationLink") String activationLink,
      @RequestParam("password") String password);

  @PostMapping("/open-api/reset-password-request")
  void sendPasswordResetEmail(@RequestParam("to") String to,
      @RequestParam("username") String username, @RequestParam("resetLink") String resetLink);
}
