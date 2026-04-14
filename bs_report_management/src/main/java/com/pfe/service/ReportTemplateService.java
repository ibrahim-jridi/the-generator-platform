package com.pfe.service;

import com.pfe.domain.ReportTemplate;
import com.pfe.service.dto.ReportTemplateDTO;

import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link ReportTemplate}.
 */
public interface ReportTemplateService {
    /**
     * Save a reportTemplate.
     *
     * @param reportTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    ReportTemplateDTO save(ReportTemplateDTO reportTemplateDTO);

    /**
     * Updates a reportTemplate.
     *
     * @param reportTemplateDTO the entity to update.
     * @return the persisted entity.
     */
    ReportTemplateDTO update(ReportTemplateDTO reportTemplateDTO);

    /**
     * Partially updates a reportTemplate.
     *
     * @param reportTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReportTemplateDTO> partialUpdate(ReportTemplateDTO reportTemplateDTO);

    /**
     * Get the "id" reportTemplate.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReportTemplateDTO> findOne(UUID id);

    /**
     * Delete the "id" reportTemplate.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    Optional<ReportTemplateDTO> findByType(String reportType);

}
