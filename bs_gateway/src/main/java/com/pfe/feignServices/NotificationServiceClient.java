package com.pfe.feignServices;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "notification-management", url = "${endpoint.notification-management}"
    + "/notification-management/api/v1/open-api")
public interface NotificationServiceClient {

  @PostMapping("/emails/resetPassword")
  String sendPasswordResetEmail(@RequestParam("to") String to,
      @RequestParam("username") String username, @RequestParam("resetLink") String resetLink);

  @PostMapping("/emails/email-verification")
  void sendEmailVerification(@RequestParam("email") String email,
      @RequestParam("username") String username,
      @RequestParam("activationLink") String activationLink,
      @RequestParam("password") String password);


  @PostMapping("/emails/pre-signup")
  ResponseEntity<Map<String, String>> sendPreSignupEmail(@RequestParam String email,
      @RequestParam("username") String username, @RequestParam String activationLink,
      @RequestParam("password") String password);

}
