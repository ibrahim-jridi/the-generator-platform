package com.pfe.service;

import com.pfe.domain.NotificationDestination;
import com.pfe.domain.NotificationDestination_;
import com.pfe.domain.Notification_;
import com.pfe.repository.INotificationDestinationRepository;
import com.pfe.service.criteria.NotificationDestinationCriteria;
import com.pfe.service.dto.NotificationDestinationDTO;
import com.pfe.service.mapper.INotificationDestinationMapper;
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
 * Service for executing complex queries for {@link NotificationDestination} entities in the database.
 * The main input is a {@link NotificationDestinationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NotificationDestinationDTO} or a {@link Page} of {@link NotificationDestinationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificationDestinationQueryService extends QueryService<NotificationDestination> {

    private final Logger log = LoggerFactory.getLogger(NotificationDestinationQueryService.class);

    private final INotificationDestinationRepository notificationDestinationRepository;

    private final INotificationDestinationMapper notificationDestinationMapper;

    public NotificationDestinationQueryService(
        INotificationDestinationRepository notificationDestinationRepository,
        INotificationDestinationMapper notificationDestinationMapper
    ) {
        this.notificationDestinationRepository = notificationDestinationRepository;
        this.notificationDestinationMapper = notificationDestinationMapper;
    }

    /**
     * Return a {@link List} of {@link NotificationDestinationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NotificationDestinationDTO> findByCriteria(NotificationDestinationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NotificationDestination> specification = createSpecification(criteria);
        return notificationDestinationMapper.toDto(notificationDestinationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NotificationDestinationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDestinationDTO> findByCriteria(NotificationDestinationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NotificationDestination> specification = createSpecification(criteria);
        return notificationDestinationRepository.findAll(specification, page).map(notificationDestinationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificationDestinationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NotificationDestination> specification = createSpecification(criteria);
        return notificationDestinationRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificationDestinationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NotificationDestination> createSpecification(NotificationDestinationCriteria criteria) {
        Specification<NotificationDestination> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), NotificationDestination_.id));
            }
            if (criteria.getSenderId() != null) {
                specification = specification.and(buildSpecification(criteria.getSenderId(), NotificationDestination_.senderId));
            }
            if (criteria.getNotificationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNotificationId(),
                            root -> root.join(NotificationDestination_.notification, JoinType.LEFT).get(Notification_.id)
                        )
                    );
            }
        }
        return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));
    }
}
