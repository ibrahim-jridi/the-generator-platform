package com.pfe.web.rest;

import com.pfe.repository.GroupRepository;
import com.pfe.service.GroupQueryService;
import com.pfe.service.GroupService;
import com.pfe.service.criteria.GroupCriteria;
import com.pfe.service.dto.GroupDTO;
import com.pfe.web.rest.errors.BadRequestAlertException;
import com.pfe.web.rest.errors.FieldErrorVM;
import com.pfe.domain.Group;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * REST controller for managing {@link Group}.
 */
@RestController
@RequestMapping("/api/v1/groups")
public class GroupResource {

    private static final Logger log = LoggerFactory.getLogger(GroupResource.class);

    private static final String ENTITY_NAME = "bsUserManagementGroup";

    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final GroupQueryService groupQueryService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public GroupResource(GroupService groupService, GroupRepository groupRepository,
        GroupQueryService groupQueryService) {
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.groupQueryService = groupQueryService;
    }

    @PostMapping("")
    @Operation(
        summary = "Create a new group",
        description = "Create a new group by providing the group details. The group must not already have an ID."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Group created successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad Request. The group already has an ID or the request is invalid.",
                content = @Content(schema = @Schema(implementation = Void.class))
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
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO)
        throws URISyntaxException {
        log.debug("REST request to save Group : {}", groupDTO);
        if (groupDTO.getId() != null) {
            throw new BadRequestAlertException("A new group cannot already have an ID", ENTITY_NAME,
                "idexists");
        }
        groupDTO = this.groupService.save(groupDTO);
        return ResponseEntity.created(new URI("/api/v1/groups/" + groupDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(this.applicationName, true, ENTITY_NAME,
                groupDTO.getId().toString()))
            .body(groupDTO);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing group",
        description = "Update the details of an existing group identified by its ID. The provided ID in the path must match the ID in the request body."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Group updated successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupDTO.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid ID or mismatch between path and request body ID.",
                content = @Content(schema = @Schema(implementation = Void.class))
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
    public ResponseEntity<GroupDTO> updateGroup(
        @PathVariable(value = "id", required = false) UUID id,
        @RequestBody GroupDTO groupDTO) {
        log.debug("REST request to update Group : {}, {}", id, groupDTO);
        if (groupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, groupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!this.groupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        groupDTO = this.groupService.update(groupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, true, ENTITY_NAME,
                groupDTO.getId().toString()))
            .body(groupDTO);
    }

    @GetMapping("")
    @Operation(
        summary = "Get all groups",
        description = "Retrieve a paginated list of all groups, optionally filtered by criteria."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved list of groups.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupDTO.class))
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
                responseCode = "500",
                description = "Internal server error.",
                content = @Content(schema = @Schema(implementation = Void.class))
            )
        }
    )
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<GroupDTO>> getAllGroups(
        GroupCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
      log.debug("REST request to get Groups by criteria: {}", criteria);

      Page<GroupDTO> page = this.groupQueryService.findByCriteria(criteria, pageable);
      HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
          ServletUriComponentsBuilder.fromCurrentRequest(), page);
      return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/count")
    @Operation(
        summary = "Count groups",
        description = "Retrieve the total number of groups matching the given criteria."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved count of groups.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Long.class))
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
                responseCode = "500",
                description = "Internal server error.",
                content = @Content(schema = @Schema(implementation = Void.class))
            )
        }
    )
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Long> countGroups(GroupCriteria criteria) {
        log.debug("REST request to count Groups by criteria: {}", criteria);
        return ResponseEntity.ok().body(this.groupQueryService.countByCriteria(criteria));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get a group by ID",
        description = "Retrieve a specific group by its ID."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved the group.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupDTO.class))
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
    public ResponseEntity<GroupDTO> getGroup(@PathVariable("id") UUID id) {
        log.debug("REST request to get Group : {}", id);
        Optional<GroupDTO> groupDTO = this.groupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(groupDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a group by ID",
        description = "Delete a specific group identified by its ID."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Group deleted successfully.",
                content = @Content(schema = @Schema(implementation = Void.class))
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
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Group : {}", id);
        this.groupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME,
                id.toString()))
            .build();
    }

    @GetMapping("/all")
    @Operation(
        summary = "Get all groups (non-paginated)",
        description = "Retrieve a list of all groups without pagination."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved the list of groups.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupDTO.class))
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
                responseCode = "500",
                description = "Internal server error.",
                content = @Content(schema = @Schema(implementation = Void.class))
            )
        }
    )
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<GroupDTO>> getGroups() {
        log.debug("REST request to get Groups");
        return ResponseEntity.ok().body(this.groupService.getGroups());
    }


    @GetMapping("/getUserIdsByGroupId")
    @Operation(
        summary = "Get user IDs by group ID",
        description = "Retrieve a list of user IDs belonging to a specific group identified by its ID."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved the list of user IDs.",
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
    public ResponseEntity<List<UUID>> getUserIdsByGroupId(@RequestParam UUID id) {
        return ResponseEntity.ok().body(this.groupService.getUserIdsByGroupId(id));
    }


  @PutMapping("/activate-or-deactivate-group/{id}")
  @Operation(
      summary = "Activate or deactivate group",
      description = "Activate or deactivate group identified by its ID."
  )
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "Successfully activate or deactivate group",
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
  public ResponseEntity<Map<String, String>> activateOrDeactivateGroup(@PathVariable UUID id) {
    log.info("Request to activate or deactivate group with id {}", id);
    String message = this.groupService.activateOrDeactivateGroupById(id);
    Map<String, String> response = new HashMap<>();
    response.put("message", message);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/delete-group-permanently/{id}")
  //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Void> permanentlyDeleteGroup(@PathVariable UUID id) {
    this.groupService.permanentlyDeleteGroup(id);
    return ResponseEntity.noContent()
        .headers(HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME,
            id.toString()))
        .build();
  }

}
