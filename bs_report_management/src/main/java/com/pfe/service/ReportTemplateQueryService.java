package com.pfe.service;

import com.pfe.domain.*;
import com.pfe.domain.ReportTemplate;
import com.pfe.domain.ReportTemplate_;
import com.pfe.repository.ReportTemplateRepository;
import com.pfe.service.criteria.ReportTemplateCriteria;
import com.pfe.service.dto.ReportTemplateDTO;
import com.pfe.service.mapper.ReportTemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ReportTemplate} entities in the database.
 * The main input is a {@link ReportTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReportTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportTemplateQueryService extends QueryService<ReportTemplate> {

    private final Logger log = LoggerFactory.getLogger(ReportTemplateQueryService.class);

    private final ReportTemplateRepository reportTemplateRepository;

    private final ReportTemplateMapper reportTemplateMapper;

    public ReportTemplateQueryService(ReportTemplateRepository reportTemplateRepository, ReportTemplateMapper reportTemplateMapper) {
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateMapper = reportTemplateMapper;
    }

    /**
     * Return a {@link Page} of {@link ReportTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportTemplateDTO> findByCriteria(ReportTemplateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportTemplate> specification = createSpecification(criteria);
        return reportTemplateRepository.findAll(specification, page).map(reportTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportTemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReportTemplate> specification = createSpecification(criteria);
        return reportTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportTemplate> createSpecification(ReportTemplateCriteria criteria) {
        Specification<ReportTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ReportTemplate_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), ReportTemplate_.type));
            }
            if (criteria.getPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPath(), ReportTemplate_.path));
            }
        }
        return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));
    }
}
