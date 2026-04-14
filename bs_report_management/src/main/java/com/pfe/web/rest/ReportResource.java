package com.pfe.web.rest;

import com.pfe.security.AuthoritiesConstants;
import com.pfe.service.ReportService;
import com.pfe.domain.ReportTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * REST controller for managing {@link ReportTemplate}.
 */
@RestController
@RequestMapping("/api/v1/report")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ConfigurationReportResource.class);
    private final ReportService reportService;

    public ReportResource(ReportService reportService) {
        this.reportService = reportService;
    }


    @PostMapping("/generate-report")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<String> generateReport(@RequestParam("templateId") UUID templateId,
                                                 @RequestParam("instanceId") String instanceId,
                                                 @RequestParam("processName") String processName,
                                                 @RequestBody Map<String, Object> processVariables) throws Exception {
        log.info("REST request to generate report from process : {}", processName);
        reportService.generateReport(templateId, processVariables, instanceId, processName);
        return ResponseEntity.ok().build();
    }
}
