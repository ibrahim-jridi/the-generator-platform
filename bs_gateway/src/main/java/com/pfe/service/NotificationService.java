package com.pfe.service;

import java.util.UUID;

public interface NotificationService {

  String requestPasswordResetToNotification(String userEmail, String username, UUID userId);

  String sendEmailVerification(String to, UUID userId, String username, String validityToken,
      String password);

  String sendEmailCompleteAccount(String to, UUID userId, String username, String validityToken,
      String password);
}
