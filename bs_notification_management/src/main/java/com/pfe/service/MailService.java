package com.pfe.service;

import com.pfe.service.dto.UserDTO;
import jakarta.activation.MimetypesFileTypeMap;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    @Value("${spring.mail.username}")
    private String username;

    @Value("${bs-app.endpoint.front-url}")
    private String baseUrl;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private static final String LOGO_BS = "templates/static/images/logo_mail.png";

    @Lazy
    @Autowired
    private MailService self;

    public MailService(
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine
    ) {
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(username);
            message.setSubject(subject);
            message.setText(content, isHtml);
            ClassPathResource clr = new ClassPathResource(LOGO_BS);
            if (clr.exists()) {
                message.addInline("springLogo", clr);
            } else {
                log.warn("Image file '{}' does not exist", LOGO_BS);
            }
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailWithAttachment(
        String to,
        String subject,
        String content,
        boolean isMultipart,
        boolean isHtml,
        byte[] fileData,
        String fileName
    ) {
        log.debug(
            "Send email [multipart '{}', html '{}'] to '{}' with subject '{}' and content '{}' and attachment '{}'",
            isMultipart,
            isHtml,
            to,
            subject,
            content,
            fileName != null ? fileName : "No attachment"
        );

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(username);
            message.setSubject(subject);
            message.setText(content, isHtml);

            // 🔥 Correction : Bien gérer l'envoi d'un fichier en pièce jointe
            if (fileData != null && fileName != null) {
                String mimeType = detectMimeType(fileName, fileData);

                // ⚠️ Correction spécifique pour `.svg`
                if (fileName.toLowerCase().endsWith(".svg")) {
                    mimeType = "image/svg+xml"; // Assurer le bon envoi des SVG
                }

                ByteArrayDataSource dataSource = null;
                try {
                    dataSource = new ByteArrayDataSource(new ByteArrayInputStream(fileData), mimeType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                message.addAttachment(fileName, dataSource);
            } else {
                log.warn("No file data or file name provided for attachment");
            }

            // Envoyer l'email
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}' with attachment '{}'", to, fileName);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}' with attachment '{}'", to, fileName, e);
        }
    }

    /**
     * 🔍 Détecte dynamiquement le type MIME en fonction du fichier
     */
    private String detectMimeType(String fileName, byte[] fileData) {
        try {
            MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
            String mimeType = fileTypeMap.getContentType(fileName);

            // 🔥 Si le type MIME est inconnu, utiliser `application/octet-stream`
            return (mimeType != null && !mimeType.equals("application/octet-stream")) ? mimeType : "application/octet-stream";
        } catch (Exception e) {
            log.warn("Could not determine MIME type for '{}'", fileName, e);
            return "application/octet-stream"; // Type générique pour éviter les erreurs
        }
    }


    @Async
    public void sendEmailFromTemplate(UserDTO user, String templateName, String titleKey, String name, String description) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user {}", user.getEmail());
            return;
        }
        Locale locale = Locale.forLanguageTag("fr");
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable("name", name);
        context.setVariable("description", description);
        context.setVariable(BASE_URL, baseUrl);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        self.sendEmail(user.getEmail(), subject, content, true, true);
    }

    @Async
    public void sendNotificationEmail(UserDTO user, String name, String description) {
        log.debug("Sending notification email to '{}'", user.getEmail());
        self.sendEmailFromTemplate(user, "notificationEmail", "email.notification.title", name, description);
    }

    @Async
    public void sendSimpleNotificationEmail(UserDTO user, String subject, String body) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getEmail());
            return;
        }
        log.debug("Sending simple email to '{}'", user.getEmail());
        self.sendEmail(user.getEmail(), subject, body, false, false);
    }

    @Async
    public void sendSimpleNotificationEmailWithFile(UserDTO user, String subject, String body, byte[] file, String fileName) {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getEmail());
            return;
        }
        log.debug("Sending simple email with file to '{}'", user.getEmail());
        self.sendEmailWithAttachment(user.getEmail(), subject, body, true, false, file, fileName);
    }
}
