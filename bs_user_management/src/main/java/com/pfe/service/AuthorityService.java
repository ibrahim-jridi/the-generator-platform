package com.pfe.service;

import com.pfe.service.dto.AuthorityDTO;
import com.pfe.domain.Authority;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Interface for managing {@link Authority}.
 */
public interface AuthorityService {

  /**
   * Get the "id" authority.
   *
   * @param id the id of the entity.
   * @return the entity.
   */
  Optional<AuthorityDTO> findOne(UUID id);

  List<AuthorityDTO> findAllAuthorities();

  List<AuthorityDTO> getAuthoritiesNotInRole(UUID id);
}
