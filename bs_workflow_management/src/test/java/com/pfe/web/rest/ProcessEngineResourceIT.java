package com.pfe.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pfe.IntegrationTest;
import com.pfe.dto.HistoricTaskInstanceDTO;
import com.pfe.services.ProcessEngineService;
import com.pfe.web.rest.errors.ProcessEngineException;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProcessEngineResourceIT {

    private static final String ENTITY_API_URL = "/api/v1/camunda";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessEngineService processEngineService;

    @Test
    @WithUnauthenticatedMockUser
    void testGetHistoricTasksByProcessInstanceId_unauthenticated() throws Exception {
        //given
        String processInstanceId = "processInstanceId";

        //when & then
        mockMvc.perform(get(ENTITY_API_URL + "/history/tasks/{processInstanceId}", processInstanceId))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "bs-demo", authorities = {"BS_demo"})
    void testGetHistoricTasksByProcessInstanceId_forbidden() throws Exception {
        //given
        String processInstanceId = "processInstanceId";

        //when & then
        mockMvc.perform(get(ENTITY_API_URL + "/history/tasks/{processInstanceId}", processInstanceId))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetHistoricTasksByProcessInstanceId_validId() throws Exception {
        //given
        String processInstanceId = "processInstanceId";
        List<HistoricTaskInstanceDTO> mockHistoricTasks = List.of(new HistoricTaskInstanceDTO());

        when(processEngineService.getHistoricTaskInstancesByProcessInstanceId(processInstanceId))
            .thenReturn(mockHistoricTasks);

        //when & then
        mockMvc.perform(get(ENTITY_API_URL + "/history/tasks/{processInstanceId}", processInstanceId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0]").exists());
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testGetHistoricTasksByProcessInstanceId_noTasks() throws Exception {
        //given
        String processInstanceId = "processInstanceId";
        List<HistoricTaskInstanceDTO> mockHistoricTasks = Collections.emptyList();

        when(processEngineService.getHistoricTaskInstancesByProcessInstanceId(processInstanceId))
            .thenReturn(mockHistoricTasks);

        //when & then
        mockMvc.perform(get(ENTITY_API_URL + "/history/tasks/{processInstanceId}", processInstanceId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void getProcessVariablesByTaskId_ValidTask() throws Exception {
        // Arrange
        String processInstanceId = "12345";
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode mockVariable1 = objectMapper.createObjectNode();
        mockVariable1.put("key1", "value1");
        ObjectNode mockVariable2 = objectMapper.createObjectNode();
        mockVariable2.put("key2", "value2");
        List<ObjectNode> mockVariables = List.of(mockVariable1, mockVariable2);

        when(processEngineService.getProcessVariablesByProcessInstanceId(
            processInstanceId)).thenReturn(mockVariables);

        // Act & Assert
        mockMvc.perform(get(ENTITY_API_URL + "/process/variables")
                .param("processInstanceId", processInstanceId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(mockVariables.size())); // Verifies response size
    }

    @Test
    @WithUnauthenticatedMockUser
    void getProcessVariablesByTaskId_forbidden() throws Exception {
        // Arrange
        String processInstanceId = "12345";

        // Act & Assert
        mockMvc.perform(get(ENTITY_API_URL + "/process/variables")
                .param("taskId", processInstanceId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUnauthenticatedMockUser
    void getProcessVariablesByTaskId_unauthenticated() throws Exception {
        // Arrange
        String processInstanceId = "12345";

        // Act & Assert
        mockMvc.perform(get(ENTITY_API_URL + "/process/variables")
                .param("processInstanceId", processInstanceId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUnauthenticatedMockUser
    void testIsUserAssignedToProcess_unauthenticated() throws Exception {
        String processInstanceId = "processInstanceId";
        String userId = "userId";

        mockMvc.perform(get(ENTITY_API_URL + "/check-user-assignment/{processInstanceId}/{userId}",
                processInstanceId, userId))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "not-authorized-user")
    void testIsUserAssignedToProcess_forbidden() throws Exception {
        String processInstanceId = "processInstanceId";
        String userId = "userId";

        mockMvc.perform(get(ENTITY_API_URL + "/check-user-assignment/{processInstanceId}/{userId}",
                processInstanceId, userId))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "bs-user", authorities = {"BS_USER"})
    void testIsUserAssignedToProcess_asUser() throws Exception {
        String processInstanceId = "processInstanceId";
        String userId = "userId";

        when(processEngineService.isUserAssignedToProcessInstance(userId, processInstanceId))
            .thenReturn(true);

        mockMvc.perform(get(ENTITY_API_URL + "/check-user-assignment/{processInstanceId}/{userId}",
                processInstanceId, userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(true));
    }

    @Test
    @WithMockUser(username = "bs-admin", authorities = {"BS_ADMIN"})
    void testIsUserAssignedToProcess_asAdmin() throws Exception {
        String processInstanceId = "processInstanceId";
        String userId = "userId";

        when(processEngineService.isUserAssignedToProcessInstance(userId, processInstanceId))
            .thenReturn(false);

        mockMvc.perform(get(ENTITY_API_URL + "/check-user-assignment/{processInstanceId}/{userId}",
                processInstanceId, userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(false));
    }

    @Test
    @WithMockUser(username = "bs-user", authorities = {"BS_USER"})
    void testIsUserAssignedToProcess_serviceException() throws Exception {
        String processInstanceId = "processInstanceId";
        String userId = "userId";

        when(processEngineService.isUserAssignedToProcessInstance(userId, processInstanceId))
            .thenThrow(new ProcessEngineException("Service error"));

        mockMvc.perform(get(ENTITY_API_URL + "/check-user-assignment/{processInstanceId}/{userId}",
                processInstanceId, userId))
            .andExpect(status().isInternalServerError());
    }

}
