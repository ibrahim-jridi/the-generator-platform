package com.pfe.management.impl;

import com.pfe.management.EmailService;
import com.pfe.service.dto.ResetPasswordEmailDTO;
import com.pfe.service.dto.request.SendMailRequest;
import jakarta.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceImpl implements EmailService {

  @Value("${spring.mail.username}")
    String mailUsername;
  private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

  @Override
  public void sendSimpleEmail(String htmlContent, String to, String subject)
      throws MessagingException, jakarta.mail.MessagingException {
    MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true);
    helper.setFrom(mailUsername);

    this.javaMailSender.send(mimeMessage);
  }

  @Override
  public ResetPasswordEmailDTO sendPasswordResetEmail(String to, String username, String resetLink)
      throws MessagingException, jakarta.mail.MessagingException, IOException {
    ResetPasswordEmailDTO resetPasswordEmailDTO = new ResetPasswordEmailDTO(to, username);
    String htmlContent = loadTemplate("resetPasswordTemplate.html");
    htmlContent = htmlContent.replaceAll("\\{\\{username}}", username);
    htmlContent = htmlContent.replaceAll("\\{\\{resetLink}}", resetLink);
    String subject = "Réinitialisation du mot de passe";
    sendSimpleEmail(htmlContent, to, subject);
    return resetPasswordEmailDTO;

  }

  @Override
  public void sendActivationEmail(String to, String username, String activationLink,
      String password) throws MessagingException, jakarta.mail.MessagingException, IOException {

    String htmlContent = loadTemplate("ActivationEmailTemplate.html");
    htmlContent = htmlContent.replaceAll("\\{\\{username}}", username);
    htmlContent = htmlContent.replaceAll("\\{\\{activationLink}}", activationLink);
    htmlContent = htmlContent.replaceAll("\\{\\{password}}", password);
    String subject = "Activation du compte";
    sendSimpleEmail(htmlContent, to, subject);
  }


  @Override
  public void sendPreSignupEmail(String to, String username, String activationLink,
      String password) throws MessagingException, jakarta.mail.MessagingException, IOException {

    String htmlContent = loadTemplate("PreSignupEmailTemplate.html");
    htmlContent = htmlContent.replaceAll("\\{\\{activationLink}}", activationLink);
    String subject = "Pré-inscription";
    sendSimpleEmail(htmlContent, to, subject);
  }

  String loadTemplate(String templateName) throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream inputStream = classLoader.getResourceAsStream("templates/" + templateName);
    if (inputStream == null) {
      throw new FileNotFoundException("Resource not found: templates/" + templateName);
    }
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    } catch (Exception e) {
      throw new RuntimeException("Failed to load template " + templateName, e);
    }
  }

  @Override
  public void sendMail(SendMailRequest sendMailRequest) {
    sendMailRequest.getTo().forEach(el -> {
      try {
        sendSimpleEmail(sendMailRequest.getMessage(), el, sendMailRequest.getSubject());
      } catch (jakarta.mail.MessagingException e) {
        throw new RuntimeException(e);
      }
    });
  }
}
