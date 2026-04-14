package com.pfe.web.rest;

import com.pfe.domain.Event;
import com.pfe.repository.EventRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link Event}.
 */
@RestController
@RequestMapping("/api/v1/events")
@Transactional
public class EventResource {

  private static final Logger LOG = LoggerFactory.getLogger(EventResource.class);

  private static final String ENTITY_NAME = "bsNotificationManagementEvent";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final EventRepository eventRepository;

  public EventResource(EventRepository eventRepository) {
    this.eventRepository = eventRepository;
  }

  /**
   * {@code POST  /events} : Create a new event.
   *
   * @param event the event to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   * event, or with status {@code 400 (Bad Request)} if the event has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Event> createEvent(@RequestBody Event event) throws URISyntaxException {
    LOG.debug("REST request to save Event : {}", event);
    if (event.getId() != null) {
      throw new BadRequestAlertException("A new event cannot already have an ID", ENTITY_NAME,
          "idexists");
    }
    event = this.eventRepository.save(event);
    return ResponseEntity.created(new URI("/api/events/" + event.getId()))
        .headers(HeaderUtil.createEntityCreationAlert(this.applicationName, true, ENTITY_NAME,
            event.getId().toString()))
        .body(event);
  }

  /**
   * {@code PUT  /events/:id} : Updates an existing event.
   *
   * @param id    the id of the event to save.
   * @param event the event to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   * event, or with status {@code 400 (Bad Request)} if the event is not valid, or with status
   * {@code 500 (Internal Server Error)} if the event couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/{id}")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Event> updateEvent(
      @PathVariable(value = "id", required = false) final UUID id, @RequestBody Event event)
      throws URISyntaxException {
    LOG.debug("REST request to update Event : {}, {}", id, event);
    if (event.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, event.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!this.eventRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    event = this.eventRepository.save(event);
    return ResponseEntity.ok()
        .headers(HeaderUtil.createEntityUpdateAlert(this.applicationName, true, ENTITY_NAME,
            event.getId().toString()))
        .body(event);
  }

  /**
   * {@code PATCH  /events/:id} : Partial updates given fields of an existing event, field will
   * ignore if it is null
   *
   * @param id    the id of the event to save.
   * @param event the event to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   * event, or with status {@code 400 (Bad Request)} if the event is not valid, or with status
   * {@code 404 (Not Found)} if the event is not found, or with status
   * {@code 500 (Internal Server Error)} if the event couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Event> partialUpdateEvent(
      @PathVariable(value = "id", required = false) final UUID id, @RequestBody Event event)
      throws URISyntaxException {
    LOG.debug("REST request to partial update Event partially : {}, {}", id, event);
    if (event.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    if (!Objects.equals(id, event.getId())) {
      throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
    }

    if (!this.eventRepository.existsById(id)) {
      throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
    }

    Optional<Event> result = this.eventRepository
        .findById(event.getId())
        .map(existingEvent -> {
          if (event.getAssign() != null) {
            existingEvent.setAssign(event.getAssign());
          }
          if (event.getDisplay() != null) {
            existingEvent.setDisplay(event.getDisplay());
          }
          if (event.getEventType() != null) {
            existingEvent.setEventType(event.getEventType());
          }
          if (event.getMessage() != null) {
            existingEvent.setMessage(event.getMessage());
          }
          if (event.getNotificationType() != null) {
            existingEvent.setNotificationType(event.getNotificationType());
          }
          if (event.getSubject() != null) {
            existingEvent.setSubject(event.getSubject());
          }
          if (event.getRecipiantId() != null) {
            existingEvent.setRecipiantId(event.getRecipiantId());
          }

          return existingEvent;
        })
        .map(this.eventRepository::save);

    return ResponseUtil.wrapOrNotFound(
        result,
        HeaderUtil.createEntityUpdateAlert(this.applicationName, true, ENTITY_NAME,
            event.getId().toString())
    );
  }

  /**
   * {@code GET  /events} : get all the events.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of events in body.
   */
  @GetMapping("")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public List<Event> getAllEvents() {
    LOG.debug("REST request to get all Events");
    return this.eventRepository.findAll();
  }

  /**
   * {@code GET  /events/:id} : get the "id" event.
   *
   * @param id the id of the event to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the event, or
   * with status {@code 404 (Not Found)}.
   */
  @GetMapping("/{id}")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Event> getEvent(@PathVariable("id") UUID id) {
    LOG.debug("REST request to get Event : {}", id);
    Optional<Event> event = this.eventRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(event);
  }

  /**
   * {@code DELETE  /events/:id} : delete the "id" event.
   *
   * @param id the id of the event to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
  public ResponseEntity<Void> deleteEvent(@PathVariable("id") UUID id) {
    LOG.debug("REST request to delete Event : {}", id);
    this.eventRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(this.applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
