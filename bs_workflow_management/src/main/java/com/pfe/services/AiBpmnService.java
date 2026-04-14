package com.pfe.services;


import com.pfe.dto.AiBpmnGenerateRequestDTO;
import com.pfe.dto.AiBpmnGenerateResponseDTO;
import com.pfe.dto.AiBpmnRefineRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service that proxies AI BPMN generation requests to the Python microservice
 * running the Ollama-backed InteractiveBPMNGenerator.
 *
 * The Python microservice should be started separately (see ai_bpmn_api.py).
 * Default URL: http://localhost:5050
 */
@Service
public class AiBpmnService {

    private static final Logger log = LoggerFactory.getLogger(AiBpmnService.class);

    @Value("${ai.bpmn.service.url:http://localhost:5050}")
    private String aiBpmnServiceUrl;

    private final RestTemplate restTemplate;
    private final RepositoryService repositoryService;

    public AiBpmnService(RepositoryService repositoryService) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);        // 10s to connect
        factory.setReadTimeout(600_000);           // 10 min read timeout for LLM
        this.restTemplate = new RestTemplate(factory);
        this.repositoryService = repositoryService;
    }

    /**
     * Fetch available Ollama models from the Python microservice
     */
    @SuppressWarnings("unchecked")
    public List<String> getAvailableModels() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                aiBpmnServiceUrl + "/models", Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (List<String>) response.getBody().get("models");
            }
        } catch (Exception e) {
            log.error("Failed to fetch Ollama models: {}", e.getMessage());
        }
        // fallback defaults
        return List.of(
            "deepseek-v3.1:671b-cloud",
            "minimax-m2.5:cloud",
            "qwen3-coder:480b-cloud"
        );
    }

    /**
     * Generate a new BPMN by delegating to the Python microservice
     */
    public AiBpmnGenerateResponseDTO generateBpmn(AiBpmnGenerateRequestDTO request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("description", request.getDescription());
            body.put("model", request.getModel());
            body.put("complexity", request.getComplexity());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<AiBpmnGenerateResponseDTO> response = restTemplate.exchange(
                aiBpmnServiceUrl + "/generate",
                HttpMethod.POST,
                entity,
                AiBpmnGenerateResponseDTO.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to generate BPMN: {}", e.getMessage());
            AiBpmnGenerateResponseDTO err = new AiBpmnGenerateResponseDTO();
            err.setValid(false);
            err.setMessage("AI service unavailable: " + e.getMessage());
            return err;
        }
    }

    /**
     * Refine an existing BPMN via follow-up prompt
     */
    public AiBpmnGenerateResponseDTO refineBpmn(AiBpmnRefineRequestDTO request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("sessionId", request.getSessionId());
            body.put("refinementPrompt", request.getRefinementPrompt());
            body.put("currentXml", request.getCurrentXml());
            body.put("conversationHistory", request.getConversationHistory());
            body.put("model", request.getModel());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<AiBpmnGenerateResponseDTO> response = restTemplate.exchange(
                aiBpmnServiceUrl + "/refine",
                HttpMethod.POST,
                entity,
                AiBpmnGenerateResponseDTO.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to refine BPMN: {}", e.getMessage());
            AiBpmnGenerateResponseDTO err = new AiBpmnGenerateResponseDTO();
            err.setValid(false);
            err.setMessage("AI service unavailable: " + e.getMessage());
            return err;
        }
    }

    /**
     * Deploy generated BPMN to Camunda via existing RepositoryService
     */
    public String deployBpmn(String xml, String name) {
        Map<String, String> processData = new HashMap<>();
        processData.put("xml", xml);
        processData.put("name", name);
        return repositoryService.deployProcessDefinition(processData).getId();
    }
}