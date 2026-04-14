package com.pfe.service;

import com.pfe.domain.*; // for static metamodels
import com.pfe.domain.EventNotification;
import com.pfe.domain.EventNotification_;
import com.pfe.domain.Notification_;
import com.pfe.repository.EventNotificationRepository;
import com.pfe.service.criteria.EventNotificationCriteria;
import com.pfe.service.dto.EventNotificationDTO;
import com.pfe.service.mapper.EventNotificationMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EventNotification} entities in the database.
 * The main input is a {@link EventNotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EventNotificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventNotificationQueryService extends QueryService<EventNotification> {

    private final Logger log = LoggerFactory.getLogger(EventNotificationQueryService.class);

    private final EventNotificationRepository eventNotificationRepository;

    private final EventNotificationMapper eventNotificationMapper;

    public EventNotificationQueryService(
        EventNotificationRepository eventNotificationRepository,
        EventNotificationMapper eventNotificationMapper
    ) {
        this.eventNotificationRepository = eventNotificationRepository;
        this.eventNotificationMapper = eventNotificationMapper;
    }

    /**
     * Return a {@link Page} of {@link EventNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventNotificationDTO> findByCriteria(EventNotificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventNotification> specification = createSpecification(criteria);
        return eventNotificationRepository.findAll(specification, page).map(eventNotificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventNotificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EventNotification> specification = createSpecification(criteria);
        return eventNotificationRepository.count(specification);
    }

    /**
     * Function to convert {@link EventNotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventNotification> createSpecification(EventNotificationCriteria criteria) {
        Specification<EventNotification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), EventNotification_.id));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaskId(), EventNotification_.taskId));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), EventNotification_.status));
            }
            if (criteria.getNotificationId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getNotificationId(),
                        root -> root.join(EventNotification_.notification, JoinType.LEFT).get(
                            Notification_.id)
                    )
                );
            }

        }
        return specification;
    }
}
