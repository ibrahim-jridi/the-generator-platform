package com.pfe.service;

import com.pfe.domain.Authority_;
import com.pfe.domain.Role;
import com.pfe.domain.Role_;
import com.pfe.repository.RoleRepository;
import com.pfe.service.criteria.RoleCriteria;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.mapper.RoleMapper;
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
 * Service for executing complex queries for {@link Role} entities in the database. The main input
 * is a {@link RoleCriteria} which gets converted to {@link Specification}, in a way that all the
 * filters must apply. It returns a {@link Page} of {@link RoleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoleQueryService extends QueryService<Role> {

  private static final Logger log = LoggerFactory.getLogger(RoleQueryService.class);

  private final RoleRepository roleRepository;

  private final RoleMapper roleMapper;

  public RoleQueryService(RoleRepository roleRepository, RoleMapper roleMapper) {
    this.roleRepository = roleRepository;
    this.roleMapper = roleMapper;
  }

  /**
   * Return a {@link Page} of {@link RoleDTO} which matches the criteria from the database.
   *
   * @param criteria The object which holds all the filters, which the entities should match.
   * @param page     The page, which should be returned.
   * @return the matching entities.
   */
  @Transactional(readOnly = true)
  public Page<RoleDTO> findByCriteria(RoleCriteria criteria, Pageable page) {
    log.debug("find by criteria : {}, page: {}", criteria, page);
    final Specification<Role> specification = createSpecification(criteria);
    return this.roleRepository.findAll(specification, page)
        .map(this.roleMapper::toDto)
        .map(this::checkIfHasUsers);
  }

  private RoleDTO checkIfHasUsers(RoleDTO roleDTO) {
    roleDTO.setHasUsers(!this.roleRepository.getUserIdsByRoleId(roleDTO.getId()).isEmpty());
    return roleDTO;
  }

  /**
   * Return the number of matching entities in the database.
   *
   * @param criteria The object which holds all the filters, which the entities should match.
   * @return the number of matching entities.
   */
  @Transactional(readOnly = true)
  public long countByCriteria(RoleCriteria criteria) {
    log.debug("count by criteria : {}", criteria);
    final Specification<Role> specification = createSpecification(criteria);
    return this.roleRepository.count(specification);
  }

  /**
   * Function to convert {@link RoleCriteria} to a {@link Specification}
   *
   * @param criteria The object which holds all the filters, which the entities should match.
   * @return the matching {@link Specification} of the entity.
   */
  protected Specification<Role> createSpecification(RoleCriteria criteria) {
    Specification<Role> specification = Specification.where(null);
    if (criteria != null) {
      // This has to be called first, because the distinct method returns null
      if (criteria.getDistinct() != null) {
        specification = specification.and(distinct(criteria.getDistinct()));
      }
      if (criteria.getId() != null) {
        specification = specification.and(buildSpecification(criteria.getId(), Role_.id));
      }

      if (criteria.getIsActive() != null) {
        specification = specification.and(
            buildSpecification(criteria.getIsActive(), Role_.isActive));
      }
      if (criteria.getLabel() != null) {
        specification = specification.and(
            buildStringSpecification(criteria.getLabel(), Role_.label));
      }
      if (criteria.getDescription() != null) {
        specification = specification.and(
            buildStringSpecification(criteria.getDescription(), Role_.description));
      }

      if (criteria.getAuthorityId() != null) {
        specification = specification.and(
            buildSpecification(criteria.getAuthorityId(),
                root -> root.join(Role_.authorities, JoinType.LEFT).get(Authority_.id))
        );
      }

    }
      return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));
  }
}
