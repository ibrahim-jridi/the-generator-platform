package com.pfe.web.rest;

import com.pfe.domain.EventNotification;
import com.pfe.repository.EventNotificationRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.EventNotificationQueryService;
import com.pfe.service.EventNotificationService;
import com.pfe.service.criteria.EventNotificationCriteria;
import com.pfe.service.dto.EventNotificationDTO;
import com.pfe.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link EventNotification}.
 */
@RestController
@RequestMapping("/api/v1/event-notifications")
public class EventNotificationResource {

    private static final Logger LOG = LoggerFactory.getLogger(EventNotificationResource.class);

    private static final String ENTITY_NAME = "bsNotificationManagementEventNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventNotificationService eventNotificationService;

    private final EventNotificationRepository eventNotificationRepository;

    private final EventNotificationQueryService eventNotificationQueryService;

    public EventNotificationResource(
        EventNotificationService eventNotificationService,
        EventNotificationRepository eventNotificationRepository,
        EventNotificationQueryService eventNotificationQueryService
    ) {
        this.eventNotificationService = eventNotificationService;
        this.eventNotificationRepository = eventNotificationRepository;
        this.eventNotificationQueryService = eventNotificationQueryService;
    }

    /**
     * {@code POST  /event-notifications} : Create a new eventNotification.
     *
     * @param eventNotificationDTO the eventNotificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventNotificationDTO, or with status {@code 400 (Bad Request)} if the eventNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<EventNotificationDTO> createEventNotification(@Valid @RequestBody EventNotificationDTO eventNotificationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save EventNotification : {}", eventNotificationDTO);
        if (eventNotificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new eventNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        eventNotificationDTO = eventNotificationService.save(eventNotificationDTO);
        return ResponseEntity.created(new URI("/api/event-notifications/" + eventNotificationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, eventNotificationDTO.getId().toString()))
            .body(eventNotificationDTO);
    }

    /**
     * {@code PUT  /event-notifications/:id} : Updates an existing eventNotification.
     *
     * @param id the id of the eventNotificationDTO to save.
     * @param eventNotificationDTO the eventNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the eventNotificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<EventNotificationDTO> updateEventNotification(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody EventNotificationDTO eventNotificationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update EventNotification : {}, {}", id, eventNotificationDTO);
        if (eventNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        eventNotificationDTO = eventNotificationService.update(eventNotificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventNotificationDTO.getId().toString()))
            .body(eventNotificationDTO);
    }

    /**
     * {@code PATCH  /event-notifications/:id} : Partial updates given fields of an existing eventNotification, field will ignore if it is null
     *
     * @param id the id of the eventNotificationDTO to save.
     * @param eventNotificationDTO the eventNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the eventNotificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eventNotificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<EventNotificationDTO> partialUpdateEventNotification(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody EventNotificationDTO eventNotificationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update EventNotification partially : {}, {}", id, eventNotificationDTO);
        if (eventNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventNotificationDTO> result = eventNotificationService.partialUpdate(eventNotificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventNotificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /event-notifications} : get all the eventNotifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventNotifications in body.
     */
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    @GetMapping("")
    public ResponseEntity<List<EventNotificationDTO>> getAllEventNotifications(
        EventNotificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get EventNotifications by criteria: {}", criteria);

        Page<EventNotificationDTO> page = eventNotificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /event-notifications/count} : count all the eventNotifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    @GetMapping("/count")
    public ResponseEntity<Long> countEventNotifications(EventNotificationCriteria criteria) {
        LOG.debug("REST request to count EventNotifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(eventNotificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /event-notifications/:id} : get the "id" eventNotification.
     *
     * @param id the id of the eventNotificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventNotificationDTO, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    @GetMapping("/{id}")
    public ResponseEntity<EventNotificationDTO> getEventNotification(@PathVariable("id") UUID id) {
        LOG.debug("REST request to get EventNotification : {}", id);
        Optional<EventNotificationDTO> eventNotificationDTO = eventNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eventNotificationDTO);
    }

    /**
     * {@code DELETE  /event-notifications/:id} : delete the "id" eventNotification.
     *
     * @param id the id of the eventNotificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventNotification(@PathVariable("id") UUID id) {
        LOG.debug("REST request to delete EventNotification : {}", id);
        eventNotificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
