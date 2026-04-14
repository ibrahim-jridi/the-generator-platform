package com.pfe.web.rest;

import com.pfe.domain.Event;
import com.pfe.management.EmailService;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.dto.request.SendMailRequest;
import jakarta.ws.rs.BadRequestException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/emails")
@Transactional
public class EmailResource {

  private static final Logger LOG = LoggerFactory.getLogger(EmailResource.class);
  private final EmailService emailService;

  public EmailResource(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping("/sendEmail")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Event> sendEmailToUser(@RequestBody String to, @RequestBody String subject)
      throws URISyntaxException {
    LOG.debug("REST request to save Event : {}");
    if (to == null) {
      throw new BadRequestException("A RECIPIANT ID COULD NOT BE NUll");
    }
    // emailService.sendSimpleEmail(to,subject);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/email-verification")

  public ResponseEntity<Map<String, String>> sendActivationEmail(@RequestParam String email,
      @RequestParam("username") String username, @RequestParam String activationLink,
      @RequestParam("password") String password) {
    Map<String, String> response = new HashMap<>();
    try {
      response.put("message", "Activation email sent successfully");

      this.emailService.sendActivationEmail(email, username, activationLink, password);
      return new ResponseEntity<>(response, HttpStatus.OK);
    } catch (Exception e) {
      response.put("message", "Failed to send activation email");
      return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/send")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity sendMail(@RequestBody SendMailRequest sendMailRequest) {
    this.emailService.sendMail(sendMailRequest);
    return ResponseEntity.ok().build();
  }
}
