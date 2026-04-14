package com.pfe.web.rest;

import com.pfe.repository.ConfigurationReportRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.ConfigurationReportQueryService;
import com.pfe.service.ConfigurationReportService;
import com.pfe.service.criteria.ConfigurationReportCriteria;
import com.pfe.service.dto.ConfigurationReportDTO;
import com.pfe.web.rest.errors.BadRequestAlertException;
import com.pfe.domain.ConfigurationReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
 * REST controller for managing {@link ConfigurationReport}.
 */
@RestController
@RequestMapping("/api/v1/configuration-reports")
public class ConfigurationReportResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationReportResource.class);

    private static final String ENTITY_NAME = "bsReportManagementConfigurationReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigurationReportService configurationReportService;

    private final ConfigurationReportRepository configurationReportRepository;

    private final ConfigurationReportQueryService configurationReportQueryService;

    public ConfigurationReportResource(
        ConfigurationReportService configurationReportService,
        ConfigurationReportRepository configurationReportRepository,
        ConfigurationReportQueryService configurationReportQueryService
    ) {
        this.configurationReportService = configurationReportService;
        this.configurationReportRepository = configurationReportRepository;
        this.configurationReportQueryService = configurationReportQueryService;
    }

    /**
     * {@code POST  /configuration-reports} : Create a new configurationReport.
     *
     * @param configurationReportDTO the configurationReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configurationReportDTO, or with status {@code 400 (Bad Request)} if the configurationReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ConfigurationReportDTO> createConfigurationReport(@RequestBody ConfigurationReportDTO configurationReportDTO)
        throws URISyntaxException {
        log.info("REST request to save ConfigurationReport : {}", configurationReportDTO);
        if (configurationReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new configurationReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        configurationReportDTO = configurationReportService.save(configurationReportDTO);
        return ResponseEntity.created(new URI("/api/configuration-reports/" + configurationReportDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, configurationReportDTO.getId().toString()))
            .body(configurationReportDTO);
    }

    /**
     * {@code PUT  /configuration-reports/:id} : Updates an existing configurationReport.
     *
     * @param id                     the id of the configurationReportDTO to save.
     * @param configurationReportDTO the configurationReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configurationReportDTO,
     * or with status {@code 400 (Bad Request)} if the configurationReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configurationReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ConfigurationReportDTO> updateConfigurationReport(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ConfigurationReportDTO configurationReportDTO
    ) throws URISyntaxException {
        log.info("REST request to update ConfigurationReport : {}, {}", id, configurationReportDTO);
        if (configurationReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configurationReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configurationReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        configurationReportDTO = configurationReportService.update(configurationReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurationReportDTO.getId().toString()))
            .body(configurationReportDTO);
    }

    /**
     * {@code PATCH  /configuration-reports/:id} : Partial updates given fields of an existing configurationReport, field will ignore if it is null
     *
     * @param id                     the id of the configurationReportDTO to save.
     * @param configurationReportDTO the configurationReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configurationReportDTO,
     * or with status {@code 400 (Bad Request)} if the configurationReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the configurationReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the configurationReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ConfigurationReportDTO> partialUpdateConfigurationReport(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ConfigurationReportDTO configurationReportDTO
    ) throws URISyntaxException {
        log.info("REST request to partial update ConfigurationReport partially : {}, {}", id, configurationReportDTO);
        if (configurationReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configurationReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configurationReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConfigurationReportDTO> result = configurationReportService.partialUpdate(configurationReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configurationReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /configuration-reports} : get all the configurationReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configurationReports in body.
     */
    @GetMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<ConfigurationReportDTO>> getAllConfigurationReports(
        ConfigurationReportCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.info("REST request to get ConfigurationReports by criteria: {}", criteria);

        Page<ConfigurationReportDTO> page = configurationReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configuration-reports/count} : count all the configurationReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Long> countConfigurationReports(ConfigurationReportCriteria criteria) {
        log.info("REST request to count ConfigurationReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(configurationReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /configuration-reports/:id} : get the "id" configurationReport.
     *
     * @param id the id of the configurationReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configurationReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ConfigurationReportDTO> getConfigurationReport(@PathVariable("id") UUID id) {
        log.info("REST request to get ConfigurationReport : {}", id);
        Optional<ConfigurationReportDTO> configurationReportDTO = configurationReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configurationReportDTO);
    }

    /**
     * {@code DELETE  /configuration-reports/:id} : delete the "id" configurationReport.
     *
     * @param id the id of the configurationReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Void> deleteConfigurationReport(@PathVariable("id") UUID id) {
        log.info("REST request to delete ConfigurationReport : {}", id);
        configurationReportService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/generate-demo-report")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<byte[]> createDemoReportTemplate(@RequestBody ConfigurationReportDTO configurationReportDTO) throws Exception {
        log.info("REST request to save ReportTemplate : {}", configurationReportDTO);
        byte[] fileContent = configurationReportService.generateReport(configurationReportDTO);
        if (fileContent != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "demoReport.pdf");
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
