package com.pfe.service;

import com.pfe.service.dto.RoleDTO;
import com.pfe.domain.Role;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for  managing {@link Role}.
 */
public interface RoleService {

  /**
   * Save a role.
   *
   * @param roleDTO the entity to save.
   * @return the persisted entity.
   */
  RoleDTO save(RoleDTO roleDTO);

  /**
   * Updates a role.
   *
   * @param roleDTO the entity to update.
   * @return the persisted entity.
   */
  RoleDTO update(RoleDTO roleDTO);

  /**
   * Partially updates a role.
   *
   * @param roleDTO the entity to update partially.
   * @return the persisted entity.
   */
  Optional<RoleDTO> partialUpdate(RoleDTO roleDTO);

  /**
   * Get the "id" role.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<RoleDTO> findOne(UUID id);

  /**
   * Delete the "id" role.
   *
   * @param id the id of the entity.
   */
  void delete(UUID id);

  Page<RoleDTO> getRolesByUserId(UUID id, Pageable pageable);

  List<RoleDTO> getAllRoles();

  List<UUID> getUserIdsByRoleId(UUID roleId);

  void activateOrDeactivateRoleById(UUID roleId);

  void permanentlyDeleteRole(UUID id);
  RoleDTO getRoleByLabel(String label);
  List<RoleDTO> getActiveInterneRoles();
}
