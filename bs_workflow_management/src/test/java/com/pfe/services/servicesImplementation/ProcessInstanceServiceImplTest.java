package com.pfe.services.servicesImplementation;

import com.pfe.dto.ProcessInstanceDTO;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.wildfly.common.Assert.assertTrue;

class ProcessInstanceServiceImplTest {

    @Mock
    private ProcessEngine processEngine;

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private HistoryService historyService;

    @Mock
    private RepositoryService repositoryService;


    @InjectMocks
    private ProcessInstanceServiceImpl processInstanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllHistoricInstances() {
        String processDefinitionId = "test-process-definition-id";
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);

        List<HistoricProcessInstance> historicInstances = new ArrayList<>();
        HistoricProcessInstance instance1 = mock(HistoricProcessInstance.class);
        HistoricProcessInstance instance2 = mock(HistoricProcessInstance.class);
        historicInstances.add(instance1);
        historicInstances.add(instance2);

        when(processEngine.getHistoryService()).thenReturn(historyService);
        when(historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionId(processDefinitionId)).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.finished()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicInstances);

        List<HistoricProcessInstance> result = processInstanceService.getAllHistoricInstances(processDefinitionId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(historyService).createHistoricProcessInstanceQuery();
        verify(mockHistoricProcessInstanceQuery).processDefinitionId(processDefinitionId);
        verify(mockHistoricProcessInstanceQuery).finished();
        verify(mockHistoricProcessInstanceQuery).list();
    }

    @Test
    void testGetAllActiveInstances() {
        String processDefinitionId = "test-process-definition-id";
        ProcessInstanceQuery mockProcessInstanceQuery = mock(ProcessInstanceQuery.class);

        List<ProcessInstance> processInstances = new ArrayList<>();
        ProcessInstance instance1 = mock(ProcessInstance.class);
        ProcessInstance instance2 = mock(ProcessInstance.class);
        processInstances.add(instance1);
        processInstances.add(instance2);

        when(processEngine.getRuntimeService()).thenReturn(runtimeService);
        when(runtimeService.createProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processDefinitionId(processDefinitionId)).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.list()).thenReturn(processInstances);

        List<ProcessInstanceDTO> result = processInstanceService.getAllActiveInstances(processDefinitionId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(runtimeService).createProcessInstanceQuery();
        verify(mockProcessInstanceQuery).processDefinitionId(processDefinitionId);
        verify(mockProcessInstanceQuery).list();
    }

    @Test
    void testGetAllProcessInstances() {
        String processDefinitionKey = "test-process-definition-key";
        ProcessInstanceQuery mockProcessInstanceQuery = mock(ProcessInstanceQuery.class);

        List<ProcessInstance> processInstances = new ArrayList<>();
        ProcessInstance instance1 = mock(ProcessInstance.class);
        ProcessInstance instance2 = mock(ProcessInstance.class);
        processInstances.add(instance1);
        processInstances.add(instance2);

        when(processEngine.getRuntimeService()).thenReturn(runtimeService);
        when(runtimeService.createProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processDefinitionId(processDefinitionKey)).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.list()).thenReturn(processInstances);

        List<ProcessInstanceDTO> result = processInstanceService.getAllProcessInstances(processDefinitionKey);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(runtimeService).createProcessInstanceQuery();
        verify(mockProcessInstanceQuery).processDefinitionId(processDefinitionKey);
        verify(mockProcessInstanceQuery).list();
    }

    @Test
    void testGetProcessInstanceCounts() {
        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        ProcessDefinition mockProcessDefinition = mock(ProcessDefinition.class);
        RuntimeService mockRuntimeService = mock(RuntimeService.class);
        HistoryService mockHistoryService = mock(HistoryService.class);
        ProcessInstanceQuery mockProcessInstanceQuery = mock(ProcessInstanceQuery.class);
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);

        when(processEngine.getRepositoryService()).thenReturn(repositoryService);
        when(repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.list()).thenReturn(List.of(mockProcessDefinition));
        when(mockProcessDefinition.getId()).thenReturn("testProcessDefinitionId");

        when(processEngine.getRuntimeService()).thenReturn(mockRuntimeService);
        when(mockRuntimeService.createProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processDefinitionId("testProcessDefinitionId")).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.active()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.count()).thenReturn(5L);

        when(processEngine.getHistoryService()).thenReturn(mockHistoryService);
        when(mockHistoryService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionId("testProcessDefinitionId")).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.suspended()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.count()).thenReturn(3L);

        Map<String, Map<String, Long>> result = processInstanceService.getProcessInstanceCounts();

        assertNotNull(result);
        assertTrue(result.containsKey("testProcessDefinitionId"));
        assertEquals(5L, result.get("testProcessDefinitionId").get("active"));
        assertEquals(3L, result.get("testProcessDefinitionId").get("suspended"));
    }

}
