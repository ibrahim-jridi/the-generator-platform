package com.pfe.service;

import com.pfe.domain.*; // for static metamodels
import com.pfe.domain.NotificationPushed;
import com.pfe.domain.NotificationPushed_;
import com.pfe.repository.INotificationPushedRepository;
import com.pfe.service.criteria.NotificationPushedCriteria;
import com.pfe.service.dto.NotificationPushedDTO;
import com.pfe.service.mapper.NotificationPushedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link NotificationPushed} entities in the database.
 * The main input is a {@link NotificationPushedCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NotificationPushedDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificationPushedQueryService extends QueryService<NotificationPushed> {

    private final Logger log = LoggerFactory.getLogger(NotificationPushedQueryService.class);

    private final INotificationPushedRepository notificationPushedRepository;

    private final NotificationPushedMapper notificationPushedMapper;

    public NotificationPushedQueryService(
        INotificationPushedRepository notificationPushedRepository, NotificationPushedMapper notificationPushedMapper
    ) {
        this.notificationPushedRepository = notificationPushedRepository;
        this.notificationPushedMapper = notificationPushedMapper;
    }

    /**
     * Return a {@link Page} of {@link NotificationPushedDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationPushedDTO> findByCriteria(NotificationPushedCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NotificationPushed> specification = createSpecification(criteria);
        return notificationPushedRepository.findAll(specification, page).map(notificationPushedMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificationPushedCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NotificationPushed> specification = createSpecification(criteria);
        return notificationPushedRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificationPushedCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NotificationPushed> createSpecification(NotificationPushedCriteria criteria) {
        Specification<NotificationPushed> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), NotificationPushed_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), NotificationPushed_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), NotificationPushed_.description));
            }
            if (criteria.getMessage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMessage(), NotificationPushed_.message));
            }
            if (criteria.getNotificationDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getNotificationDate(), NotificationPushed_.notificationDate)
                );
            }
            if (criteria.getRecipientId() != null) {
                specification = specification.and(buildSpecification(criteria.getRecipientId(), NotificationPushed_.recipientId));
            }
            if (criteria.getIsSeen() != null) {
                specification = specification.and(buildSpecification(criteria.getIsSeen(), NotificationPushed_.isSeen));
            }
        }
        return specification;
    }
}
