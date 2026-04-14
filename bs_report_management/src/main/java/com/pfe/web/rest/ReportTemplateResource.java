package com.pfe.web.rest;

import com.pfe.repository.ReportTemplateRepository;
import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.MinioReportService;
import com.pfe.service.ReportTemplateQueryService;
import com.pfe.service.ReportTemplateService;
import com.pfe.service.criteria.ReportTemplateCriteria;
import com.pfe.service.dto.ReportTemplateDTO;
import com.pfe.web.rest.errors.BadRequestAlertException;
import com.pfe.domain.ReportTemplate;
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
 * REST controller for managing {@link ReportTemplate}.
 */
@RestController
@RequestMapping("/api/v1/report-templates")
public class ReportTemplateResource {

    private final Logger log = LoggerFactory.getLogger(ReportTemplateResource.class);

    private static final String ENTITY_NAME = "bsReportManagementReportTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportTemplateService reportTemplateService;

    private final ReportTemplateRepository reportTemplateRepository;

    private final ReportTemplateQueryService reportTemplateQueryService;

    private final MinioReportService minioService;

    public ReportTemplateResource(
        ReportTemplateService reportTemplateService,
        ReportTemplateRepository reportTemplateRepository,
        ReportTemplateQueryService reportTemplateQueryService, MinioReportService minioService
    ) {
        this.reportTemplateService = reportTemplateService;
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateQueryService = reportTemplateQueryService;
        this.minioService = minioService;
    }

    /**
     * {@code POST  /report-templates} : Create a new reportTemplate.
     *
     * @param reportTemplateDTO the reportTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportTemplateDTO, or with status {@code 400 (Bad Request)} if the reportTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ReportTemplateDTO> createReportTemplate(@RequestBody ReportTemplateDTO reportTemplateDTO)
        throws URISyntaxException {
        log.info("REST request to save ReportTemplate : {}", reportTemplateDTO);
        if (reportTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        reportTemplateDTO = reportTemplateService.save(reportTemplateDTO);
        return ResponseEntity.created(new URI("/api/report-templates/" + reportTemplateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, reportTemplateDTO.getId().toString()))
            .body(reportTemplateDTO);
    }

    /**
     * {@code PUT  /report-templates/:id} : Updates an existing reportTemplate.
     *
     * @param id                the id of the reportTemplateDTO to save.
     * @param reportTemplateDTO the reportTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the reportTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ReportTemplateDTO> updateReportTemplate(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ReportTemplateDTO reportTemplateDTO
    ) throws URISyntaxException {
        log.info("REST request to update ReportTemplate : {}, {}", id, reportTemplateDTO);
        if (reportTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        reportTemplateDTO = reportTemplateService.update(reportTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportTemplateDTO.getId().toString()))
            .body(reportTemplateDTO);
    }

    /**
     * {@code PATCH  /report-templates/:id} : Partial updates given fields of an existing reportTemplate, field will ignore if it is null
     *
     * @param id                the id of the reportTemplateDTO to save.
     * @param reportTemplateDTO the reportTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the reportTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = {"application/json", "application/merge-patch+json"})
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ReportTemplateDTO> partialUpdateReportTemplate(
        @PathVariable(value = "id", required = false) final UUID id,
        @RequestBody ReportTemplateDTO reportTemplateDTO
    ) throws URISyntaxException {
        log.info("REST request to partial update ReportTemplate partially : {}, {}", id, reportTemplateDTO);
        if (reportTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportTemplateDTO> result = reportTemplateService.partialUpdate(reportTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-templates} : get all the reportTemplates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportTemplates in body.
     */
    @GetMapping("")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<ReportTemplateDTO>> getAllReportTemplates(
        ReportTemplateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.info("REST request to get ReportTemplates by criteria: {}", criteria);

        Page<ReportTemplateDTO> page = reportTemplateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-templates/count} : count all the reportTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Long> countReportTemplates(ReportTemplateCriteria criteria) {
        log.info("REST request to count ReportTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-templates/:id} : get the "id" reportTemplate.
     *
     * @param id the id of the reportTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ReportTemplateDTO> getReportTemplate(@PathVariable("id") UUID id) {
        log.info("REST request to get ReportTemplate : {}", id);
        Optional<ReportTemplateDTO> reportTemplateDTO = reportTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportTemplateDTO);
    }

    /**
     * {@code DELETE  /report-templates/:id} : delete the "id" reportTemplate.
     *
     * @param id the id of the reportTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Void> deleteReportTemplate(@PathVariable("id") UUID id) {
        log.info("REST request to delete ReportTemplate : {}", id);
        reportTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
