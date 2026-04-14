package com.pfe.service.mapper;

import com.pfe.domain.EventNotification;
import com.pfe.service.dto.EventNotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventNotification} and its DTO {@link EventNotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventNotificationMapper extends EntityMapper<EventNotificationDTO, EventNotification> {}
