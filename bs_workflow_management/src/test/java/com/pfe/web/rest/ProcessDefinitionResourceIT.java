package com.pfe.web.rest;

import com.pfe.IntegrationTest;
import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.services.ProcessEngineService;
import com.pfe.services.criteria.ProcessDefinitionCriteria;
import com.pfe.services.criteria.ProcessInstanceCriteria;
import com.pfe.services.utils.JwtUtils;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProcessDefinitionResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;
    @MockBean
    private ProcessEngineService processEngineService;

    @Autowired
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Autowired
    private IdentityService identityService;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ManagementService managementService;

    private ProcessDefinitionDTO sampleDTO;

    private ProcessDefinition processDefinition;

    private ProcessInstance processInstance;

    private static final String ENTITY_API_URL = "/api/v1/camunda";

    private static final String PROCESS_KEY = "process";


    @BeforeEach
    void setUp() {
        identityService.setAuthenticatedUserId(TestUtil.getRootID().toString());
        this.sampleDTO = new ProcessDefinitionDTO();
        this.sampleDTO.setName(PROCESS_KEY);
        this.sampleDTO.setSuspensionState(true);
        this.repositoryService.createDeployment()
            .addClasspathResource("bpmnForTest/process.bpmn")
            .deploy();
        this.repositoryService.createDeployment()
            .addClasspathResource("bpmnForTest/processWithTask.bpmn")
            .deploy();
        this.processDefinition = this.repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(PROCESS_KEY)
            .latestVersion()
            .singleResult();
        this.processInstance = this.processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_KEY);

        when(jwtUtils.getConnectedUserId()).thenReturn(TestUtil.getRootID());

    }

    // list process

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getDeployedProcesses() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/list-process")
                .param("page", "0")
                .param("size", "5")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value(this.sampleDTO.getName()))
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void testGetDeployedProcessesWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/list-process")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testGetDeployedProcessesWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/list-process")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    // start process with key
    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void startProcess() throws Exception {
        Map<String, String> variables = Map.of("key1", "value1", "key2", "value2");
        this.mockMvc.perform(post(ENTITY_API_URL + "/start/" + this.processDefinition.getKey())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(variables)))
            .andExpect(status().isOk());
    }

    @Test
    void startProcessWithForbiddenAuthorities() throws Exception {
        Map<String, String> variables = Map.of("key1", "value1", "key2", "value2");
        this.mockMvc.perform(post(ENTITY_API_URL + "/start/" + this.processDefinition.getKey())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(variables)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void startProcessWithUnauthenticatedUser() throws Exception {
        Map<String, String> variables = Map.of("key1", "value1", "key2", "value2");
        this.mockMvc.perform(post(ENTITY_API_URL + "/start/" + this.processDefinition.getKey())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(variables)))
            .andExpect(status().isUnauthorized());
    }

    // start process with id


    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void startProcessById() throws Exception {
        Map<String, String> variables = Map.of("key1", "value1", "key2", "value2");
        this.mockMvc.perform(post(ENTITY_API_URL + "/startById/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(variables)))
            .andExpect(status().isOk());
    }

    @Test
    void startProcessByIdWithForbiddenAuthorities() throws Exception {
        Map<String, String> variables = Map.of("key1", "value1", "key2", "value2");
        this.mockMvc.perform(post(ENTITY_API_URL + "/startById/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(variables)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void startProcessByIdWithUnauthenticatedUser() throws Exception {
        Map<String, String> variables = Map.of("key1", "value1", "key2", "value2");
        this.mockMvc.perform(post(ENTITY_API_URL + "/startById/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(variables)))
            .andExpect(status().isUnauthorized());
    }


    // get all process instances
    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getProcessXmlById() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/xml/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void getProcessXmlByIdWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/xml/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void getProcessXmlByIdWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/xml/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    // toggle process definition state
    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void toggleProcessDefinitionState() throws Exception {
        this.mockMvc.perform(post(ENTITY_API_URL + "/toggle-state/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("true")); // assuming the method returns true
    }

    @Test
    void toggleProcessDefinitionStateWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(post(ENTITY_API_URL + "/toggle-state/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void toggleProcessDefinitionStateWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(post(ENTITY_API_URL + "/toggle-state/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }


    // get current user process instances by process key
    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getCurrentUserProcessInstancesByKey() throws Exception {

        this.mockMvc.perform(get(ENTITY_API_URL + "/process-instances-currentUser-byKey/" + PROCESS_KEY)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(this.processInstance.getProcessInstanceId()));
    }

    @Test
    void getCurrentUserProcessInstancesByKeyWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-instances-currentUser-byKey/" + this.processDefinition.getKey())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void getCurrentUserProcessInstancesByKeyWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-instances-currentUser-byKey/" + this.processDefinition.getKey())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    // get process instance

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getProcessInstances() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-instances/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.length()").isNotEmpty())
            .andExpect(jsonPath("$.[*].processDefinitionId").value(hasItem(this.processDefinition.getId())));

    }

    @Test
    void testGetProcessInstancesWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-instances/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testGetProcessInstancesWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-instances/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getDeployedProcessesByCriteria() throws Exception {
        ProcessDefinitionCriteria criteria =new ProcessDefinitionCriteria(this.sampleDTO.getId(), this.sampleDTO.getName(), this.sampleDTO.getVersion(), this.sampleDTO.getKey());
        this.mockMvc.perform(post(ENTITY_API_URL + "/list-process")
                .param("page", "0")
                .param("size", "5")
                .content(TestUtil.convertObjectToJsonBytes(criteria))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value(this.sampleDTO.getName()))
            .andExpect(jsonPath("$.content[0].id").exists())
            .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void testGetDeployedProcessesByCriteriaWithForbiddenAuthorities() throws Exception {
        ProcessDefinitionCriteria criteria =new ProcessDefinitionCriteria(this.sampleDTO.getId(), this.sampleDTO.getName(), this.sampleDTO.getVersion(), this.sampleDTO.getKey());
        this.mockMvc.perform(post(ENTITY_API_URL + "/list-process")
                .param("page", "0")
                .param("size", "5")
                .content(TestUtil.convertObjectToJsonBytes(criteria))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }


    @Test
    @WithUnauthenticatedMockUser
    void testGetDeployedProcessesByCriteriaWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(post(ENTITY_API_URL + "/list-process")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getProcessInstancesByCriteria() throws Exception {
        ProcessInstanceCriteria criteria = new ProcessInstanceCriteria(this.processInstance.getId(), null, this.processInstance.getProcessDefinitionId(), null);
        this.mockMvc.perform(post(ENTITY_API_URL + "/process-instances/" + this.processDefinition.getId())
                .content(TestUtil.convertObjectToJsonBytes(criteria))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(jsonPath("$.content[*].id").value(hasItem(this.processInstance.getId())))
            .andExpect(jsonPath("$.content[*].processDefinitionId").value(hasItem(this.processInstance.getProcessDefinitionId())));



    }

    @Test
    void testGetProcessInstancesByCriteriaWithForbiddenAuthorities() throws Exception {
        ProcessInstanceCriteria criteria = new ProcessInstanceCriteria(this.processInstance.getId(), null, this.processInstance.getProcessDefinitionId(), null);
        this.mockMvc.perform(post(ENTITY_API_URL + "/process-instances/" + this.processDefinition.getId())
                .content(TestUtil.convertObjectToJsonBytes(criteria))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testGetProcessInstancesByCriteriaWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(post(ENTITY_API_URL + "/process-instances/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    private String generateContent() throws Exception {
        String bpmnFilePath = "src/test/resources/bpmnForTest/process.bpmn";
        String bpmnXml = new String(Files.readAllBytes(Paths.get(bpmnFilePath)));
        Map<String, String> bpmnData = new HashMap<>();
        bpmnData.put("name", this.sampleDTO.getName());
        bpmnData.put("xml", bpmnXml);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(bpmnData);
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testDeployProcessDefinition() throws Exception {
        // Perform the POST request
        this.mockMvc.perform(post(ENTITY_API_URL + "/repository/deploy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(generateContent()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(this.sampleDTO.getName()));
    }


    @Test
    @WithUnauthenticatedMockUser
    void testDeployProcessDefinitionWithUnauthenticatedUser() throws Exception {
        // Perform the POST request
        this.mockMvc.perform(post(ENTITY_API_URL + "/repository/deploy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(generateContent()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeployProcessDefinitionWithForbiddenAuthorities() throws Exception {
        // Perform the POST request
        this.mockMvc.perform(post(ENTITY_API_URL + "/repository/deploy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(generateContent()))
            .andExpect(status().isForbidden());
    }

    // get deployed process by id

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getDeployedProcessById() throws Exception {
        // Perform the GET request
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-definition/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(this.processDefinition.getId()))
            .andExpect(jsonPath("$.name").value(this.sampleDTO.getName()));
    }

    @Test
    @WithMockUser(username = "bs-user", authorities = {"BS_USER"})
    void getDeployedProcessByIdAsUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-definition/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(this.processDefinition.getId()))
            .andExpect(jsonPath("$.name").value(this.sampleDTO.getName()));
    }

    @Test
    void getDeployedProcessByIdWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-definition/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void getDeployedProcessByIdWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-definition/" + this.processDefinition.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }


    // start process once by key and get the started instance
    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void startProcessOnceByKeyAndGetStartedInstanceAsAdmin() throws Exception {
        Map<String, String> variables = new HashMap<>();
        variables.put("key1", "value1");
        variables.put("key2", "value2");

        this.mockMvc.perform(post(ENTITY_API_URL + "/start-process-once-and-get-started-instance/" + PROCESS_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.processInstanceId").value(this.processInstance.getId()));
    }

    @Test
    @WithMockUser(username = "bs-user", authorities = {"BS_USER"})
    void startProcessOnceByKeyAndGetStartedInstanceAsUser() throws Exception {
        Map<String, String> variables = new HashMap<>();
        variables.put("key1", "value1");
        variables.put("key2", "value2");

        this.mockMvc.perform(post(ENTITY_API_URL + "/start-process-once-and-get-started-instance/" + PROCESS_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.processInstanceId").value(this.processInstance.getId()));
    }

    @Test
    void startProcessOnceByKeyAndGetStartedInstanceWithForbiddenAuthorities() throws Exception {
        Map<String, String> variables = new HashMap<>();
        variables.put("key1", "value1");

        this.mockMvc.perform(post(ENTITY_API_URL + "/start-process-once-and-get-started-instance/" + PROCESS_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void startProcessOnceByKeyAndGetStartedInstanceWithUnauthenticatedUser() throws Exception {
        Map<String, String> variables = new HashMap<>();
        variables.put("key1", "value1");

        this.mockMvc.perform(post(ENTITY_API_URL + "/start-process-once-and-get-started-instance/" + PROCESS_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
            .andExpect(status().isUnauthorized());
    }

    // start process once by key

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void startProcessOnceByKeyWhenProcessAlreadyRunning() throws Exception {
        Map<String, String> variables = new HashMap<>();
        variables.put("key1", "value1");
        variables.put("key2", "value2");

        this.mockMvc.perform(post(ENTITY_API_URL + "/start-process-once/" + PROCESS_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.detail").value("Process instance already started"));
    }

    @Test
    void startProcessOnceByKeyWithForbiddenAuthorities() throws Exception {
        Map<String, String> variables = new HashMap<>();
        variables.put("key1", "value1");

        this.mockMvc.perform(post(ENTITY_API_URL + "/start-process-once/" + PROCESS_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void startProcessOnceByKeyWithUnauthenticatedUser() throws Exception {
        Map<String, String> variables = new HashMap<>();
        variables.put("key1", "value1");

        this.mockMvc.perform(post(ENTITY_API_URL + "/start-process-once/" + PROCESS_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(variables)))
            .andExpect(status().isUnauthorized());
    }

    // get process variables by process instance
    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getProcessVariablesByProcessInstanceIdAsAdmin() throws Exception {
        this.processEngine.getRuntimeService()
            .setVariable(this.processInstance.getId(), "variable1", "value1");
        this.processEngine.getRuntimeService()
            .setVariable(this.processInstance.getId(), "variable2", 123);

        this.mockMvc.perform(get(ENTITY_API_URL + "/process/variables")
                .param("processInstanceId", this.processInstance.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].name").exists())
            .andExpect(jsonPath("$[0].value").exists());
    }

    @Test
    @WithMockUser(username = "bs-user", authorities = {"BS_USER"})
    void getProcessVariablesByProcessInstanceIdAsUser() throws Exception {
        this.processEngine.getRuntimeService()
            .setVariable(this.processInstance.getId(), "variable1", "value1");
        this.processEngine.getRuntimeService()
            .setVariable(this.processInstance.getId(), "variable2", 123);
        this.mockMvc.perform(get(ENTITY_API_URL + "/process/variables")
                .param("processInstanceId", this.processInstance.getId()) // Use the process instance ID from @BeforeEach
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].name").value("variable1"))
            .andExpect(jsonPath("$[0].value").value("value1"));
    }

    @Test
    void getProcessVariablesByProcessInstanceIdWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process/variables")
                .param("processInstanceId", this.processInstance.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden()); // Expect 403 Forbidden
    }

    @Test
    @WithUnauthenticatedMockUser
    void getProcessVariablesByProcessInstanceIdWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process/variables")
                .param("processInstanceId", this.processInstance.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized()); // Expect 401 Unauthorized
    }


    @Test
    void getCurrentTaskWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/current-task-by-userId/" + this.processInstance.getId() + "/" + TestUtil.getRootID())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void getCurrentTaskWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/current-task-by-userId/" + this.processInstance.getId() + "/" + TestUtil.getRootID())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }


    @AfterEach
    void cleanupCamunda() {
        runtimeService.createProcessInstanceQuery()
            .list()
            .forEach(pi -> runtimeService.deleteProcessInstance(pi.getId(), "Test Cleanup"));

        historyService.createHistoricProcessInstanceQuery()
            .list()
            .forEach(hpi -> historyService.deleteHistoricProcessInstance(hpi.getId()));

        taskService.createTaskQuery()
            .list()
            .forEach(task -> taskService.deleteTask(task.getId(), true));

        repositoryService.createDeploymentQuery()
            .list()
            .forEach(deployment -> repositoryService.deleteDeployment(deployment.getId(), true));

        managementService.createJobQuery()
            .list()
            .forEach(job -> managementService.deleteJob(job.getId()));
    }



}
