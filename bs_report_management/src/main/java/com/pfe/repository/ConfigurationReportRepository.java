package com.pfe.repository;

import com.pfe.domain.ConfigurationReport;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConfigurationReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigurationReportRepository extends BaseRepository<ConfigurationReport, UUID> {}
