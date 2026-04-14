package com.pfe.repository;

import com.pfe.domain.EventNotification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pfe.domain.enums.Status;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventNotification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventNotificationRepository  extends BaseRepository<EventNotification, UUID> {

    Optional<EventNotification>  findByNotificationIdAndTaskIdAndDeletedFalse(UUID notificationId, String taskId);

    List<EventNotification> findByStatusAndDeletedFalse(Status status);
}
