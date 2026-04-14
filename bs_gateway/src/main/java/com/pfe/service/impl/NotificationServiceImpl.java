package com.pfe.service.impl;

import com.pfe.feignServices.NotificationServiceClient;
import com.pfe.security.jwt.JwtTokenUtil;
import com.pfe.service.NotificationService;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);
  private final JwtTokenUtil jwtTokenUtil;
  private final NotificationServiceClient notificationServiceClient;

  @Value(value = "${bs.front-url}")
  private String frontUrl;

  public NotificationServiceImpl(JwtTokenUtil jwtTokenUtil,
      NotificationServiceClient notificationServiceClient) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.notificationServiceClient = notificationServiceClient;
  }

  @Override
  public String requestPasswordResetToNotification(String userEmail, String username, UUID userId) {
    String result = null;
    try {
      String resetLink = this.frontUrl + "/auth/forgot-password?token=" + this.generateToken(userId);
      result = this.notificationServiceClient.sendPasswordResetEmail(userEmail, username, resetLink);
    } catch (Exception e) {
      log.debug("EXCEPTION WHILE SENDING EMAIL !");
      result = null;
    }
    return result;
  }

  @Override
  public String sendEmailVerification(String to, UUID userId, String username, String validityToken,
      String password) {
    try {
      String resetLink = "";
      resetLink = this.frontUrl + "/auth/activation-mail?token=" + validityToken;
      if (!to.isEmpty() && !validityToken.isEmpty()) {
        this.notificationServiceClient.sendEmailVerification(to, username, resetLink, password);
      } else {
        log.debug("USER NOT FOUND FROM USER MANAGEMENT SERVICE");
      }
    } catch (Exception e) {
      log.debug("EXCEPTION WHILE SENDING EMAIL !");
      return null;
    }
    return to;
  }


  @Override
  public String sendEmailCompleteAccount(String to, UUID userId, String username,
      String validityToken,
      String password) {
    try {
      String link = "";
      link = this.frontUrl + "/auth/pre-signup?token=" + validityToken + "&id=" + userId + "&email="
          + to;
      if (!to.isEmpty() && !validityToken.isEmpty()) {
        this.notificationServiceClient.sendPreSignupEmail(to, username, link, password);
      } else {
        log.debug("USER NOT FOUND FROM USER MANAGEMENT SERVICE");
      }
    } catch (Exception e) {
      log.debug("EXCEPTION WHILE SENDING EMAIL !");
      return null;
    }
    return to;
  }

  private String generateToken(UUID userId) {
    long validityDuration = 400000;
    String token = this.jwtTokenUtil.generateToken(userId, validityDuration);
    return token;
  }


}
