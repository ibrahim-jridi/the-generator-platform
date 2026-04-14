package com.pfe.service;

import com.pfe.domain.EventNotification;
import com.pfe.domain.enums.Status;
import com.pfe.service.dto.EventNotificationDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link EventNotification}.
 */
public interface EventNotificationService {
    /**
     * Save a eventNotification.
     *
     * @param eventNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    EventNotificationDTO save(EventNotificationDTO eventNotificationDTO);

    /**
     * Updates a eventNotification.
     *
     * @param eventNotificationDTO the entity to update.
     * @return the persisted entity.
     */
    EventNotificationDTO update(EventNotificationDTO eventNotificationDTO);

    /**
     * Partially updates a eventNotification.
     *
     * @param eventNotificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EventNotificationDTO> partialUpdate(EventNotificationDTO eventNotificationDTO);

    /**
     * Get the "id" eventNotification.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EventNotificationDTO> findOne(UUID id);

    /**
     * Delete the "id" eventNotification.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    Optional<EventNotificationDTO> findByNotificationIdAndTaskId(UUID notificationId,String taskId);

    List<EventNotificationDTO> findByStatus(Status status);

    EventNotificationDTO saveAndFlushEventNotification(EventNotificationDTO eventNotificationDTO);
}
