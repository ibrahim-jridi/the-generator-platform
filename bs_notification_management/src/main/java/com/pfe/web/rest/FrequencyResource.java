package com.pfe.web.rest;

import com.pfe.domain.Frequency;
import com.pfe.repository.IFrequencyRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.FrequencyQueryService;
import com.pfe.service.IFrequencyService;
import com.pfe.service.criteria.FrequencyCriteria;
import com.pfe.service.dto.FrequencyDTO;
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
 * REST controller for managing {@link Frequency}.
 */
@RestController
@RequestMapping("/api/v1/frequencies")
public class FrequencyResource {

    private final Logger log = LoggerFactory.getLogger(FrequencyResource.class);

    private static final String ENTITY_NAME = "bsFrequencyManagement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IFrequencyService frequencyService;

    private final IFrequencyRepository frequencyRepository;

    private final FrequencyQueryService frequencyQueryService;

    public FrequencyResource(
        IFrequencyService frequencyService,
        IFrequencyRepository frequencyRepository,
        FrequencyQueryService frequencyQueryService
    ) {
        this.frequencyService = frequencyService;
        this.frequencyRepository = frequencyRepository;
        this.frequencyQueryService = frequencyQueryService;
    }

    /**
     * {@code POST  /frequencies} : Create a new frequency.
     *
     * @param frequencyDTO the frequencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new frequencyDTO, or with status {@code 400 (Bad Request)} if the frequency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<FrequencyDTO> createFrequency(@RequestBody FrequencyDTO frequencyDTO) throws URISyntaxException {
        log.debug("REST request to save Frequency : {}", frequencyDTO);
        if (frequencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new frequency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FrequencyDTO result = frequencyService.save(frequencyDTO);
        return ResponseEntity
            .created(new URI("/api/frequencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /frequencies/:id} : Updates an existing frequency.
     *
     * @param id the id of the frequencyDTO to save.
     * @param frequencyDTO the frequencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frequencyDTO,
     * or with status {@code 400 (Bad Request)} if the frequencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the frequencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<FrequencyDTO> updateFrequency(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody FrequencyDTO frequencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Frequency : {}, {}", id, frequencyDTO);
        if (frequencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frequencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frequencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FrequencyDTO result = frequencyService.update(frequencyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frequencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /frequencies/:id} : Partial updates given fields of an existing frequency, field will ignore if it is null
     *
     * @param id the id of the frequencyDTO to save.
     * @param frequencyDTO the frequencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated frequencyDTO,
     * or with status {@code 400 (Bad Request)} if the frequencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the frequencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the frequencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<FrequencyDTO> partialUpdateFrequency(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody FrequencyDTO frequencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Frequency partially : {}, {}", id, frequencyDTO);
        if (frequencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, frequencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!frequencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FrequencyDTO> result = frequencyService.partialUpdate(frequencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, frequencyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /frequencies} : get all the frequencies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of frequencies in body.
     */
    @GetMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<FrequencyDTO>> getAllFrequencies(
        FrequencyCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Frequencies by criteria: {}", criteria);

        Page<FrequencyDTO> page = frequencyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /frequencies/count} : count all the frequencies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Long> countFrequencies(FrequencyCriteria criteria) {
        log.debug("REST request to count Frequencies by criteria: {}", criteria);
        return ResponseEntity.ok().body(frequencyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /frequencies/:id} : get the "id" frequency.
     *
     * @param id the id of the frequencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the frequencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<FrequencyDTO> getFrequency(@PathVariable("id") UUID id) {
        log.debug("REST request to get Frequency : {}", id);
        Optional<FrequencyDTO> frequencyDTO = frequencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(frequencyDTO);
    }

    /**
     * {@code DELETE  /frequencies/:id} : delete the "id" frequency.
     *
     * @param id the id of the frequencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Void> deleteFrequency(@PathVariable("id") UUID id) {
        log.debug("REST request to delete Frequency : {}", id);
        frequencyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
