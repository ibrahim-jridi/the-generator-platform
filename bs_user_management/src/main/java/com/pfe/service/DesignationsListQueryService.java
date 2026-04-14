package com.pfe.service;


import com.pfe.domain.*;
import com.pfe.domain.DesignationsList;
import com.pfe.repository.DesignationsListRepository;
import com.pfe.service.criteria.DesignationsListCriteria;
import com.pfe.service.mapper.DesignationsListMapper;
import com.pfe.domain.DesignationsList_;
import com.pfe.domain.Role_;
import com.pfe.domain.User_;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.JoinType;
import tech.jhipster.service.QueryService;

@Service
@Transactional(readOnly = true)
public class DesignationsListQueryService extends QueryService<DesignationsList> {

    private final DesignationsListRepository designationsListRepository;
    private final DesignationsListMapper designationsListMapper;
    public DesignationsListQueryService(DesignationsListRepository designationsListRepository, DesignationsListMapper designationsListMapper) {
        this.designationsListRepository = designationsListRepository;
        this.designationsListMapper = designationsListMapper;
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */

    public Page<DesignationsList> findByCriteria(DesignationsListCriteria criteria, Pageable pageable) {
        Specification<DesignationsList> specification = createSpecification(criteria);

        return designationsListRepository.findAll(specification, pageable);
    }

    protected Specification<DesignationsList> createSpecification(DesignationsListCriteria criteria) {
        Specification<DesignationsList> specification = Specification.where(null);

        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), DesignationsList_.id));
            }
            if (criteria.getPmUserId() != null) {
                specification = specification.and(
                    (root, query, cb) ->
                        cb.equal(root.join(DesignationsList_.pmUser, JoinType.LEFT).get(User_.id), criteria.getPmUserId().getEquals())
                );
            }
            if (criteria.getDesignatedUserId() != null) {
                specification = specification.and(
                    (root, query, cb) ->
                        cb.equal(root.join(DesignationsList_.designatedUser, JoinType.LEFT).get(User_.id), criteria.getDesignatedUserId().getEquals())
                );
            }
            if (criteria.getRoleId() != null) {
                specification = specification.and(
                    (root, query, cb) ->
                        cb.equal(root.join(DesignationsList_.role, JoinType.LEFT).get(Role_.id), criteria.getRoleId().getEquals())
                );
            }
        }

        return specification;
    }

}
