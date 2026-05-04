package com.pfe.service.impl;

import com.pfe.domain.Authority;
import com.pfe.repository.AuthorityRepository;
import com.pfe.service.AuthorityService;
import com.pfe.service.RoleService;
import com.pfe.service.dto.AuthorityDTO;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.mapper.AuthorityMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Authority}.
 */
@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

    private static final Logger log = LoggerFactory.getLogger(AuthorityServiceImpl.class);

    private final AuthorityRepository authorityRepository;

    private final AuthorityMapper authorityMapper;
    private final RoleService roleService;
    private final Keycloak keycloak;
    @Value("${bs-app.keycloak.realm}")
    private String realm;
    @Value(value = "${bs-app.keycloak.resource}")
    private String clientIdName;
    public AuthorityServiceImpl(AuthorityRepository authorityRepository,
        AuthorityMapper authorityMapper, RoleService roleService, Keycloak keycloak) {
        this.authorityRepository = authorityRepository;
        this.authorityMapper = authorityMapper;
        this.roleService = roleService;
        this.keycloak = keycloak;
    }

    @Transactional
    public void syncAuthorities() {
        List<Authority> authorities = this.authorityRepository.findAuthoritiesWithFirstInsert();
        addAuthorityToKeycloak(authorities);
    }
    private void addAuthorityToKeycloak(List<Authority> authorities)
        throws IllegalArgumentException {
        List<ClientRepresentation> clients = this.keycloak.realm(this.realm).clients()
            .findByClientId(this.clientIdName);
        if (clients.isEmpty()) {
            throw new IllegalArgumentException("Client not found in Keycloak.");
        }
        String clientId = clients.get(0).getId();

        List<RoleRepresentation> rolesKeyclock = this.keycloak.realm(this.realm).clients()
            .get(clientId)
            .roles()
            .list();
        List<Authority> missingAuthoritiesToAdd = new ArrayList<>();

        for (Authority authority : authorities) {
            Optional<RoleRepresentation> existingRole = rolesKeyclock.stream()
                .filter(role -> role.getName().equals(authority.getLabel()))
                .findFirst();

            if (!existingRole.isPresent()) {
                missingAuthoritiesToAdd.add(authority);
                log.info(
                    "Authority {} does not exist in Keycloak.", authority.getLabel());
            } else {
                log.error("Role exists: {} ", existingRole.get().getName());
            }
        }

        if (!missingAuthoritiesToAdd.isEmpty()) {
            addMissingRolesToKeycloak(missingAuthoritiesToAdd, clientId);
        } else {
            log.error("All authorities already exist in Keycloak.");
        }
    }

    private void addMissingRolesToKeycloak(List<Authority> missingAuthorities, String clientId) {
        for (Authority authority : missingAuthorities) {
            RoleRepresentation newRole = new RoleRepresentation();
            newRole.setName(authority.getLabel());
            newRole.setDescription(
                "Automatically created role for authority: " + authority.getLabel());

            this.keycloak.realm(this.realm).clients().get(clientId).roles().create(newRole);
            log.info("Added missing role: {}", newRole.getName());
            updateDirtInsertAuthority(authority);
        }
    }

    private void updateDirtInsertAuthority(Authority authority) {
        Authority updateAuthority = this.authorityRepository.findById(authority.getId())
            .orElse(null);
        if (updateAuthority != null) {
            updateAuthority.setFirstInsert(false);
            this.authorityRepository.save(updateAuthority);
            log.info("Changed value of 'firstInsert' authority to 0.");
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AuthorityDTO> findOne(UUID id) {
        log.debug("Request to get Authority : {}", id);
        return this.authorityRepository.findById(id).map(this.authorityMapper::toDto);
    }

//    @Override
//    public List<AuthorityDTO> findAllAuthorities() {
//        return this.authorityMapper.toDto(this.authorityRepository.findAll());
//    }
//
//    @Override
//    public List<AuthorityDTO> getAuthoritiesNotInRole(UUID id) {
//        RoleDTO roleDTO = this.roleService.findOne(id)
//            .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));
//
//        Set<AuthorityDTO> authorityDTOS = roleDTO.getAuthorities();
//        List<AuthorityDTO> authorityDTOList = this.authorityMapper.toDto(
//            this.authorityRepository.findAll());
//
//        // Remove authorities that are in authorityDTOS from authorityDTOList
//        authorityDTOList.removeAll(authorityDTOS);
//
//        return authorityDTOList;
//    }
@Override
public List<AuthorityDTO> findAllAuthorities() {
    String clientId = getClientId();

    List<RoleRepresentation> keycloakRoles = this.keycloak.realm(this.realm)
        .clients()
        .get(clientId)
        .roles()
        .list();

    // Map Keycloak roles to local Authority entities by label, fallback to label-only DTO
    List<Authority> localAuthorities = this.authorityRepository.findAll();
    Map<String, Authority> localByLabel = localAuthorities.stream()
        .collect(Collectors.toMap(Authority::getLabel, a -> a, (a1, a2) -> a1));

    return keycloakRoles.stream()
        .map(role -> {
            Authority local = localByLabel.get(role.getName());
            if (local != null) {
                return this.authorityMapper.toDto(local);
            }
            // Role exists in Keycloak but not locally — map manually
            AuthorityDTO dto = new AuthorityDTO();
            dto.setLabel(role.getName());
            dto.setDescription(role.getDescription());
            return dto;
        })
        .collect(Collectors.toList());
}

    @Override
    public List<AuthorityDTO> getAuthoritiesNotInRole(UUID id) {
        RoleDTO roleDTO = this.roleService.findOne(id)
            .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));

        Set<AuthorityDTO> authorityDTOS = roleDTO.getAuthorities();

        // Use the updated findAllAuthorities() which pulls from Keycloak
        List<AuthorityDTO> allAuthorities = findAllAuthorities();
        allAuthorities.removeAll(authorityDTOS);

        return allAuthorities;
    }

// --- private helper ---

    private String getClientId() {
        List<ClientRepresentation> clients = this.keycloak.realm(this.realm)
            .clients()
            .findByClientId(this.clientIdName);
        if (clients.isEmpty()) {
            throw new IllegalArgumentException("Client '" + this.clientIdName + "' not found in Keycloak.");
        }
        return clients.get(0).getId();
    }
}
