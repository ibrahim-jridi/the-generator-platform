package com.pfe.service;

import com.pfe.domain.*; // for static metamodels
import com.pfe.domain.ConfigurationReport;
import com.pfe.domain.ConfigurationReport_;
import com.pfe.repository.ConfigurationReportRepository;
import com.pfe.service.criteria.ConfigurationReportCriteria;
import com.pfe.service.dto.ConfigurationReportDTO;
import com.pfe.service.mapper.ConfigurationReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ConfigurationReport} entities in the database.
 * The main input is a {@link ConfigurationReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ConfigurationReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConfigurationReportQueryService extends QueryService<ConfigurationReport> {

    private final Logger log = LoggerFactory.getLogger(ConfigurationReportQueryService.class);

    private final ConfigurationReportRepository configurationReportRepository;

    private final ConfigurationReportMapper configurationReportMapper;

    public ConfigurationReportQueryService(
        ConfigurationReportRepository configurationReportRepository,
        ConfigurationReportMapper configurationReportMapper
    ) {
        this.configurationReportRepository = configurationReportRepository;
        this.configurationReportMapper = configurationReportMapper;
    }

    /**
     * Return a {@link Page} of {@link ConfigurationReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigurationReportDTO> findByCriteria(ConfigurationReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ConfigurationReport> specification = createSpecification(criteria);
        return configurationReportRepository.findAll(specification, page).map(configurationReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConfigurationReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ConfigurationReport> specification = createSpecification(criteria);
        return configurationReportRepository.count(specification);
    }

    /**
     * Function to convert {@link ConfigurationReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ConfigurationReport> createSpecification(ConfigurationReportCriteria criteria) {
        Specification<ConfigurationReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ConfigurationReport_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), ConfigurationReport_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), ConfigurationReport_.address));
            }
            if (criteria.getPostalCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalCode(), ConfigurationReport_.postalCode));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), ConfigurationReport_.phone));
            }
            if (criteria.getFax() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFax(), ConfigurationReport_.fax));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), ConfigurationReport_.email));
            }
            if (criteria.getLogo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogo(), ConfigurationReport_.logo));
            }
            if (criteria.getFooter() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFooter(), ConfigurationReport_.footer));
            }
        }
        return specification;
    }
}
