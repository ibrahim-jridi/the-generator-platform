package com.pfe.service;

import com.pfe.domain.NotificationPushed;
import com.pfe.service.dto.NotificationPushedDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link NotificationPushed}.
 */
public interface NotificationPushedService {
    /**
     * Save a notificationPushed.
     *
     * @param notificationPushedDTO the entity to save.
     * @return the persisted entity.
     */
    NotificationPushedDTO save(NotificationPushedDTO notificationPushedDTO);

    /**
     * Updates a notificationPushed.
     *
     * @param notificationPushedDTO the entity to update.
     * @return the persisted entity.
     */
    NotificationPushedDTO update(NotificationPushedDTO notificationPushedDTO);

    /**
     * Partially updates a notificationPushed.
     *
     * @param notificationPushedDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificationPushedDTO> partialUpdate(NotificationPushedDTO notificationPushedDTO);

    /**
     * Get the "id" notificationPushed.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificationPushedDTO> findOne(UUID id);

    /**
     * Delete the "id" notificationPushed.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    List<NotificationPushedDTO> findByRecipient(UUID recipientId);


    Page<NotificationPushedDTO> findByTaskId(String taskId, Pageable pageable);
}
