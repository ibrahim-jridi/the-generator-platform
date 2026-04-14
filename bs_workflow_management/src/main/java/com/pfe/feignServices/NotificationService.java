package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import com.pfe.dto.request.Notification;
import com.pfe.dto.request.SendMailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "notification-service", url = "${bs-app.endpoint.gateway}"
    + "/notification-management/api/v1", configuration = FeignClientConfig.class)

public interface NotificationService {
    @RequestMapping(method = RequestMethod.POST, value = "/emails/send", produces = "application/json")
    ResponseEntity<?> sendMail(@RequestBody SendMailRequest sendMailRequest);

    @PostMapping(value = "/notifications/send-notification")
    ResponseEntity<?> sendAndSaveNotificationToUser(@RequestBody Notification notification);

    @PostMapping(value = "/notifications/start/{notificationId}/{taskId}")
    ResponseEntity<Void> startNotification(@PathVariable("notificationId") UUID notificationId, @PathVariable("taskId") String taskId);

    @PostMapping(value = "/notifications/stop/{notificationId}/{taskId}")
    ResponseEntity<Void> stopNotification(@PathVariable("notificationId") UUID notificationId, @PathVariable("taskId") String taskId);

}
