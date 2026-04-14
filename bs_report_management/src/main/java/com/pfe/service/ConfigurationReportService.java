package com.pfe.service;

import com.pfe.domain.ConfigurationReport;
import com.pfe.service.dto.ConfigurationReportDTO;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link ConfigurationReport}.
 */
public interface ConfigurationReportService {
    /**
     * Save a configurationReport.
     *
     * @param configurationReportDTO the entity to save.
     * @return the persisted entity.
     */
    ConfigurationReportDTO save(ConfigurationReportDTO configurationReportDTO);

    /**
     * Updates a configurationReport.
     *
     * @param configurationReportDTO the entity to update.
     * @return the persisted entity.
     */
    ConfigurationReportDTO update(ConfigurationReportDTO configurationReportDTO);

    /**
     * Partially updates a configurationReport.
     *
     * @param configurationReportDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ConfigurationReportDTO> partialUpdate(ConfigurationReportDTO configurationReportDTO);

    /**
     * Get the "id" configurationReport.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ConfigurationReportDTO> findOne(UUID id);

    /**
     * Delete the "id" configurationReport.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    byte[] generateReport(ConfigurationReportDTO configurationReportDTO) throws Exception;

    ConfigurationReportDTO findCurrentConfigReport();

}
