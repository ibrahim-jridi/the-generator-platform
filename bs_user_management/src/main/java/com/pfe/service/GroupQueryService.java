package com.pfe.service;

import com.pfe.domain.Group;
import com.pfe.domain.Group_;
import com.pfe.repository.GroupRepository;
import com.pfe.service.criteria.GroupCriteria;
import com.pfe.service.dto.GroupDTO;
import com.pfe.service.mapper.GroupMapper;
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
 * Service for executing complex queries for {@link Group} entities in the database. The main input
 * is a {@link GroupCriteria} which gets converted to {@link Specification}, in a way that all the
 * filters must apply. It returns a {@link Page} of {@link GroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GroupQueryService extends QueryService<Group> {

  private static final Logger log = LoggerFactory.getLogger(GroupQueryService.class);

  private final GroupRepository groupRepository;

  private final GroupMapper groupMapper;

  public GroupQueryService(GroupRepository groupRepository, GroupMapper groupMapper) {
    this.groupRepository = groupRepository;
    this.groupMapper = groupMapper;
  }

  /**
   * Return a {@link Page} of {@link GroupDTO} which matches the criteria from the database.
   *
   * @param criteria The object which holds all the filters, which the entities should match.
   * @param page     The page, which should be returned.
   * @return the matching entities.
   */
  @Transactional(readOnly = true)
  public Page<GroupDTO> findByCriteria(GroupCriteria criteria, Pageable page) {
    log.debug("find by criteria : {}, page: {}", criteria, page);
    final Specification<Group> specification = createSpecification(criteria);
    return this.groupRepository.findAll(specification, page)
        .map(this.groupMapper::toDto)
        .map(this::countGroupUsers);
  }

  /**
   * Return the number of matching entities in the database.
   *
   * @param criteria The object which holds all the filters, which the entities should match.
   * @return the number of matching entities.
   */
  @Transactional(readOnly = true)
  public long countByCriteria(GroupCriteria criteria) {
    log.debug("count by criteria : {}", criteria);
    final Specification<Group> specification = createSpecification(criteria);
    return this.groupRepository.count(specification);
  }

  /**
   * Function to convert {@link GroupCriteria} to a {@link Specification}
   *
   * @param criteria The object which holds all the filters, which the entities should match.
   * @return the matching {@link Specification} of the entity.
   */
  protected Specification<Group> createSpecification(GroupCriteria criteria) {
    Specification<Group> specification = Specification.where(null);
    if (criteria != null) {
      // This has to be called first, because the distinct method returns null
      if (criteria.getDistinct() != null) {
        specification = specification.and(distinct(criteria.getDistinct()));
      }
      if (criteria.getId() != null) {
        specification = specification.and(buildSpecification(criteria.getId(), Group_.id));
      }
      if (criteria.getIsActive() != null) {
        specification = specification.and(
            buildSpecification(criteria.getIsActive(), Group_.isActive));
      }
      if (criteria.getLabel() != null) {
        specification = specification.and(
            buildStringSpecification(criteria.getLabel(), Group_.label));
      }
      if (criteria.getDescription() != null) {
        specification = specification.and(
            buildStringSpecification(criteria.getDescription(), Group_.description));
      }
      if (criteria.getParentId() != null) {
        specification = specification.and(
            buildSpecification(criteria.getParentId(),
                root -> root.join(Group_.parent, JoinType.LEFT).get(Group_.id))
        );
      }
    }
      return specification.and((root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("deleted")));
  }

  private GroupDTO countGroupUsers(GroupDTO groupDTO) {
    groupDTO.setNumberOfUsers(groupRepository.getUserIdsByGroupId(groupDTO.getId()).size());
    return groupDTO;
  }
}
