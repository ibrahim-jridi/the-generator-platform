package com.pfe.repository;

import com.pfe.domain.Group;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Group entity.
 */
@Repository
public interface GroupRepository extends BaseRepository<Group, UUID> {

    @Query(value = "SELECT u.user_id from bs_rel_user_group u where u.group_id =:groupId", nativeQuery = true)
    List<UUID> getUserIdsByGroupId(@Param("groupId") UUID groupId);

    Group findFirstByLabel(String label);

    List<Group> findAllByLabelIn(List<String> groupsLabelsFromKeyCloak);

    Boolean existsByLabelAndDeletedFalse(String label);

    List<Group> findAllByParentIdAndDeletedFalse(UUID parentGroupId);


    Boolean existsByLabel(String groupeLabel);
    Set<Group> findAllByIdInAndDeletedFalse(Set<UUID> collect);
    List<Group> findAllByDeletedFalse();

}
