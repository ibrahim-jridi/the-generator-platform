package com.pfe.service;

import com.pfe.config.Constants;
import com.pfe.domain.Group_;
import com.pfe.domain.Role_;
import com.pfe.domain.User;
import com.pfe.domain.User_;
import com.pfe.repository.UserRepository;
import com.pfe.service.criteria.UserCriteria;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.mapper.UserMapper;
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
 * Service for executing complex queries for {@link User} entities in the database. The main input
 * is a {@link UserCriteria} which gets converted to {@link Specification}, in a way that all the
 * filters must apply. It returns a {@link Page} of {@link UserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserQueryService extends QueryService<User> {

    private static final Logger log = LoggerFactory.getLogger(UserQueryService.class);

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserQueryService(UserRepository userRepository,
        UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Return a {@link Page} of {@link UserDTO} which matches the criteria from the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page     The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDTO> findByCriteria(UserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);

        Specification<User> deletedFalseSpec = (root, query, criteriaBuilder) ->
            criteriaBuilder.isFalse(root.get("deleted"));
        Specification<User> notRootUserSpec = (root, query, criteriaBuilder) ->
            criteriaBuilder.notEqual(root.get(User_.username), Constants.BS_ROOT);
        Specification<User> globalSpec = deletedFalseSpec.and(notRootUserSpec);

        final Specification<User> specification =
            Specification.where(globalSpec).and(createSpecification(criteria));
        return this.userRepository.findAll(specification, page)
            .map(this.userMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<User> specification = createSpecification(criteria);
        return this.userRepository.count(specification);
    }

    /**
     * Function to convert {@link UserCriteria} to a {@link Specification}
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<User> createSpecification(UserCriteria criteria) {
        Specification<User> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getId(), User_.id));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getEmail(), User_.email));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getIsActive(), User_.isActive));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGender(), User_.gender));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getFirstName(), User_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getLastName(), User_.lastName));
            }
            if (criteria.getUsername() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getUsername(), User_.username));
            }
            if (criteria.getEmailVerified() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEmailVerified(), User_.emailVerified));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDeleted(), User_.deleted));
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getRoleId(),
                        root -> root.join(User_.roles, JoinType.LEFT).get(Role_.id))
                );
            }
            if (criteria.getGroupId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getGroupId(),
                        root -> root.join(User_.groups, JoinType.LEFT).get(Group_.id))
                );
            }
        }
        return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));
    }
}
