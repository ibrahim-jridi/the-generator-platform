package com.pfe.web.rest;

import com.pfe.domain.NotificationPushed;
import com.pfe.repository.INotificationPushedRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.*;
import com.pfe.service.NotificationPushedQueryService;
import com.pfe.service.NotificationPushedService;
import com.pfe.service.criteria.NotificationPushedCriteria;
import com.pfe.service.dto.NotificationPushedDTO;
import com.pfe.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link NotificationPushed}.
 */
@RestController
@RequestMapping("/api/v1/notification-pushed")
public class NotificationPushedResource {

    private final Logger log = LoggerFactory.getLogger(NotificationPushedResource.class);

    private static final String ENTITY_NAME = "bsNotificationManagementNotificationPushed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationPushedService notificationPushedService;

    private final INotificationPushedRepository notificationPushedRepository;

    private final NotificationPushedQueryService notificationPushedQueryService;

    public NotificationPushedResource(
        NotificationPushedService notificationPushedService,
        INotificationPushedRepository notificationPushedRepository,
        NotificationPushedQueryService notificationPushedQueryService
    ) {
        this.notificationPushedService = notificationPushedService;
        this.notificationPushedRepository = notificationPushedRepository;
        this.notificationPushedQueryService = notificationPushedQueryService;
    }

    /**
     * {@code POST  /notification-pusheds} : Create a new notificationPushed.
     *
     * @param notificationPushedDTO the notificationPushedDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationPushedDTO, or with status {@code 400 (Bad Request)} if the notificationPushed has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<NotificationPushedDTO> createNotificationPushed(@Valid @RequestBody NotificationPushedDTO notificationPushedDTO)
        throws URISyntaxException {
        log.debug("REST request to save NotificationPushed : {}", notificationPushedDTO);
        if (notificationPushedDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationPushed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        notificationPushedDTO = notificationPushedService.save(notificationPushedDTO);
        return ResponseEntity.created(new URI("/api/notification-pusheds/" + notificationPushedDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, notificationPushedDTO.getId().toString()))
            .body(notificationPushedDTO);
    }

    /**
     * {@code PUT  /notification-pusheds/:id} : Updates an existing notificationPushed.
     *
     * @param id the id of the notificationPushedDTO to save.
     * @param notificationPushedDTO the notificationPushedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationPushedDTO,
     * or with status {@code 400 (Bad Request)} if the notificationPushedDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationPushedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<NotificationPushedDTO> updateNotificationPushed(
        @PathVariable(value = "id", required = false) final UUID id,
        @Valid @RequestBody NotificationPushedDTO notificationPushedDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NotificationPushed : {}, {}", id, notificationPushedDTO);
        if (notificationPushedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationPushedDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationPushedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        notificationPushedDTO = notificationPushedService.update(notificationPushedDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationPushedDTO.getId().toString()))
            .body(notificationPushedDTO);
    }

    /**
     * {@code PATCH  /notification-pusheds/:id} : Partial updates given fields of an existing notificationPushed, field will ignore if it is null
     *
     * @param id the id of the notificationPushedDTO to save.
     * @param notificationPushedDTO the notificationPushedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationPushedDTO,
     * or with status {@code 400 (Bad Request)} if the notificationPushedDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationPushedDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationPushedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<NotificationPushedDTO> partialUpdateNotificationPushed(
        @PathVariable(value = "id", required = false) final UUID id,
        @NotNull @RequestBody NotificationPushedDTO notificationPushedDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NotificationPushed partially : {}, {}", id, notificationPushedDTO);
        if (notificationPushedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationPushedDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationPushedRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationPushedDTO> result = notificationPushedService.partialUpdate(notificationPushedDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationPushedDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notification-pusheds} : get all the notificationPusheds.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationPusheds in body.
     */
    @GetMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<NotificationPushedDTO>> getAllNotificationPusheds(
        NotificationPushedCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get NotificationPusheds by criteria: {}", criteria);

        Page<NotificationPushedDTO> page = notificationPushedQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notification-pusheds/count} : count all the notificationPusheds.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Long> countNotificationPusheds(NotificationPushedCriteria criteria) {
        log.debug("REST request to count NotificationPusheds by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificationPushedQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notification-pusheds/:id} : get the "id" notificationPushed.
     *
     * @param id the id of the notificationPushedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationPushedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<NotificationPushedDTO> getNotificationPushed(@PathVariable("id") UUID id) {
        log.debug("REST request to get NotificationPushed : {}", id);
        Optional<NotificationPushedDTO> notificationPushedDTO = notificationPushedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationPushedDTO);
    }

    /**
     * {@code DELETE  /notification-pusheds/:id} : delete the "id" notificationPushed.
     *
     * @param id the id of the notificationPushedDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Void> deleteNotificationPushed(@PathVariable("id") UUID id) {
        log.debug("REST request to delete NotificationPushed : {}", id);
        notificationPushedService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /notification-pusheds} : get all the notificationPusheds.
     *
     * @param recipientId the id of the list notificationPushedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationPusheds in body.
     */
    @GetMapping("/by-recipient/{recipientId}")
 //   @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<NotificationPushedDTO>> getAllNotificationPushedByRecipient(
        @PathVariable("recipientId") UUID recipientId) {
        log.debug("REST request to get NotificationPusheds by recipient id: {}", recipientId);

        List<NotificationPushedDTO> list = notificationPushedService.findByRecipient(recipientId);
        return ResponseEntity.ok().body(list);
    }

    /**
     * {@code GET  /notification-pusheds} : get all the notificationPusheds.
     *
     * @param taskId the id of the list notificationPushedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationPusheds in body.
     */
    @GetMapping("/by-task/{taskId}")
 //   @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<NotificationPushedDTO>> getAllNotificationPushedByTaskId(
        @PathVariable("taskId") String taskId,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get NotificationPusheds by task id: {}", taskId);

        Page<NotificationPushedDTO> page = notificationPushedService.findByTaskId(taskId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
