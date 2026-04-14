package com.pfe.service;

import com.pfe.service.dto.GroupDTO;
import com.pfe.domain.Group;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link Group}.
 */
public interface GroupService {

    /**
     * Save a group.
     *
     * @param groupDTO the entity to save.
     * @return the persisted entity.
     */
    GroupDTO save(GroupDTO groupDTO);

    /**
     * Updates a group.
     *
     * @param groupDTO the entity to update.
     * @return the persisted entity.
     */
    GroupDTO update(GroupDTO groupDTO);

    /**
     * Get the "id" group.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<GroupDTO> findOne(UUID id);

    /**
     * Delete the "id" group.
     *
     * @param id the id of the entity.
     */
    void delete(UUID id);

    List<GroupDTO> getGroups();

    List<UUID> getUserIdsByGroupId(UUID groupId);

    String activateOrDeactivateGroupById(UUID id);

    void permanentlyDeleteGroup(UUID id);
}
