package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class HistoryServiceImplTest {

    @Mock
    private HistoryService historyService;

    @InjectMocks
    private HistoryServiceImpl historyServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCompletedProcessInstances() {
        String processDefinitionKey = "test-definition-key";
        HistoricProcessInstanceQuery mockQuery = mock(HistoricProcessInstanceQuery.class);
        List<HistoricProcessInstance> mockInstances = new ArrayList<>();
        mockInstances.add(mock(HistoricProcessInstance.class));
        mockInstances.add(mock(HistoricProcessInstance.class));

        when(historyService.createHistoricProcessInstanceQuery()).thenReturn(mockQuery);
        when(mockQuery.processDefinitionKey(processDefinitionKey)).thenReturn(mockQuery);
        when(mockQuery.finished()).thenReturn(mockQuery);
        when(mockQuery.list()).thenReturn(mockInstances);

        List<HistoricProcessInstance> result = historyServiceImpl.getCompletedProcessInstances(processDefinitionKey);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(historyService).createHistoricProcessInstanceQuery();
        verify(mockQuery).processDefinitionKey(processDefinitionKey);
        verify(mockQuery).finished();
        verify(mockQuery).list();
    }

    @Test
    void testGetProcessInstanceHistory() {
        String processInstanceId = "test-instance-id";
        HistoricProcessInstanceQuery mockQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockInstance = mock(HistoricProcessInstance.class);

        when(historyService.createHistoricProcessInstanceQuery()).thenReturn(mockQuery);
        when(mockQuery.processInstanceId(processInstanceId)).thenReturn(mockQuery);
        when(mockQuery.singleResult()).thenReturn(mockInstance);

        HistoricProcessInstance result = historyServiceImpl.getProcessInstanceHistory(processInstanceId);

        assertNotNull(result);
        verify(historyService).createHistoricProcessInstanceQuery();
        verify(mockQuery).processInstanceId(processInstanceId);
        verify(mockQuery).singleResult();
    }

    @Test
    void testGetHistoricTaskInstanceCount() {
        HistoricTaskInstanceQuery mockHistoricTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);
        when(historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.count()).thenReturn(10L);

        long result = historyServiceImpl.getHistoricTaskInstanceCount();

        assertEquals(10L, result);
        verify(historyService).createHistoricTaskInstanceQuery();
    }
}
