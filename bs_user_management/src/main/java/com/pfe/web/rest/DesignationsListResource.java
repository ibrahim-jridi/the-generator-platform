package com.pfe.web.rest;

import com.pfe.service.*;
import com.pfe.service.DesignationsListQueryService;
import com.pfe.service.DesignationsListService;
import com.pfe.service.dto.request.ApplicantListUserResponse;
import com.pfe.service.dto.request.ApplicationUsersResponse;
import com.pfe.domain.DesignationsList;
import com.pfe.service.criteria.DesignationsListCriteria;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.dto.DesignationsListDTO;
import com.pfe.service.dto.request.AssignRoleApplication;
import com.pfe.service.dto.request.SaveDesignationRequest;
import com.pfe.service.impl.DesignationsListImpl;
import com.pfe.service.impl.UserServiceImpl;
import com.pfe.service.mapper.DesignationsListMapper;
import com.pfe.service.KeycloakAuthService;
import com.pfe.service.UserQueryService;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/designations")
public class DesignationsListResource {
    private static final Logger log = LoggerFactory.getLogger(DesignationsListResource.class);
    private final UserServiceImpl userServiceImpl;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private static final String ENTITY_NAME = "bs_designations_list";
    private final KeycloakAuthService keycloakAuthService;

    private  final DesignationsListImpl designationsListImpl;
    private  final DesignationsListMapper designationsListMapper;
    private final DesignationsListQueryService designationsListQueryService;
    private final DesignationsListService designationsListService;



    public DesignationsListResource(KeycloakAuthService keycloakAuthService, DesignationsListImpl designationsListImpl, DesignationsListMapper designationsListMapper, UserQueryService userQueryService, DesignationsListQueryService designationsListQueryService, DesignationsListService designationsListService,
        UserServiceImpl userServiceImpl) {
        this.keycloakAuthService = keycloakAuthService;
        this.designationsListImpl = designationsListImpl;
        this.designationsListMapper = designationsListMapper;
        this.designationsListQueryService = designationsListQueryService;
        this.designationsListService = designationsListService;
        this.userServiceImpl = userServiceImpl;
    }


    @PostMapping
    @PreAuthorize("hasAnyAuthority('BS_DESIGNATIONS_USERS')" )

    public ResponseEntity<DesignationsListDTO> save(@RequestBody DesignationsListDTO dto) {
        log.info("Received POST request to save DesignationsListDTO: {}", dto);
        DesignationsListDTO savedDto = this.designationsListImpl.save(dto);
        log.info("Successfully saved DesignationsListDTO with ID: {}", savedDto.getId());
        return ResponseEntity.ok(savedDto);
    }


    @GetMapping("/pm/{pmUserId}")
    @PreAuthorize("hasAnyAuthority('BS_DESIGNATIONS_USERS')" )
    public ResponseEntity<List<DesignationsList>> getListByPmUserId(@PathVariable UUID pmUserId,
                                                                    @ParameterObject Pageable pageable
    ) {
        log.info("Received GET request to fetch DesignationsList for pmUserId: {}", pmUserId);

        DesignationsListCriteria criteria = new DesignationsListCriteria();

        UUIDFilter pmUserIdFilter = new UUIDFilter();
        pmUserIdFilter.setEquals(pmUserId);

            criteria.setPmUserId(pmUserIdFilter);


        criteria.setPageable(pageable);

        Page<DesignationsList> result = this.designationsListQueryService.findByCriteria(criteria,
            pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), result
        );

        log.info("Successfully retrieved {} DesignationsList entries for pmUserId: {}", result.getTotalElements(), pmUserId);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }
    @GetMapping("/designated-user/{pmUserId}")
    @PreAuthorize("hasAnyAuthority('BS_LABORATORY')" )
    public ResponseEntity<List<DesignationsList>> getListByDesignatedUserId(@PathVariable UUID pmUserId,
                                                                    @ParameterObject Pageable pageable
    ) {
        log.info("Received GET request to fetch DesignationsList for pmUserId: {}", pmUserId);

        DesignationsListCriteria criteria = new DesignationsListCriteria();

        UUIDFilter pmUserIdFilter = new UUIDFilter();
        pmUserIdFilter.setEquals(pmUserId);

            criteria.setDesignatedUserId(pmUserIdFilter);

        criteria.setPageable(pageable);

        Page<DesignationsList> result = this.designationsListQueryService.findByCriteria(criteria,
            pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), result
        );

        log.info("Successfully retrieved {} DesignationsList entries for pmUserId: {}", result.getTotalElements(), pmUserId);
        return ResponseEntity.ok().headers(headers).body(result.getContent());
    }
//    @PreAuthorize("hasAnyAuthority('BS_VIEW_EXTERNAL_APPS')")
    @GetMapping("/external-applications")
    public ResponseEntity<ApplicationUsersResponse> getApplicationNames() {
        try {
            ResponseEntity<ApplicationUsersResponse> response = designationsListImpl.getApplicationNames();
            if (response == null || response.getBody() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApplicationUsersResponse());
            }
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (Exception e) {
            log.error("Error fetching applications using token: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApplicationUsersResponse());
        }
    }

    @PostMapping("/save-designation")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""
        + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<DesignationsListDTO> saveDesignation(
        @RequestBody SaveDesignationRequest request) {
        log.info("Received POST request to save DesignationsListDTO: {}", request.getOwner(),
            request.getDesignated(), request.getRole());
        DesignationsListDTO savedDto = designationsListImpl.saveDesignation(request.getOwner(),
            request.getDesignated(), request.getRole(),request.getPmUserId(),request.getLaboratoryId());
        log.info("Successfully saved DesignationsListDTO with ID: {}", savedDto.getId());
        return ResponseEntity.ok(savedDto);
    }

    @GetMapping("/designated-user/exists")
    public ResponseEntity<Boolean> existsDesignationsListByDesignatedUserId(
        @RequestParam UUID userId
    ) {
        log.info("REST request to khnow if user exists in designaion List bu userId: {}", userId);
        boolean exists = designationsListService.existsDesignationsListByDesignatedUserId(userId);
        return ResponseEntity.ok(exists);
    }
    @GetMapping("/designated-users/{pmKeycloakId}")
    public ResponseEntity<Map<String,String>> getDesignatedUserIdsByPmUserId(@PathVariable UUID pmKeycloakId) {
        if (pmKeycloakId == null) {
            return ResponseEntity.badRequest().build();
        }

        Map<String,String> designatedUserIds = designationsListService.getDesignatedUserIdsByPmId(pmKeycloakId);
        return ResponseEntity.ok(designatedUserIds);
    }

    @PutMapping("/update-designations-users_attributes")
    public ResponseEntity<String> saveDesignationsUsersAttributes(@RequestParam("username") String username) {
        try {
            designationsListService.saveDesignationsUsersAttributes(username);
            return ResponseEntity.ok("User attributes saved successfully.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found: " + username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to save user attributes: " + e.getMessage());
        }
    }

    @PutMapping("/update-attributes")
    public ResponseEntity<String> saveDesignationsIndustryUsersAttributes(@RequestParam("username") String username) {
        try {
            designationsListService.saveDesignationsUsersAttributes(username);
            return ResponseEntity.ok("User attributes saved successfully.");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("User not found: " + username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to save user attributes: " + e.getMessage());
        }
    }

    @GetMapping("/ectd-role/{keycloakId}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""
        + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<String>> getEctdRole(@PathVariable("keycloakId") UUID keycloakId) {
        log.info("REST request to get User Ectd Role by keycloak id : {}", keycloakId);
        return ResponseEntity.ok(this.keycloakAuthService.getEctdRole(keycloakId));
    }


    @GetMapping("/application-ids/{keycloakId}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""
        + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<String>> getApplicationIds(@PathVariable("keycloakId") UUID keycloakId) {
        log.info("REST request to get User Application Ids by keycloak id : {}", keycloakId);
        return ResponseEntity.ok(this.keycloakAuthService.getApplicationsIds(keycloakId));
    }

    @PostMapping("/ectd-role-applications")
    public ResponseEntity<String> updateKeycloakUserDesignations(@RequestBody AssignRoleApplication assignRoleApplication) {
        try {
            userServiceImpl.updateKeycloakUserDesignations(assignRoleApplication);
            return ResponseEntity.ok("User attributes updated successfully in Keycloak");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('BS_DESIGNATIONS_USERS')" )
    public ResponseEntity<Void> deleteDesignation(@PathVariable("id")  UUID id) {
        log.debug("REST request to delete Group : {}", id);
        designationsListService.deleteDesignation(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME,
                id.toString()))
            .build();
    }
    @GetMapping("/{designatedUserId}/pm-has-ectd")
    public ResponseEntity<Boolean> doesPmHaveEctd(@PathVariable UUID designatedUserId) {

        if (designatedUserId == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean pmHasEctd = designationsListService.pmHasEctdByDesignatedUser(designatedUserId);

        return ResponseEntity.ok(pmHasEctd);
    }
    @GetMapping("/applicants-attributes")
    public ResponseEntity<List<ApplicantListUserResponse>> getApplicantListByEctdRole() {
        try {
            ResponseEntity<List<ApplicantListUserResponse>> response =
                designationsListImpl.getApplicantsAttributes();

            if (response == null || response.getBody() == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
            }

            return ResponseEntity.status(response.getStatusCode())
                .body(response.getBody());

        } catch (Exception e) {
            log.error("Error fetching applications using token: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.emptyList());
        }
    }

}
