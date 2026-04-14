package com.pfe.services.servicesImplementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pfe.domain.AbstractTask;
import com.pfe.domain.enumeration.TaskStatus;
import com.pfe.dto.*;
import com.pfe.dto.HistoricProcessInstanceWithTaskDTO;
import com.pfe.feignServices.FormService;
import com.pfe.feignServices.UserService;
import com.pfe.mapper.HistoricProcessInstanceMapper;
import com.pfe.mapper.HistoricTaskInstanceMapper;
import com.pfe.mapper.ProcessInstanceMapper;
import com.pfe.mapper.TaskMapper;
import com.pfe.security.SecurityUtils;
import com.pfe.services.JsonProcessingService;
import com.pfe.services.ProcessEngineService;
import com.pfe.services.ProcessInstanceQueryService;
import com.pfe.services.TaskQueryService;
import com.pfe.services.criteria.HistoricProcessInstanceTaskCriteria;
import com.pfe.services.criteria.ProcessInstanceCriteria;
import com.pfe.services.criteria.TaskCriteria;
import com.pfe.services.utils.JwtUtils;
import com.pfe.web.rest.errors.CustomException;
import com.pfe.web.rest.errors.ProcessEngineException;
import com.pfe.dto.AbstractProcessInstance;
import com.pfe.dto.FormDTO;
import com.pfe.dto.HistoricProcessInstanceDTO;
import com.pfe.dto.HistoricTaskInstanceDTO;
import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.dto.ProcessInstanceDTO;
import com.pfe.dto.SubmissionDTO;
import com.pfe.dto.TaskDTO;
import com.pfe.dto.UserDTO;
import jakarta.persistence.EntityNotFoundException;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProcessEngineServiceImpl implements ProcessEngineService {

    private static final Logger log = LoggerFactory.getLogger(ProcessEngineServiceImpl.class);
    private static final String XML_BPMN = "http://camunda.org/schema/1.0/bpmn";
    private static final String ELEMENT_PROCESS_NAME ="categoryProcess";

    private final ProcessEngine processEngine;
    private final TaskMapper taskMapper;
    private final FormService formServiceClient;
    private final UserService userService;
    private final ProcessInstanceMapper processInstanceMapper;
    private final JwtUtils jwtUtils;
    private final JsonProcessingService jsonProcessingService;

    private final HistoricTaskInstanceMapper historicTaskInstanceMapper;

    private final ProcessInstanceQueryService processInstanceQueryService;

    private final TaskQueryService taskQueryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;

    public ProcessEngineServiceImpl(ProcessEngine processEngine, TaskMapper taskMapper, FormService formServiceClient, UserService userService, ProcessInstanceMapper processInstanceMapper, JwtUtils jwtUtils, HistoricTaskInstanceMapper historicTaskInstanceMapper, JsonProcessingService jsonProcessingService, ProcessInstanceQueryService processInstanceQueryService, TaskQueryService taskQueryService, RuntimeService runtimeService, TaskService taskService) {
        this.processEngine = processEngine;
        this.taskMapper = taskMapper;
        this.formServiceClient = formServiceClient;
        this.userService = userService;
        this.processInstanceMapper = processInstanceMapper;
        this.jwtUtils = jwtUtils;
        this.historicTaskInstanceMapper = historicTaskInstanceMapper;
        this.jsonProcessingService = jsonProcessingService;
        this.processInstanceQueryService = processInstanceQueryService;
        this.taskQueryService = taskQueryService;
        this.runtimeService = runtimeService;
        this.taskService = taskService;
    }

    /*                   process mgmt       */
    @Override
    public ProcessInstanceDTO startProcessByKey(String processKey, Map<String, String> variables) {
        try {
            this.processEngine.getIdentityService().setAuthenticatedUserId(SecurityUtils.getUserIdFromCurrentUser());
            Map<String, Object> processVariables = new HashMap<>();
            if (variables != null) {
                processVariables.putAll(variables);
            }
            String businessKey = generateBusinessKey(processKey, true);
            ProcessInstance processInstance = this.processEngine.getRuntimeService()
                .startProcessInstanceByKey(processKey, businessKey, processVariables);
            HistoricProcessInstance historicProcessInstance = this.processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .singleResult();

            ProcessInstanceDTO processInstanceDTO = this.processInstanceMapper.toDto(processInstance);
            processInstanceDTO.setStartUserId(historicProcessInstance.getStartUserId());

            return processInstanceDTO;
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to start process by key: " + processKey, e);
        }
    }

    private String getProcessKeyById(String processId) {
        return this.processEngine.getRepositoryService()
            .createProcessDefinitionQuery()
            .processDefinitionId(processId)
            .singleResult()
            .getKey();
    }

    private String generateBusinessKey(String processIdOrKey, boolean isProcessKey) {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HHmmsss");
        String timestamp = formatter.format(now);
        if (!isProcessKey) {
            return getProcessKeyById(processIdOrKey) + "-" + timestamp;
        }
        return processIdOrKey + "-" + timestamp;
    }

    @Override
    public ProcessInstanceDTO startProcessById(String processId, Map<String, String> variables) {
        try {
            this.processEngine.getIdentityService().setAuthenticatedUserId(SecurityUtils.getIDCurrentUser());
            String businessKey = generateBusinessKey(processId, false);
            Map<String, Object> processVariables = new HashMap<>();
            if (variables != null) {
                processVariables.putAll(variables);
            }
            ProcessInstance processInstance = this.processEngine.getRuntimeService()
                .startProcessInstanceById(processId, businessKey, processVariables);
            HistoricProcessInstance historicProcessInstance = this.processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .singleResult();

            ProcessInstanceDTO processInstanceDTO = this.processInstanceMapper.toDto(processInstance);
            processInstanceDTO.setStartUserId(historicProcessInstance.getStartUserId());

            return processInstanceDTO;

        } catch (Exception e) {
            throw new ProcessEngineException("Failed to start process by ID: " + processId, e);
        }
    }

    @Override
    public void signalExecution(String executionId) {
        try {
            this.processEngine.getRuntimeService().signal(executionId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to signal execution: " + executionId, e);
        }
    }

    @Override
    public String getProcessXmlById(String processDefinitionId) {
        try {
            BpmnModelInstance modelInstance = this.processEngine.getRepositoryService()
                .getBpmnModelInstance(processDefinitionId);
            return Bpmn.convertToString(modelInstance);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get BPMN XML for process definition ID: " + processDefinitionId, e);
        }
    }

    @Override
    public boolean toggleProcessDefinitionState(String processDefinitionId) {
        try {

            ProcessDefinition processDefinition = this.processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();

            if (processDefinition == null) {
                throw new ProcessEngineException("Process definition with ID: " + processDefinitionId + " not found");
            }


            boolean isSuspended = processDefinition.isSuspended();

            if (isSuspended) {

                this.processEngine.getRepositoryService().activateProcessDefinitionById(processDefinitionId, true, null);
            } else {

                this.processEngine.getRepositoryService().suspendProcessDefinitionById(processDefinitionId, true, null);
            }


            return !isSuspended;
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to toggle state for process definition with ID: " + processDefinitionId, e);
        }
    }

    @Override
    public List<AbstractProcessInstance> getProcessInstances(String processDefinitionId) {
        log.debug("getProcessInstances with ID {}", processDefinitionId);
        ProcessDefinition processDefinition = this.processEngine.getRepositoryService()
            .createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId)
            .singleResult();

        if (processDefinition == null) {
            log.error("Process definition with ID: {} not found.", processDefinitionId);
            throw new EntityNotFoundException(
                "Process definition with ID " + processDefinitionId + " not found.");
        }
        String processDefinitionName = processDefinition.getName();

        List<AbstractProcessInstance> allInstances = new ArrayList<>();

        List<ProcessInstance> activeInstances = this.processEngine.getRuntimeService()
            .createProcessInstanceQuery()
            .processDefinitionId(processDefinitionId)
            .active()
            .list();

        List<ProcessInstanceDTO> activeInstanceDtos = activeInstances.stream()
            .map((ProcessInstance procInstance) -> {
                ProcessInstanceDTO processInstanceDTO = this.processInstanceMapper.toDto(procInstance);
                processInstanceDTO.setProcessDefinitionName(processDefinitionName);
                return processInstanceDTO;
            })
            .toList();

        allInstances.addAll(activeInstanceDtos);

        List<ProcessInstance> suspendedInstances = this.processEngine.getRuntimeService()
            .createProcessInstanceQuery()
            .processDefinitionId(processDefinitionId)
            .suspended()
            .list();

        List<ProcessInstanceDTO> suspendedInstanceDtos = suspendedInstances.stream()
            .map((ProcessInstance procInstance) -> {
                ProcessInstanceDTO processInstanceDTO = this.processInstanceMapper.toDto(procInstance);
                processInstanceDTO.setProcessDefinitionName(processDefinitionName);
                return processInstanceDTO;
            })
            .toList();

        allInstances.addAll(suspendedInstanceDtos);


        List<HistoricProcessInstance> completedInstances = this.processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processDefinitionId(processDefinitionId)
            .finished()
            .list();

        List<HistoricProcessInstanceDTO> completedInstanceDtos = completedInstances.stream()
            .map(HistoricProcessInstanceMapper.INSTANCE::toDto)
            .toList();

        allInstances.addAll(completedInstanceDtos);

        return allInstances;
    }

    @Override
    public Page<AbstractProcessInstance> getProcessInstancesByCriteria(
        ProcessInstanceCriteria criteria, String processDefinitionId, Pageable pageable) {
        log.debug("getProcessInstances with ID {}", processDefinitionId);
        ProcessDefinition processDefinition = this.processEngine.getRepositoryService()
            .createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId)
            .singleResult();
        if (processDefinition == null) {
            log.error("Process definition with ID: {} not found.", processDefinitionId);
            throw new EntityNotFoundException(
                "Process definition with ID " + processDefinitionId + " not found.");
        }
        List<AbstractProcessInstance> allInstances = new ArrayList<>();
        List<ProcessInstanceDTO> processInstances = this.processInstanceQueryService.findProcessInstanceByCriteria(criteria, processDefinition);
        List<HistoricProcessInstanceDTO> completedInstanceDtos = this.processInstanceQueryService.findHistoricProcessInstanceByCriteria(criteria, processDefinition);
        allInstances.addAll(processInstances);
        allInstances.addAll(completedInstanceDtos);

        int total = allInstances.size();
        if (pageable.getOffset() >= total) {
            pageable = PageRequest.of(0, pageable.getPageSize());
        }
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        List<AbstractProcessInstance> instances = allInstances.subList(start, end);
        return new PageImpl<>(instances, pageable, total);
    }


    @Override
    public List<AbstractProcessInstance> getCurrentUserProcessInstances(String processDefinitionId) {
        UUID connectedUserId = this.jwtUtils.getConnectedUserId();

        return this.processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processDefinitionId(processDefinitionId)
            .list()
            .stream()
            .filter(instance -> instance.getStartUserId() != null && instance.getStartUserId().equals(connectedUserId.toString()))
            .map(HistoricProcessInstanceMapper.INSTANCE::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AbstractProcessInstance> getCurrentUserProcessInstancesByKey(String processKey) {
        UUID connectedUserId = this.jwtUtils.getConnectedUserId();

        return this.processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processDefinitionKey(processKey)
            .list()
            .stream()
            .filter(instance -> instance.getStartUserId() != null &&
                instance.getStartUserId().equals(connectedUserId.toString()))
            .map(HistoricProcessInstanceMapper.INSTANCE::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<AbstractProcessInstance> getCurrentUserProcessInstancesByUserId() {
        UUID connectedUserId = this.jwtUtils.getConnectedUserId();

        return this.processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .list()
            .stream()
            .filter(instance -> instance.getStartUserId() != null &&
                instance.getStartUserId().equals(connectedUserId.toString()))
            .map(HistoricProcessInstanceMapper.INSTANCE::toDto)
            .collect(Collectors.toList());
    }

    /*                 task mgmt                            */
    @Override
    public List<Task> getTasksByAssignee(String assignee) {
        try {
            return this.processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get tasks for assignee: " + assignee, e);
        }
    }

    @Override
    public void completeTask(String taskId) {
        try {
            this.processEngine.getTaskService().complete(taskId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to complete task: " + taskId, e);
        }
    }

    @Override
    public TaskDTO getCurrentTaskByProcessInstanceId(String processInstanceId) {
        try {
            String userId = SecurityUtils.getUserIdFromCurrentUser();

            TaskQuery task = this.processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .initializeFormKeys();

//            Task task = processEngine.getTaskService()
//                .createTaskQuery()
//                .processInstanceId(processInstanceId)
//                .active()
//                .initializeFormKeys()
//                .singleResult();

            if (task.list() == null || task.list().isEmpty()) {
                throw new ProcessEngineException("No active task found for process instance: " + processInstanceId);
            }

            Task currentTask;
            if (task.count() > 1) {
                currentTask = task.list().get(0);
            } else {
                currentTask = task.singleResult();
            }

            TaskDTO taskDTO = this.taskMapper.toDto(currentTask);

            if (taskDTO.getFormKey() != null) {
                FormDTO form = this.formServiceClient.getForm(UUID.fromString(taskDTO.getFormKey()))
                    .getBody();
                taskDTO.setForm(form);
            } else {
                taskDTO.setForm(null);
            }

            if (taskDTO.getAssignee() != null && taskDTO.getAssignee().equals(userId)) {
                UUID assigneeId = UUID.fromString(taskDTO.getAssignee());
                ResponseEntity<UserDTO> response = this.userService.getuser(assigneeId);
                UserDTO user = response.getBody();
                taskDTO.setUserDTO(user);
            }

            return taskDTO;
        } catch (ProcessEngineException es) {
            throw es;
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get current task for process instance: " + processInstanceId, e);
        }
    }


    @Override
    public TaskDTO getCurrentTaskById(String id) {
        log.debug("Get current task by id: {}", id);
        try {
            String userId = SecurityUtils.getUserIdFromCurrentUser();

            Task task = this.processEngine.getTaskService()
                .createTaskQuery()
                .taskId(id)
                .active()
                .initializeFormKeys()
                .singleResult();

            if (task == null) {
                log.info("No active task found for id: {}", id);
                throw new ProcessEngineException("No active task found for id: " + id);
            }

            TaskDTO taskDTO = this.taskMapper.toDto(task);

            if (task.getFormKey() != null) {
                FormDTO form = this.formServiceClient.getForm(UUID.fromString(task.getFormKey()))
                    .getBody();
                taskDTO.setForm(form);
            } else {
                taskDTO.setForm(null);
            }

            if (taskDTO.getAssignee() != null && taskDTO.getAssignee().equals(userId)) {
                UUID assigneeId = UUID.fromString(taskDTO.getAssignee());
                ResponseEntity<UserDTO> response = this.userService.getuser(assigneeId);
                UserDTO user = response.getBody();
                taskDTO.setUserDTO(user);
            }
            String businessKey = this.processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult()
                .getBusinessKey();
            taskDTO.setBusinessKey(businessKey);

            return taskDTO;
        } catch (ProcessEngineException es) {
            log.error("Failed to get current task by id: {}", id, es);
            throw es;
        } catch (Exception e) {
            log.error("Failed to get current task by id: {}", id, e);
            throw new ProcessEngineException("Failed to get current task for id: " + id, e);
        }
    }


    @Override
    public TaskDTO getCurrentTaskByUserId(String processInstanceId, String userId) {
        try {
            Task task = this.processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstanceId)
                .active()
                .initializeFormKeys()
                .singleResult();

            if (task == null) {
                throw new ProcessEngineException("No active task found for process instance: " + processInstanceId);
            }

            TaskDTO taskDTO = this.taskMapper.toDto(task);

            if (task.getFormKey() != null) {
                FormDTO form = this.formServiceClient.getForm(UUID.fromString(task.getFormKey())).getBody();
                taskDTO.setForm(form);
            } else {
                taskDTO.setForm(null);
            }

            if (userId != null) {
                UUID assigneeId = UUID.fromString(userId);
                ResponseEntity<UserDTO> userResponse = this.userService.getuser(assigneeId);
                UserDTO user = userResponse.getBody();
                taskDTO.setUserDTO(user);
            }

            return taskDTO;
        } catch (ProcessEngineException es) {
            throw es;
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get current task for process instance: " + processInstanceId, e);
        }
    }

    @Override
    public TaskDTO getCurrentTaskByAssignee(String processInstanceId, String assignee) {
        try {
            Task task = this.processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(assignee)
                .active()
                .initializeFormKeys()
                .singleResult();

            if (task != null) {
                FormDTO form = this.formServiceClient.getForm(UUID.fromString(task.getFormKey())).getBody();

                TaskDTO taskDTO = this.taskMapper.toDto(task);

                taskDTO.setForm(form);
                if (taskDTO.getAssignee() != null) {
                    UUID assigneeId = UUID.fromString(taskDTO.getAssignee());
                    ResponseEntity<UserDTO> userResponse = this.userService.getuser(assigneeId);
                    UserDTO user = userResponse.getBody();
                    taskDTO.setUserDTO(user);
                }

                return taskDTO;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get current task for process instance: " + processInstanceId + " and assignee: " + assignee, e);
        }
    }

    @Override
    public List<AbstractTask> getTasksByUserId(String userId) {
        try {
            List<Task> currentTasks = this.processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(userId)
                .initializeFormKeys()
                .orderByTaskCreateTime()
                .desc()
                .list();
            List<TaskDTO> currentTaskDTOs = currentTasks.stream().map(this.taskMapper::toDto).toList();


            List<HistoricTaskInstance> historicTasks = this.processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .orderByHistoricTaskInstanceEndTime().asc()
                .list();

            List<HistoricTaskInstanceDTO> historicTaskDTOs = historicTasks.stream()
                .map(historicTask -> {
                    HistoricTaskInstanceDTO dto = this.historicTaskInstanceMapper.toDto(historicTask);
                    dto.setStatus(TaskStatus.COMPLETED);

                    ProcessDefinition processDefinition = this.processEngine.getRepositoryService()
                        .createProcessDefinitionQuery()
                        .processDefinitionId(historicTask.getProcessDefinitionId())
                        .singleResult();
                    if (processDefinition != null) {
                        dto.setProcessDefinitionVersion(processDefinition.getVersion());
                        dto.setProcessDefinitionName(processDefinition.getName());
                    }


                    dto.setStatus(TaskStatus.COMPLETED);

                    if (dto.getAssignee() != null) {
                        UUID assigneeId = UUID.fromString(dto.getAssignee());
                        ResponseEntity<UserDTO> userResponse = this.userService.getuser(assigneeId);
                        UserDTO user = userResponse.getBody();
                        dto.setUserDTO(user);
                    }
                    return dto;
                })
                .toList();

            List<AbstractTask> allTasks = new ArrayList<>();
            allTasks.addAll(currentTaskDTOs);
            allTasks.addAll(historicTaskDTOs);

            return allTasks;
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get all tasks (current and historic) for user ID: " + userId, e);
        }
    }

    @Override
    public List<TaskDTO> getTasksByInstanceId(String processInstanceId) {
        try {
            List<Task> tasks = this.processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstanceId)
                .initializeFormKeys()
                .list();

            return tasks.stream()
                .map(task -> {
                    FormDTO form = (task.getFormKey() != null) ?
                        this.formServiceClient.getForm(UUID.fromString(task.getFormKey())).getBody() :
                        null;

                    TaskDTO taskDTO = this.taskMapper.toDto(task);
                    taskDTO.setForm(form);

                    TaskStatus status = task.isSuspended() ? TaskStatus.COMPLETED : TaskStatus.ACTIVE;
                    taskDTO.setStatus(status);

                    return taskDTO;
                })
                .toList();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get tasks for process instance ID: " + processInstanceId, e);
        }
    }

    @Override
    public List<TaskDTO> getTasksByOwner(String owner) {
        try {
            List<Task> tasks = this.processEngine.getTaskService()
                .createTaskQuery()
                .taskOwner(owner)
                .initializeFormKeys()
                .list();

            return tasks.stream()
                .map(task -> {
                    FormDTO form = (task.getFormKey() != null) ?
                        this.formServiceClient.getForm(UUID.fromString(task.getFormKey())).getBody() :
                        null;

                    TaskDTO taskDTO = this.taskMapper.toDto(task);
                    taskDTO.setForm(form);

                    TaskStatus status = task.isSuspended() ? TaskStatus.COMPLETED : TaskStatus.ACTIVE;
                    taskDTO.setStatus(status);

                    return taskDTO;
                })
                .toList();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get tasks for owner: " + owner, e);
        }
    }


    @Override
    public TaskDTO validateAndMoveToNextTask(String taskId, Map<String, Object> variables) {
        try {
            String userId = SecurityUtils.getUserIdFromCurrentUser();

            Task currentTask = this.processEngine.getTaskService().createTaskQuery().taskId(taskId).initializeFormKeys().singleResult();
            if (currentTask == null) {
                throw new ProcessEngineException("Task with ID " + taskId + " not found.");
            }

            String formId = this.processEngine.getFormService().getTaskFormKey(currentTask.getProcessDefinitionId(), currentTask.getTaskDefinitionKey());

            if (formId != null) {
                submitFormToMicroservice(formId, taskId, variables);
            }

            //for tasks assigned to group
            if (!currentTask.getAssignee().equals(userId)) {
                this.processEngine.getTaskService().setAssignee(taskId, userId);
                currentTask.setAssignee(userId);
            }

            this.processEngine.getTaskService().complete(taskId, variables);
            TaskDTO taskDTO = this.taskMapper.toDto(currentTask);

            if (taskDTO.getAssignee() != null && taskDTO.getAssignee().equals(userId)) {
                UUID assigneeId = UUID.fromString(currentTask.getAssignee());
                ResponseEntity<UserDTO> userResponse = this.userService.getuser(assigneeId);
                UserDTO user = userResponse.getBody();
                taskDTO.setUserDTO(user);
            }

            return taskDTO;
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to validate task and move to the next task.", e);
        }
    }


    public void submitFormToMicroservice(String formId, String taskId, Map<String, Object> variables) {
        try {
            List<SubmissionDTO> submissionDTOList = variables.entrySet().stream()
                .map(entry -> {
                    SubmissionDTO submissionDTO = new SubmissionDTO();
                    submissionDTO.setKey(entry.getKey());
                    ObjectMapper objectMapper = new ObjectMapper();
                    try {
                        String jsonString = objectMapper.writeValueAsString(entry.getValue());
                        submissionDTO.setValue(jsonString);
                    } catch (JsonProcessingException e) {
                        log.debug("Failed to set write value as string", e);
                        throw new RuntimeException(e);
                    }
                    submissionDTO.setInstanceTaskId(taskId);
                    submissionDTO.setFormId(formId);
                    return submissionDTO;
                }).toList();

            this.formServiceClient.submitFormData(submissionDTOList);
            log.info("Form data submitted successfully to form microservice.");
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to submit form data to form microservice.", e);
        }
    }

    /*               external tasks mgmt              */
    @Override
    public List<LockedExternalTask> fetchAndLockTasks(String workerId, String topicName, int maxTasks) {
        try {
            return this.processEngine.getExternalTaskService()
                .fetchAndLock(maxTasks, workerId)
                .topic(topicName, 1000L)
                .execute();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to fetch and lock tasks for worker: " + workerId + ", topic: " + topicName, e);
        }
    }

    @Override
    public void completeExternalTask(String taskId, String workerId) {
        try {
            this.processEngine.getExternalTaskService().complete(taskId, workerId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to complete external task: " + taskId + " for worker: " + workerId, e);
        }
    }

    /*            user and group mgmt          */

    @Override
    public void createUser(String userId, String firstName, String lastName, String email) {
        try {
            User user = this.processEngine.getIdentityService().newUser(userId);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            this.processEngine.getIdentityService().saveUser(user);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to create user with ID: " + userId, e);
        }
    }

    @Override
    public User findUserById(String userId) {
        try {
            return this.processEngine.getIdentityService()
                .createUserQuery()
                .userId(userId)
                .singleResult();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to find user by ID: " + userId, e);
        }
    }

    @Override
    public void createGroup(String groupId, String groupName) {
        try {
            Group group = this.processEngine.getIdentityService().newGroup(groupId);
            group.setName(groupName);
            this.processEngine.getIdentityService().saveGroup(group);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to create group with ID: " + groupId, e);
        }
    }

    @Override
    public void addUserToGroup(String userId, String groupId) {
        try {
            this.processEngine.getIdentityService()
                .createMembership(userId, groupId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to add user: " + userId + " to group: " + groupId, e);
        }
    }

    /*               repository mgmt                */

    @Override
    public Deployment deployProcessDefinition(String name, String xml) {
        try {
            return this.processEngine.getRepositoryService()
                .createDeployment()
                .name(name)
                .addString(name + ".bpmn", xml)
                .deploy();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to deploy process definition: " + name, e);
        }
    }

    @Override
    public void deleteDeployment(String deploymentId) {
        try {
            this.processEngine.getRepositoryService().deleteDeployment(deploymentId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to delete deployment with ID: " + deploymentId, e);
        }
    }

    @Override
    public long getProcessDefinitionCount() {
        try {
            return this.processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .count();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get process definition count", e);
        }
    }

    /*            history mgmt        */

    @Override
    public List<HistoricProcessInstance> getCompletedProcessInstances(String processDefinitionKey) {
        try {
            return this.processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .finished()
                .list();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get completed process instances for key: " + processDefinitionKey, e);
        }
    }

    @Override
    public HistoricProcessInstance getProcessInstanceHistory(String processInstanceId) {
        try {
            return this.processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get process instance history for ID: " + processInstanceId, e);
        }
    }

    @Override
    public long getHistoricTaskInstanceCount() {
        try {
            return this.processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .count();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get historic task instance count", e);
        }
    }

    @Override
    public List<HistoricTaskInstanceDTO> getHistoricTaskInstancesByProcessInstanceId(String processInstanceId) {
        log.debug("get all historic tasks by process instance Id : {}", processInstanceId);

        try {
            List<HistoricTaskInstance> historicTasks = this.processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricTaskInstanceEndTime().asc()
                .list();
            if (historicTasks.isEmpty()) {
                return Collections.emptyList();
            }
            List<UUID> assigneeIds = historicTasks.stream()
                .map(HistoricTaskInstance::getAssignee)
                .filter(Objects::nonNull)
                .distinct()
                .map(UUID::fromString)
                .toList();

            Map<UUID, UserDTO> userMap;
            if(!assigneeIds.isEmpty()) {
                ResponseEntity<List<UserDTO>> userResponse = this.userService.getUsersByIds(assigneeIds);
                List<UserDTO> users = userResponse.getBody();

                if(users == null || users.isEmpty()) {
                    log.error("Assigned tasks found but no users returned for assignees!");
                    throw new CustomException(HttpStatus.NOT_FOUND,"Assigned tasks found but no users returned for assignees!");
                }

                userMap = users.stream()
                    .collect(Collectors.toMap(UserDTO::getId, user -> user));

            } else {
                userMap = Collections.emptyMap();
            }

            return historicTasks.stream()
                .map(historicTask -> {
                    HistoricTaskInstanceDTO dto = this.historicTaskInstanceMapper.toDto(historicTask);

                    String formKey = this.processEngine.getFormService()
                        .getTaskFormKey(historicTask.getProcessDefinitionId(), historicTask.getTaskDefinitionKey());

                    ProcessDefinition processDefinition = this.processEngine.getRepositoryService()
                        .createProcessDefinitionQuery()
                        .processDefinitionId(historicTask.getProcessDefinitionId())
                        .singleResult();
                    if (processDefinition != null) {
                        dto.setProcessDefinitionVersion(processDefinition.getVersion());
                        dto.setProcessDefinitionName(processDefinition.getName());
                    }
                    if (formKey != null) {
                        FormDTO form = this.formServiceClient.getForm(UUID.fromString(formKey)).getBody();

                        dto.setForm(form);

                    }
                    List<SubmissionDTO> submissions = this.formServiceClient
                        .getAllSubmissionsByTaskInstanceId(UUID.fromString(historicTask.getId()))
                        .getBody();
                    dto.setSubmissionDTO(submissions);
                    if (historicTask.getAssignee() != null) {
                        UUID assigneeUUID = UUID.fromString(historicTask.getAssignee());
                        dto.setUserDTO(userMap.get(assigneeUUID));
                    }
                    dto.setStatus(TaskStatus.COMPLETED);
                    return dto;
                })
                .toList();
        } catch (CustomException ce) {
            log.error("Assigned tasks found but no users returned for assignees!");
            throw ce;
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get historic task instances for process instance ID: " + processInstanceId, e);
        }
    }

    @Override
    public List<HistoricTaskInstance> getHistoricTaskInstancesByAssignee(String assignee) {
        try {
            return this.processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .taskAssignee(assignee)
                .orderByHistoricTaskInstanceEndTime().asc()
                .list();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get historic task instances for assignee: " + assignee, e);
        }
    }

    /*            authorization mgmt             */
    @Override
    public void createAuthorization(String userId, String resourceId, Resources resourceType, Permission permission) {
        try {
            Authorization authorization = this.processEngine.getAuthorizationService()
                .createNewAuthorization(Authorization.AUTH_TYPE_GLOBAL);
            authorization.setUserId(userId);
            authorization.addPermission(permission);
            authorization.setResourceId(resourceId);
            authorization.setResource(resourceType);
            this.processEngine.getAuthorizationService().saveAuthorization(authorization);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to create authorization for user: " + userId, e);
        }
    }

    @Override
    public Authorization getAuthorization(String authorizationId) {
        try {
            return this.processEngine.getAuthorizationService()
                .createAuthorizationQuery()
                .authorizationId(authorizationId)
                .singleResult();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get authorization with ID: " + authorizationId, e);
        }
    }

    @Override
    public void deleteAuthorization(String authorizationId) {
        try {
            this.processEngine.getAuthorizationService().deleteAuthorization(authorizationId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to delete authorization with ID: " + authorizationId, e);
        }
    }

    /*             job mgmt               */
    @Override
    public List<Job> getAllJobs() {
        try {
            return this.processEngine.getManagementService()
                .createJobQuery()
                .list();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get all jobs", e);
        }
    }

    @Override
    public void executeJob(String jobId) {
        try {
            this.processEngine.getManagementService().executeJob(jobId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to execute job with ID: " + jobId, e);
        }
    }

    @Override
    public void deleteJob(String jobId) {
        try {
            this.processEngine.getManagementService().deleteJob(jobId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to delete job with ID: " + jobId, e);
        }
    }

    @Override
    public long getJobCount() {
        try {
            return this.processEngine.getManagementService()
                .createJobQuery()
                .count();
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get job count", e);
        }
    }


    /*                    case mgmt                   */
    @Override
    public CaseInstance createCaseInstance(String caseDefinitionKey) {
        try {
            return this.processEngine.getCaseService().createCaseInstanceByKey(caseDefinitionKey);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to create case instance with definition key: " + caseDefinitionKey, e);
        }
    }

    @Override
    public void manuallyStartCaseExecution(String caseExecutionId) {
        try {
            this.processEngine.getCaseService().manuallyStartCaseExecution(caseExecutionId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to manually start case execution with ID: " + caseExecutionId, e);
        }
    }

    @Override
    public void completeCaseExecution(String caseExecutionId) {
        try {
            this.processEngine.getCaseService().completeCaseExecution(caseExecutionId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to complete case execution with ID: " + caseExecutionId, e);
        }
    }

    /*              form mgmt            */

    @Override
    public TaskFormData getTaskFormData(String taskId) {
        try {
            return this.processEngine.getFormService().getTaskFormData(taskId);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get task form data for task ID: " + taskId, e);
        }
    }

    @Override
    public void submitTaskForm(String taskId, Map<String, Object> variables) {
        try {
            this.processEngine.getFormService().submitTaskForm(taskId, variables);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to submit task form for task ID: " + taskId, e);
        }
    }

    private List<TaskDTO> getTasksByGroupId(String userId) {
        log.debug("getTasksByGroupId userId: {}", userId);
        try {
            List<UUID> groupIds = this.userService.getGroupIdsByUserId(UUID.fromString(userId)).getBody();
            List<String> groupIdStrings = new ArrayList<>();
            if (groupIds != null) {
                groupIdStrings = groupIds.stream().map(UUID::toString).toList();
            }

            List<TaskDTO> allTasks = new ArrayList<>();

            for (String groupId : groupIdStrings) {
                List<Task> currentTasks = this.processEngine.getTaskService()
                    .createTaskQuery()
                    .taskAssignee(groupId)
                    .initializeFormKeys()
                    .list();

                List<TaskDTO> currentTaskDTOs = currentTasks.stream()
                    .map(this.taskMapper::toDto)
                    .toList();

                allTasks.addAll(currentTaskDTOs);
            }

            return allTasks;

        } catch (Exception e) {
            log.error("Failed to get all current tasks for user ID : {}", userId);
            throw new ProcessEngineException("Failed to get all current tasks for user ID: " + userId, e);
        }
    }
    private List<TaskDTO> getTasksByRoleId(String userId) {

        log.debug("getTasksByRoleId userId: {}", userId);
        try {
            List<UUID> roleIds = this.userService.getRoleIdsByUserId(UUID.fromString(userId)).getBody();
            List<String> roleIdStrings = new ArrayList<>();
            if (roleIds != null) {
                roleIdStrings = roleIds.stream().map(UUID::toString).toList();
            }

            List<TaskDTO> allTasks = new ArrayList<>();

            for (String roleId : roleIdStrings) {
                List<Task> currentTasks = this.processEngine.getTaskService()
                    .createTaskQuery()
                    .taskAssignee(roleId)
                    .initializeFormKeys()
                    .list();

                List<TaskDTO> currentTaskDTOs = currentTasks.stream()
                    .map(this.taskMapper::toDto)
                    .toList();

                allTasks.addAll(currentTaskDTOs);
            }

            return allTasks;

        } catch (Exception e) {
            log.error("Failed to get all current tasks for user ID : {}", userId);
            throw new ProcessEngineException("Failed to get all current tasks for user ID: " + userId, e);
        }
    }


    private List<TaskDTO> getTasksAssignedUsernameByUserId(String userId) {
        try {
            log.debug("getTasksAssignedUsernameByUserId : {}", userId);
            ResponseEntity<UserDTO> response = this.userService.getuser(UUID.fromString(userId));
            UserDTO user = response.getBody();

            if (user == null) {
                log.error("Failed to get user with ID: {}", userId);
                throw new ProcessEngineException("Failed to get user with ID: " + userId);
            }

            List<Task> currentTasks = this.processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(user.getUsername())
                .initializeFormKeys()
                .list();

            return currentTasks.stream()
                .map(this.taskMapper::toDto)
                .toList();

        } catch (Exception e) {
            log.error("Failed to get all current tasks for user id: {}", userId);
            throw new ProcessEngineException(
                "Failed to get all current tasks for user id: " + userId, e);
        }
    }


    @Override
    public Page<AbstractTask> getHistoricTasksByUserId(String userId, Pageable pageable) {
        log.debug("Request to get HistoricTaskInstance by userId : {}", userId);
        try {
            List<HistoricTaskInstance> historicTasks = this.processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .orderByHistoricTaskInstanceEndTime().desc()
                .list();

            List<HistoricTaskInstanceDTO> historicTaskDTOs = historicTasks.stream().map(historicTask -> {
                HistoricTaskInstanceDTO historicTaskDTO = this.historicTaskInstanceMapper.toDto(historicTask);

                String businessKey = this.processEngine.getHistoryService()
                    .createHistoricProcessInstanceQuery()
                    .processInstanceId(historicTask.getProcessInstanceId())
                    .singleResult()
                    .getBusinessKey();

                historicTaskDTO.setBusinessKey(businessKey);
                historicTaskDTO.setStatus(TaskStatus.COMPLETED);

                ProcessDefinition processDefinition = this.processEngine.getRepositoryService()
                    .createProcessDefinitionQuery()
                    .processDefinitionId(historicTask.getProcessDefinitionId())
                    .singleResult();
                if (processDefinition != null) {
                    historicTaskDTO.setProcessDefinitionVersion(processDefinition.getVersion());
                    historicTaskDTO.setProcessDefinitionName(processDefinition.getName());
                }
                return historicTaskDTO;
            }).toList();

            int start = Math.toIntExact(pageable.getOffset());
            int totalElements = historicTaskDTOs.size();

            if (start >= totalElements) {
                return new PageImpl<>(Collections.emptyList(), pageable, totalElements);
            }

            int end = Math.min((start + pageable.getPageSize()), totalElements);
            List<AbstractTask> pagedTasks = new ArrayList<>(historicTaskDTOs.subList(start, end));
            log.debug("Found {} historicTasks ", totalElements);
            return new PageImpl<>(pagedTasks, pageable, historicTaskDTOs.size());

        } catch (Exception e) {
            log.error("Error listing historic tasks by userId", e.getMessage(), e);
            throw new ProcessEngineException("Failed to get all historic tasks for user ID: " + userId, e);
        }
    }

    @Override
    public Page<AbstractTask> getActiveTasksByUserId(Pageable pageable) {
        log.debug("Request to get active tasks by user ");
        try {
            String userId = SecurityUtils.getUserIdFromCurrentUser();
            List<Task> currentTasks = this.processEngine.getTaskService()
                .createTaskQuery()
                .orderByTaskCreateTime()
                .desc()
                .taskAssignee(userId)
                .initializeFormKeys()
                .list();

            Map<String, HistoricProcessInstance> historicProcessInstanceMap = new HashMap<>();
            Map<UUID, String> userIdToNameMap = new HashMap<>();

            if (!currentTasks.isEmpty()) {
                Set<String> processInstanceIds = currentTasks.stream()
                    .map(Task::getProcessInstanceId)
                    .collect(Collectors.toSet());

                List<HistoricProcessInstance> historicProcessInstances = processEngine.getHistoryService()
                    .createHistoricProcessInstanceQuery()
                    .processInstanceIds(processInstanceIds)
                    .list();

                historicProcessInstanceMap = historicProcessInstances.stream()
                    .collect(Collectors.toMap(HistoricProcessInstance::getId, hpi -> hpi, (hpi1, hpi2) -> hpi1));

                List<UUID> startUserIds = historicProcessInstances.stream()
                    .map(HistoricProcessInstance::getStartUserId)
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(UUID::fromString)
                    .toList();

                if (!startUserIds.isEmpty()) {
                    try {
                        ResponseEntity<List<UserDTO>> userResponse = this.userService.getUsersByIds(startUserIds);
                        List<UserDTO> users = userResponse.getBody();
                        if (users != null) {
                            userIdToNameMap = users.stream()
                                .collect(Collectors.toMap(UserDTO::getId, user -> user.getFirstName() + " " + user.getLastName()));
                        }
                    } catch (Exception e) {
                        log.warn("Impossible de récupérer les utilisateurs en batch", e);
                    }
                }
            }

            final Map<String, HistoricProcessInstance> finalHistoricProcessInstanceMap = historicProcessInstanceMap;
            final Map<UUID, String> finalUserIdToNameMap = userIdToNameMap;

            List<TaskDTO> currentUserTaskDTOs = currentTasks.stream().map(task -> {
                TaskDTO taskDTO = this.taskMapper.toDto(task);
                HistoricProcessInstance hpi = finalHistoricProcessInstanceMap.get(task.getProcessInstanceId());

                if (hpi != null) {
                    taskDTO.setBusinessKey(hpi.getBusinessKey());
                    String startUserId = hpi.getStartUserId();
                    if (startUserId != null) {
                        String ownerName = finalUserIdToNameMap.getOrDefault(UUID.fromString(startUserId), "Pas de user");
                        taskDTO.setOwner(ownerName);
                    } else {
                        taskDTO.setOwner("Pas de user");
                    }
                } else {

                    try {
                        ProcessInstance pi = this.processEngine.getRuntimeService()
                            .createProcessInstanceQuery()
                            .processInstanceId(task.getProcessInstanceId())
                            .singleResult();
                        if (pi != null) {
                            taskDTO.setBusinessKey(pi.getBusinessKey());
                        }
                    } catch (Exception e) {
                        log.warn("Could not find runtime instance for task {}", task.getId(), e);
                    }
                    taskDTO.setOwner("Pas de user");
                    log.warn("Could not find HistoricProcessInstance for active task with processInstanceId: {}", task.getProcessInstanceId());
                }
                return taskDTO;
            }).toList();

            List<TaskDTO> currentGroupTaskDTOs = getTasksByGroupId(userId);
            List<TaskDTO> currentRoleTaskDTOs = getTasksByRoleId(userId);
            List<TaskDTO> currentTasksAssignedUsername = getTasksAssignedUsernameByUserId(userId);

            List<TaskDTO> currentUserGroupRoleTaskDTOs = new ArrayList<>(
                Stream.concat(Stream.concat(currentUserTaskDTOs.stream(),
                            currentGroupTaskDTOs.stream()),
                        currentRoleTaskDTOs.stream())
                    .toList());

            List<TaskDTO> currentTaskDTOs = new ArrayList<>(
                Stream.concat(currentUserGroupRoleTaskDTOs.stream(),
                        currentTasksAssignedUsername.stream())
                    .toList());
            currentTaskDTOs.sort(Comparator.comparing(TaskDTO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())));


            int start = Math.toIntExact(pageable.getOffset());
            int totalElements = currentTaskDTOs.size();

            if (start >= totalElements) {
                return new PageImpl<>(Collections.emptyList(), pageable, totalElements);
            }

            int end = Math.min((start + pageable.getPageSize()), totalElements);
            List<AbstractTask> pagedTasks = new ArrayList<>(currentTaskDTOs.subList(start, end));
            return new PageImpl<>(pagedTasks, pageable, currentTaskDTOs.size());
        } catch (Exception e) {
            log.error("Failed to get all active tasks for user id", e);
            throw new ProcessEngineException("Failed to get all active tasks for user id " , e);
        }
    }

    /**
     * Retrieves variables from a process instance by an instance id.
     *
     * @param processInstanceId the ID of the instance
     * @return List of ObjectNode representing variables
     */
    @Override
    public List<ObjectNode> getProcessVariablesByProcessInstanceId(String processInstanceId) {
        try {
            return getVariablesFromRunningInstance(processInstanceId);
        } catch (Exception e) {
            log.error("No process instance found for ID : {}", processInstanceId);
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves variables for a running process instance.
     *
     * @param processInstanceId the ID of the process instance
     * @return List of ObjectNode representing variables
     */
    public List<ObjectNode> getVariablesFromRunningInstance(String processInstanceId) {
        try {
            Map<String, Object> variables = this.processEngine.getRuntimeService().getVariables(processInstanceId);
            return variables.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    String type = (value != null) ? value.getClass().getSimpleName() : "null";
                    return this.jsonProcessingService.createJsonNode(key, type, value);
                })
                .toList();
        } catch (Exception e) {
            log.debug("No variables found for running process instance ID: {}", processInstanceId);
            return Collections.emptyList();
        }
    }

    @Override
    public ProcessInstanceDTO startProcessOnceByKey(String processKey, Map<String, String> variables) {
        log.debug("Request to start process once by key {}", processKey);
        try {
            String userId = SecurityUtils.getUserIdFromCurrentUser();
            if (!hasUserStartedProcessInstance(processKey, userId)) {
                return startProcessByKey(processKey, variables);
            } else {
                log.info("Process already started. Updating variables in existing instance(s).");

                List<Task> tasks = this.taskService.createTaskQuery()
                    .processDefinitionKey(processKey)
                    .taskAssignee(userId)
                    .active()
                    .list();

                if (tasks.isEmpty()) {
                    log.warn("No active tasks found for updating variables.");
                    throw new ProcessEngineException("No active instance found for variable update.");
                }

                for (Task task : tasks) {
                    this.runtimeService.setVariables(task.getProcessInstanceId(), variables);
                    log.info("Updated variables for the instance {}", task.getProcessInstanceId());
                }

                return null;
            }

        } catch (Exception e) {
            log.error("Failed to update or start process {}", processKey, e);
            throw new ProcessEngineException("Failed to update or start process: " + processKey, e);
        }
    }

    @Override
    public ProcessInstanceDTO startUniqueProcessInstanceByKey(String processKey, Map<String, String> variables) {
        log.debug("Request to start process once by key {}", processKey);
        try {
            String userId = SecurityUtils.getIDCurrentUser();
            if (!hasUserActiveProcessInstance(processKey, userId)) {
                return startProcessByKey(processKey, variables);
            } else {
                log.info("Process already started. Updating variables in existing instance(s).");

                List<Task> tasks = this.taskService.createTaskQuery()
                    .processDefinitionKey(processKey)
                    .taskAssignee(userId)
                    .active()
                    .list();

                if (tasks.isEmpty()) {
                    log.warn("No active tasks found for updating variables.");
                    throw new ProcessEngineException("No active instance found for variable update.");
                }

                for (Task task : tasks) {
                    this.runtimeService.setVariables(task.getProcessInstanceId(), variables);
                    log.info("Updated variables for the instance {}", task.getProcessInstanceId());
                }

                throw new ProcessEngineException("An active instance of the process already exists for user: " + userId);
            }

        } catch (Exception e) {
            log.error("Failed to update or start process {}", processKey, e);
            throw new ProcessEngineException("Failed to update or start process: " + processKey, e);
        }
    }

    @Override
    public ProcessInstanceDTO startProcessOnceByKeyOrGetStartedProcessInstance(String processKey,
        Map<String, String> variables) {
        log.debug("Request to start process once by key {}", processKey);
        try {
            String userId = SecurityUtils.getIDCurrentUser();
            if (!hasUserStartedProcessInstance(processKey, userId)) {
                return startProcessByKey(processKey, variables);
            } else if (getUserStartedProcessInstanceId(processKey, userId) != null) {
                ProcessInstanceDTO processInstanceDTO = new ProcessInstanceDTO();
                processInstanceDTO.setProcessInstanceId(
                    getUserStartedProcessInstanceId(processKey, userId));
                return processInstanceDTO;
            } else {
                return null;
            }

        } catch (Exception e) {
            log.error("Failed to start process by key: {}", processKey);
            throw new ProcessEngineException("Failed to start process by key: " + processKey, e);
        }

    }

    public boolean hasUserStartedProcessInstance(String processDefinitionKey, String userId) {
        log.debug("Checking if user {} has participated in processDefinitionKey: {}", userId, processDefinitionKey);


        List<HistoricTaskInstance> userTasks = this.processEngine.getHistoryService()
            .createHistoricTaskInstanceQuery()
            .taskAssignee(userId)
            .list();

        for (HistoricTaskInstance task : userTasks) {
            if (task.getProcessInstanceId() == null) {
                continue;
            }

            HistoricProcessInstance processInstance = this.processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult();

            if (processInstance != null && processDefinitionKey.equals(processInstance.getProcessDefinitionKey())) {
                return true;
            }
        }

        return false;
    }
    public boolean hasUserActiveProcessInstance(String processDefinitionKey, String userId) {
        log.debug("Checking if user {} has active instances of processDefinitionKey: {}", userId, processDefinitionKey);

        List<HistoricProcessInstance> activeInstances = processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processDefinitionKey(processDefinitionKey)
            .startedBy(userId)
            .unfinished()
            .list();


        return !activeInstances.isEmpty();
    }


    public String getUserStartedProcessInstanceId(String processDefinitionKey,
        String userId) {
        log.debug(
            "Checking if user {} has started at least one instance of processDefinitionKey: {}",
            userId, processDefinitionKey);

        // Check active or suspended instances
        HistoricProcessInstance activeOrSuspendedInstance = this.processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processDefinitionKey(processDefinitionKey)
            .startedBy(userId)
            .unfinished()
            .singleResult();

        // Check completed instances
        HistoricProcessInstance completedInstance = this.processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processDefinitionKey(processDefinitionKey)
            .startedBy(userId)
            .finished()
            .singleResult();

        if (activeOrSuspendedInstance != null) {
            return activeOrSuspendedInstance.getId();
        }
        if (completedInstance != null) {
            return completedInstance.getId();
        }

        return null;
    }

    @Override
    public Page<HistoricTaskInstanceDTO> getHistoricTasksByUserIdAndCriteria(String userId, TaskCriteria criteria, Pageable pageable) {
        return this.taskQueryService.findHistoricTasksByCriteria(userId, criteria, pageable);
    }

    @Override
    public Page<TaskDTO> getActiveTasksByUserIdAndCriteria(TaskCriteria criteria, Pageable pageable) {
        log.debug("Request to get active tasks by user ");
        try {
            String userId = SecurityUtils.getUserIdFromCurrentUser();
            ResponseEntity<UserDTO> response = this.userService.getuser(UUID.fromString(userId));
            UserDTO user = response.getBody();

            return this.taskQueryService.findActiveTasksByCriteria(userId, user.getUsername(), criteria, pageable);
        } catch (Exception e) {
            log.error("Failed to get all active tasks for user id", e);
            throw new ProcessEngineException("Failed to get all active tasks for user id " , e);
        }
    }



    @Override
    public List<ProcessDefinitionDTO> getProcessByCategory(String categoryProcessParam) {
        RepositoryService repositoryService = this.processEngine.getRepositoryService();

        return repositoryService.createProcessDefinitionQuery()
            .active()
            .list()
            .stream()
            .filter(processDefinition -> {
                BpmnModelInstance modelInstance = repositoryService.getBpmnModelInstance(processDefinition.getId());

                if (modelInstance != null) {
                    Collection<org.camunda.bpm.model.bpmn.instance.Process> processes =
                        modelInstance.getModelElementsByType(org.camunda.bpm.model.bpmn.instance.Process.class);

                    for (org.camunda.bpm.model.bpmn.instance.Process process : processes) {
                        String categoryProcess = process.getAttributeValueNs(XML_BPMN, ELEMENT_PROCESS_NAME);
                        if (categoryProcessParam.equals(categoryProcess)) {
                            return true;
                        }
                    }
                }
                return false;
            })
            .map(processDefinition -> new ProcessDefinitionDTO(
                processDefinition.getId(),
                processDefinition.getKey(),
                processDefinition.getName(),
                processDefinition.getVersion(),
                processDefinition.getDeploymentId(),
                processDefinition.getResourceName(),
                !processDefinition.isSuspended(),
                processDefinition.isStartableInTasklist()
            ))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isUserAssignedToProcessInstance(String userId, String processInstanceId) {
        log.debug("Checking if user {} is assigned to process instance {}", userId,
            processInstanceId);
        if (userId == null || processInstanceId == null) {
            throw new IllegalArgumentException("Problem in user id or processInstance Id");
        }
        try {
            long activeTaskCount = this.processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(userId)
                .count();

            long historicTaskCount = this.processEngine.getHistoryService()
                .createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .taskAssignee(userId)
                .count();

            return activeTaskCount > 0 || historicTaskCount > 0;

        } catch (Exception e) {
            log.error("Failed to check user assignment for process instance", e);
            throw new ProcessEngineException(
                "Failed to check if user " + userId + " is assigned to process instance "
                    + processInstanceId, e);
        }
    }

    @Override
    public Page<HistoricProcessInstanceWithTaskDTO> getProcessInstancesByUserId(UUID userId, Pageable pageable, HistoricProcessInstanceTaskCriteria criteria) {
        userId = this.jwtUtils.getConnectedUserId();

        HistoricProcessInstanceQuery query = this.processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .startedBy(userId.toString());

        if (criteria.id() != null) {
            query.processInstanceId(criteria.id());
        }

        if (criteria.processDefinitionName() != null) {
            query.processDefinitionName(criteria.processDefinitionName());
        }

        if (criteria.startTime() != null) {
            query.startDateOn(criteria.startTime());
        }
        if (criteria.endTime() != null) {
            query.finishDateOn(criteria.endTime());
        }
        if (criteria.state() != null) {
            switch (criteria.state()) {
                case ACTIVE -> query.unfinished();
                case COMPLETED -> query.finished();
                case SUSPENDED -> query.suspended();
            }
        }

        List<HistoricProcessInstance> allHistoricInstances = query
            .orderByProcessInstanceStartTime()
            .desc()
            .list();

        List<HistoricProcessInstanceWithTaskDTO> allInstances = allHistoricInstances.stream().map(hpi -> {
            HistoricProcessInstanceDTO dto = HistoricProcessInstanceMapper.INSTANCE.toDto(hpi);

            List<Task> task = this.processEngine.getTaskService()
                .createTaskQuery()
                .processInstanceId(hpi.getId())
                .active()
                .list();

            String currentTaskName = task.isEmpty() ? null : task.get(0).getName();
            HistoricProcessInstanceWithTaskDTO result = new HistoricProcessInstanceWithTaskDTO();
            result.setInstance(dto);
            result.setCurrentTaskName(currentTaskName);

            return result;
        }).collect(Collectors.toList());

        int total = allInstances.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);

        if (start > end) {
            start = 0;
            end = Math.min(pageable.getPageSize(), total);
        }

        List<HistoricProcessInstanceWithTaskDTO> pagedContent = allInstances.subList(start, end);

        return new PageImpl<>(pagedContent, pageable, total);
    }

    public ProcessInstanceDTO startProcessByProcessInstanceId(String processInstanceId, Map<String, String> variables,String processDefinitionKey){

        try {
            String currentUserId = SecurityUtils.getUserIdFromCurrentUser();

            List<Task> activeTasks = processEngine.getTaskService()
                .createTaskQuery()
                .processDefinitionKey(processDefinitionKey)
                .taskAssignee(currentUserId)
                .active()
                .list();

            if (!activeTasks.isEmpty()) {
                throw new IllegalStateException("Cannot start new process: Active task already exists for this process instance");
            }
            HistoricProcessInstance historicInstance = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .singleResult();

            if (historicInstance == null) {
                throw new IllegalArgumentException("Aucune instance terminée trouvée avec cet ID: " );
            }

            String processDefinitionId = historicInstance.getProcessDefinitionId();

            List<HistoricVariableInstance> historicVariables = processEngine.getHistoryService()
                .createHistoricVariableInstanceQuery()
               .processInstanceId(processInstanceId)
                .list();

            Map<String, Object> processVariables = new HashMap<>();
            for (HistoricVariableInstance var : historicVariables) {
                processVariables.put(var.getVariableName(), var.getValue());
            }

            if (variables != null) {
                processVariables.putAll(new HashMap<>(variables));
            }

            processEngine.getIdentityService().setAuthenticatedUserId(SecurityUtils.getIDCurrentUser());

            String businessKey = generateBusinessKey(processDefinitionId, false);

            ProcessInstance newInstance = processEngine.getRuntimeService()
                .startProcessInstanceById(processDefinitionId, businessKey, processVariables);

            HistoricProcessInstance newHistoricInstance = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(newInstance.getRootProcessInstanceId())
                .singleResult();

            ProcessInstanceDTO dto = processInstanceMapper.toDto(newInstance);
            dto.setStartUserId(newHistoricInstance.getStartUserId());

            return dto;

        } catch (Exception e) {
            throw new ProcessEngineException("Erreur lors du redémarrage du process à partir de l'instance: " ,e);
        }
    }
    @Override
    public Optional<String> getLastFinishedProcessInstanceIdByUserId(String userId , String processDefinitionKey) {
        List<HistoricTaskInstance> tasks = processEngine
            .getHistoryService()
            .createHistoricTaskInstanceQuery()
            .processDefinitionKey(processDefinitionKey)
            .taskAssignee(userId)
            .finished()
            .list();

        HistoricTaskInstance historicInstance = tasks.stream()
            .filter(t -> t.getEndTime() != null)
            .max(Comparator.comparing(HistoricTaskInstance::getEndTime))
            .orElse(null);
        return historicInstance != null ? Optional.of(historicInstance.getProcessInstanceId()) : Optional.empty();
    }

}
