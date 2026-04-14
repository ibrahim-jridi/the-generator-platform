package com.pfe.web.rest;

import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.AuthorityQueryService;
import com.pfe.service.AuthorityService;
import com.pfe.service.criteria.AuthorityCriteria;
import com.pfe.service.dto.AuthorityDTO;
import com.pfe.domain.Authority;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for  managing {@link Authority}.
 */
@RestController
@RequestMapping("/api/v1/authorities")
public class AuthorityResource {

    private static final Logger log = LoggerFactory.getLogger(AuthorityResource.class);

    private static final String ENTITY_NAME = "bsUserManagementAuthority";
    private final AuthorityService authorityService;
    private final AuthorityQueryService authorityQueryService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public AuthorityResource(
        AuthorityService authorityService,
        AuthorityQueryService authorityQueryService
    ) {
        this.authorityService = authorityService;
        this.authorityQueryService = authorityQueryService;
    }

    /**
     * {@code GET  /authorities} : get all the authorities.
     */
    @Operation(summary = "Get all authorities", description = "Retrieve all authorities based on criteria and pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authorities retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<AuthorityDTO>> getAllAuthorities(
        AuthorityCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Authorities by criteria: {}", criteria);

        Page<AuthorityDTO> page = this.authorityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /authorities/count} : count all the authorities.
     */
    @Operation(summary = "Count authorities", description = "Count the number of authorities matching the specified criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/count")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Long> countAuthorities(AuthorityCriteria criteria) {
        log.debug("REST request to count Authorities by criteria: {}", criteria);
        return ResponseEntity.ok().body(this.authorityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /authorities/:id} : get the "id" authority.
     */
    @Operation(summary = "Get an authority by ID", description = "Retrieve an authority by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authority retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content),
        @ApiResponse(responseCode = "404", description = "Authority not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<AuthorityDTO> getAuthority(@PathVariable("id") UUID id) {
        log.debug("REST request to get Authority : {}", id);
        Optional<AuthorityDTO> authorityDTO = this.authorityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(authorityDTO);
    }

    /**
     * {@code GET  /authorities/findAll} : get all authorities without criteria.
     */
    @Operation(summary = "Find all authorities", description = "Retrieve all authorities without applying any criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authorities retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/findAll")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<AuthorityDTO>> findAllAuthorities() {
        log.debug("REST request to get All Authorities");
        return ResponseEntity.ok(this.authorityService.findAllAuthorities());
    }

    /**
     * {@code GET  /authorities/getAuthorities/:id} : get authorities not in a role.
     */
    @Operation(summary = "Get authorities not in a role", description = "Retrieve authorities not assigned to the specified role")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authorities retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request parameters", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden access", content = @Content),
        @ApiResponse(responseCode = "404", description = "Role not found", content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/getAuthorities/{id}")
//    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<AuthorityDTO>> getAuthoritiesNotInRole(@PathVariable("id") UUID id) {
        log.debug("REST request to get All Authorities Not in ROLE");
        return ResponseEntity.ok(this.authorityService.getAuthoritiesNotInRole(id));
    }
}
