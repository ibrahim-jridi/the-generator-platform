package com.pfe.service.mapper;

import com.pfe.domain.Notification;
import com.pfe.domain.NotificationDestination;
import com.pfe.service.dto.NotificationDTO;
import com.pfe.service.dto.NotificationDestinationDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link NotificationDestination} and its DTO {@link NotificationDestinationDTO}.
 */
@Mapper(componentModel = "spring")
public interface INotificationDestinationMapper extends EntityMapper<NotificationDestinationDTO, NotificationDestination> {
    @Mapping(target = "notificationId", source = "notification.id")
    NotificationDestinationDTO toDto(NotificationDestination s);

    @Named("notificationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NotificationDTO toDtoNotificationId(Notification notification);
}
