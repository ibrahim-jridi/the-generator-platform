package com.pfe.web.rest;

import com.pfe.IntegrationTest;
import com.pfe.dto.DmnActivityDecisionDefinitionDTO;
import com.pfe.services.IDmnService;
import com.pfe.services.criteria.DecisionDefinitionCriteria;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DecisionDefinitionResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IDmnService dmnService;

    private DmnActivityDecisionDefinitionDTO sampleDTO;

    @Autowired
    private RuntimeService runtimeService;

    private static final String  RANDOM_ID = "RANDOM_ID";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.sampleDTO = new DmnActivityDecisionDefinitionDTO();
        this.sampleDTO.setName("test");
        this.repositoryService.createDeploymentQuery().list()
            .forEach(deployment -> this.repositoryService.deleteDeployment(deployment.getId(), true));  // 'true' for cascade delete

        this.repositoryService.createDeployment()
            .addClasspathResource("diagram.dmn")
            .deploy();
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetAllDecisionDefinition() throws Exception {
        this.mockMvc.perform(get("/api/v1/camunda/fetch-all-decision-definition")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].name").value(this.sampleDTO.getName()));
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetDecisionDefinitionById() throws Exception {
        this.mockMvc.perform(get("/api/v1/camunda/fetch-decision-definition-by-id/" + RANDOM_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetDecisionDefinitionByIdWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get("/api/v1/camunda/fetch-decision-definition-by-id/" + RANDOM_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllDecisionDefinitionWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get("/api/v1/camunda/fetch-all-decision-definition")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }


    @Test
    @WithUnauthenticatedMockUser
    void testGetDecisionDefinitionByIdWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/api/v1/camunda/fetch-decision-definition-by-id/" + RANDOM_ID)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testGetAllDecisionDefinitionWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get("/api/v1/camunda/fetch-all-decision-definition")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }


    private String generateContentDmn() throws Exception {
        String dmnFilePath = "src/test/resources/diagram.dmn";
        String dmnXml = new String(Files.readAllBytes(Paths.get(dmnFilePath)));
        Map<String, String> dmnData = new HashMap<>();
        dmnData.put("name", this.sampleDTO.getName());
        dmnData.put("xml", dmnXml);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(dmnData);
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testDeployDecisionDefinition_withFile() throws Exception {
        // Perform the POST request with the DMN data
        this.mockMvc.perform(post("/api/v1/camunda/repository/deploy-dmn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(generateContentDmn()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(this.sampleDTO.getName()));

    }

    @Test
    void testDeployDecisionDefinition_withFile_WithForbiddenAuthorities() throws Exception {
        // Perform the POST request with the DMN data
        this.mockMvc.perform(post("/api/v1/camunda/repository/deploy-dmn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(generateContentDmn()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testDeployDecisionDefinition_withFile_WithUnauthenticatedUser() throws Exception {
        // Perform the POST request with the DMN data
        this.mockMvc.perform(post("/api/v1/camunda/repository/deploy-dmn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(generateContentDmn()))
            .andExpect(status().isUnauthorized());

    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetAllDecisionDefinitionByCriteria() throws Exception {
        DecisionDefinitionCriteria decisionDefinitionCriteria=new DecisionDefinitionCriteria(null, this.sampleDTO.getName(),null,null);
        this.mockMvc.perform(post("/api/v1/camunda/fetch-all-decision-definition")
                .param("page", "0")
                .param("size", "5")
                .content(TestUtil.convertObjectToJsonBytes(decisionDefinitionCriteria))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].name").value(this.sampleDTO.getName()))
            .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testGetAllDecisionDefinitionByCriteriaWithForbiddenAuthorities() throws Exception {
        DecisionDefinitionCriteria decisionDefinitionCriteria=new DecisionDefinitionCriteria(null, this.sampleDTO.getName(),null,null);
        this.mockMvc.perform(post("/api/v1/camunda/fetch-all-decision-definition")
                .param("page", "0")
                .param("size", "5")
                .content(TestUtil.convertObjectToJsonBytes(decisionDefinitionCriteria))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testGetAllDecisionDefinitionByCriteriaWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(post("/api/v1/camunda/fetch-all-decision-definition")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }



}
