package com.pfe.web.rest;

import com.pfe.domain.Days;
import com.pfe.repository.IDaysRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.DaysQueryService;
import com.pfe.service.IDaysService;
import com.pfe.service.criteria.DaysCriteria;
import com.pfe.service.dto.DaysDTO;
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
 * REST controller for managing {@link Days}.
 */
@RestController
@RequestMapping("/api/v1/days")
public class DaysResource {

    private final Logger log = LoggerFactory.getLogger(DaysResource.class);

    private static final String ENTITY_NAME = "bsDaysManagement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IDaysService daysService;

    private final IDaysRepository daysRepository;

    private final DaysQueryService daysQueryService;

    public DaysResource(IDaysService daysService, IDaysRepository daysRepository, DaysQueryService daysQueryService) {
        this.daysService = daysService;
        this.daysRepository = daysRepository;
        this.daysQueryService = daysQueryService;
    }

    /**
     * {@code POST  /days} : Create a new days.
     *
     * @param daysDTO the daysDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new daysDTO, or with status {@code 400 (Bad Request)} if the days has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<DaysDTO> createDays(@RequestBody DaysDTO daysDTO) throws URISyntaxException {
        log.debug("REST request to save Days : {}", daysDTO);
        if (daysDTO.getId() != null) {
            throw new BadRequestAlertException("A new days cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DaysDTO result = daysService.save(daysDTO);
        return ResponseEntity
            .created(new URI("/api/days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /days/:id} : Updates an existing days.
     *
     * @param id the id of the daysDTO to save.
     * @param daysDTO the daysDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated daysDTO,
     * or with status {@code 400 (Bad Request)} if the daysDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the daysDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<DaysDTO> updateDays(@PathVariable(value = "id", required = false) final UUID id, @RequestBody DaysDTO daysDTO)
        throws URISyntaxException {
        log.debug("REST request to update Days : {}, {}", id, daysDTO);
        if (daysDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, daysDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!daysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DaysDTO result = daysService.update(daysDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, daysDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /days/:id} : Partial updates given fields of an existing days, field will ignore if it is null
     *
     * @param id the id of the daysDTO to save.
     * @param daysDTO the daysDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated daysDTO,
     * or with status {@code 400 (Bad Request)} if the daysDTO is not valid,
     * or with status {@code 404 (Not Found)} if the daysDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the daysDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<DaysDTO> partialUpdateDays(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody DaysDTO daysDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Days partially : {}, {}", id, daysDTO);
        if (daysDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, daysDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!daysRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DaysDTO> result = daysService.partialUpdate(daysDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, daysDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /days} : get all the days.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of days in body.
     */
    @GetMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<DaysDTO>> getAllDays(
        DaysCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Days by criteria: {}", criteria);

        Page<DaysDTO> page = daysQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /days/count} : count all the days.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Long> countDays(DaysCriteria criteria) {
        log.debug("REST request to count Days by criteria: {}", criteria);
        return ResponseEntity.ok().body(daysQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /days/:id} : get the "id" days.
     *
     * @param id the id of the daysDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the daysDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<DaysDTO> getDays(@PathVariable("id") UUID id) {
        log.debug("REST request to get Days : {}", id);
        Optional<DaysDTO> daysDTO = daysService.findOne(id);
        return ResponseUtil.wrapOrNotFound(daysDTO);
    }

    /**
     * {@code DELETE  /days/:id} : delete the "id" days.
     *
     * @param id the id of the daysDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Void> deleteDays(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Days : {}", id);
        daysService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
