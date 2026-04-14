package com.pfe.services.servicesImplementation;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pfe.domain.AbstractTask;
import com.pfe.domain.enumeration.TaskStatus;
import com.pfe.dto.*;
import com.pfe.feignServices.UserService;
import com.pfe.mapper.HistoricProcessInstanceMapper;
import com.pfe.mapper.HistoricTaskInstanceMapper;
import com.pfe.mapper.ProcessInstanceMapper;
import com.pfe.mapper.TaskMapper;
import com.pfe.security.SecurityUtils;
import com.pfe.services.JsonProcessingService;
import com.pfe.services.ProcessInstanceQueryService;
import com.pfe.services.criteria.HistoricProcessInstanceTaskCriteria;
import com.pfe.services.utils.JwtUtils;
import com.pfe.web.rest.errors.CustomException;
import com.pfe.web.rest.errors.ProcessEngineException;
import com.pfe.dto.AbstractProcessInstance;
import com.pfe.dto.FormDTO;
import com.pfe.dto.HistoricProcessInstanceDTO;
import com.pfe.dto.HistoricProcessInstanceWithTaskDTO;
import com.pfe.dto.HistoricTaskInstanceDTO;
import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.dto.ProcessInstanceDTO;
import com.pfe.dto.SubmissionDTO;
import com.pfe.dto.TaskDTO;
import com.pfe.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.AuthorizationQuery;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.externaltask.ExternalTaskQueryBuilder;
import org.camunda.bpm.engine.externaltask.ExternalTaskQueryTopicBuilder;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.*;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProcessEngineServiceTest {

    @Mock
    private ProcessEngine processEngine;
    @Mock
    private UserService userService;
    @Mock
    private HistoricTaskInstanceMapper historicTaskInstanceMapper;

    @Mock
    private com.pfe.feignServices.FormService formServiceClient;

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private RepositoryService repositoryService;

    @Mock
    private HistoryService historyService;

    @Mock
    private ExternalTaskService externalTaskService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private FormService formService;

    @Mock
    private CaseService caseService;
    @Mock
    private ManagementService managementService;

    @Mock
    private IdentityService identityService;

    @Mock
    private TaskService taskService;

    @Mock
    private Task task;

    @Mock
    private ProcessInstanceMapper processInstanceMapper;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ProcessInstanceQueryService processInstanceQueryService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    JsonProcessingService jsonProcessingService;
    @Mock
    private ProcessDefinitionQuery processDefinitionQuery;

    @InjectMocks
    private ProcessEngineServiceImpl processEngineService;

    @Mock
    private HistoricProcessInstanceMapper historicProcessInstanceMapper;

    private static final String PROCESS_DEFINITION_ID = "process";
    private static final String PROCESS_KEY = "processKey";
    private static final String PROCESS_INSTANCE_ID = UUID.randomUUID().toString();
    private static final String PROCESS_BUSINESS_KEY = "businessKey";
    private static final String EXECUTION_ID = "executionId";
    private static final String TASK_ID = "taskId";
    private static final String TASK_DEFINITION_KEY = "taskId";
    private static final String FORM_ID = "formId";
    private static final String FORM_KEY = "formKey";
    private static final String USER_ID = UUID.randomUUID().toString();
    private static final String GROUP_ID = "groupId";
    private static final String DEPLOYMENT_NAME = "deploymentName";
    private static final String DEPLOYMENT_ID = "deploymentId";
    private static final String AUTHORIZATION_ID = "authorizationId";
    private static final String JOB_ID = "jobId";
    private static final String CASE_DEFINITION_KEY = "caseKey";
    private static final String CASE_EXECUTION_ID = "caseExecutionId";
    private static final Logger log = LoggerFactory.getLogger(ProcessEngineServiceImpl.class);


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    //signal execution
    @Test
    void SignalExecution() {
        when(this.processEngine.getRuntimeService()).thenReturn(this.runtimeService);
        this.processEngineService.signalExecution(EXECUTION_ID);
        verify(this.runtimeService).signal(EXECUTION_ID);
    }

    @Test
    void testSignalExecution_Failure() {
        when(this.processEngine.getRuntimeService()).thenReturn(this.runtimeService);
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("Signal failed")).when(this.runtimeService).signal(EXECUTION_ID);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.signalExecution(EXECUTION_ID);
        });
        assertEquals("Failed to signal execution: " + EXECUTION_ID, exception.getMessage());
    }

    //process xml by id

    @Test
    void testGetProcessXmlById() {
        BpmnModelInstance mockBpmnInstance = Bpmn.createExecutableProcess(PROCESS_DEFINITION_ID)
            .startEvent()
            .endEvent()
            .done();
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.getBpmnModelInstance(PROCESS_DEFINITION_ID)).thenReturn(mockBpmnInstance);
        String processXML = this.processEngineService.getProcessXmlById(PROCESS_DEFINITION_ID);
        String expectedXml = Bpmn.convertToString(mockBpmnInstance);
        assertEquals(processXML, expectedXml);
    }

    @Test
    void testGetProcessXmlById_Failure() {
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("Signal failed")).when(this.repositoryService).getBpmnModelInstance(PROCESS_DEFINITION_ID);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.getProcessXmlById(PROCESS_DEFINITION_ID);
        });
        assertEquals("Failed to get BPMN XML for process definition ID: " + PROCESS_DEFINITION_ID, exception.getMessage());
    }

    //toggle process definition state
    @Test
    void testToggleProcessDefinitionState_suspended() {
        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        ProcessDefinition mockProcessDefinition = mock(ProcessDefinition.class);
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.singleResult()).thenReturn(mockProcessDefinition);
        Boolean isSuspended = this.processEngineService.toggleProcessDefinitionState(PROCESS_DEFINITION_ID);
        assertEquals(isSuspended, true);
    }

    @Test
    void testToggleProcessDefinitionState_active() {
        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        ProcessDefinition mockProcessDefinition = mock(ProcessDefinition.class);
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.singleResult()).thenReturn(mockProcessDefinition);
        when(mockProcessDefinition.isSuspended()).thenReturn(true);
        Boolean isSuspended = this.processEngineService.toggleProcessDefinitionState(PROCESS_DEFINITION_ID);
        assertEquals(isSuspended, false);
    }

    @Test
    void testToggleProcessDefinitionState_Null_ProcessDefinition() {
        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.singleResult()).thenReturn(null);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.toggleProcessDefinitionState(PROCESS_DEFINITION_ID);
        });
        assertEquals("Failed to toggle state for process definition with ID: " + PROCESS_DEFINITION_ID, exception.getMessage());
    }

    //get process instance

    @Test
    void testGetProcessInstances() {
        ProcessDefinition mockProcessDefinition = mock(ProcessDefinition.class);
        ProcessInstance processInstance = mock(ProcessInstance.class);
        when(processInstance.getProcessDefinitionId()).thenReturn("test-definition-id");
        when(processInstance.getBusinessKey()).thenReturn("test-business-key");
        when(processInstance.isSuspended()).thenReturn(true);
        List<ProcessInstance> processInstances = List.of(processInstance);
        ProcessInstanceDTO processInstanceDTO = new ProcessInstanceDTO();
        processInstanceDTO.setProcessDefinitionId("test-definition-id");
        processInstanceDTO.setBusinessKey("test-business-key");
        processInstanceDTO.setState("SUSPENDED");

        ProcessDefinitionQuery processDefinitionQuery = mock(ProcessDefinitionQuery.class);
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.singleResult()).thenReturn(mockProcessDefinition);

        ProcessInstanceQuery activeQuery = mock(ProcessInstanceQuery.class);
        when(this.processEngine.getRuntimeService()).thenReturn(this.runtimeService);
        when(this.runtimeService.createProcessInstanceQuery()).thenReturn(activeQuery);
        when(activeQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(activeQuery);
        when(activeQuery.active()).thenReturn(activeQuery);
        when(activeQuery.list()).thenReturn(processInstances);

        ProcessInstanceQuery suspendedQuery = mock(ProcessInstanceQuery.class);
        when(activeQuery.suspended()).thenReturn(suspendedQuery);
        when(suspendedQuery.list()).thenReturn(processInstances);

        when(this.processInstanceMapper.toDto(any(ProcessInstance.class)))
            .thenAnswer(invocation -> {
                ProcessInstanceDTO dto = new ProcessInstanceDTO();
                dto.setProcessDefinitionName("process definition");
                return dto;
            });

        HistoricProcessInstanceQuery completedQuery = mock(HistoricProcessInstanceQuery.class);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(completedQuery);
        when(completedQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(completedQuery);
        when(completedQuery.finished()).thenReturn(completedQuery);
        when(completedQuery.list()).thenReturn(List.of(mock(HistoricProcessInstance.class)));

        List<AbstractProcessInstance> result = this.processEngineService.getProcessInstances(PROCESS_DEFINITION_ID);
        assertEquals(result.size(), 3);
    }

    @Test
    void testGetProcessInstances_EntityNotFoundException() {
        ProcessDefinitionQuery processDefinitionQuery = mock(ProcessDefinitionQuery.class);
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.singleResult()).thenReturn(null);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            this.processEngineService.getProcessInstances(PROCESS_DEFINITION_ID);
        });
        // Validate the exception message
        assertEquals("Process definition with ID " + PROCESS_DEFINITION_ID + " not found.", exception.getMessage());
    }

    //get process instance by criteria
    @Test
    void testGetProcessInstanceByCriteria() {
        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        ProcessDefinition mockProcessDefinition = mock(ProcessDefinition.class);
        ProcessInstanceDTO processInstanceDTO = new ProcessInstanceDTO();
        List<ProcessInstanceDTO> processInstanceDTOS = new ArrayList<>();
        processInstanceDTOS.add(processInstanceDTO);
        List<HistoricProcessInstanceDTO> historicTaskInstanceDTOS = new ArrayList<>();
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.singleResult()).thenReturn(mockProcessDefinition);
        when(this.processInstanceQueryService.findProcessInstanceByCriteria(null, mockProcessDefinition)).thenReturn(processInstanceDTOS);
        when(this.processInstanceQueryService.findHistoricProcessInstanceByCriteria(null, mockProcessDefinition)).thenReturn(historicTaskInstanceDTOS);
        Page<AbstractProcessInstance> expectedPage = new PageImpl(processInstanceDTOS, Pageable.ofSize(1), 1);
        Page<AbstractProcessInstance> page = this.processEngineService.getProcessInstancesByCriteria(null, PROCESS_DEFINITION_ID, Pageable.ofSize(1));
        assertEquals(page, expectedPage);
    }

    @Test
    void testGetProcessInstanceByCriteria_EntityNotFoundException() {
        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.singleResult()).thenReturn(null);
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            this.processEngineService.getProcessInstancesByCriteria(null, PROCESS_DEFINITION_ID, null);
        });
        assertEquals("Process definition with ID " + PROCESS_DEFINITION_ID + " not found.", exception.getMessage());
    }

    // get current user process instances

    @Test
    void testGetCurrentUserProcessInstances() {
        UUID currentUserId = UUID.randomUUID();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstances(PROCESS_DEFINITION_ID);
        assertEquals(result, Collections.emptyList());
    }

    @Test
    void testGetCurrentUserProcessInstances_full_process_instance_list() {
        UUID currentUserId = UUID.randomUUID();
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricProcessInstance.getStartUserId()).thenReturn(currentUserId.toString());
        historicProcessInstances.add(mockHistoricProcessInstance);
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstances(PROCESS_DEFINITION_ID);
        HistoricProcessInstanceDTO historicProcessInstanceDTO = new HistoricProcessInstanceDTO();
        historicProcessInstanceDTO.setStartUserId(currentUserId.toString());
        List<AbstractProcessInstance> expected = new ArrayList<>();
        expected.add(historicProcessInstanceDTO);
        assertEquals(result, expected);
    }

    @Test
    void testGetCurrentUserProcessInstances_full_process_instance_list_empty_response() {
        UUID currentUserId = UUID.randomUUID();
        UUID wrongCurrentUserId = UUID.randomUUID();
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricProcessInstance.getStartUserId()).thenReturn(wrongCurrentUserId.toString());
        historicProcessInstances.add(mockHistoricProcessInstance);
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstances(PROCESS_DEFINITION_ID);
        List<AbstractProcessInstance> expected = new ArrayList<>();
        assertEquals(result, expected);
    }

    @Test
    void testGetCurrentUserProcessInstances_full_process_instance_list_empty_response_null_starterId() {
        UUID currentUserId = UUID.randomUUID();
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricProcessInstance.getStartUserId()).thenReturn(null);
        historicProcessInstances.add(mockHistoricProcessInstance);
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstances(PROCESS_DEFINITION_ID);
        List<AbstractProcessInstance> expected = new ArrayList<>();
        assertEquals(result, expected);
    }

    // get current user process instances by key

    @Test
    void testGetCurrentUserProcessInstanceByKey() {
        UUID currentUserId = UUID.randomUUID();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionKey(PROCESS_KEY)).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstancesByKey(PROCESS_KEY);
        assertEquals(result, Collections.emptyList());
    }


    @Test
    void testGetCurrentUserProcessInstancesByKey_full_process_instance_list() {
        UUID currentUserId = UUID.randomUUID();
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricProcessInstance.getStartUserId()).thenReturn(currentUserId.toString());
        historicProcessInstances.add(mockHistoricProcessInstance);
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionKey(PROCESS_KEY)).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstancesByKey(PROCESS_KEY);
        HistoricProcessInstanceDTO historicProcessInstanceDTO = new HistoricProcessInstanceDTO();
        historicProcessInstanceDTO.setStartUserId(currentUserId.toString());
        List<AbstractProcessInstance> expected = new ArrayList<>();
        expected.add(historicProcessInstanceDTO);
        assertEquals(result, expected);
    }

    @Test
    void testGetCurrentUserProcessInstancesByKey_full_process_instance_list_empty_response() {
        UUID currentUserId = UUID.randomUUID();
        UUID wrongCurrentUserId = UUID.randomUUID();
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricProcessInstance.getStartUserId()).thenReturn(wrongCurrentUserId.toString());
        historicProcessInstances.add(mockHistoricProcessInstance);
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionKey(PROCESS_KEY)).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstancesByKey(PROCESS_KEY);
        List<AbstractProcessInstance> expected = new ArrayList<>();
        assertEquals(result, expected);
    }

    @Test
    void testGetCurrentUserProcessInstancesByKey_full_process_instance_list_empty_response_null_starterId() {
        UUID currentUserId = UUID.randomUUID();
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricProcessInstance.getStartUserId()).thenReturn(null);
        historicProcessInstances.add(mockHistoricProcessInstance);
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processDefinitionKey(PROCESS_KEY)).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstancesByKey(PROCESS_KEY);
        List<AbstractProcessInstance> expected = new ArrayList<>();
        assertEquals(result, expected);
    }


    // get current user process instances by user id

    @Test
    void testGetCurrentUserProcessInstanceByUserId() {
        UUID currentUserId = UUID.randomUUID();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstancesByUserId();
        assertEquals(result, Collections.emptyList());
    }


    @Test
    void testGetCurrentUserProcessInstancesByUserId_full_process_instance_list() {
        UUID currentUserId = UUID.randomUUID();
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricProcessInstance.getStartUserId()).thenReturn(currentUserId.toString());
        historicProcessInstances.add(mockHistoricProcessInstance);
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstancesByUserId();
        HistoricProcessInstanceDTO historicProcessInstanceDTO = new HistoricProcessInstanceDTO();
        historicProcessInstanceDTO.setStartUserId(currentUserId.toString());
        List<AbstractProcessInstance> expected = new ArrayList<>();
        expected.add(historicProcessInstanceDTO);
        assertEquals(result, expected);
    }

    @Test
    void testGetCurrentUserProcessInstancesByUserId_full_process_instance_list_empty_response() {
        UUID currentUserId = UUID.randomUUID();
        UUID wrongCurrentUserId = UUID.randomUUID();
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricProcessInstance.getStartUserId()).thenReturn(wrongCurrentUserId.toString());
        historicProcessInstances.add(mockHistoricProcessInstance);
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstancesByUserId();
        List<AbstractProcessInstance> expected = new ArrayList<>();
        assertEquals(result, expected);
    }

    @Test
    void testGetCurrentUserProcessInstancesByUserId_full_process_instance_list_empty_response_null_starterId() {
        UUID currentUserId = UUID.randomUUID();
        List<HistoricProcessInstance> historicProcessInstances = new ArrayList<>();
        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricProcessInstance.getStartUserId()).thenReturn(null);
        historicProcessInstances.add(mockHistoricProcessInstance);
        when(this.jwtUtils.getConnectedUserId()).thenReturn(currentUserId);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.list()).thenReturn(historicProcessInstances);
        List<AbstractProcessInstance> result = this.processEngineService.getCurrentUserProcessInstancesByUserId();
        List<AbstractProcessInstance> expected = new ArrayList<>();
        assertEquals(result, expected);
    }

    // get tasks by assignee

    @Test
    void testGetTasksAssignee() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(USER_ID)).thenReturn(mockTaskQuery);
        List<Task> result = this.processEngineService.getTasksByAssignee(USER_ID);
        assertEquals(result, mockTaskQuery.list());
    }

    @Test
    void testGetTasksAssignee_Failure() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("task failed")).when(mockTaskQuery).taskAssignee(USER_ID);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.getTasksByAssignee(USER_ID);
        });
        assertEquals("Failed to get tasks for assignee: " + USER_ID, exception.getMessage());
    }

    // get complete tasks

    @Test
    void testCompleteTask() {
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        this.processEngineService.completeTask(TASK_ID);
        verify(this.taskService).complete(TASK_ID);
    }


    @Test
    void testCompleteTask_Failure() {
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("task failed")).when(this.taskService).complete(TASK_ID);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.completeTask(TASK_ID);
        });
        assertEquals("Failed to complete task: " + TASK_ID, exception.getMessage());
    }

    // get current task by process instance id

    @Test
    void testGetCurrentTaskByProcessInstanceId_formKey_and_assigneeId_null() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            UUID currentUserId = UUID.randomUUID();
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            TaskDTO result = this.processEngineService.getCurrentTaskByProcessInstanceId(PROCESS_INSTANCE_ID);
            assertEquals(result, taskDto);
        }
    }

    @Test
    void testGetCurrentTaskByProcessInstanceId_formKey_and_assigneeId_null_multi_current_task() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.count()).thenReturn(2L);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            UUID currentUserId = UUID.randomUUID();
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            TaskDTO result = this.processEngineService.getCurrentTaskByProcessInstanceId(PROCESS_INSTANCE_ID);
            assertEquals(result, taskDto);
        }
    }

    @Test
    void testGetCurrentTaskByProcessInstanceId_with_formKey() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        UUID formKey = UUID.randomUUID();
        when(mockTask.getFormKey()).thenReturn(formKey.toString());
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setFormKey(formKey.toString());
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.formServiceClient.getForm(formKey)).thenReturn(mockResponseEntity);
        FormDTO formDTO = new FormDTO();
        when(mockResponseEntity.getBody()).thenReturn(formDTO);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            UUID currentUserId = UUID.randomUUID();
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            TaskDTO result = this.processEngineService.getCurrentTaskByProcessInstanceId(PROCESS_INSTANCE_ID);
            assertEquals(result, taskDto);
        }
    }

    @Test
    void testGetCurrentTaskByProcessInstanceId_with_assigneeId() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        when(mockTask.getAssignee()).thenReturn(currentUserId.toString());
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setAssignee(currentUserId.toString());
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.userService.getuser(currentUserId)).thenReturn(mockResponseEntity);
        UserDTO userDTO = new UserDTO();
        when(mockResponseEntity.getBody()).thenReturn(userDTO);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            TaskDTO result = this.processEngineService.getCurrentTaskByProcessInstanceId(PROCESS_INSTANCE_ID);
            assertEquals(result, taskDto);
        }
    }

    @Test
    void testGetCurrentTaskByProcessInstanceId_with_assigneeId_not_equal_current_userId() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        when(mockTask.getAssignee()).thenReturn(UUID.randomUUID().toString());
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setAssignee(UUID.randomUUID().toString());
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.userService.getuser(currentUserId)).thenReturn(mockResponseEntity);
        UserDTO userDTO = new UserDTO();
        when(mockResponseEntity.getBody()).thenReturn(userDTO);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            TaskDTO result = this.processEngineService.getCurrentTaskByProcessInstanceId(PROCESS_INSTANCE_ID);
            assertEquals(result, taskDto);
        }
    }


    @Test
    void testGetCurrentTaskByProcessInstanceId_Failure_empty_task_list() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            when(this.processEngine.getTaskService()).thenReturn(this.taskService);
            when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
            when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.list()).thenReturn(new ArrayList<>());
            com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
                com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
                this.processEngineService.getCurrentTaskByProcessInstanceId(PROCESS_INSTANCE_ID);
            });
            assertEquals("No active task found for process instance: " + PROCESS_INSTANCE_ID, exception.getMessage());
        }
    }

    @Test
    void testGetCurrentTaskByProcessInstanceId_Failure_null_task_list() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            when(this.processEngine.getTaskService()).thenReturn(this.taskService);
            when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
            when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.list()).thenReturn(null);
            com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
                com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
                this.processEngineService.getCurrentTaskByProcessInstanceId(PROCESS_INSTANCE_ID);
            });
            assertEquals("No active task found for process instance: " + PROCESS_INSTANCE_ID, exception.getMessage());
        }
    }

    @Test
    void testGetCurrentTaskByProcessInstanceId_Failure_null_pointer_exception() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            when(this.processEngine.getTaskService()).thenReturn(this.taskService);
            when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
            when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.initializeFormKeys()).thenReturn(null);
            com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
                com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
                this.processEngineService.getCurrentTaskByProcessInstanceId(PROCESS_INSTANCE_ID);
            });
            assertEquals("Failed to get current task for process instance: " + PROCESS_INSTANCE_ID, exception.getMessage());
        }
    }


    // get current task by task id

    @Test
    void testGetCurrentTaskByById_formKey_and_assigneeId_null() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        ProcessInstanceQuery mockProcessInstanceQuery = mock(ProcessInstanceQuery.class);
        ProcessInstance mockProcessInstance = mock(ProcessInstance.class);
        when(this.processEngine.getRuntimeService()).thenReturn(this.runtimeService);
        when(this.runtimeService.createProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processInstanceId(mockTask.getProcessInstanceId())).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.singleResult()).thenReturn(mockProcessInstance);
        when(mockProcessInstance.getBusinessKey()).thenReturn(PROCESS_BUSINESS_KEY);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            UUID currentUserId = UUID.randomUUID();
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            TaskDTO result = this.processEngineService.getCurrentTaskById(TASK_ID);
            assertEquals(result, taskDto);
        }
    }

    @Test
    void testGetCurrentTaskById_with_formKey() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        UUID formKey = UUID.randomUUID();
        when(mockTask.getFormKey()).thenReturn(formKey.toString());
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setFormKey(formKey.toString());
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.formServiceClient.getForm(formKey)).thenReturn(mockResponseEntity);
        FormDTO formDTO = new FormDTO();
        when(mockResponseEntity.getBody()).thenReturn(formDTO);
        ProcessInstanceQuery mockProcessInstanceQuery = mock(ProcessInstanceQuery.class);
        ProcessInstance mockProcessInstance = mock(ProcessInstance.class);
        when(this.processEngine.getRuntimeService()).thenReturn(this.runtimeService);
        when(this.runtimeService.createProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processInstanceId(mockTask.getProcessInstanceId())).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.singleResult()).thenReturn(mockProcessInstance);
        when(mockProcessInstance.getBusinessKey()).thenReturn(PROCESS_BUSINESS_KEY);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            UUID currentUserId = UUID.randomUUID();
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            TaskDTO result = this.processEngineService.getCurrentTaskById(TASK_ID);
            assertEquals(result, taskDto);
        }
    }

    @Test
    void testGetCurrentTaskById_with_assigneeId() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        when(mockTask.getAssignee()).thenReturn(currentUserId.toString());
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setAssignee(currentUserId.toString());
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.userService.getuser(currentUserId)).thenReturn(mockResponseEntity);
        UserDTO userDTO = new UserDTO();
        when(mockResponseEntity.getBody()).thenReturn(userDTO);
        ProcessInstanceQuery mockProcessInstanceQuery = mock(ProcessInstanceQuery.class);
        ProcessInstance mockProcessInstance = mock(ProcessInstance.class);
        when(this.processEngine.getRuntimeService()).thenReturn(this.runtimeService);
        when(this.runtimeService.createProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processInstanceId(mockTask.getProcessInstanceId())).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.singleResult()).thenReturn(mockProcessInstance);
        when(mockProcessInstance.getBusinessKey()).thenReturn(PROCESS_BUSINESS_KEY);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            TaskDTO result = this.processEngineService.getCurrentTaskById(TASK_ID);
            assertEquals(result, taskDto);
        }
    }

    @Test
    void testGetCurrentTaskById_with_assigneeId_not_equal_current_userId() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        when(mockTask.getAssignee()).thenReturn(UUID.randomUUID().toString());
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setAssignee(UUID.randomUUID().toString());
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.userService.getuser(currentUserId)).thenReturn(mockResponseEntity);
        UserDTO userDTO = new UserDTO();
        when(mockResponseEntity.getBody()).thenReturn(userDTO);
        ProcessInstanceQuery mockProcessInstanceQuery = mock(ProcessInstanceQuery.class);
        ProcessInstance mockProcessInstance = mock(ProcessInstance.class);
        when(this.processEngine.getRuntimeService()).thenReturn(this.runtimeService);
        when(this.runtimeService.createProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processInstanceId(mockTask.getProcessInstanceId())).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.singleResult()).thenReturn(mockProcessInstance);
        when(mockProcessInstance.getBusinessKey()).thenReturn(PROCESS_BUSINESS_KEY);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            TaskDTO result = this.processEngineService.getCurrentTaskById(TASK_ID);
            assertEquals(result, taskDto);
        }
    }


    @Test
    void testGetCurrentTaskById_Failure_empty_task_list() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            when(this.processEngine.getTaskService()).thenReturn(this.taskService);
            when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
            when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.list()).thenReturn(new ArrayList<>());
            com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
                com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
                this.processEngineService.getCurrentTaskById(TASK_ID);
            });
            assertEquals("No active task found for id: " + TASK_ID, exception.getMessage());
        }
    }

    @Test
    void testGetCurrentTaskById_Failure_null_task_list() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            when(this.processEngine.getTaskService()).thenReturn(this.taskService);
            when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
            when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.list()).thenReturn(null);
            com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
                com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
                this.processEngineService.getCurrentTaskById(TASK_ID);
            });
            assertEquals("No active task found for id: " + TASK_ID, exception.getMessage());
        }
    }

    @Test
    void testGetCurrentTaskById_Failure_null_pointer_exception() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            when(this.processEngine.getTaskService()).thenReturn(this.taskService);
            when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
            when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.initializeFormKeys()).thenReturn(null);
            com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
                com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
                this.processEngineService.getCurrentTaskById(TASK_ID);
            });
            assertEquals("Failed to get current task for id: " + TASK_ID, exception.getMessage());
        }
    }

    // get current task by user id

    @Test
    void testGetCurrentTaskByUserId_with_formKey() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        UUID formKey = UUID.randomUUID();
        when(mockTask.getFormKey()).thenReturn(formKey.toString());
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setFormKey(formKey.toString());
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.formServiceClient.getForm(formKey)).thenReturn(mockResponseEntity);
        FormDTO formDTO = new FormDTO();
        when(mockResponseEntity.getBody()).thenReturn(formDTO);
        TaskDTO result = this.processEngineService.getCurrentTaskByUserId(PROCESS_INSTANCE_ID, null);
        assertEquals(result, taskDto);
    }

    @Test
    void testGetCurrentTaskByUserId_with_assigneeId() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getId()).thenReturn(TASK_ID);
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setAssignee(currentUserId.toString());
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);
        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.userService.getuser(currentUserId)).thenReturn(mockResponseEntity);
        UserDTO userDTO = new UserDTO();
        when(mockResponseEntity.getBody()).thenReturn(userDTO);
        TaskDTO result = this.processEngineService.getCurrentTaskByUserId(PROCESS_INSTANCE_ID, currentUserId.toString());
        assertEquals(result, taskDto);
    }

    @Test
    void testGetCurrentTaskByUserId_Failure_empty_task_list() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(currentUserId.toString());
            when(this.processEngine.getTaskService()).thenReturn(this.taskService);
            when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
            when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
            when(mockTaskQuery.list()).thenReturn(new ArrayList<>());
            com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
                com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
                this.processEngineService.getCurrentTaskByUserId(PROCESS_INSTANCE_ID, currentUserId.toString());
            });
            assertEquals("No active task found for process instance: " + PROCESS_INSTANCE_ID, exception.getMessage());
        }

    }

    @Test
    void testGetCurrentTaskByUserId_Failure_null_task_list() {
        UUID currentUserId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);

        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(null);

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.getCurrentTaskByUserId(PROCESS_INSTANCE_ID, currentUserId.toString());
        });

        assertEquals("No active task found for process instance: " + PROCESS_INSTANCE_ID, exception.getMessage());
    }

    @Test
    void testGetCurrentTaskByUserId_Failure_null_pointer_exception() {
        UUID currentUserId = UUID.randomUUID();

        TaskQuery mockTaskQuery = mock(TaskQuery.class);

        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(null);

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.getCurrentTaskByUserId(PROCESS_INSTANCE_ID, currentUserId.toString());
        });

        assertEquals("Failed to get current task for process instance: " + PROCESS_INSTANCE_ID, exception.getMessage());
    }

    // get current task by assignee

    @Test
    void testGetCurrentTaskByAssignee_task_null() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);

        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(null)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.singleResult()).thenReturn(null);

        TaskDTO result = this.processEngineService.getCurrentTaskByAssignee(PROCESS_INSTANCE_ID, null);

        assertNull(result);
    }

    @Test
    void testGetCurrentTaskByAssignee_with_formKey() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);

        UUID formKey = UUID.randomUUID();
        when(mockTask.getId()).thenReturn(TASK_ID);
        when(mockTask.getFormKey()).thenReturn(formKey.toString());

        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setFormKey(formKey.toString());

        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(null)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);

        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.formServiceClient.getForm(formKey)).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(new FormDTO());
        TaskDTO result = this.processEngineService.getCurrentTaskByAssignee(PROCESS_INSTANCE_ID, null);
        assertEquals(result, taskDto);
    }

    @Test
    void testGetCurrentTaskByAssignee_with_assigneeId() {
        UUID assigneeId = UUID.randomUUID();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        UUID formKey = UUID.randomUUID();

        when(mockTask.getId()).thenReturn(TASK_ID);
        when(mockTask.getFormKey()).thenReturn(formKey.toString());
        when(mockTask.getAssignee()).thenReturn(assigneeId.toString());

        TaskDTO taskDto = new TaskDTO();
        taskDto.setId(TASK_ID);
        taskDto.setAssignee(assigneeId.toString());
        taskDto.setFormKey(formKey.toString());

        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(assigneeId.toString())).thenReturn(mockTaskQuery);
        when(mockTaskQuery.active()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDto);

        ResponseEntity mockUserResponseEntity = mock(ResponseEntity.class);
        when(this.userService.getuser(assigneeId)).thenReturn(mockUserResponseEntity);
        when(mockUserResponseEntity.getBody()).thenReturn(new UserDTO());

        ResponseEntity mockFormResponseEntity = mock(ResponseEntity.class);
        when(this.formServiceClient.getForm(formKey)).thenReturn(mockFormResponseEntity);
        when(mockFormResponseEntity.getBody()).thenReturn(new FormDTO());

        TaskDTO result = this.processEngineService.getCurrentTaskByAssignee(PROCESS_INSTANCE_ID, assigneeId.toString());

        assertEquals(result, taskDto);
    }

    @Test
    void testGetCurrentTaskByAssignee_Failure() {
        UUID assigneeId = UUID.randomUUID();
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.getCurrentTaskByAssignee(PROCESS_INSTANCE_ID, assigneeId.toString());
        });
        assertEquals("Failed to get current task for process instance: "
                + PROCESS_INSTANCE_ID
                + " and assignee: "
                + assigneeId,
            exception.getMessage()
        );
    }

    // get tasks by user id

    @Test
    void testGetTasksByUserId() {
        String assignee = UUID.randomUUID().toString();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(assignee)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.orderByTaskCreateTime()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.desc()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);

        HistoricTaskInstanceQuery mockHistoricTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);
        HistoricTaskInstance mockHistoricTaskInstance = mock(HistoricTaskInstance.class);
        when(mockHistoricTaskInstance.getProcessDefinitionId()).thenReturn(PROCESS_DEFINITION_ID);
        List<HistoricTaskInstance> mockHistoricTaskInstances = new ArrayList<>();
        mockHistoricTaskInstances.add(mockHistoricTaskInstance);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.taskAssignee(assignee)).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.finished()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.orderByHistoricTaskInstanceEndTime()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.asc()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.list()).thenReturn(mockHistoricTaskInstances);

        HistoricTaskInstanceDTO historicTaskInstanceDTO = new HistoricTaskInstanceDTO();
        when(this.historicTaskInstanceMapper.toDto(mockHistoricTaskInstance)).thenReturn(historicTaskInstanceDTO);

        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.singleResult()).thenReturn(mock(ProcessDefinition.class));

        List<AbstractTask> result = this.processEngineService.getTasksByUserId(assignee);
        assertEquals(result.size(), 2);
        assertNull(result.get(0));
        assertEquals(result.get(1), historicTaskInstanceDTO);
    }

    @Test
    void testGetTasksByUserId_processDefinitionId_null() {
        String assignee = UUID.randomUUID().toString();
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        List<Task> mockTaskList = new ArrayList<>();
        mockTaskList.add(mockTask);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(assignee)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.orderByTaskCreateTime()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.desc()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTaskList);

        HistoricTaskInstanceQuery mockHistoricTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);
        HistoricTaskInstance mockHistoricTaskInstance = mock(HistoricTaskInstance.class);
        when(mockHistoricTaskInstance.getProcessDefinitionId()).thenReturn(PROCESS_DEFINITION_ID);
        List<HistoricTaskInstance> mockHistoricTaskInstances = new ArrayList<>();
        mockHistoricTaskInstances.add(mockHistoricTaskInstance);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.taskAssignee(assignee)).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.finished()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.orderByHistoricTaskInstanceEndTime()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.asc()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.list()).thenReturn(mockHistoricTaskInstances);

        HistoricTaskInstanceDTO historicTaskInstanceDTO = new HistoricTaskInstanceDTO();
        when(this.historicTaskInstanceMapper.toDto(mockHistoricTaskInstance)).thenReturn(historicTaskInstanceDTO);

        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.processDefinitionId(PROCESS_DEFINITION_ID)).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.singleResult()).thenReturn(null);

        List<AbstractTask> result = this.processEngineService.getTasksByUserId(assignee);
        assertEquals(result.size(), 2);
        assertNull(result.get(0));
        assertEquals(result.get(1), historicTaskInstanceDTO);
    }


    @Test
    void testGetTasksByUserId_Failure() {
        UUID assigneeId = UUID.randomUUID();
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.getTasksByUserId(assigneeId.toString());
        });
        assertEquals(
            "Failed to get all tasks (current and historic) for user ID: " + assigneeId,
            exception.getMessage()
        );
    }

    // get tasks by instance id

    @Test
    void testGetTasksByInstanceId_formKey_null_suspended_true() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getProcessInstanceId()).thenReturn(PROCESS_INSTANCE_ID);
        when(mockTask.getFormKey()).thenReturn(null);
        when(mockTask.isSuspended()).thenReturn(true);

        List<Task> tasks = new ArrayList<>();
        tasks.add(mockTask);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(tasks);
        when(this.taskMapper.toDto(mockTask)).thenReturn(new TaskDTO());
        List<TaskDTO> result = this.processEngineService.getTasksByInstanceId(PROCESS_INSTANCE_ID);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setProcessInstanceId(PROCESS_INSTANCE_ID);
        taskDTO.setFormKey(null);
        taskDTO.setStatus(TaskStatus.COMPLETED);
        List<TaskDTO> tasksDTO = new ArrayList<>();
        tasksDTO.add(taskDTO);
        assertEquals(result.size(), tasksDTO.size());
        Assertions.assertEquals(result.get(0).getStatus(), tasksDTO.get(0).getStatus());
        assertEquals(result.get(0).getFormKey(), tasksDTO.get(0).getFormKey());

    }

    @Test
    void testGetTasksByInstanceId_with_formKey_suspended_false() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getProcessInstanceId()).thenReturn(PROCESS_INSTANCE_ID);
        when(mockTask.getFormKey()).thenReturn(PROCESS_INSTANCE_ID);
        when(mockTask.isSuspended()).thenReturn(false);

        List<Task> tasks = new ArrayList<>();
        tasks.add(mockTask);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(tasks);

        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.formServiceClient.getForm(UUID.fromString(PROCESS_INSTANCE_ID))).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(new FormDTO());

        when(this.taskMapper.toDto(mockTask)).thenReturn(new TaskDTO());

        List<TaskDTO> result = this.processEngineService.getTasksByInstanceId(PROCESS_INSTANCE_ID);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setProcessInstanceId(PROCESS_INSTANCE_ID);
        taskDTO.setFormKey(PROCESS_INSTANCE_ID);
        taskDTO.setStatus(TaskStatus.ACTIVE);
        List<TaskDTO> tasksDTO = new ArrayList<>();
        tasksDTO.add(taskDTO);
        assertEquals(result.size(), tasksDTO.size());
        Assertions.assertEquals(result.get(0).getStatus(), tasksDTO.get(0).getStatus());
    }

    @Test
    void testGetTasksByInstanceId_Failure() {
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.getTasksByInstanceId(PROCESS_INSTANCE_ID);
        });
        assertEquals(
            "Failed to get tasks for process instance ID: " + PROCESS_INSTANCE_ID,
            exception.getMessage()
        );
    }

    // get tasks by owner

    @Test
    void testGetTasksByOwner_formKey_null_suspended_true() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getProcessInstanceId()).thenReturn(PROCESS_INSTANCE_ID);
        when(mockTask.getFormKey()).thenReturn(null);
        when(mockTask.isSuspended()).thenReturn(true);

        List<Task> tasks = new ArrayList<>();
        tasks.add(mockTask);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskOwner(USER_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(tasks);
        when(this.taskMapper.toDto(mockTask)).thenReturn(new TaskDTO());
        List<TaskDTO> result = this.processEngineService.getTasksByOwner(USER_ID);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setOwner(USER_ID);
        taskDTO.setFormKey(null);
        taskDTO.setStatus(TaskStatus.COMPLETED);
        List<TaskDTO> tasksDTO = new ArrayList<>();
        tasksDTO.add(taskDTO);
        assertEquals(result.size(), tasksDTO.size());
        Assertions.assertEquals(result.get(0).getStatus(), tasksDTO.get(0).getStatus());
        assertEquals(result.get(0).getFormKey(), tasksDTO.get(0).getFormKey());

    }

    @Test
    void testGetTasksByOwner_with_formKey_suspended_false() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);
        when(mockTask.getProcessInstanceId()).thenReturn(PROCESS_INSTANCE_ID);
        when(mockTask.getFormKey()).thenReturn(PROCESS_INSTANCE_ID);
        when(mockTask.isSuspended()).thenReturn(false);

        List<Task> tasks = new ArrayList<>();
        tasks.add(mockTask);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskOwner(USER_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(tasks);

        ResponseEntity mockResponseEntity = mock(ResponseEntity.class);
        when(this.formServiceClient.getForm(UUID.fromString(PROCESS_INSTANCE_ID))).thenReturn(mockResponseEntity);
        when(mockResponseEntity.getBody()).thenReturn(new FormDTO());

        when(this.taskMapper.toDto(mockTask)).thenReturn(new TaskDTO());

        List<TaskDTO> result = this.processEngineService.getTasksByOwner(USER_ID);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setProcessInstanceId(PROCESS_INSTANCE_ID);
        taskDTO.setFormKey(PROCESS_INSTANCE_ID);
        taskDTO.setStatus(TaskStatus.ACTIVE);
        List<TaskDTO> tasksDTO = new ArrayList<>();
        tasksDTO.add(taskDTO);
        assertEquals(result.size(), tasksDTO.size());
        Assertions.assertEquals(result.get(0).getStatus(), tasksDTO.get(0).getStatus());
    }

    @Test
    void testGetTasksByOwner_Failure() {
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.getTasksByOwner(USER_ID);
        });
        assertEquals(
            "Failed to get tasks for owner: " + USER_ID,
            exception.getMessage()
        );
    }

    // validate and move to next task

    @Test
    void testValidateAndMoveToNextTask_null_formKey_assignee() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);

        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(mockTask.getProcessInstanceId()).thenReturn(TASK_ID);
        when(mockTask.getTaskDefinitionKey()).thenReturn(TASK_ID);
        when(this.processEngine.getFormService()).thenReturn(this.formService);
        when(this.formService.getTaskFormKey(TASK_ID, TASK_ID)).thenReturn(null);
        when(mockTask.getAssignee()).thenReturn("");

        Map<String, Object> variables = new HashMap<>();
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setAssignee(null);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDTO);

        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            UUID userId = UUID.randomUUID();
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(userId.toString());

            TaskDTO result = this.processEngineService.validateAndMoveToNextTask(TASK_ID, variables);

            assertNull(result.getFormKey());
            assertNull(result.getAssignee());
        }
    }

    @Test
    void testValidateAndMoveToNextTask() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        Task mockTask = mock(Task.class);

        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.singleResult()).thenReturn(mockTask);
        when(mockTask.getProcessDefinitionId()).thenReturn(PROCESS_DEFINITION_ID);
        when(mockTask.getTaskDefinitionKey()).thenReturn(TASK_ID);
        when(this.processEngine.getFormService()).thenReturn(this.formService);
        when(this.formService.getTaskFormKey(PROCESS_DEFINITION_ID, TASK_DEFINITION_KEY)).thenReturn(FORM_ID);
        when(mockTask.getAssignee()).thenReturn(USER_ID);
        when(mockTask.getFormKey()).thenReturn(FORM_ID);


        Map<String, Object> variables = new HashMap<>();
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setAssignee(USER_ID);
        taskDTO.setFormKey(FORM_ID);
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDTO);


        try (MockedStatic<SecurityUtils> mocked = mockStatic(SecurityUtils.class)) {
            UUID userId = UUID.randomUUID();
            mocked.when(SecurityUtils::getUserIdFromCurrentUser).thenReturn(userId.toString());

            TaskDTO result = this.processEngineService.validateAndMoveToNextTask(TASK_ID, variables);

            Assertions.assertEquals(result.getAssignee(), USER_ID);
            assertEquals(result.getFormKey(), FORM_ID);
            assertEquals(result, taskDTO);
        }
    }

    @Test
    void testValidateAndMoveToNextTask_Failure_null_task() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskId(TASK_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.singleResult()).thenReturn(null);

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.validateAndMoveToNextTask(TASK_ID, null);
        });
        assertEquals(
            "Failed to validate task and move to the next task.",
            exception.getMessage()
        );
    }

    @Test
    void testValidateAndMoveToNextTask_Failure() {
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            this.processEngineService.validateAndMoveToNextTask(TASK_ID, null);
        });
        assertEquals(
            "Failed to validate task and move to the next task.",
            exception.getMessage()
        );
    }

    // submit form to microservice

    @Test
    void testSubmitFormToMicroservice() throws Exception {
        // Arrange
        String formId = "form123";
        String taskId = "task456";
        Map<String, Object> variables = new HashMap<>();
        variables.put("key1", "value1");
        variables.put("key2", 123);

        // Act
        this.processEngineService.submitFormToMicroservice(formId, taskId, variables);

        // Assert
        verify(this.formServiceClient, times(1)).submitFormData(anyList());
    }

    @Test
    void testSubmitFormToMicroservice_JsonProcessingException() throws Exception {
        // Arrange
        String formId = "form123";
        String taskId = "task456";
        Map<String, Object> variables = new HashMap<>();
        variables.put("key1", new Object()); // This will cause a JsonProcessingException.

        // Act & Assert
        assertThrows(RuntimeException.class, () -> this.processEngineService.submitFormToMicroservice(formId, taskId, variables));

        // Verify no submission happened
        verify(this.formServiceClient, never()).submitFormData(anyList());
    }

    @Test
    void testSubmitFormToMicroservice_SubmissionException() throws Exception {
        // Arrange
        String formId = "form123";
        String taskId = "task456";
        Map<String, Object> variables = new HashMap<>();
        variables.put("key1", "value1");

        doThrow(new RuntimeException("Submission failed"))
            .when(this.formServiceClient).submitFormData(anyList());

        // Act & Assert
        assertThrows(com.pfe.web.rest.errors.ProcessEngineException.class, () -> this.processEngineService.submitFormToMicroservice(formId, taskId, variables));
    }

    /*               external tasks mgmt              */

    // fetch and lock tasks

    @Test
    void testFetchAndLockTasks() {

        //Arrange
        int maxTasks = 30;
        String topicName = "";
        ExternalTaskQueryBuilder mockExternalTaskQueryBuilder = mock(ExternalTaskQueryBuilder.class);
        ExternalTaskQueryTopicBuilder mockExternalTaskQueryTopicBuilder = mock(ExternalTaskQueryTopicBuilder.class);
        List<LockedExternalTask> mockLockedExternalTask = Collections.singletonList(mock(LockedExternalTask.class));
        when(this.processEngine.getExternalTaskService()).thenReturn(this.externalTaskService);
        when(this.externalTaskService.fetchAndLock(maxTasks, USER_ID)).thenReturn(mockExternalTaskQueryBuilder);
        when(mockExternalTaskQueryBuilder.topic(topicName, 1000L)).thenReturn(mockExternalTaskQueryTopicBuilder);
        when(mockExternalTaskQueryTopicBuilder.execute()).thenReturn(mockLockedExternalTask);

        //Act
        List<LockedExternalTask> result = this.processEngineService.fetchAndLockTasks(USER_ID, topicName, maxTasks);

        //Assert
        assertEquals(mockLockedExternalTask, result);
        verify(this.processEngine, times(1)).getExternalTaskService();
        verify(this.externalTaskService, times(1)).fetchAndLock(maxTasks, USER_ID);
    }

    @Test
    void testFetchAndLockTasks_Failure() {

        //Arrange
        int maxTasks = 30;
        String topicName = "";
        when(this.processEngine.getExternalTaskService()).thenReturn(this.externalTaskService);

        //Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> this.processEngineService.fetchAndLockTasks(USER_ID, topicName, maxTasks)
        );
        assertEquals(
            "Failed to fetch and lock tasks for worker: " +
                USER_ID +
                ", topic: " +
                topicName,
            exception.getMessage()
        );
    }

    // complete external task

    @Test
    void testCompleteExternalTask() {
        when(this.processEngine.getExternalTaskService()).thenReturn(this.externalTaskService);
        this.processEngineService.completeExternalTask(TASK_ID, USER_ID);
        verify(this.externalTaskService, times(1)).complete(TASK_ID, USER_ID);
    }

    @Test
    void testCompleteExternalTask_Failure() {
        when(this.processEngine.getExternalTaskService()).thenReturn(this.externalTaskService);
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("Submission failed"))
            .when(this.externalTaskService).complete(anyString(), anyString());
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () -> this.processEngineService.completeExternalTask(TASK_ID, USER_ID));

        assertEquals(
            "Failed to complete external task: " + TASK_ID + " for worker: " + USER_ID,
            exception.getMessage());
    }

    /*            user and group mgmt          */
    // create user

    @Test
    void testCreateUser() {
        User mockUser = mock(User.class);
        when(this.processEngine.getIdentityService()).thenReturn(this.identityService);
        when(this.identityService.newUser(USER_ID)).thenReturn(mockUser);
        mockUser.setFirstName("");
        mockUser.setLastName("");
        mockUser.setEmail("");

        this.processEngineService.createUser(USER_ID, "", "", "");

        verify(this.identityService, times(1)).saveUser(mockUser);
    }

    @Test
    void testCreateUser_Failure() {
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("Create user failed"))
            .when(this.identityService).saveUser(any());
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> this.processEngineService.createUser(USER_ID, "", "", ""));
        assertEquals("Failed to create user with ID: " + USER_ID, exception.getMessage());
    }

    // find user by id

    @Test
    void testFindUserById() {
        UserQuery mockUserQuery = mock(UserQuery.class);
        User mockUser = mock(User.class);
        when(this.processEngine.getIdentityService()).thenReturn(this.identityService);
        when(this.identityService.createUserQuery()).thenReturn(mockUserQuery);
        when(mockUserQuery.userId(USER_ID)).thenReturn(mockUserQuery);
        when(mockUserQuery.singleResult()).thenReturn(mockUser);

        this.processEngineService.findUserById(USER_ID);

        verify(this.identityService, times(1)).createUserQuery();
        verify(mockUserQuery, times(1)).singleResult();

    }

    @Test
    void testFindUserById_Failure() {
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("failed to find user"))
            .when(this.identityService).createUserQuery();
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> this.processEngineService.findUserById(USER_ID)
        );

        assertEquals(
            "Failed to find user by ID: " + USER_ID,
            exception.getMessage()
        );
    }

    // create group

    @Test
    void testCreateGroup() {
        Group mockGroup = mock(Group.class);
        when(this.processEngine.getIdentityService()).thenReturn(this.identityService);
        when(this.identityService.newGroup(GROUP_ID)).thenReturn(mockGroup);

        this.processEngineService.createGroup(GROUP_ID, "");

        verify(this.identityService, times(1)).newGroup(GROUP_ID);
        verify(this.identityService, times(1)).saveGroup(mockGroup);

    }

    @Test
    void testCreateGroup_Failure() {
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("failed to create group"))
            .when(this.identityService).newGroup(GROUP_ID);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> this.processEngineService.createGroup(GROUP_ID, "")
        );

        assertEquals(
            "Failed to create group with ID: " + GROUP_ID,
            exception.getMessage()
        );
    }

    // add user to group

    @Test
    void testAddUserToGroup() {
        when(this.processEngine.getIdentityService()).thenReturn(this.identityService);

        this.processEngineService.addUserToGroup(USER_ID, GROUP_ID);

        verify(this.identityService, times(1)).createMembership(USER_ID, GROUP_ID);
    }

    @Test
    void testAddUserToGroup_Failure() {
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("Failed to add user to group"))
            .when(this.identityService).createMembership(USER_ID, GROUP_ID);
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> this.processEngineService.addUserToGroup(USER_ID, GROUP_ID)
        );
        assertEquals("Failed to add user: " + USER_ID + " to group: " + GROUP_ID,
            exception.getMessage());
    }

    // Deploy process definition

    @Test
    void testDeployProcessDefinition() {
        DeploymentBuilder mockDeploymentBuilder = mock(DeploymentBuilder.class);
        Deployment mockDeployment = mock(Deployment.class);

        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createDeployment()).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.name(DEPLOYMENT_NAME)).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.addString(DEPLOYMENT_NAME + ".bpmn", "xml")).thenReturn(mockDeploymentBuilder);
        when(mockDeploymentBuilder.deploy()).thenReturn(mockDeployment);


        Deployment result = this.processEngineService.deployProcessDefinition(DEPLOYMENT_NAME, "xml");

        assertEquals(mockDeployment, result);
    }

    @Test
    void testDeployProcessDefinition_Failure() {
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createDeployment()).thenThrow(new RuntimeException("Deployment failed"));

        Exception exception = assertThrows(com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.deployProcessDefinition(DEPLOYMENT_NAME, "xml")
        );

        assertEquals("Failed to deploy process definition: " + DEPLOYMENT_NAME, exception.getMessage());
    }

    // delete deployment

    @Test
    void testDeleteDeployment() {
        // Arrange
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);

        // Act
        this.processEngineService.deleteDeployment(DEPLOYMENT_ID);

        // Assert
        verify(this.processEngine, times(1)).getRepositoryService();
        verify(this.repositoryService, times(1)).deleteDeployment(DEPLOYMENT_ID);
    }

    @Test
    void testDeleteDeployment_Failure() {
        // Arrange
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        doThrow(new RuntimeException("Delete failed")).when(this.repositoryService).deleteDeployment(DEPLOYMENT_ID);

        // Act & Assert
        Exception exception = assertThrows(com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.deleteDeployment(DEPLOYMENT_ID)
        );

        assertEquals("Failed to delete deployment with ID: " + DEPLOYMENT_ID, exception.getMessage());
        verify(this.processEngine, times(1)).getRepositoryService();
        verify(this.repositoryService, times(1)).deleteDeployment(DEPLOYMENT_ID);
    }

    // get process definition count

    @Test
    void testGetProcessDefinitionCount() {
        // Arrange
        long expectedCount = 42;
        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);


        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.count()).thenReturn(expectedCount);

        // Act
        long result = this.processEngineService.getProcessDefinitionCount();

        // Assert
        assertEquals(expectedCount, result);
        verify(this.processEngine, times(1)).getRepositoryService();
        verify(this.repositoryService, times(1)).createProcessDefinitionQuery();
        verify(mockProcessDefinitionQuery, times(1)).count();
    }

    @Test
    void testGetProcessDefinitionCount_Failure() {
        // Arrange
        when(this.processEngine.getRepositoryService()).thenReturn(this.repositoryService);
        when(this.repositoryService.createProcessDefinitionQuery()).thenThrow(new RuntimeException("Query failed"));

        // Act & Assert
        Exception exception = assertThrows(com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.getProcessDefinitionCount()
        );

        assertEquals("Failed to get process definition count", exception.getMessage());
    }

    /*            history mgmt        */

    // completed process instance
    @Test
    void testCompletedProcessInstances() {
        HistoricProcessInstanceQuery mockProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        List<HistoricProcessInstance> mockHistoricProcessInstances = Collections.singletonList(mock(HistoricProcessInstance.class));
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processDefinitionKey(PROCESS_KEY)).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.finished()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.list()).thenReturn(mockHistoricProcessInstances);

        List<HistoricProcessInstance> result = this.processEngineService.getCompletedProcessInstances(PROCESS_KEY);

        assertEquals(mockHistoricProcessInstances, result);
    }

    @Test
    void testCompletedProcessInstances_Failure() {
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("failed to complete process instance")).when(this.processEngine).getRepositoryService();

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> this.processEngineService.getCompletedProcessInstances(PROCESS_KEY));

        assertEquals(
            "Failed to get completed process instances for key: " + PROCESS_KEY,
            exception.getMessage()
        );
    }

    // get process instance history

    @Test
    void testGetProcessInstancesHistory() {
        HistoricProcessInstanceQuery mockProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricProcessInstance mockHistoricProcessInstances = mock(HistoricProcessInstance.class);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockProcessInstanceQuery);
        when(mockProcessInstanceQuery.singleResult()).thenReturn(mockHistoricProcessInstances);

        HistoricProcessInstance result = this.processEngineService.getProcessInstanceHistory(PROCESS_INSTANCE_ID);

        assertEquals(mockHistoricProcessInstances, result);
    }

    @Test
    void testGetProcessInstanceHistory_Failure() {
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("failed to get process instance history")).when(this.processEngine).getRepositoryService();

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> this.processEngineService.getProcessInstanceHistory(PROCESS_INSTANCE_ID));

        assertEquals(
            "Failed to get process instance history for ID: " + PROCESS_INSTANCE_ID,
            exception.getMessage()
        );
    }

    // get historic task instances by assignee
    @Test
    void testGetHistoricTaskInstancesByAssignee() {
        HistoricTaskInstanceQuery mockHistoricTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);
        List<HistoricTaskInstance> mockHistoricTaskInstances = Collections.singletonList(mock(HistoricTaskInstance.class));
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.taskAssignee(USER_ID)).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.orderByHistoricTaskInstanceEndTime()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.asc()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.list()).thenReturn(mockHistoricTaskInstances);

        List<HistoricTaskInstance> result = this.processEngineService.getHistoricTaskInstancesByAssignee(USER_ID);

        assertEquals(mockHistoricTaskInstances, result);

    }

    @Test
    void testGetHistoricTaskInstancesByAssignee_Failure() {
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("Failed to get historic task instances for assignee"))
            .when(this.processEngine).getRepositoryService();

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> this.processEngineService.getHistoricTaskInstancesByAssignee(USER_ID)
        );

        assertEquals(
            "Failed to get historic task instances for assignee: " + USER_ID,
            exception.getMessage()
        );
    }

    /*            authorization mgmt             */

    // create authorization

    @Test
    void testCreateAuthorization() {
        Authorization mockAuthorization = mock(Authorization.class);
        Resources mockResources = mock(Resources.class);
        Permission mockPermission = mock(Permission.class);
        when(this.processEngine.getAuthorizationService()).thenReturn(this.authorizationService);
        when(this.authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL)).thenReturn(mockAuthorization);
        mockAuthorization.setUserId(USER_ID);
        mockAuthorization.setResourceId("resourceId");
        mockAuthorization.setResource(mockResources);
        mockAuthorization.addPermission(mockPermission);

        this.processEngineService.createAuthorization(USER_ID, "resourceId", mockResources, mockPermission);

        ArgumentCaptor<Authorization> captor = ArgumentCaptor.forClass(Authorization.class);
        verify(this.authorizationService, times(1)).saveAuthorization(captor.capture());
        Authorization capturedAuthorization = captor.getValue();

        assertEquals(mockAuthorization, capturedAuthorization);
    }

    @Test
    void testCreateAuthorization_Failure() {
        Resources mockResources = mock(Resources.class);
        Permission mockPermission = mock(Permission.class);

        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("Failed to create authorization for user"))
            .when(this.processEngine).getRepositoryService();

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> this.processEngineService.createAuthorization(USER_ID, "resourceId", mockResources, mockPermission)
        );

        assertEquals(
            "Failed to create authorization for user: " + USER_ID,
            exception.getMessage()
        );
    }

    // get authorization
    @Test
    void testGetAuthorization() {
        AuthorizationQuery mockAuthorizationQuery = mock(AuthorizationQuery.class);
        Authorization mockAuthorization = mock(Authorization.class);

        when(this.processEngine.getAuthorizationService()).thenReturn(this.authorizationService);
        when(this.authorizationService.createAuthorizationQuery()).thenReturn(mockAuthorizationQuery);
        when(mockAuthorizationQuery.authorizationId(AUTHORIZATION_ID)).thenReturn(mockAuthorizationQuery);
        when(mockAuthorizationQuery.singleResult()).thenReturn(mockAuthorization);

        Authorization result = this.processEngineService.getAuthorization(AUTHORIZATION_ID);

        assertEquals(mockAuthorization, result);
    }

    // delete authorization

    @Test
    void testDeleteAuthorization() {
        when(this.processEngine.getAuthorizationService()).thenReturn(this.authorizationService);

        // Act
        this.processEngineService.deleteAuthorization(AUTHORIZATION_ID);

        // Assert
        verify(this.authorizationService, times(1)).deleteAuthorization(AUTHORIZATION_ID);
    }

    @Test
    void testDeleteAuthorization_Failure() {
        // Arrange
        doThrow(new RuntimeException("Delete failed"))
            .when(this.processEngine).getAuthorizationService();

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.deleteAuthorization(AUTHORIZATION_ID)
        );

        // Verify exception message
        assertEquals(
            "Failed to delete authorization with ID: " + AUTHORIZATION_ID,
            exception.getMessage()
        );
    }

    /*             job mgmt               */

    @Test
    void testGetAllJobs() {
        // Arrange
        Job mockJob1 = mock(Job.class);
        Job mockJob2 = mock(Job.class);
        JobQuery mockJobQuery = mock(JobQuery.class);
        when(this.processEngine.getManagementService()).thenReturn(this.managementService);
        when(this.managementService.createJobQuery()).thenReturn(mockJobQuery);
        List<Job> mockJobList = Arrays.asList(mockJob1, mockJob2);
        when(mockJobQuery.list()).thenReturn(mockJobList);

        // Act
        List<Job> result = this.processEngineService.getAllJobs();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(mockJob1, result.get(0));
        assertSame(mockJob2, result.get(1));
        verify(this.managementService, times(1)).createJobQuery();
        verify(mockJobQuery, times(1)).list();
    }

    @Test
    void testGetAllJobs_Failure() {
        // Arrange
        when(this.processEngine.getManagementService()).thenThrow(new RuntimeException("Job query failed"));

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.getAllJobs()
        );

        // Verify exception message
        assertEquals(
            "Failed to get all jobs",
            exception.getMessage()
        );
    }

    // execute job
    @Test
    void testExecuteJob() {
        // Arrange
        when(this.processEngine.getManagementService()).thenReturn(this.managementService);

        // Act
        this.processEngineService.executeJob(JOB_ID);

        // Assert
        verify(this.managementService, times(1)).executeJob(JOB_ID);
    }

    @Test
    void testExecuteJob_Failure() {
        // Arrange
        doThrow(new RuntimeException("Execution failed"))
            .when(this.processEngine).getManagementService();

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.executeJob(JOB_ID)
        );

        // Verify exception message
        assertEquals(
            "Failed to execute job with ID: " + JOB_ID,
            exception.getMessage()
        );
    }

    // delete job
    @Test
    void testDeleteJob() {
        // Arrange
        when(this.processEngine.getManagementService()).thenReturn(this.managementService);
        // Act
        this.processEngineService.deleteJob(JOB_ID);

        // Assert
        verify(this.managementService, times(1)).deleteJob(JOB_ID);
    }

    @Test
    void testDeleteJob_Failure() {
        // Arrange
        doThrow(new RuntimeException("Deletion failed"))
            .when(this.processEngine).getManagementService();

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.deleteJob(JOB_ID)
        );

        // Verify exception message
        assertEquals(
            "Failed to delete job with ID: " + JOB_ID,
            exception.getMessage()
        );
    }

    // get job count

    @Test
    void testGetJobCount() {
        // Arrange
        long mockJobCount = 42L;
        JobQuery mockJobQuery = mock(JobQuery.class);
        when(this.processEngine.getManagementService()).thenReturn(this.managementService);
        when(this.managementService.createJobQuery()).thenReturn(mockJobQuery);

        when(mockJobQuery.count()).thenReturn(mockJobCount);

        // Act
        long result = this.processEngineService.getJobCount();

        // Assert
        assertEquals(mockJobCount, result);
        verify(this.managementService, times(1)).createJobQuery();
        verify(mockJobQuery, times(1)).count();
    }

    @Test
    void testGetJobCount_Failure() {
        // Arrange
        when(this.processEngine.getManagementService()).thenThrow(new RuntimeException("Count query failed"));

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.getJobCount()
        );

        // Verify exception message
        assertEquals(
            "Failed to get job count",
            exception.getMessage()
        );
    }

    /*                    case mgmt                   */

    // create case instance
    @Test
    void testCreateCaseInstance() {
        // Arrange
        CaseInstance mockCaseInstance = mock(CaseInstance.class);
        when(this.processEngine.getCaseService()).thenReturn(this.caseService);
        when(this.caseService.createCaseInstanceByKey(CASE_DEFINITION_KEY)).thenReturn(mockCaseInstance);

        // Act
        CaseInstance result = this.processEngineService.createCaseInstance(CASE_DEFINITION_KEY);

        // Assert
        assertSame(mockCaseInstance, result);
        verify(this.caseService, times(1)).createCaseInstanceByKey(CASE_DEFINITION_KEY);
    }

    @Test
    void testCreateCaseInstance_Failure() {
        // Arrange
        when(this.processEngine.getCaseService())
            .thenThrow(new RuntimeException("Case creation failed"));

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.createCaseInstance(CASE_DEFINITION_KEY)
        );

        // Verify exception message
        assertEquals(
            "Failed to create case instance with definition key: " + CASE_DEFINITION_KEY,
            exception.getMessage()
        );
    }

    // manually start case instance
    @Test
    void testManuallyStartCaseExecution() {
        // Arrange
        when(this.processEngine.getCaseService()).thenReturn(this.caseService);

        // Act
        this.processEngineService.manuallyStartCaseExecution(CASE_DEFINITION_KEY);

        // Assert
        verify(this.caseService, times(1)).manuallyStartCaseExecution(CASE_DEFINITION_KEY);
    }

    @Test
    void testManuallyStartCaseExecution_Failure() {
        // Arrange
        when(this.processEngine.getCaseService())
            .thenThrow(new RuntimeException("Case start failed"));

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.manuallyStartCaseExecution(CASE_DEFINITION_KEY)
        );

        // Verify exception message
        assertEquals(
            "Failed to manually start case execution with ID: " + CASE_DEFINITION_KEY,
            exception.getMessage()
        );
    }

    // complete case execution
    @Test
    void testCompleteCaseExecution() {
        // Arrange
        when(this.processEngine.getCaseService()).thenReturn(this.caseService);

        // Act
        this.processEngineService.completeCaseExecution(CASE_EXECUTION_ID);

        // Assert
        verify(this.caseService, times(1)).completeCaseExecution(CASE_EXECUTION_ID);
    }

    @Test
    void testCompleteCaseExecution_Failure() {
        // Arrange
        when(this.processEngine.getCaseService())
            .thenThrow(new RuntimeException("Case execution failed"));

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.completeCaseExecution(CASE_EXECUTION_ID)
        );

        // Verify exception message
        assertEquals(
            "Failed to complete case execution with ID: " + CASE_EXECUTION_ID,
            exception.getMessage()
        );
    }

    /*              form mgmt            */

    // get task from form data
    @Test
    void testGetTaskFormData() {
        // Arrange
        when(this.processEngine.getFormService()).thenReturn(this.formService);

        // Act
        this.processEngineService.getTaskFormData(TASK_ID);

        // Assert
        verify(this.formService, times(1)).getTaskFormData(TASK_ID);
    }

    @Test
    void testGetTaskFormData_Failure() {
        // Arrange
        when(this.processEngine.getFormService())
            .thenThrow(new RuntimeException("get form failed"));

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.getTaskFormData(TASK_ID)
        );

        // Verify exception message
        assertEquals(
            "Failed to get task form data for task ID: " + TASK_ID,
            exception.getMessage()
        );
    }

    // submit task from
    @Test
    void testSubmitTaskForm() {
        // Arrange
        when(this.processEngine.getFormService()).thenReturn(this.formService);

        // Act
        this.processEngineService.submitTaskForm(TASK_ID, null);

        // Assert
        verify(this.formService, times(1)).submitTaskForm(TASK_ID, null);
    }

    @Test
    void testSubmitTaskForm_Failure() {
        // Arrange
        when(this.processEngine.getFormService())
            .thenThrow(new RuntimeException("submit task form failed"));

        // Act & Assert
        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class, () ->
            this.processEngineService.submitTaskForm(TASK_ID, null)
        );

        // Verify exception message
        assertEquals(
            "Failed to submit task form for task ID: " + TASK_ID,
            exception.getMessage()
        );
    }

    // get task by group id

    @Test
    void testGetTasksByGroupId() throws Exception {
        // Arrange
        UUID groupId1 = UUID.randomUUID();
        UUID groupId2 = UUID.randomUUID();
        List<UUID> groupIds = Arrays.asList(groupId1, groupId2);

        when(this.userService.getGroupIdsByUserId(UUID.fromString(USER_ID))).thenReturn(ResponseEntity.ok(groupIds));
        Task mockTask = mock(Task.class);
        List<Task> mockTasks = Collections.singletonList(mockTask);

        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(anyString())).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTasks);


        TaskDTO taskDTO = new TaskDTO();
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDTO);

        //reflection
        Method privateMethod = ProcessEngineServiceImpl.class.getDeclaredMethod("getTasksByGroupId", String.class);
        privateMethod.setAccessible(true);

        // Act
        @SuppressWarnings("unchecked")
        List<TaskDTO> result = (List<TaskDTO>) privateMethod.invoke(this.processEngineService, USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetTasksByGroupId_null_groupId() throws Exception {
        // Arrange

        when(this.userService.getGroupIdsByUserId(UUID.fromString(USER_ID))).thenReturn(ResponseEntity.ok(null));
        Task mockTask = mock(Task.class);
        List<Task> mockTasks = Collections.emptyList();

        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(anyString())).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTasks);


        TaskDTO taskDTO = new TaskDTO();
        when(this.taskMapper.toDto(mockTask)).thenReturn(taskDTO);

        //reflection
        Method privateMethod = ProcessEngineServiceImpl.class.getDeclaredMethod("getTasksByGroupId", String.class);
        privateMethod.setAccessible(true);

        // Act
        @SuppressWarnings("unchecked")
        List<TaskDTO> result = (List<TaskDTO>) privateMethod.invoke(this.processEngineService, USER_ID);


        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetTasksByGroupId_Failure() throws Exception {
        // Arrange
        when(this.userService.getGroupIdsByUserId(UUID.fromString(USER_ID)))
            .thenThrow(new RuntimeException("Failed to fetch group IDs"));

        //reflection
        Method privateMethod = ProcessEngineServiceImpl.class.getDeclaredMethod("getTasksByGroupId", String.class);
        privateMethod.setAccessible(true);

        // Act & Assert
        InvocationTargetException exception = assertThrowsExactly(InvocationTargetException.class, () ->
            privateMethod.invoke(this.processEngineService, USER_ID)
        );

        // Verify exception message and cause
        Throwable cause = exception.getCause();
        assertEquals(com.pfe.web.rest.errors.ProcessEngineException.class, cause.getClass());
        assertEquals("Failed to get all current tasks for user ID: " + USER_ID,
            cause.getMessage());
        verify(this.userService, times(1)).getGroupIdsByUserId(UUID.fromString(USER_ID));
        verifyNoInteractions(this.taskService);
        verifyNoInteractions(this.taskMapper);
    }

    // get tasks assigned username by user id


    @Test
    void testGetTasksAssignedUsernameByUserId() throws Exception {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setId(UUID.fromString(USER_ID));
        userDTO.setUsername("userName");
        when(this.userService.getuser(UUID.fromString(USER_ID))).thenReturn(ResponseEntity.ok(userDTO));
        Task mockTask = mock(Task.class);
        List<Task> mockTasks = Collections.singletonList(mockTask);

        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(userDTO.getUsername())).thenReturn(mockTaskQuery);
        when(mockTaskQuery.initializeFormKeys()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.list()).thenReturn(mockTasks);


        TaskDTO taskDTO = new TaskDTO();
        when(taskMapper.toDto(mockTask)).thenReturn(taskDTO);

        //reflection
        Method privateMethod = ProcessEngineServiceImpl.class.getDeclaredMethod("getTasksAssignedUsernameByUserId", String.class);
        privateMethod.setAccessible(true);

        // Act
        @SuppressWarnings("unchecked")
        List<TaskDTO> result = (List<TaskDTO>) privateMethod.invoke(processEngineService, USER_ID);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetTasksAssignedUsernameByUserId_null_user_Failure() throws Exception {
        when(userService.getuser(UUID.fromString(USER_ID))).thenReturn(ResponseEntity.ok(null));

        //reflection
        Method privateMethod = ProcessEngineServiceImpl.class.getDeclaredMethod("getTasksAssignedUsernameByUserId", String.class);
        privateMethod.setAccessible(true);

        // Act & Assert
        InvocationTargetException exception = assertThrowsExactly(InvocationTargetException.class, () ->
            privateMethod.invoke(processEngineService, USER_ID)
        );

        // Verify exception message and cause
        Throwable cause = exception.getCause();
        assertEquals(com.pfe.web.rest.errors.ProcessEngineException.class, cause.getClass());
        assertEquals("Failed to get all current tasks for user id: " + USER_ID,
            cause.getMessage());

        Throwable causeOfCause = cause.getCause();
        assertEquals(com.pfe.web.rest.errors.ProcessEngineException.class, causeOfCause.getClass());
        assertEquals("Failed to get user with ID: " + USER_ID,
            causeOfCause.getMessage());

        verify(userService, times(1)).getuser(UUID.fromString(USER_ID));
        verifyNoInteractions(taskService);
        verifyNoInteractions(taskMapper);
    }

    // get historic tasks by user id

    @Test
    void testGetHistoricTasksByUserId() {
        Pageable pageable = PageRequest.of(0, 10);
        HistoricTaskInstanceQuery mockHistoricTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);

        List<HistoricTaskInstance> historicTasks = new ArrayList<>();
        HistoricTaskInstance task1 = mock(HistoricTaskInstance.class);
        when(task1.getProcessInstanceId()).thenReturn(PROCESS_INSTANCE_ID);
        when(task1.getProcessDefinitionId()).thenReturn(PROCESS_DEFINITION_ID);
        historicTasks.add(task1);

        when(processEngine.getHistoryService()).thenReturn(historyService);
        when(historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.taskAssignee(USER_ID)).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.finished()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.orderByHistoricTaskInstanceEndTime()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.desc()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.list()).thenReturn(historicTasks);

        HistoricTaskInstanceDTO taskDTO = new HistoricTaskInstanceDTO();
        when(historicTaskInstanceMapper.toDto(task1)).thenReturn(taskDTO);

        HistoricProcessInstanceQuery mockHistoricProcessInstanceQuery = mock(HistoricProcessInstanceQuery.class);
        HistoricTaskInstance mockHistoricTaskInstance = mock(HistoricTaskInstance.class);
        HistoricProcessInstance mockHistoricProcessInstance = mock(HistoricProcessInstance.class);
        when(mockHistoricTaskInstance.getProcessDefinitionId()).thenReturn(UUID.randomUUID().toString());
        when(processEngine.getHistoryService()).thenReturn(historyService);
        when(historyService.createHistoricProcessInstanceQuery()).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.processInstanceId(anyString())).thenReturn(mockHistoricProcessInstanceQuery);
        when(mockHistoricProcessInstanceQuery.singleResult()).thenReturn(mockHistoricProcessInstance);
        when(mockHistoricProcessInstance.getBusinessKey()).thenReturn(PROCESS_BUSINESS_KEY);


        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        ProcessDefinition mockProcessDefinition = mock(ProcessDefinition.class);
        when(processEngine.getRepositoryService()).thenReturn(repositoryService);
        when(repositoryService.createProcessDefinitionQuery()).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.processDefinitionId(anyString())).thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.singleResult()).thenReturn(mockProcessDefinition);

        Page<AbstractTask> result = processEngineService.getHistoricTasksByUserId(USER_ID, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        Assertions.assertEquals(PROCESS_BUSINESS_KEY, ((HistoricTaskInstanceDTO) result.getContent().get(0)).getBusinessKey());
        verify(historyService).createHistoricTaskInstanceQuery();
        verify(repositoryService).createProcessDefinitionQuery();
        verify(historicTaskInstanceMapper).toDto(task1);
    }

    @Test
    void testGetActiveTasksByUserId() {
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("failed to get active tasks"))
            .when(processEngine).getHistoryService();

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> processEngineService.getHistoricTasksByUserId(USER_ID, null)
        );

        assertEquals(
            "Failed to get all historic tasks for user ID: " + USER_ID,
            exception.getMessage()
        );
    }

    // get active tasks by user id

    @Test
    void testGetActiveTasksByCurrentUserId_Failure() {
        doThrow(new com.pfe.web.rest.errors.ProcessEngineException("failed to get active tasks"))
            .when(processEngine).getTaskService();

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            com.pfe.web.rest.errors.ProcessEngineException.class,
            () -> processEngineService.getActiveTasksByUserId(null)
        );

        assertEquals(
            "Failed to get all active tasks for user id ",
            exception.getMessage()
        );
    }

    //get variables from running instances
    @Test
    void testGetVariablesFromRunningInstance() {
        Map<String, Object> mockVariables = Map.of(
            "variable1", "value1",
            "variable2", 42
        );

        when(processEngine.getRuntimeService()).thenReturn(runtimeService);
        when(runtimeService.getVariables(PROCESS_INSTANCE_ID)).thenReturn(mockVariables);

        ObjectNode node1 = mock(ObjectNode.class);
        ObjectNode node2 = mock(ObjectNode.class);

        when(jsonProcessingService.createJsonNode("variable1", "String", "value1")).thenReturn(node1);
        when(jsonProcessingService.createJsonNode("variable2", "Integer", 42)).thenReturn(node2);

        List<ObjectNode> result = processEngineService.getVariablesFromRunningInstance(PROCESS_INSTANCE_ID);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(node1));
        assertTrue(result.contains(node2));

        verify(runtimeService).getVariables(PROCESS_INSTANCE_ID);
        verify(jsonProcessingService).createJsonNode("variable1", "String", "value1");
        verify(jsonProcessingService).createJsonNode("variable2", "Integer", 42);
    }

    @Test
    void testGetVariablesFromRunningInstance_NoVariables() {
        when(processEngine.getRuntimeService()).thenReturn(runtimeService);
        when(runtimeService.getVariables(PROCESS_INSTANCE_ID)).thenReturn(Collections.emptyMap());

        List<ObjectNode> result = processEngineService.getVariablesFromRunningInstance(PROCESS_INSTANCE_ID);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(runtimeService).getVariables(PROCESS_INSTANCE_ID);
    }

    @Test
    void testGetVariablesFromRunningInstance_Exception() {
        when(processEngine.getRuntimeService()).thenReturn(runtimeService);
        when(runtimeService.getVariables(PROCESS_INSTANCE_ID)).thenThrow(new RuntimeException("Test Exception"));

        List<ObjectNode> result = processEngineService.getVariablesFromRunningInstance(PROCESS_INSTANCE_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(runtimeService).getVariables(PROCESS_INSTANCE_ID);
    }

    //.
    @Test
    void testGetHistoricTaskInstancesByProcessInstanceId_noHistoricTasks() {
        //given
        String processInstanceId = "processInstanceId";

        //mock required objects
        org.camunda.bpm.engine.HistoryService mockHistoryService = mock(HistoryService.class);
        HistoricTaskInstanceQuery mockHistoricTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);

        //define the behavior of processEngine
        when(processEngine.getHistoryService()).thenReturn(mockHistoryService);
        when(mockHistoryService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.processInstanceId(processInstanceId)).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.finished()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.orderByHistoricTaskInstanceEndTime()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.asc()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.list()).thenReturn(Collections.emptyList());

        //when
        List<HistoricTaskInstanceDTO> result = processEngineService.getHistoricTaskInstancesByProcessInstanceId(processInstanceId);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetHistoricTaskInstancesByProcessInstanceId_usersNotFound() {
        //given
        String processInstanceId = "processInstanceId";
        List<HistoricTaskInstance> historicTasks = mockHistoricTasks();

        //mock required objects
        HistoryService mockHistoryService = mock(HistoryService.class);
        HistoricTaskInstanceQuery mockHistoricTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);

        //mock processEngine and HistoryService
        when(processEngine.getHistoryService()).thenReturn(mockHistoryService);
        when(mockHistoryService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.processInstanceId(processInstanceId)).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.finished()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.orderByHistoricTaskInstanceEndTime()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.asc()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.list()).thenReturn(historicTasks);

        //mock userService behavior
        when(userService.getUsersByIds(anyList())).thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK));

        //when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            processEngineService.getHistoricTaskInstancesByProcessInstanceId(processInstanceId);
        });

        assertEquals("Assigned tasks found but no users returned for assignees!", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    void testGetHistoricTaskInstancesByProcessInstanceId_usersFound() {
        //given
        String processInstanceId = "processInstanceId";
        List<HistoricTaskInstance> historicTasks = mockHistoricTasks();
        UserDTO userDTO1 = new UserDTO();
        userDTO1.setId(UUID.fromString("172d001a-621c-48c9-b252-0a8cd45404dc"));
        userDTO1.setUsername("int-111");
        UserDTO userDTO2 = new UserDTO();
        userDTO1.setId(UUID.fromString("24e4aa50-6a75-4cde-8bd3-8ae53da204ca"));
        userDTO1.setUsername("int-222");
        List<UserDTO> userDTOs = List.of(userDTO1, userDTO2);

        //mock ProcessEngine and related services
        HistoryService mockHistoryService = mock(HistoryService.class);
        HistoricTaskInstanceQuery mockHistoricTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);

        when(processEngine.getHistoryService()).thenReturn(mockHistoryService);
        when(mockHistoryService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.processInstanceId(processInstanceId)).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.finished()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.orderByHistoricTaskInstanceEndTime()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.asc()).thenReturn(mockHistoricTaskInstanceQuery);
        when(mockHistoricTaskInstanceQuery.list()).thenReturn(historicTasks);

        //mock userService response
        when(userService.getUsersByIds(anyList())).thenReturn(new ResponseEntity<>(userDTOs, HttpStatus.OK));

        //mock mapper response
        when(historicTaskInstanceMapper.toDto(any(HistoricTaskInstance.class)))
            .thenAnswer(invocation -> {
                HistoricTaskInstance task = invocation.getArgument(0);
                HistoricTaskInstanceDTO dto = new HistoricTaskInstanceDTO();
                dto.setId(task.getId());
                return dto;
            });

        //mock FormService and RepositoryService
        org.camunda.bpm.engine.FormService mockFormService = mock(FormService.class);
        RepositoryService mockRepositoryService = mock(RepositoryService.class);
        ProcessDefinitionQuery mockProcessDefinitionQuery = mock(ProcessDefinitionQuery.class);
        ProcessDefinition mockProcessDefinition = mock(ProcessDefinition.class);

        when(processEngine.getFormService()).thenReturn(mockFormService);
        when(mockFormService.getTaskFormKey(anyString(), anyString()))
            .thenReturn("f47ac10b-58cc-4372-a567-0e02b2c3d479");

        when(processEngine.getRepositoryService()).thenReturn(mockRepositoryService);
        when(mockRepositoryService.createProcessDefinitionQuery())
            .thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.processDefinitionId(anyString()))
            .thenReturn(mockProcessDefinitionQuery);
        when(mockProcessDefinitionQuery.singleResult())
            .thenReturn(mockProcessDefinition);
        when(mockProcessDefinition.getVersion()).thenReturn(1);
        when(mockProcessDefinition.getName()).thenReturn("Test Process");

        //mock FormServiceClient
        FormDTO mockFormDTO = new FormDTO();
        mockFormDTO.setId(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479"));
        when(formServiceClient.getForm(any(UUID.class))).thenReturn(new ResponseEntity<>(mockFormDTO, HttpStatus.OK));

        List<SubmissionDTO> mockSubmissions = List.of(new SubmissionDTO());
        when(formServiceClient.getAllSubmissionsByTaskInstanceId(any(UUID.class)))
            .thenReturn(new ResponseEntity<>(mockSubmissions, HttpStatus.OK));

        //when
        List<HistoricTaskInstanceDTO> result = processEngineService.getHistoricTaskInstancesByProcessInstanceId(processInstanceId);

        //then
        assertEquals(2, result.size());
        HistoricTaskInstanceDTO firstTask = result.get(0);
        Assertions.assertEquals(TaskStatus.COMPLETED, firstTask.getStatus());
        assertEquals(1, firstTask.getProcessDefinitionVersion());
        assertEquals("Test Process", firstTask.getProcessDefinitionName());
    }

    @Test
    void testGetHistoricTaskInstancesByProcessInstanceId_exceptionHandling() {
        //given
        String processInstanceId = "processInstanceId";

        //mock required objects
        HistoryService mockHistoryService = mock(HistoryService.class);
        HistoricTaskInstanceQuery mockHistoricTaskInstanceQuery = mock(HistoricTaskInstanceQuery.class);

        //mock processEngine and HistoryService
        when(processEngine.getHistoryService()).thenReturn(mockHistoryService);
        when(mockHistoryService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricTaskInstanceQuery);

        when(mockHistoricTaskInstanceQuery.processInstanceId(processInstanceId))
            .thenThrow(new RuntimeException("Some error"));

        //when & then
        assertThrows(com.pfe.web.rest.errors.ProcessEngineException.class, () -> {
            processEngineService.getHistoricTaskInstancesByProcessInstanceId(processInstanceId);
        });
    }

    private List<HistoricTaskInstance> mockHistoricTasks() {
        HistoricTaskInstance task1 = mock(HistoricTaskInstance.class);
        when(task1.getAssignee()).thenReturn("172d001a-621c-48c9-b252-0a8cd45404dc");
        when(task1.getId()).thenReturn("99240917-f561-47c6-af53-5a3b4dec908e");
        when(task1.getProcessDefinitionId()).thenReturn("processDefinitionId1");

        HistoricTaskInstance task2 = mock(HistoricTaskInstance.class);
        when(task2.getAssignee()).thenReturn("24e4aa50-6a75-4cde-8bd3-8ae53da204ca");
        when(task2.getId()).thenReturn("a7eca4d8-61fa-41ee-a55a-9d690bdc3e4c");
        when(task2.getProcessDefinitionId()).thenReturn("processDefinitionId2");

        return List.of(task1, task2);
    }

    @Test
    void testGetInstanceVariablesByProcessInstanceId_ValidTask() throws Exception {
        // Arrange
        String processInstanceId = "abc123";
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(processEngine.getTaskService()).thenReturn(taskService);
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(task.getProcessInstanceId()).thenReturn(processInstanceId);

        // Mock private method
        List<ObjectNode> expectedVariables = List.of(mock(ObjectNode.class));
        ProcessEngineServiceImpl spyService = spy(processEngineService);
        doReturn(expectedVariables).when(spyService).getVariablesFromRunningInstance(processInstanceId);

        // Act
        List<ObjectNode> result = spyService.getProcessVariablesByProcessInstanceId(
            processInstanceId);

        // Assert
        assertEquals(expectedVariables, result);
        verify(spyService, times(1)).getVariablesFromRunningInstance(processInstanceId);
    }

    @Test
    void testGetProcessVariablesByProcessInstanceId_InvalidProcessInstance() throws Exception {
        // Arrange
        String processInstanceId = "invalid_process_instance_id";
        ProcessEngineServiceImpl spyService = spy(processEngineService);

        doThrow(new RuntimeException("No process instance found"))
            .when(spyService)
            .getVariablesFromRunningInstance(processInstanceId);

        // Act
        List<ObjectNode> result = spyService.getProcessVariablesByProcessInstanceId(
            processInstanceId);

        // Assert
        assertEquals(Collections.emptyList(), result);
        verify(spyService).getVariablesFromRunningInstance(
            processInstanceId);
    }

    @Test
    void testGetProcessByCategory() {
        ProcessDefinition processDefinition = mock(ProcessDefinition.class);
        when(processDefinition.getId()).thenReturn("process1");
        when(processDefinition.getKey()).thenReturn("processKey1");
        when(processDefinition.getName()).thenReturn("Process 1");
        when(processDefinition.getVersion()).thenReturn(1);
        when(processDefinition.getDeploymentId()).thenReturn("deployment1");
        when(processDefinition.getResourceName()).thenReturn("resource.bpmn");
        when(processDefinition.isSuspended()).thenReturn(false);
        when(processDefinition.isStartableInTasklist()).thenReturn(true);

        when(processEngine.getRepositoryService()).thenReturn(repositoryService);
        when(repositoryService.createProcessDefinitionQuery()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.active()).thenReturn(processDefinitionQuery);
        when(processDefinitionQuery.list()).thenReturn(List.of(processDefinition));

        BpmnModelInstance modelInstance = mock(BpmnModelInstance.class);
        when(repositoryService.getBpmnModelInstance("process1")).thenReturn(modelInstance);

        Process process = mock(Process.class);
        when(process.getAttributeValueNs("http://camunda.org/schema/1.0/bpmn", "categoryProcess"))
            .thenReturn("creationEntreprise");
        when(modelInstance.getModelElementsByType(Process.class)).thenReturn(Collections.singleton(process));

        List<ProcessDefinitionDTO> result = processEngineService.getProcessByCategory("creationEntreprise");

        assertEquals(1, result.size());
        ProcessDefinitionDTO dto = result.get(0);
        assertEquals("process1", dto.getId());
        assertEquals("processKey1", dto.getKey());
        assertEquals("Process 1", dto.getName());
        assertEquals(1, dto.getVersion());
        assertEquals("deployment1", dto.getDeploymentId());
        assertEquals("resource.bpmn", dto.getResourceName());
        assertTrue(dto.isSuspensionState());
        assertTrue(dto.getStartableInTasklist());
    }

    @Test
    void testIsUserAssignedToProcessInstance_ActiveTasks() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(processEngine.getTaskService()).thenReturn(taskService);
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(USER_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.count()).thenReturn(1L);

        HistoricTaskInstanceQuery mockHistoricQuery = mock(HistoricTaskInstanceQuery.class);
        when(processEngine.getHistoryService()).thenReturn(historyService);
        when(historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricQuery);
        when(mockHistoricQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(
            mockHistoricQuery);
        when(mockHistoricQuery.taskAssignee(USER_ID)).thenReturn(mockHistoricQuery);
        when(mockHistoricQuery.count()).thenReturn(0L);

        boolean result = processEngineService.isUserAssignedToProcessInstance(USER_ID,
            PROCESS_INSTANCE_ID);

        assertTrue(result, "User should be assigned when active tasks are present");
        verify(mockTaskQuery).count();
        verify(mockHistoricQuery).count();
    }

    @Test
    void testIsUserAssignedToProcessInstance_HistoricTasks() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(processEngine.getTaskService()).thenReturn(taskService);
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(USER_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.count()).thenReturn(0L);

        HistoricTaskInstanceQuery mockHistoricQuery = mock(HistoricTaskInstanceQuery.class);
        when(processEngine.getHistoryService()).thenReturn(historyService);
        when(historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricQuery);
        when(mockHistoricQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(
            mockHistoricQuery);
        when(mockHistoricQuery.taskAssignee(USER_ID)).thenReturn(mockHistoricQuery);
        when(mockHistoricQuery.count()).thenReturn(1L);

        boolean result = processEngineService.isUserAssignedToProcessInstance(USER_ID,
            PROCESS_INSTANCE_ID);

        assertTrue(result, "User should be assigned when historic tasks are present");
        verify(mockTaskQuery).count();
        verify(mockHistoricQuery).count();
    }

    @Test
    void testIsUserAssignedToProcessInstance_BothActiveAndHistoricTasks() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(processEngine.getTaskService()).thenReturn(taskService);
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(USER_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.count()).thenReturn(1L);

        HistoricTaskInstanceQuery mockHistoricQuery = mock(HistoricTaskInstanceQuery.class);
        when(processEngine.getHistoryService()).thenReturn(historyService);
        when(historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricQuery);
        when(mockHistoricQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(
            mockHistoricQuery);
        when(mockHistoricQuery.taskAssignee(USER_ID)).thenReturn(mockHistoricQuery);
        when(mockHistoricQuery.count()).thenReturn(1L);

        boolean result = processEngineService.isUserAssignedToProcessInstance(USER_ID,
            PROCESS_INSTANCE_ID);

        assertTrue(result,
            "User should be assigned when both active and historic tasks are present");
        verify(mockTaskQuery).count();
    }

    @Test
    void testIsUserAssignedToProcessInstance_NotAssigned() {
        TaskQuery mockTaskQuery = mock(TaskQuery.class);
        when(processEngine.getTaskService()).thenReturn(taskService);
        when(taskService.createTaskQuery()).thenReturn(mockTaskQuery);
        when(mockTaskQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.taskAssignee(USER_ID)).thenReturn(mockTaskQuery);
        when(mockTaskQuery.count()).thenReturn(0L);

        HistoricTaskInstanceQuery mockHistoricQuery = mock(HistoricTaskInstanceQuery.class);
        when(processEngine.getHistoryService()).thenReturn(historyService);
        when(historyService.createHistoricTaskInstanceQuery()).thenReturn(mockHistoricQuery);
        when(mockHistoricQuery.processInstanceId(PROCESS_INSTANCE_ID)).thenReturn(
            mockHistoricQuery);
        when(mockHistoricQuery.taskAssignee(USER_ID)).thenReturn(mockHistoricQuery);
        when(mockHistoricQuery.count()).thenReturn(0L);

        boolean result = processEngineService.isUserAssignedToProcessInstance(USER_ID,
            PROCESS_INSTANCE_ID);

        assertFalse(result, "User should not be assigned when no tasks are present");
        verify(mockTaskQuery).count();
        verify(mockHistoricQuery).count();
    }

    @Test
    void testIsUserAssignedToProcessInstance_Failure() {
        when(processEngine.getTaskService()).thenReturn(taskService);
        when(taskService.createTaskQuery()).thenThrow(new RuntimeException("Query failed"));

        com.pfe.web.rest.errors.ProcessEngineException exception = assertThrows(
            ProcessEngineException.class, () -> {
            processEngineService.isUserAssignedToProcessInstance(USER_ID, PROCESS_INSTANCE_ID);
        });

        assertEquals("Failed to check if user " + USER_ID + " is assigned to process instance "
                + PROCESS_INSTANCE_ID,
            exception.getMessage(), "Exception message should match expected format");
    }

    @Test
    void GetHistoricInstancesWithUserConnected() {
        // Setup mocks
        UUID userId = UUID.randomUUID();
        when(this.jwtUtils.getConnectedUserId()).thenReturn(userId);

        HistoricProcessInstanceQuery query = mock(HistoricProcessInstanceQuery.class);
        when(this.processEngine.getHistoryService()).thenReturn(this.historyService);
        when(this.historyService.createHistoricProcessInstanceQuery()).thenReturn(query);
        when(query.startedBy(userId.toString())).thenReturn(query);
        when(query.orderByProcessInstanceStartTime()).thenReturn(query);
        when(query.desc()).thenReturn(query);

        HistoricProcessInstance instance = mock(HistoricProcessInstance.class);
        when(instance.getId()).thenReturn("proc-123");
        when(query.list()).thenReturn(List.of(instance));

        HistoricProcessInstanceDTO dto = new HistoricProcessInstanceDTO();
        dto.setId("proc-123");
        when(this.historicProcessInstanceMapper.toDto(instance)).thenReturn(dto);

        Task task = mock(Task.class);
        when(task.getName()).thenReturn("Current Task");

        TaskQuery taskQuery = mock(TaskQuery.class);
        when(this.processEngine.getTaskService()).thenReturn(this.taskService);
        when(this.taskService.createTaskQuery()).thenReturn(taskQuery);
        when(taskQuery.processInstanceId("proc-123")).thenReturn(taskQuery);
        when(taskQuery.active()).thenReturn(taskQuery);
        when(taskQuery.list()).thenReturn(List.of(task));

        HistoricProcessInstanceTaskCriteria criteria = new HistoricProcessInstanceTaskCriteria(null, null, null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);

        // Call
        Page<HistoricProcessInstanceWithTaskDTO> result =
            this.processEngineService.getProcessInstancesByUserId(userId, pageable, criteria);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("proc-123", result.getContent().get(0).getInstance().getId());
        assertEquals("Current Task", result.getContent().get(0).getCurrentTaskName());
    }

}
