package com.pfe.service;

import com.pfe.domain.Frequency_;
import com.pfe.domain.Notification;
import com.pfe.domain.NotificationDestination_;
import com.pfe.domain.Notification_;
import com.pfe.repository.INotificationRepository;
import com.pfe.service.criteria.NotificationCriteria;
import com.pfe.service.dto.NotificationDTO;
import com.pfe.service.mapper.INotificationMapper;
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
 * Service for executing complex queries for {@link Notification} entities in the database.
 * The main input is a {@link NotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NotificationDTO} or a {@link Page} of {@link NotificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificationQueryService extends QueryService<Notification> {

    private final Logger log = LoggerFactory.getLogger(NotificationQueryService.class);

    private final INotificationRepository notificationRepository;

    private final INotificationMapper notificationMapper;

    public NotificationQueryService(INotificationRepository notificationRepository, INotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Return a {@link List} of {@link NotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> findByCriteria(NotificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Notification> specification = createSpecification(criteria);
        return notificationMapper.toDto(notificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findByCriteria(NotificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Notification> specification = createSpecification(criteria);
        return notificationRepository.findAll(specification, page).map(notificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Notification> specification = createSpecification(criteria);
        return notificationRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Notification> createSpecification(NotificationCriteria criteria) {
        Specification<Notification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Notification_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Notification_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Notification_.description));
            }
            if (criteria.getBroadcastChannel() != null) {
                specification = specification.and(buildSpecification(criteria.getBroadcastChannel(), Notification_.broadcastChannel));
            }
            if (criteria.getDestinationType() != null) {
                specification = specification.and(buildSpecification(criteria.getDestinationType(), Notification_.destinationTypeEnum));
            }
            if (criteria.getTopic() != null) {
                specification = specification.and(buildSpecification(criteria.getTopic(), Notification_.topic));
            }
            if (criteria.getFrequencyEnum() != null) {
                specification = specification.and(buildSpecification(criteria.getFrequencyEnum(), Notification_.frequencyEnum));
            }
            if (criteria.getNotificationDestinationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotificationDestinationId(),
                            root -> root.join(Notification_.notificationDestinations, JoinType.LEFT).get(NotificationDestination_.id)
                        )
                    );
            }
            if (criteria.getFrequencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFrequencyId(),
                            root -> root.join(Notification_.frequencies, JoinType.LEFT).get(String.valueOf(Frequency_.id))
                        )
                    );
            }
        }
        return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));
    }
}
