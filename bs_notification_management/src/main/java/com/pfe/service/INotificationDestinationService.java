package com.pfe.service;

import com.pfe.domain.Notification;
import com.pfe.domain.NotificationDestination;
import com.pfe.service.dto.NotificationDestinationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Service Interface for managing {@link NotificationDestination}.
 */
public interface INotificationDestinationService {
    /**
     * Save a notificationDestination.
     *
     * @param notificationDestinationDTO the entity to save.
     * @return the persisted entity.
     */
    NotificationDestinationDTO save(NotificationDestinationDTO notificationDestinationDTO);

    /**
     * Updates a notificationDestination.
     *
     * @param notificationDestinationDTO the entity to update.
     * @return the persisted entity.
     */
    NotificationDestinationDTO update(NotificationDestinationDTO notificationDestinationDTO);

    /**
     * Partially updates a notificationDestination.
     *
     * @param notificationDestinationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificationDestinationDTO> partialUpdate(NotificationDestinationDTO notificationDestinationDTO);

    /**
     * Get all the notificationDestinations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NotificationDestinationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" notificationDestination.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificationDestinationDTO> findOne(UUID id);

    /**
     * Delete the "id" notificationDestination.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    void updateNotificationDestinations(Notification notification, Set<NotificationDestinationDTO> destinationDTOs);
}
