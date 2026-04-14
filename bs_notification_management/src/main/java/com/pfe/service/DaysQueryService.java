package com.pfe.service;

import com.pfe.domain.Days;
import com.pfe.domain.Days_;
import com.pfe.repository.IDaysRepository;
import com.pfe.service.criteria.DaysCriteria;
import com.pfe.service.dto.DaysDTO;
import com.pfe.service.mapper.IDaysMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

import java.util.List;

/**
 * Service for executing complex queries for {@link Days} entities in the database.
 * The main input is a {@link DaysCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DaysDTO} or a {@link Page} of {@link DaysDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DaysQueryService extends QueryService<Days> {

    private final Logger log = LoggerFactory.getLogger(DaysQueryService.class);

    private final IDaysRepository daysRepository;

    private final IDaysMapper daysMapper;

    public DaysQueryService(IDaysRepository daysRepository, IDaysMapper daysMapper) {
        this.daysRepository = daysRepository;
        this.daysMapper = daysMapper;
    }

    /**
     * Return a {@link List} of {@link DaysDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DaysDTO> findByCriteria(DaysCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Days> specification = createSpecification(criteria);
        return daysMapper.toDto(daysRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DaysDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DaysDTO> findByCriteria(DaysCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Days> specification = createSpecification(criteria);
        return daysRepository.findAll(specification, page).map(daysMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DaysCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Days> specification = createSpecification(criteria);
        return daysRepository.count(specification);
    }

    /**
     * Function to convert {@link DaysCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Days> createSpecification(DaysCriteria criteria) {
        Specification<Days> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Days_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Days_.name));
            }
        }
        return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));
    }
}
