package com.pfe.service.mapper;

import com.pfe.domain.Frequency;
import com.pfe.domain.Notification;
import com.pfe.service.dto.FrequencyDTO;
import com.pfe.service.dto.NotificationDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Frequency} and its DTO {@link FrequencyDTO}.
 */
@Mapper(componentModel = "spring")
public interface IFrequencyMapper extends EntityMapper<FrequencyDTO, Frequency> {

    @Mapping(target = "notificationId", source = "notification.id")
    @Mapping(target = "days", source = "days")
    FrequencyDTO toDto(Frequency f);

    @Named("notificationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    NotificationDTO toDtoNotificationId(Notification notification);

    @Mapping(target = "days", source = "days")
    Frequency toEntity(FrequencyDTO frequencyDTO);
}
