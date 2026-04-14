package com.pfe.web.rest;


import com.pfe.dto.AiBpmnGenerateRequestDTO;
import com.pfe.dto.AiBpmnGenerateResponseDTO;
import com.pfe.dto.AiBpmnRefineRequestDTO;
import com.pfe.services.AiBpmnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for AI-powered BPMN generation via Ollama.
 * Delegates to AiBpmnService which communicates with the Python microservice.
 */
@RestController
@RequestMapping("/api/v1/ai-bpmn")
public class AiBpmnController {

    private static final Logger log = LoggerFactory.getLogger(AiBpmnController.class);

    private final AiBpmnService aiBpmnService;

    public AiBpmnController(AiBpmnService aiBpmnService) {
        this.aiBpmnService = aiBpmnService;
    }

    /**
     * GET /models → list available Ollama models
     */
    @GetMapping("/models")
   // @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<String>> getAvailableModels() {
        log.info("REST request to get available Ollama models");
        return ResponseEntity.ok(aiBpmnService.getAvailableModels());
    }

    /**
     * POST /generate → generate a new BPMN from a workflow description
     * Body: { description, model, complexity }
     * Returns: { xml, sessionId, validationResult, qualityAnalysis }
     */
    @PostMapping("/generate")
   // @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<AiBpmnGenerateResponseDTO> generateBpmn(
            @RequestBody AiBpmnGenerateRequestDTO request) {
        log.info("REST request to generate BPMN for description: {}", request.getDescription());
        AiBpmnGenerateResponseDTO response = aiBpmnService.generateBpmn(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /refine → refine an existing BPMN via follow-up prompt
     * Body: { sessionId, refinementPrompt, currentXml, conversationHistory }
     * Returns: { xml, sessionId, validationResult, qualityAnalysis }
     */
    @PostMapping("/refine")
//   // @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<AiBpmnGenerateResponseDTO> refineBpmn(
            @RequestBody AiBpmnRefineRequestDTO request) {
        log.info("REST request to refine BPMN, sessionId: {}", request.getSessionId());
        AiBpmnGenerateResponseDTO response = aiBpmnService.refineBpmn(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /deploy → deploy generated BPMN XML to Camunda directly
     * Body: { xml, name }
     */
    @PostMapping("/deploy")
//   // @PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<String> deployGeneratedBpmn(@RequestBody java.util.Map<String, String> body) {
        log.info("REST request to deploy AI-generated BPMN: {}", body.get("name"));
        String deploymentId = aiBpmnService.deployBpmn(body.get("xml"), body.get("name"));
        return ResponseEntity.ok(deploymentId);
    }
}
