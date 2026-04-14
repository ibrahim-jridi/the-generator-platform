package com.pfe.service.mapper;

import com.pfe.domain.NotificationPushed;
import com.pfe.service.dto.NotificationPushedDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link NotificationPushed} and its DTO {@link NotificationPushedDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationPushedMapper extends EntityMapper<NotificationPushedDTO, NotificationPushed> {}
