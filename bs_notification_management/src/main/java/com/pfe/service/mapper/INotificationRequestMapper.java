package com.pfe.service.mapper;

import com.pfe.domain.Notification;
import com.pfe.service.dto.request.NotificationRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface INotificationRequestMapper extends EntityMapper<NotificationRequest, Notification> {}

