package com.pfe.service.mapper;

import com.pfe.domain.Notification;
import com.pfe.service.dto.NotificationDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface INotificationMapper extends EntityMapper<NotificationDTO, Notification> {}
