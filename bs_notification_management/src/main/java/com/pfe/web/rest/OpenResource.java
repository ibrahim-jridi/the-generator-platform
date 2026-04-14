package com.pfe.web.rest;

import com.pfe.management.EmailService;
import com.pfe.service.dto.ResetPasswordEmailDTO;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.BadRequestException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/open-api")
public class OpenResource {

    private static final Logger LOG = LoggerFactory.getLogger(EmailResource.class);
    private final EmailService emailService;

    public OpenResource(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/reset-password-request")
    public ResponseEntity<ResetPasswordEmailDTO> sendEmailResetPassword(@RequestParam("to") String to, @RequestParam("username") String username, @RequestParam("resetLink") String resetLink) throws MessagingException, IOException {
        LOG.debug("STARTING TO SEND RESET PASSEWORD EMAIL");
        if (to == null) {
            throw new BadRequestException("A RECIPIANT ID COULD NOT BE NUll");
        }
        Map<String, String> response = new HashMap<>();
        try{

            response.put("message", "Reset password email sent successfully");
            return new ResponseEntity<>(emailService.sendPasswordResetEmail(to,username, resetLink), HttpStatus.OK);
        }catch (Exception e){
            LOG.error("Exception while sending reset password email", e);
            response.put("message", "Failed to send reset password email");
            return new ResponseEntity<>(emailService.sendPasswordResetEmail(to,username, resetLink), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/emails/email-verification")
    public ResponseEntity<Map<String, String>> sendActivationEmail(@RequestParam String email, @RequestParam("username") String username, @RequestParam String activationLink, @RequestParam("password") String password) {
        Map<String, String> response = new HashMap<>();
        try {
            response.put("message", "Activation email sent successfully");

            emailService.sendActivationEmail(email,username, activationLink, password);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Failed to send activation email");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/emails/pre-signup")
    public ResponseEntity<Map<String, String>> sendPreSignupEmail(@RequestParam String email,
        @RequestParam("username") String username, @RequestParam String activationLink,
        @RequestParam("password") String password) {
        Map<String, String> response = new HashMap<>();
        try {
            response.put("message", "Activation email sent successfully");

            emailService.sendPreSignupEmail(email, username, activationLink, password);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("message", "Failed to send activation email");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
