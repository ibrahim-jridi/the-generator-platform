package com.pfe.service;

import com.pfe.domain.Frequency;
import com.pfe.domain.Frequency_;
import com.pfe.domain.Notification_;
import com.pfe.repository.IFrequencyRepository;
import com.pfe.service.criteria.FrequencyCriteria;
import com.pfe.service.dto.FrequencyDTO;
import com.pfe.service.mapper.IFrequencyMapper;
import jakarta.persistence.criteria.JoinType;
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
 * Service for executing complex queries for {@link Frequency} entities in the database.
 * The main input is a {@link FrequencyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FrequencyDTO} or a {@link Page} of {@link FrequencyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FrequencyQueryService extends QueryService<Frequency> {

    private final Logger log = LoggerFactory.getLogger(FrequencyQueryService.class);

    private final IFrequencyRepository frequencyRepository;

    private final IFrequencyMapper frequencyMapper;

    public FrequencyQueryService(IFrequencyRepository frequencyRepository, IFrequencyMapper frequencyMapper) {
        this.frequencyRepository = frequencyRepository;
        this.frequencyMapper = frequencyMapper;
    }

    /**
     * Return a {@link List} of {@link FrequencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FrequencyDTO> findByCriteria(FrequencyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Frequency> specification = createSpecification(criteria);
        return frequencyMapper.toDto(frequencyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FrequencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FrequencyDTO> findByCriteria(FrequencyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Frequency> specification = createSpecification(criteria);
        return frequencyRepository.findAll(specification, page).map(frequencyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FrequencyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Frequency> specification = createSpecification(criteria);
        return frequencyRepository.count(specification);
    }

    /**
     * Function to convert {@link FrequencyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Frequency> createSpecification(FrequencyCriteria criteria) {
        Specification<Frequency> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Frequency_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Frequency_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Frequency_.endDate));
            }
            if (criteria.getIsRepeat() != null) {
                specification = specification.and(buildSpecification(criteria.getIsRepeat(), Frequency_.isRepeat));
            }
            if (criteria.getTiming() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTiming(), Frequency_.timing));
            }
            if (criteria.getConfigurationDay() != null) {
                specification = specification.and(buildSpecification(criteria.getConfigurationDay(), Frequency_.configurationDayEnum));
            }
            if (criteria.getFrequencyConfig() != null) {
                specification = specification.and(buildSpecification(criteria.getFrequencyConfig(), Frequency_.frequencyConfig));
            }
            if (criteria.getNotificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotificationId(),
                            root -> root.join(Frequency_.notification, JoinType.LEFT).get(Notification_.id)
                        )
                    );
            }
        }
        return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));
    }
}
