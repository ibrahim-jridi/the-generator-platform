package com.pfe.rest;

import com.pfe.service.KeycloakService;
import com.pfe.service.NotificationService;
import com.pfe.service.dto.request.ResetPasswordRequestDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/send-email")
public class EmailRessource {

    private final NotificationService notificationService;
    private final KeycloakService keycloakService;
    private static final Logger log = LoggerFactory.getLogger(EmailRessource.class);

    public EmailRessource(NotificationService notificationService,
        KeycloakService keycloakService) {
        this.notificationService = notificationService;
        this.keycloakService = keycloakService;
    }

    @PostMapping("/email-resetPassword")
    public ResponseEntity<Map<String, String>> sendResetPasswordEmail(
        @RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            UserRepresentation userRepresentation = this.keycloakService.findByEmailAndUsername(
                resetPasswordRequestDTO.getUserEmail(), resetPasswordRequestDTO.getUsername());
            if (userRepresentation != null) {
                this.notificationService.requestPasswordResetToNotification(
                    resetPasswordRequestDTO.getUserEmail(), resetPasswordRequestDTO.getUsername(),
                    UUID.fromString(userRepresentation.getId()));
            }
            response.put("message", "Reset password email sent successfully ");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Failed to send reset password email");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/email-verification")
    public ResponseEntity<?> sendEmailVerification(@RequestParam String to,
        @RequestParam UUID userId,
        @RequestParam String validityToken, @RequestParam String password) {
        try {
            return new ResponseEntity<>(
                this.notificationService.sendEmailVerification(to, userId, "", validityToken,
                    password),
                HttpStatus.OK);
        } catch (Exception e) {
            log.error("EXCEPTION WHILE SENDING EMAIL: {}", e);
            return null;
        }
    }
}
