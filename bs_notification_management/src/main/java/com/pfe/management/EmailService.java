package com.pfe.management;

import com.pfe.service.dto.ResetPasswordEmailDTO;
import com.pfe.service.dto.request.SendMailRequest;
import jakarta.mail.MessagingException;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

  void sendSimpleEmail(String htmlContent, String to, String subject) throws MessagingException;

  ResetPasswordEmailDTO sendPasswordResetEmail(String to, String username, String resetLink)
      throws MessagingException, IOException;

  void sendActivationEmail(String to, String username, String activationLink, String password)
      throws MessagingException, IOException;

  void sendPreSignupEmail(String to, String username, String activationLink, String password)
      throws MessagingException, IOException;


  void sendMail(SendMailRequest sendMailRequest);
}
