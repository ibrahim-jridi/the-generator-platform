package com.pfe.web.rest;

import com.pfe.repository.RoleRepository;
import com.pfe.service.RoleQueryService;
import com.pfe.service.RoleService;
import com.pfe.service.criteria.RoleCriteria;
import com.pfe.service.dto.RoleDTO;
import com.pfe.web.rest.errors.BadRequestAlertException;
import com.pfe.domain.Role;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Role}.
 */
@RestController
@RequestMapping("/api/v1/roles")
public class RoleResource {

  private static final Logger log = LoggerFactory.getLogger(RoleResource.class);

  private static final String ENTITY_NAME = "bsUserManagementRole";
  private final RoleService roleService;
  private final RoleRepository roleRepository;
  private final RoleQueryService roleQueryService;
  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  public RoleResource(RoleService roleService, RoleRepository roleRepository,
      RoleQueryService roleQueryService) {
    this.roleService = roleService;
    this.roleRepository = roleRepository;
    this.roleQueryService = roleQueryService;
  }

  /**
   * {@code POST  /roles} : Create a new role.
   *
   * @param roleDTO the roleDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   * roleDTO, or with status {@code 400 (Bad Request)} if the role has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO)
      throws URISyntaxException {
    log.debug("REST request to save Role : {}", roleDTO);

    roleDTO = this.roleService.save(roleDTO);
    return ResponseEntity.created(new URI("/api/v1/roles/" + roleDTO.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(this.applicationName, true, ENTITY_NAME,
            roleDTO.getId().toString()))
        .body(roleDTO);
  }

  /**
   * {@code PUT  /roles/:id} : Updates an existing role.
   *
   * @param id      the id of the roleDTO to save.
   * @param roleDTO the roleDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   * roleDTO, or with status {@code 400 (Bad Request)} if the roleDTO is not valid, or with status
   * {@code 500 (Internal Server Error)} if the roleDTO couldn't be updated.
   */
  @PutMapping("/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<RoleDTO> updateRole(
      @PathVariable(value = "id", required = false) final UUID id,
      @RequestBody RoleDTO roleDTO) {
    log.debug("REST request to update Role : {}, {}", id, roleDTO);
    if (roleDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, roleDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!this.roleRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    roleDTO = this.roleService.update(roleDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, true, ENTITY_NAME,
            roleDTO.getId().toString()))
        .body(roleDTO);
  }

  /**
   * {@code GET  /roles} : get all the roles.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles in body.
   */
  @GetMapping("")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<RoleDTO>> getAllRoles(
      RoleCriteria criteria,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable
  ) {
    log.debug("REST request to get Roles by criteria: {}", criteria);

    Page<RoleDTO> page = this.roleQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
        ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /roles/count} : count all the roles.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
   */
  @GetMapping("/count")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Long> countRoles(RoleCriteria criteria) {
    log.debug("REST request to count Roles by criteria: {}", criteria);
    return ResponseEntity.ok().body(this.roleQueryService.countByCriteria(criteria));
  }

  /**
   * {@code GET  /roles/:id} : get the "id" role.
   *
   * @param id the id of the roleDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleDTO, or
   * with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<RoleDTO> getRole(@PathVariable("id") UUID id) {
    log.debug("REST request to get Role : {}", id);
    Optional<RoleDTO> roleDTO = this.roleService.findOne(id);
    return ResponseUtil.wrapOrNotFound(roleDTO);
  }

  /**
   * {@code DELETE  /roles/:id} : delete the "id" role.
   *
   * @param id the id of the roleDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Void> deleteRole(@PathVariable("id") UUID id) {
    log.debug("REST request to delete Role : {}", id);
    this.roleService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME,
                id.toString()))
        .build();
  }

  @GetMapping("/getRolesByUserId/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Page<RoleDTO>> getRolesByUserId(@PathVariable("id") UUID id,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.debug("REST request to get Roles By UserId  : {}", id);
    return ResponseEntity.ok(this.roleService.getRolesByUserId(id, pageable));
  }

  @GetMapping("/all")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.BS_USER + "\") ")
  public ResponseEntity<List<RoleDTO>> findAllRoles() {
    return ResponseEntity.ok(this.roleService.getAllRoles());
  }

  @GetMapping("/getUserIdsByRoleId")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UUID>> getUserIdsByRoleId(@RequestParam UUID id) {
    return ResponseEntity.ok().body(this.roleService.getUserIdsByRoleId(id));
  }

  @GetMapping("/activate-or-deactivate-role-by-id/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Void> activateOrDeactivateRoleById(@PathVariable("id") UUID roleId) {
    this.roleService.activateOrDeactivateRoleById(roleId);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, true, ENTITY_NAME,
            roleId.toString()))
        .build();
  }

  @DeleteMapping("/delete-role-permanently/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Void> permanentlyDeleteRole(@PathVariable UUID id) {
    this.roleService.permanentlyDeleteRole(id);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME,
            id.toString()))
        .build();
  }

    @GetMapping("/get-role-by-label/{label}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<RoleDTO> getRoleByLabel(@PathVariable String label) {
        RoleDTO roleDTO = roleService.getRoleByLabel(label);
        return ResponseEntity.ok(roleDTO);
    }

  @GetMapping("/get-interne-roles")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<RoleDTO>> getActiveInterneRoles() {
    List<RoleDTO> internRolesDTO = roleService.getActiveInterneRoles();
    return ResponseEntity.ok(internRolesDTO);
  }
}
