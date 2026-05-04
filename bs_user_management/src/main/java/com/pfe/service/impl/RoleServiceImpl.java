package com.pfe.service.impl;

import com.pfe.domain.Authority;
import com.pfe.domain.Role;
import com.pfe.domain.User;
import com.pfe.domain.enumeration.RolesType;
import com.pfe.repository.AuthorityRepository;
import com.pfe.repository.RoleRepository;
import com.pfe.repository.UserRepository;
import com.pfe.service.RoleService;
import com.pfe.service.UserService;
import com.pfe.service.dto.AuthorityDTO;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.mapper.RoleMapper;
import com.pfe.validator.IRoleValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service Implementation for managing {@link Role}.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

  private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

  private final RoleRepository roleRepository;

  private final RoleMapper roleMapper;
  private final AuthorityRepository authorityRepository;
  private final UserService userService;
  private final UserRepository userRepository;
  private final Keycloak keycloak;
  @Value("${bs-app.keycloak.realm}")
  private String realm;
  @Value(value = "${bs-app.keycloak.resource}")
  private String clientIdName;

  private final IRoleValidator roleValidator;

  public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper,
      AuthorityRepository authorityRepository, UserService userService,
      UserRepository userRepository,
      Keycloak keycloak, IRoleValidator roleValidator) {
    this.roleRepository = roleRepository;
    this.roleMapper = roleMapper;
    this.authorityRepository = authorityRepository;
    this.userService = userService;
    this.userRepository = userRepository;
    this.keycloak = keycloak;
    this.roleValidator = roleValidator;
  }

//  @Override
//  public RoleDTO save(RoleDTO roleDTO) {
//    log.debug("Request to save Role : {}", roleDTO);
//
//    this.roleValidator.beforeSave(roleDTO);
//    roleDTO.getAuthorities().forEach(authorityDTO ->
//        this.authorityRepository.findById(authorityDTO.getId()).orElseThrow(() ->
//            new ResponseStatusException(HttpStatus.NOT_FOUND,
//                "Authority not found with id " + authorityDTO.getId()))
//    );
//
//    Role role = this.roleMapper.toEntity(roleDTO);
//    role.setIsActive(true);
//    return this.roleMapper.toDto(this.roleRepository.save(role));
//  }
@Override
public RoleDTO save(RoleDTO roleDTO) {
  log.debug("Request to save Role : {}", roleDTO);

  this.roleValidator.beforeSave(roleDTO);

  Set<Authority> resolvedAuthorities = roleDTO.getAuthorities().stream()
      .map(authorityDTO -> {
        // If id exists, look up by id; otherwise look up by label
        if (authorityDTO.getId() != null) {
          return this.authorityRepository.findById(authorityDTO.getId())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                  "Authority not found with id " + authorityDTO.getId()));
        } else {
          return this.authorityRepository.findByLabel(authorityDTO.getLabel())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                  "Authority not found with label " + authorityDTO.getLabel()));
        }
      })
      .collect(Collectors.toSet());

  Role role = this.roleMapper.toEntity(roleDTO);
  role.setAuthorities(resolvedAuthorities);
  role.setIsActive(true);
  return this.roleMapper.toDto(this.roleRepository.save(role));
}

//  @Override
//  public RoleDTO update(RoleDTO roleDTO) {
//    log.debug("Request to update Role : {}", roleDTO);
//    this.roleValidator.beforeUpdate(roleDTO);
//    Role role = this.roleRepository.findById(roleDTO.getId())
//        .orElseThrow(() -> new RuntimeException("Role with id not found"));
//    Set<Authority> roleAuthorities = this.authorityRepository.findAllByIdInAndDeletedFalse(
//        roleDTO.getAuthorities().stream().map(AuthorityDTO::getId).collect(
//            Collectors.toSet()));
//    role.setAuthorities(roleAuthorities);
//    role.setDescription(roleDTO.getDescription());
//    role.setIsActive(roleDTO.getIsActive());
//    this.roleRepository.save(role);
//    this.updateUserAuthorities(role);
//    return this.roleMapper.toDto(role);
//  }
@Override
public RoleDTO update(RoleDTO roleDTO) {
  log.debug("Request to update Role : {}", roleDTO);
  this.roleValidator.beforeUpdate(roleDTO);

  Role role = this.roleRepository.findById(roleDTO.getId())
      .orElseThrow(() -> new RuntimeException("Role with id not found"));

  // Resolve authorities by id OR by label
  Set<Authority> resolvedAuthorities = roleDTO.getAuthorities().stream()
      .map(authorityDTO -> {
        if (authorityDTO.getId() != null) {
          return this.authorityRepository.findById(authorityDTO.getId())
              .filter(a -> !a.getDeleted())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                  "Authority not found with id " + authorityDTO.getId()));
        } else {
          return this.authorityRepository.findByLabel(authorityDTO.getLabel())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                  "Authority not found with label " + authorityDTO.getLabel()));
        }
      })
      .collect(Collectors.toSet());

  role.setAuthorities(resolvedAuthorities);
  role.setDescription(roleDTO.getDescription());
  role.setIsActive(roleDTO.getIsActive());
  this.roleRepository.save(role);
  this.updateUserAuthorities(role);
  return this.roleMapper.toDto(role);
}

  private void updateUserAuthorities(Role role) {
    Set<Authority> finalListAuthority = new HashSet<>();

    List<User> userListByRoleId = this.userRepository.findAllUsersByRoleId(
        role.getId());

    userListByRoleId.forEach(user -> {
      Set<Role> userRoles = user.getRoles();
      userRoles.forEach(roleUser -> finalListAuthority.addAll(roleUser.getAuthorities()));
      List<ClientRepresentation> clients = this.keycloak.realm(this.realm).clients()
          .findByClientId(this.clientIdName);
      String clientId = clients.get(0).getId();
      UsersResource usersResource = this.keycloak.realm(this.realm).users();
      UserResource userResource = usersResource.get(
          String.valueOf(user.getKeycloakId()));
      UserRepresentation userRepresentation = userResource.toRepresentation();

      removeAllClientRolesFromUser(String.valueOf(user.getKeycloakId()), clientId);

      finalListAuthority.forEach(
          authority -> assignRoleToUser(this.keycloak, String.valueOf(userRepresentation.getId()),
              authority.getLabel()));
    });

  }

  private void assignRoleToUser(Keycloak keycloak, String userId, String roleName) {
    List<ClientRepresentation> clients = keycloak.realm(this.realm).clients()
        .findByClientId(this.clientIdName);

    if (clients.isEmpty()) {
      return;
    }

    String clientId = clients.get(0).getId();
    List<RoleRepresentation> clientRoles = keycloak.realm(this.realm).clients().get(clientId)
        .roles().list();

    RoleRepresentation roleToAssign = clientRoles.stream()
        .filter(role -> role.getName().equals(roleName))
        .findFirst()
        .orElse(null);

    if (roleToAssign != null) {
      keycloak.realm(this.realm).users().get(userId).roles().clientLevel(clientId)
          .add(Collections.singletonList(roleToAssign));
    }
  }

  public void removeAllClientRolesFromUser(String userId, String clientId) {
    List<RoleRepresentation> roles = this.keycloak.realm(this.realm)
        .clients().get(clientId)
        .roles()
        .list();

    this.keycloak.realm(this.realm)
        .users()
        .get(userId)
        .roles()
        .clientLevel(clientId)
        .remove(roles);
  }

  @Override
  public Optional<RoleDTO> partialUpdate(RoleDTO roleDTO) {
    log.debug("Request to partially update Role : {}", roleDTO);

    return this.roleRepository
        .findById(roleDTO.getId())
        .map(existingRole -> {
          this.roleMapper.partialUpdate(existingRole, roleDTO);

          return existingRole;
        })
        .map(this.roleRepository::save)
        .map(this.roleMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<RoleDTO> findOne(UUID id) {
    log.debug("Request to get Role : {}", id);
    return this.roleRepository.findById(id)
        .map(this.roleMapper::toDto)
        .map(this::checkIfHasUsers);
  }

  @Override
  public void delete(UUID id) {
    log.debug("Request to delete Role : {}", id);
    Role role = this.roleRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Role with id not found"));
    this.removeAuthoritiesFromRole(role);
    this.userService.removeRoleAssociation(id);
    this.roleRepository.deleteById(id);
  }

  @Override
  public Page<RoleDTO> getRolesByUserId(UUID id, Pageable pageable) {
    Optional<UserDTO> userDTO = this.userService.findOne(id);

    List<RoleDTO> roleDTOS = userDTO
        .map(user -> user.getRoles().stream()
            .toList())
        .orElse(Collections.emptyList());

    int start = (int) pageable.getOffset();
    int end = Math.min((start + pageable.getPageSize()), roleDTOS.size());

    List<RoleDTO> pageContent = roleDTOS.subList(start, end);
    return new PageImpl<>(pageContent, pageable, roleDTOS.size());
  }

  @Override
  public List<RoleDTO> getAllRoles() {
    return this.roleMapper.toDto(this.roleRepository.findAll());
  }

  @Override
  public List<UUID> getUserIdsByRoleId(UUID roleId) {
    return this.roleRepository.getUserIdsByRoleId(roleId);
  }

  @Override
  public void activateOrDeactivateRoleById(UUID roleId) {
    Role role = this.roleRepository.findById(roleId)
        .orElseThrow(() -> new RuntimeException("Role with id not found"));
    role.setIsActive(!role.getIsActive());
    this.roleRepository.save(role);
  }

  @Override
  public void permanentlyDeleteRole(UUID roleId) {
    Role role = this.roleRepository.findById(roleId)
        .orElseThrow(() -> new RuntimeException("Role with id not found"));
    role.setDeleted(true);
    role.setAuthorities(null);
    this.roleRepository.save(role);
  }

    private void removeAuthoritiesFromRole(Role role) {
    role.setAuthorities(null);
    this.roleRepository.save(role);
  }

  private RoleDTO checkIfHasUsers(RoleDTO roleDTO) {
    roleDTO.setHasUsers(!this.roleRepository.getUserIdsByRoleId(roleDTO.getId()).isEmpty());
    return roleDTO;
  }
    @Override
    public RoleDTO getRoleByLabel(String label) {
        return roleRepository.findByLabel(label)
            .map(roleMapper::toDto)
            .orElseThrow(() -> new RuntimeException("Rôle introuvable avec le label : " + label));
    }

  @Override
  public List<RoleDTO> getActiveInterneRoles() {
    List<Role> interneRoles = roleRepository.findByRoleTypeAndIsActive(RolesType.INTERNE, true);
    List<RoleDTO> interneRolesDto = roleMapper.toDto(interneRoles);
    return interneRolesDto;
  }
}
