package com.pfe.service;

import com.pfe.domain.Frequency;
import com.pfe.domain.Notification;
import com.pfe.service.dto.FrequencyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Service Interface for managing {@link Frequency}.
 */
public interface IFrequencyService {
    /**
     * Save a frequency.
     *
     * @param frequencyDTO the entity to save.
     * @return the persisted entity.
     */
    FrequencyDTO save(FrequencyDTO frequencyDTO);

    /**
     * Updates a frequency.
     *
     * @param frequencyDTO the entity to update.
     * @return the persisted entity.
     */
    FrequencyDTO update(FrequencyDTO frequencyDTO);

    /**
     * Partially updates a frequency.
     *
     * @param frequencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FrequencyDTO> partialUpdate(FrequencyDTO frequencyDTO);

    /**
     * Get all the frequencies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FrequencyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" frequency.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FrequencyDTO> findOne(UUID id);

    /**
     * Delete the "id" frequency.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    void updateNotificationFrequencies(Notification notification, Set<FrequencyDTO> frequencyDTOs);
}
