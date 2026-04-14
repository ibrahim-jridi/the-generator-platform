package com.pfe.repository;

import com.pfe.domain.ReportTemplate;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportTemplateRepository extends BaseRepository<ReportTemplate, UUID> {
    Optional<ReportTemplate> findByTypeAndDeletedFalse(String type);
}
