package com.pfe.web.rest;

import com.pfe.domain.NotificationDestination;
import com.pfe.repository.INotificationDestinationRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.INotificationDestinationService;
import com.pfe.service.NotificationDestinationQueryService;
import com.pfe.service.criteria.NotificationDestinationCriteria;
import com.pfe.service.dto.NotificationDestinationDTO;
import com.pfe.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing {@link NotificationDestination}.
 */
@RestController
@RequestMapping("/api/v1/notification-destinations")
public class NotificationDestinationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationDestinationResource.class);

    private static final String ENTITY_NAME = "bsNotificationDestinationManagement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final INotificationDestinationService notificationDestinationService;

    private final INotificationDestinationRepository notificationDestinationRepository;

    private final NotificationDestinationQueryService notificationDestinationQueryService;

    public NotificationDestinationResource(
        INotificationDestinationService notificationDestinationService,
        INotificationDestinationRepository notificationDestinationRepository,
        NotificationDestinationQueryService notificationDestinationQueryService
    ) {
        this.notificationDestinationService = notificationDestinationService;
        this.notificationDestinationRepository = notificationDestinationRepository;
        this.notificationDestinationQueryService = notificationDestinationQueryService;
    }

    /**
     * {@code POST  /notification-destinations} : Create a new notificationDestination.
     *
     * @param notificationDestinationDTO the notificationDestinationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationDestinationDTO, or with status {@code 400 (Bad Request)} if the notificationDestination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<NotificationDestinationDTO> createNotificationDestination(
        @RequestBody NotificationDestinationDTO notificationDestinationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save NotificationDestination : {}", notificationDestinationDTO);
        if (notificationDestinationDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationDestination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificationDestinationDTO result = notificationDestinationService.save(notificationDestinationDTO);
        return ResponseEntity
            .created(new URI("/api/notification-destinations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notification-destinations/:id} : Updates an existing notificationDestination.
     *
     * @param id the id of the notificationDestinationDTO to save.
     * @param notificationDestinationDTO the notificationDestinationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationDestinationDTO,
     * or with status {@code 400 (Bad Request)} if the notificationDestinationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationDestinationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<NotificationDestinationDTO> updateNotificationDestination(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody NotificationDestinationDTO notificationDestinationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NotificationDestination : {}, {}", id, notificationDestinationDTO);
        if (notificationDestinationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationDestinationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationDestinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NotificationDestinationDTO result = notificationDestinationService.update(notificationDestinationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationDestinationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notification-destinations/:id} : Partial updates given fields of an existing notificationDestination, field will ignore if it is null
     *
     * @param id the id of the notificationDestinationDTO to save.
     * @param notificationDestinationDTO the notificationDestinationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationDestinationDTO,
     * or with status {@code 400 (Bad Request)} if the notificationDestinationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationDestinationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationDestinationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<NotificationDestinationDTO> partialUpdateNotificationDestination(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody NotificationDestinationDTO notificationDestinationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NotificationDestination partially : {}, {}", id, notificationDestinationDTO);
        if (notificationDestinationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationDestinationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationDestinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationDestinationDTO> result = notificationDestinationService.partialUpdate(notificationDestinationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationDestinationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notification-destinations} : get all the notificationDestinations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationDestinations in body.
     */
    @GetMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<NotificationDestinationDTO>> getAllNotificationDestinations(
        NotificationDestinationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get NotificationDestinations by criteria: {}", criteria);

        Page<NotificationDestinationDTO> page = notificationDestinationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notification-destinations/count} : count all the notificationDestinations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Long> countNotificationDestinations(NotificationDestinationCriteria criteria) {
        log.debug("REST request to count NotificationDestinations by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificationDestinationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notification-destinations/:id} : get the "id" notificationDestination.
     *
     * @param id the id of the notificationDestinationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationDestinationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<NotificationDestinationDTO> getNotificationDestination(@PathVariable("id") UUID id) {
        log.debug("REST request to get NotificationDestination : {}", id);
        Optional<NotificationDestinationDTO> notificationDestinationDTO = notificationDestinationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationDestinationDTO);
    }

    /**
     * {@code DELETE  /notification-destinations/:id} : delete the "id" notificationDestination.
     *
     * @param id the id of the notificationDestinationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Void> deleteNotificationDestination(@PathVariable("id") UUID id) {
        log.debug("REST request to delete NotificationDestination : {}", id);
        notificationDestinationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
