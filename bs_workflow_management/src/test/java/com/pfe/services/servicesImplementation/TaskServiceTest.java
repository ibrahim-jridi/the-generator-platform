package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private ProcessEngine processEngine;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private TaskService taskService;

    @Mock
    private HistoryService historyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the processEngine methods to return the respective services
        when(processEngine.getRepositoryService()).thenReturn(repositoryService);
        when(processEngine.getRuntimeService()).thenReturn(runtimeService);
        when(processEngine.getTaskService()).thenReturn(taskService);
        when(processEngine.getHistoryService()).thenReturn(historyService);
    }

    @Test
    void testGetHistoricTasksByUserId() throws IOException {
        deployerProcessTest();
        startProcessTest();
        completeUserTasksTest();
        completeProcessTest();

        HistoricTaskInstance mockHistoricTask = mock(HistoricTaskInstance.class);
        HistoricTaskInstanceQuery mockHistoricTaskQuery = mock(HistoricTaskInstanceQuery.class);
        when(historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskQuery);
        when(mockHistoricTaskQuery.processInstanceId("mockInstanceId")).thenReturn(mockHistoricTaskQuery);
        when(mockHistoricTaskQuery.finished()).thenReturn(mockHistoricTaskQuery);
        when(mockHistoricTaskQuery.list()).thenReturn(Collections.singletonList(mockHistoricTask));
        when(mockHistoricTask.getName()).thenReturn("form1");

        List<HistoricTaskInstance> finishedTasks = historyService.createHistoricTaskInstanceQuery()
            .processInstanceId("mockInstanceId")
            .finished()
            .list();

        assertNotNull(finishedTasks);
        assertFalse(finishedTasks.isEmpty());
        assertEquals("form1", finishedTasks.get(0).getName());
    }

    @Test
    void testGetActiveTasksByUserId() throws IOException {
        deployerProcessTest();
        startProcessTest();

        Task mockTask = mock(Task.class);
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee("userId")).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(Collections.<Task>singletonList(mockTask));
        when(mockTask.getId()).thenReturn("task1");

        List<Task> finishedTasks = taskService.createTaskQuery()
            .taskAssignee("userId")
            .initializeFormKeys()
            .list();

        assertNotNull(finishedTasks);
        assertFalse(finishedTasks.isEmpty());
    }

    void deployerProcessTest() throws IOException {
        String bpmnFilePath = "src/test/resources/bpmnForTest/processWithTask.bpmn";
        String bpmnXml = new String(Files.readAllBytes(Paths.get(bpmnFilePath)));

        Deployment mockDeployment = mock(Deployment.class);
        DeploymentBuilder mockDeploymentBuilder = mock(DeploymentBuilder.class);

        when(repositoryService.createDeployment()).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.name("process 1")).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.addString("process 1.bpmn", bpmnXml)).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.deploy()).thenReturn(mockDeployment);

        Deployment result = repositoryService.createDeployment()
            .name("process 1")
            .addString("process 1.bpmn", bpmnXml)
            .deploy();

        assertNotNull(result);
        verify(mockDeploymentBuilder, times(1)).deploy();
    }

    void startProcessTest() {
        ProcessInstance mockProcessInstance = mock(ProcessInstance.class);
        when(runtimeService.startProcessInstanceByKey("process")).thenReturn(mockProcessInstance);
        when(mockProcessInstance.getId()).thenReturn("mockInstanceId");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("process");

        assertNotNull(processInstance);
        assertEquals("mockInstanceId", processInstance.getId());
    }

    private void completeUserTasksTest(){
        Task mockTask = mock(Task.class);
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId("mockInstanceId")).thenReturn(mockTaskQuery);

        when(mockTaskQuery.list()).thenReturn(Collections.<Task>singletonList(mockTask));
        when(mockTask.getId()).thenReturn("task1");

        taskService.createTaskQuery().processInstanceId("mockInstanceId").list()
            .forEach(task -> taskService.complete(task.getId()));

        verify(taskService, times(1)).complete("task1");
    }

    private void completeProcessTest(){
        ProcessInstanceQuery mockProcessInstanceQuery = mock(ProcessInstanceQuery.class);
        when(runtimeService.createProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processInstanceId("mockInstanceId")).thenReturn(null);
        assertNull(mockProcessInstanceQuery.processInstanceId("mockInstanceId"));
    }
}
