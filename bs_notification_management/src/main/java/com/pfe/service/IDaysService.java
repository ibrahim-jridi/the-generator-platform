package com.pfe.service;

import com.pfe.domain.Days;
import com.pfe.service.dto.DaysDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link Days}.
 */
public interface IDaysService {
    /**
     * Save a days.
     *
     * @param daysDTO the entity to save.
     * @return the persisted entity.
     */
    DaysDTO save(DaysDTO daysDTO);

    /**
     * Updates a days.
     *
     * @param daysDTO the entity to update.
     * @return the persisted entity.
     */
    DaysDTO update(DaysDTO daysDTO);

    /**
     * Partially updates a days.
     *
     * @param daysDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DaysDTO> partialUpdate(DaysDTO daysDTO);

    /**
     * Get all the days.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DaysDTO> findAll(Pageable pageable);

    /**
     * Get the "id" days.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DaysDTO> findOne(UUID id);

    /**
     * Delete the "id" days.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);
}
