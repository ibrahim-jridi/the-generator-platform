package com.pfe.web.rest;

import com.pfe.IntegrationTest;
import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.services.criteria.TaskCriteria;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TaskManagementRessourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private ProcessEngine processEngine;

    private ProcessDefinitionDTO sampleDTO;

    private ProcessDefinition processDefinition;

    private ProcessInstance process;

    private static final String ENTITY_API_URL = "/api/v1/camunda";

    private static final String PROCESS_DEFINITION_ID="process";

    private Task completedtask;

    private Task activetask;

    private String userId;


    @BeforeEach
    void setUp() {
        this.userId = "13c9a935-7d64-4740-a3ce-1e0ecde7d2c1";
        this.sampleDTO = new ProcessDefinitionDTO();
        this.sampleDTO.setName("process");
        this.sampleDTO.setSuspensionState(true);
        this.repositoryService.createDeployment()
            .addClasspathResource("bpmnForTest/processWithTask.bpmn")
            .deploy();

        this.processDefinition = this.repositoryService.createProcessDefinitionQuery()
            .processDefinitionKey(PROCESS_DEFINITION_ID)
            .latestVersion()
            .singleResult();

        this.process = this.processEngine.getRuntimeService().startProcessInstanceByKey(PROCESS_DEFINITION_ID);



        this.completedtask = this.processEngine.getTaskService()
            .createTaskQuery()
            .processInstanceId(this.process.getId())
            .singleResult();


        if (this.completedtask != null) {
            this.processEngine.getTaskService().setAssignee(this.completedtask.getId(), this.userId);
            this.processEngine.getTaskService().complete(this.completedtask.getId());
        }

        this.activetask = this.processEngine.getTaskService()
            .createTaskQuery()
            .processInstanceId(this.process.getId())
            .active()
            .singleResult();

        if (this.activetask != null) {
            this.processEngine.getTaskService().setAssignee(this.activetask.getId(), TestUtil.getRootID().toString());
        }


    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetHistoricTasksByUserIdAndCriteria() throws Exception {
        TaskCriteria taskCriteria = new TaskCriteria(this.completedtask.getId(), null, this.completedtask.getName(), null, null);
        this.mockMvc.perform(post("/api/v1/camunda/historic-tasks-by-user/{userId}", this.userId)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(taskCriteria)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[*].id").value(hasItem(this.completedtask.getId())))
            .andExpect(jsonPath("$.content[*].name").value(hasItem(this.completedtask.getName())));
    }

    @Test
    void testGetHistoricTasksByUserIdAndCriteriaWithForbiddenAuthorities() throws Exception {
        TaskCriteria taskCriteria = new TaskCriteria(this.completedtask.getId(), null, this.completedtask.getName(), null, null);
        this.mockMvc.perform(post("/api/v1/camunda/historic-tasks-by-user/{userId}", this.userId)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(taskCriteria)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testGetHistoricTasksByUserIdAndCriteriaWithUnauthenticatedUser() throws Exception {
        TaskCriteria taskCriteria = new TaskCriteria(this.completedtask.getId(), null, this.completedtask.getName(), null, null);
        this.mockMvc.perform(post("/api/v1/camunda/historic-tasks-by-user/{userId}", this.userId)
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(taskCriteria)))
            .andExpect(status().isUnauthorized());

    }

    // get active tasks by user

    @Test
    void testGetActiveTasksByUserIdAndCriteriaWithForbiddenAuthorities() throws Exception {
        TaskCriteria taskCriteria = new TaskCriteria(this.activetask.getId(), null, this.activetask.getName(), null, null);
        this.mockMvc.perform(post("/api/v1/camunda/active-tasks-by-user")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(taskCriteria)))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testGetActiveTasksByUserIdAndCriteriaWithUnauthenticatedUser() throws Exception {
        TaskCriteria taskCriteria = new TaskCriteria(this.activetask.getId(), null, this.activetask.getName(), null, null);
        this.mockMvc.perform(post("/api/v1/camunda/active-tasks-by-user")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(taskCriteria)))
            .andExpect(status().isUnauthorized());

    }

    // get current task by process instance ID

    @Test
    void getCurrentTaskWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/current-task/" + this.process.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithUnauthenticatedMockUser
    void getCurrentTaskWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/current-task/" + this.process.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    // get active tasks by user ID

    @Test
    void getActiveTasksByUserIdWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/active-tasks-by-user")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden()); // Expect 403 Forbidden
    }

    @Test
    @WithUnauthenticatedMockUser
    void getActiveTasksByUserIdWithUnauthenticatedUser() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/active-tasks-by-user")
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized()); // Expect 401 Unauthorized
    }

    // get historic tasks by user ID
    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getHistoricTasksByUserIdAsAdmin() throws Exception {
        // Perform the request to get historic tasks
        this.mockMvc.perform(get(ENTITY_API_URL + "/historic-tasks-by-user/" + TestUtil.getRootID())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray()) // Verify the response is a list
            .andExpect(jsonPath("$[0].id").exists()) // Verify at least one task exists
            .andExpect(jsonPath("$[0].name").exists()); // Verify the task has a name
    }

    @Test
    @WithMockUser(username = "bs-user", authorities = {"BS_USER"})
    void getHistoricTasksByUserIdAsUser() throws Exception {
        // Perform the request to get historic tasks
        this.mockMvc.perform(get(ENTITY_API_URL + "/historic-tasks-by-user/" + TestUtil.getRootID())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray()) // Verify the response is a list
            .andExpect(jsonPath("$[0].id").exists()) // Verify at least one task exists
            .andExpect(jsonPath("$[0].name").exists()); // Verify the task has a name
    }

    @Test
    void getHistoricTasksByUserIdWithForbiddenAuthorities() throws Exception {
        // Perform the request without proper authorization
        this.mockMvc.perform(get(ENTITY_API_URL + "/historic-tasks-by-user/" + TestUtil.getRootID())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden()); // Expect 403 Forbidden
    }

    @Test
    @WithUnauthenticatedMockUser
    void getHistoricTasksByUserIdWithUnauthenticatedUser() throws Exception {
        // Perform the request without authentication
        this.mockMvc.perform(get(ENTITY_API_URL + "/historic-tasks-by-user/" + TestUtil.getRootID())
                .param("page", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized()); // Expect 401 Unauthorized
    }
    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetProcessByCategory() throws Exception {

        this.mockMvc.perform(get(ENTITY_API_URL + "/process-by-category")
                .param("category", "creationEntreprise")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

        this.mockMvc.perform(get(ENTITY_API_URL + "/process-by-category")
                .param("category", "creationEntreprise")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.size()").value(1));

    }
    @Test
    void testGetProcessByCategoryWithForbiddenAuthorities() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-by-category")
                .param("category", "creationEntreprise")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
    @Test
    @WithUnauthenticatedMockUser
    void testGetGetProcessByCategoryUnAuthorized() throws Exception {
        this.mockMvc.perform(get(ENTITY_API_URL + "/process-by-category")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
}
