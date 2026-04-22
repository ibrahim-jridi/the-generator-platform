package com.pfe.repository;

import com.pfe.domain.Role;
import com.pfe.domain.enumeration.RolesType;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Role entity.
 * <p>
 * When extending this class, extend RoleRepositoryWithBagRelationships too. For more information
 * refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, UUID> {

    Boolean existsByLabelAndDeletedFalse(String label);
    Optional<Role> findByLabel(String label);

    @Query(value = "SELECT u.user_id from bs_rel_user_role u " +
        "where u.role_id =:roleId", nativeQuery = true)
    List<UUID> getUserIdsByRoleId(@Param("roleId") UUID roleId);
    Role findByLabelAndDeletedFalse(String labelRole);
    Set<Role> findAllByIdInAndDeletedFalse(Set<UUID> collect);
    List<Role> findByRoleTypeAndIsActive(RolesType rolesType , boolean isActive);
    List<Role> findAllByDeletedFalse();

}
