package com.pfe.service;

import com.pfe.service.dto.NotificationDTO;
import com.pfe.service.dto.UserDTO;

import java.util.UUID;

public interface INotificationSchedulerService {

    public void startScheduleNotification(UUID notificationId, String taskId);

    public void stopScheduleNotification(UUID notificationId, String taskId);

    public void sendNotificationToUser(NotificationDTO notification, UserDTO userDTO);
}
