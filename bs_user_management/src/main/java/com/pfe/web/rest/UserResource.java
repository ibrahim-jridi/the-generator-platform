package com.pfe.web.rest;
import com.pfe.domain.User;
import com.pfe.repository.RoleRepository;
import com.pfe.repository.UserRepository;
import com.pfe.security.SecurityUtils;
import com.pfe.security.Jwt.JwtTokenUtil;
import com.pfe.service.KeycloakAuthService;
import com.pfe.service.UserQueryService;
import com.pfe.service.UserService;
import com.pfe.service.criteria.UserCriteria;
import com.pfe.service.dto.ResetPasswordDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.dto.UserDenominationDTO;
import com.pfe.service.dto.request.CreateAccountRequest;
import com.pfe.service.dto.request.CreateCompanyAccountRequest;
import com.pfe.service.dto.ProfileDTO;
import com.pfe.service.dto.request.UpdateProfileRequest;
import com.pfe.service.dto.response.CheckEligibilityResponse;
import com.pfe.web.rest.errors.BadRequestAlertException;
import com.pfe.web.rest.errors.FieldErrorVM;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link User}.
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserResource {

  private static final Logger log = LoggerFactory.getLogger(UserResource.class);

  private static final String ENTITY_NAME = "bsUserManagementUser";
  private final KeycloakAuthService keycloakAuthService;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserService userService;
  private final UserRepository userRepository;
  private final UserQueryService userQueryService;
  private final Keycloak keycloak;
  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final RoleRepository roleRepository;

  @Value("${bs-app.keycloak.realm}")
  private String realm;

    private static final String CACHE_NAME = "applicantCache";


  public UserResource(
      UserService userService,
      UserRepository userRepository,
      UserQueryService userQueryService,
      Keycloak keycloak,
      KeycloakAuthService keycloakAuthService, JwtTokenUtil jwtTokenUtil, RoleRepository roleRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
    this.userQueryService = userQueryService;
    this.keycloak = keycloak;
    this.keycloakAuthService = keycloakAuthService;
    this.jwtTokenUtil = jwtTokenUtil;
      this.roleRepository = roleRepository;
  }

  /**
   * {@code POST  /user} : Create a new User.
   *
   * @param createAccountRequest the userDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   * userDTO, or with status  {@code 400 (Bad Request)} if the User has already an ID.
   */
  @PostMapping("")
  @Operation(
      summary = "Create a new user",
      description = "Create a new user by providing the user details."
  )
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "201",
              description = "User created successfully.",
              content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad Request. The user already has an ID or the request is invalid.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Unauthorized. User authentication is required.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "403",
              description = "Forbidden. The user does not have the required role to create a user.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal Server Error. An unexpected error occurred on the server.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "default",
              description = "An unexpected error occurred.",
              content = {
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = FieldErrorVM.class),
                      examples = @ExampleObject(
                          value = "{\n" +
                              "  \"type\": \"/error/type\",\n" +
                              "  \"title\": \"Error Title\",\n" +
                              "  \"status\": 500,\n" +
                              "  \"detail\": \"Detailed description of the error\",\n" +
                              "  \"instance\": \"/error/instance\"\n" +
                              "}"
                      )
                  )
              }
          )
      }
  )
//  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<UserDTO> createUser(
      @RequestBody CreateAccountRequest createAccountRequest , @RequestParam(required = false, defaultValue = "false") Boolean confirm ) {
    log.info("REST request to save User : {}", createAccountRequest);
    UserDTO userDTO = this.userService.createUser(createAccountRequest,confirm);
    return ResponseEntity.ok().body(userDTO);
  }

  @PostMapping("/company-user")
  @Operation(
      summary = "Create a new company user",
      description = "Create a new company user by providing the user details."
  )
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "201",
              description = "Company user created successfully.",
              content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad Request. The  user already has an ID or the request is invalid.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Unauthorized. User authentication is required.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "403",
              description = "Forbidden. The user does not have the required role to create a user.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal Server Error. An unexpected error occurred on the server.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "default",
              description = "An unexpected error occurred.",
              content = {
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = FieldErrorVM.class),
                      examples = @ExampleObject(
                          value = "{\n" +
                              "  \"type\": \"/error/type\",\n" +
                              "  \"title\": \"Error Title\",\n" +
                              "  \"status\": 500,\n" +
                              "  \"detail\": \"Detailed description of the error\",\n" +
                              "  \"instance\": \"/error/instance\"\n" +
                              "}"
                      )
                  )
              }
          )
      }
  )
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<UserDTO> createCompanyUser(
        @RequestBody CreateCompanyAccountRequest createCompanyAccountRequest) {
        log.info("REST request to create company User : {}", createCompanyAccountRequest);
        UserDTO userDTO = this.userService.createCompanyUser(createCompanyAccountRequest);
        return ResponseEntity.ok().body(userDTO);
    }

  /**
   * {@code PUT  /user/:id} : Updates an existing User.
   *
   * @param id      the id of the UserDTO to save.
   * @param userDTO the UserDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   * userDTO, or with status {@code 400 (Bad Request)} if the UserDTO is not valid, or with status
   * {@code 500 (Internal Server Error)} if the UserDTO couldn't be updated.
   */
  @PutMapping("/{id}")
  @Operation(
      summary = "Update a user",
      description = "Update a user by providing the user id."
  )
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "201",
              description = "User updated successfully.",
              content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
          ),
          @ApiResponse(
              responseCode = "400",
              description = "Bad Request.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Unauthorized. User authentication is required.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "403",
              description = "Forbidden. The user does not have the required role to update a user.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal Server Error. An unexpected error occurred on the server.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "default",
              description = "An unexpected error occurred.",
              content = {
                  @Content(
                      mediaType = "application/json",
                      schema = @Schema(implementation = FieldErrorVM.class),
                      examples = @ExampleObject(
                          value = "{\n" +
                              "  \"type\": \"/error/type\",\n" +
                              "  \"title\": \"Error Title\",\n" +
                              "  \"status\": 500,\n" +
                              "  \"detail\": \"Detailed description of the error\",\n" +
                              "  \"instance\": \"/error/instance\"\n" +
                              "}"
                      )
                  )
              }
          )
      }
  )
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.BS_USER + "\")")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable(value = "id", required = false) final UUID id,
      @Valid @RequestBody UserDTO userDTO
  ) {
    log.info("REST request to update User : {}, {}", id, userDTO);
    if (userDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, userDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!this.userRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    userDTO = this.userService.update(userDTO);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, true, ENTITY_NAME,
            userDTO.getId().toString()))
        .body(userDTO);
  }

  /**
   * {@code PATCH  /user/:id} : Partial updates given fields of an existing User, field will ignore
   * if it is null
   *
   * @param id      the id of the UserDTO to save.
   * @param userDTO the UserDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   * UserDTO, or with status {@code 400 (Bad Request)} if the UserDTO is not valid, or with status
   * {@code 404 (Not Found)} if the UserDTO is not found, or with status
   * {@code 500 (Internal Server Error)} if the UserDTO couldn't be updated.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<UserDTO> partialUpdateUser(
      @PathVariable(value = "id", required = false) final UUID id,
      @NotNull @RequestBody UserDTO userDTO
  ) {
    log.info("REST request to partial update User partially : {}, {}", id,
        userDTO);
    if (userDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, userDTO.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!this.userRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<UserDTO> result = this.userService.partialUpdate(userDTO);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(this.applicationName, true, ENTITY_NAME,
            userDTO.getId().toString())
    );
  }

  /**
   * {@code GET  /user} : get all the Users.
   *
   * @param pageable the pagination information.
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Users in body.
   */
  @GetMapping("")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UserDTO>> getAllUsers(
      UserCriteria criteria,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable
  ) {
    log.info("REST request to get Users by criteria: {}", criteria);

    Page<UserDTO> page = this.userQueryService.findByCriteria(criteria, pageable);
    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
        ServletUriComponentsBuilder.fromCurrentRequest(), page);
    return ResponseEntity.ok().headers(headers).body(page.getContent());
  }

  /**
   * {@code GET  /user/count} : count all the Users.
   *
   * @param criteria the criteria which the requested entities should match.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
   */
  @GetMapping("/count")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Long> countUsers(UserCriteria criteria) {
    log.info("REST request to count Users by criteria: {}", criteria);
    return ResponseEntity.ok().body(this.userQueryService.countByCriteria(criteria));
  }


  @GetMapping("/get-all-physical-users")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UserDTO>> getAllPhysicalUsers() {
    log.info("REST request to get all physical users");
    return ResponseEntity.ok().body(this.userService.getAllPhysicalUsers());
  }


  /**
   * {@code GET  /user/:id} : get the "id" User.
   *
   * @param id the id of the UserDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the UserDTO, or
   * with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<UserDTO> getUser(@PathVariable("id") UUID id) {
    log.info("REST request to get User : {}", id);
    Optional<UserDTO> userDTO = this.userService.findOne(id);
    return ResponseUtil.wrapOrNotFound(userDTO);
  }

  /**
   * {@code DELETE  /user/:id} : delete the "id" User.
   *
   * @param id the id of the UserDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Void> deleteUser(@PathVariable("id") UUID id) {
    log.info("REST request to delete User : {}", id);
    this.userService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME,
                id.toString()))
        .build();
  }

  @GetMapping("/users")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UserRepresentation>> getAllUsers() {
      List<UserRepresentation> users = userService.getAllUsersFromKeycloak();
      return ResponseEntity.ok(users);
  }

  @PutMapping("/delete/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<UserDTO> updateUser(
      @PathVariable(value = "id", required = false) final UUID id
  ) {
    log.info("REST request to delete User : {}", id);

    if (!this.userRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }
    return ResponseEntity.ok(this.userService.deleteUser(id));

  }


  @PutMapping("/completeProfile/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<UserDTO> completeUserProfile(
      @PathVariable(value = "id", required = false) final UUID id,
      @RequestBody UserDTO userDTO
  ) {
    log.info("REST request to complete User : {}", id);

    if (!this.userRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }
    return ResponseEntity.ok(
        this.userService.completeUserProfile(userDTO, String.valueOf(id)).orElse(null));

  }


  @GetMapping("/resend-email-verification")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Boolean> resendVerificationMail(HttpServletRequest request) {
    String token = getTokenFromRequest(request);
    this.keycloakAuthService.getUserByToken(token);
    if (token != null) {
      Claims claims = this.jwtTokenUtil.extractAllClaims(token);
      String username = claims.getSubject();
      this.userService.resendEmailVerification(token);
    }
    return ResponseEntity.ok().body(true);
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  @GetMapping("/getUsersByGroupId/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Page<UserDTO>> getUsersByGroupId(
      @PathVariable("id") UUID id,
      @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
    log.info("REST request to get Groups By UserId  : {}", id);
    return ResponseEntity.ok(this.userService.getUsersByGroupId(id, pageable));
  }

  @GetMapping("/all")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UserDTO>> findAllUsers() {
    log.info("REST request to get all users");
    return ResponseEntity.ok(this.userService.getAllUsers());
  }

  @GetMapping("/getProfileCompleted")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<String> getIsProfileCompleted(@RequestParam("username") String username) {
    log.info("REST reqyest to get if user profile completed");
    try {
      String isProfileCompleted = this.userService.isProfileCompleted(username);
      return ResponseEntity.ok(isProfileCompleted);
    } catch (Exception e) {
      throw e;
    }
  }

  @GetMapping("/by-ids")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UserDTO>> getUsersByIds(@RequestParam List<UUID> ids) {
    log.info("REST request to get Users by IDs : {}", ids);
    try {
      List<UserDTO> users = this.userService.findByIds(ids);
      if (users.isEmpty()) {
        return ResponseEntity.noContent().build();
      } else {
        return ResponseEntity.ok(users);
      }
    } catch (ResponseStatusException e) {
      log.error("Error retrieving users by IDs", e);
      throw e;
    }
  }

    @GetMapping("/getEmailById")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<String> getEmailById(@RequestParam UUID id) {
        return ResponseEntity.ok().body(this.userService.getEmailById(id));
    }

  @GetMapping("/getGroupIdsByUserId")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UUID>> getGroupIdsByUserId(@RequestParam UUID id) {
    return ResponseEntity.ok().body(this.userService.getGroupIdsByUserId(id));
  }

  @GetMapping("/getRoleIdsByUserId")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UUID>> getRoleIdsByUserId(@RequestParam UUID id) {
    return ResponseEntity.ok().body(this.userService.getRoleIdsByUserId(id));
  }

  @GetMapping("/get-all-group-users-by-group-id/{id}")
  @Operation(
      summary = "Get user by group ID",
      description = "Retrieve a list of user belonging to a specific group identified by its ID."
  )
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved the list of user .",
              content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class))
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Unauthorized. User authentication is required.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "403",
              description = "Forbidden. The user does not have the required role to create a group.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Group not found.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(schema = @Schema(implementation = Void.class))
          )
      }
  )
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UserDTO>> getUserByGroupId(
      @PathVariable(value = "id", required = false) final UUID id) {
    return ResponseEntity.ok().body(this.userService.getUsersByGroupId(id));
  }

  @PostMapping("/reassign-users-to-new-groups")
  @Operation(
      summary = "Reassign users to group",
      description = "Reassign list of users to new groups"
  )
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully reassign list of users to new groups",
              content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class))
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Unauthorized. User authentication is required.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "403",
              description = "Forbidden. The user does not have the required role to create a group.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Group not found.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(schema = @Schema(implementation = Void.class))
          )
      }
  )
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UserDTO>> reassignUserToNewGroups(
      @RequestBody List<UserDTO> usersToReassign) {
    this.userService.reassignUsersToNewGroups(usersToReassign);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME,
            "Updated"))
        .build();
  }

  @GetMapping("/get-all-role-users-by-role-id/{id}")
  @Operation(
      summary = "Get user by role ID",
      description = "Retrieve a list of user associated to role identified by its ID."
  )
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully retrieved the list of user .",
              content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class))
          ),
          @ApiResponse(
              responseCode = "401",
              description = "Unauthorized. User authentication is required.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "403",
              description = "Forbidden. The user does not have the required role to create a group.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "404",
              description = "Group not found.",
              content = @Content(schema = @Schema(implementation = Void.class))
          ),
          @ApiResponse(
              responseCode = "500",
              description = "Internal server error.",
              content = @Content(schema = @Schema(implementation = Void.class))
          )
      }
  )
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<List<UserDTO>> getUsersByRoleId(
      @PathVariable(value = "id") final UUID id) {
    return ResponseEntity.ok().body(this.userService.getUsersByRoleId(id));
  }

    @PostMapping("/reassign-users-to-new-roles")
    @Operation(
        summary = "Reassign users to roles",
        description = "Reassign list of users to new roles"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully reassign list of users to new roles",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UUID.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized. User authentication is required.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden. The user does not have the required role to create a group.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Group not found.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error.",
                content = @Content(schema = @Schema(implementation = Void.class))
            )
        }
    )
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<UserDTO>> reassignUserToNewRoles(
        @RequestBody List<UserDTO> usersToReassign) {
        this.userService.reassignUsersToNewRoles(usersToReassign);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME,
                "Updated"))
            .build();
    }


  @PostMapping("/reset-password-first-connection")
  public ResponseEntity<Void> resetPasswordFirstConnection(
      @RequestBody ResetPasswordDTO resetPasswordDTO) {
    this.userService.resetPasswordFirstConnection(resetPasswordDTO);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME,
            "Updated"))
        .build();
  }

  //@PreAuthorize("hasRole('" + AuthoritiesConstants.BS_USER + "')")
    @PostMapping("/profile")
    public ResponseEntity<UserDTO> saveAndCompleteMyProfile(
      @RequestBody ProfileDTO profileDTO) {
        String userId = SecurityUtils.getUserIdFromCurrentUser();
    return this.userService.saveAndCompleteMyProfile(UUID.fromString(userId), profileDTO);
    }

    @PutMapping("/update-user")
    @Operation(
        summary = "Update a user",
        description = "Update a user by providing the user id."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "User updated successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad Request.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized. User authentication is required.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden. The user does not have the required role to update a user.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal Server Error. An unexpected error occurred on the server.",
                content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                responseCode = "default",
                description = "An unexpected error occurred.",
                content = {
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = FieldErrorVM.class),
                        examples = @ExampleObject(
                            value = "{\n" +
                                "  \"type\": \"/error/type\",\n" +
                                "  \"title\": \"Error Title\",\n" +
                                "  \"status\": 500,\n" +
                                "  \"detail\": \"Detailed description of the error\",\n" +
                                "  \"instance\": \"/error/instance\"\n" +
                                "}"
                        )
                    )
                }
            )
        }
    )
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<UserDTO> updateAndCompletedProfile(
        @Valid @RequestBody UpdateProfileRequest updateProfileRequest
        ) {
        String userId = SecurityUtils.getUserIdFromCurrentUser();
        UUID userUuid = UUID.fromString(userId);
        log.info("REST request to update User : {}, {}", userUuid, updateProfileRequest.getUserDTO());
        if (updateProfileRequest.getUserDTO().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(userUuid, updateProfileRequest.getUserDTO().getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!this.userRepository.existsById(userUuid)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserDTO userDTO= this.userService.updateAndCompletedProfile(updateProfileRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, true, ENTITY_NAME,
                userDTO.getId().toString()))
            .body(userDTO);
    }
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username, @RequestParam String roleLabel) {
        CheckEligibilityResponse result = userService.checkUsernameEligibility(username, roleLabel);
        Boolean status = result.getStatus();
        HttpHeaders headers = result.getHeaders();
        return ResponseEntity.ok().headers(headers).body(status);
  }

    @GetMapping("/profile/exists")
    public ResponseEntity<Boolean> doesProfileKeyExist(
        @RequestParam UUID userId,
        @RequestParam String key
    ) {
        log.info("REST request to check if key exists by userId in profile: {}", key);
        boolean exists = userService.doesProfileKeyExist(userId, key);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/profile/value")
    public ResponseEntity<String> getProfileValue(
        @RequestParam UUID userId,
        @RequestParam String key
    ) {
        log.info("REST request to get Value of key in profile : {}", key);
        return userService.getProfileValue(userId, key)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/user-by-role/exists")
    public ResponseEntity<Boolean> existsUserByRole(
        @RequestParam UUID userId,
        @RequestParam UUID roleId
    ) {
        log.info("REST request to check if user had role by roleId : {}", roleId);
        boolean exists = userService.existsUserByRole(userId, roleId);
        return ResponseEntity.ok(exists);
    }
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    @GetMapping("/user-by-phone-number/exists")
    public ResponseEntity<Boolean> existPhoneNumber(
        @RequestParam String phoneNumber
    ) {
        log.info("REST request to check if phone Number exist : {}", phoneNumber);
        boolean exists = userService.existPhoneNumber(phoneNumber);
        return ResponseEntity.ok(exists);
    }

  @GetMapping("/denominations")
  //@PreAuthorize("hasRole('" + AuthoritiesConstants.BS_USER + "') or hasRole('" + AuthoritiesConstants.BS_ADMIN + "')")
  public ResponseEntity<List<UserDenominationDTO>> getUsernamesAndDenominationsByRole(@RequestParam String roleLabel) {
    log.info("REST request to get usernames and denominations for roleLabel: {}", roleLabel);
    List<UserDenominationDTO> result = userService.getUsernamesAndDenominationsByRole(roleLabel);
    return ResponseEntity.ok(result);
  }

    @GetMapping("/get-username-by-id/{id}")
    public ResponseEntity<String> getUsernameById(@PathVariable UUID id) {
        String username = userService.getUsernameById(id);
        return ResponseEntity.ok(username);
    }

    @PostMapping("/profile/pm")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<UserDTO> saveDataPm(
        @RequestParam String  keycloackId,
        @RequestBody ProfileDTO profileDTO) {
        return this.userService.saveDataPm(keycloackId, profileDTO);
    }

    @GetMapping("/get-by-keycloackId/{keycloackId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public Optional<UserDTO> findByKeycloakId(@PathVariable("keycloackId") UUID keycloackId) {
        log.info("REST request to get User : {}", keycloackId);
        return this.userService.findByKeycloakId(keycloackId);

    }

    @GetMapping("/get-optional-user/{id}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public Optional<UserDTO> getOptionalUser(@PathVariable("id") UUID id) {
        log.info("REST request to get User : {}", id);
        return this.userService.findOne(id);

    }


    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    @PutMapping("/update-profile-completed")
    public ResponseEntity<String> updateProfileCompleted() {
        userService.updateProfileCompletedKeycloak();
        return ResponseEntity.ok("User profile_completed attribute updated for selected users.");
    }
}
