package com.pfe.web.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pfe.domain.AbstractTask;
import com.pfe.dto.*;
import com.pfe.dto.HistoricProcessInstanceWithTaskDTO;
import com.pfe.dto.request.StartProcessRequestDTO;
import com.pfe.mapper.DeployMapper;
import com.pfe.services.IDmnService;
import com.pfe.services.ProcessEngineService;
import com.pfe.services.ProcessInstanceService;
import com.pfe.services.RepositoryService;
import com.pfe.services.criteria.*;
import com.pfe.dto.AbstractProcessInstance;
import com.pfe.dto.DeployDTO;
import com.pfe.dto.DmnActivityDecisionDefinitionDTO;
import com.pfe.dto.HistoricTaskInstanceDTO;
import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.dto.ProcessInstanceDTO;
import com.pfe.dto.TaskDTO;
import com.pfe.dto.XmlFileDto;
import com.pfe.services.criteria.DecisionDefinitionCriteria;
import com.pfe.services.criteria.HistoricProcessInstanceTaskCriteria;
import com.pfe.services.criteria.ProcessDefinitionCriteria;
import com.pfe.services.criteria.ProcessInstanceCriteria;
import com.pfe.services.criteria.TaskCriteria;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/camunda")
public class CamundaController {
    private static final Logger log = LoggerFactory.getLogger(CamundaController.class);


    private final RepositoryService repositoryService;
    private final ProcessEngine processEngine;
    private final ProcessInstanceService processInstanceService;
    private final ProcessEngineService processEngineService;
    private final IDmnService dmnService;

    public CamundaController(ProcessEngine processEngine, RepositoryService repositoryService, ProcessInstanceService processInstanceService,
                             ProcessEngineService processEngineService, IDmnService dmnService) {
        this.processEngine = processEngine;
        this.processInstanceService = processInstanceService;
        this.repositoryService = repositoryService;
        this.processEngineService = processEngineService;
        this.dmnService = dmnService;
    }

    // --- Process Management ---

    @GetMapping("/list-process")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Page<ProcessDefinitionDTO>> getDeployedProcesses(Pageable pageable) {
        Page<ProcessDefinitionDTO> page = this.repositoryService.getDeployedProcesses(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @PostMapping("/start/{processKey}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ProcessInstanceDTO> startProcess(@PathVariable String processKey, @RequestBody Map<String, String> variables) {
        ProcessInstanceDTO processInstanceDTO = this.processEngineService.startProcessByKey(processKey, variables);
        return ResponseEntity.ok(processInstanceDTO);
    }

    @PostMapping("/startById/{processId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ProcessInstanceDTO> startProcessById(@PathVariable String processId, @RequestBody Map<String, String> variables) {
        ProcessInstanceDTO processInstanceDTO = this.processEngineService.startProcessById(processId, variables);
        return ResponseEntity.ok(processInstanceDTO);
    }

    @GetMapping("/xml/{processDefinitionId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<String> getProcessXmlById(@PathVariable String processDefinitionId) {
        String xml = this.processEngineService.getProcessXmlById(processDefinitionId);
        return ResponseEntity.ok(xml);
    }

    @PostMapping("/toggle-state/{processDefinitionId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public boolean toggleProcessDefinitionState(@PathVariable String processDefinitionId) {
        return this.processEngineService.toggleProcessDefinitionState(processDefinitionId);

    }

    @GetMapping("/process-instances/{processDefinitionId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<AbstractProcessInstance>> getProcessInstances(@PathVariable String processDefinitionId) {
        List<AbstractProcessInstance> instances = this.processEngineService.getProcessInstances(processDefinitionId);
        return ResponseEntity.ok(instances);
    }


    @GetMapping("/process-instances-currentUser-byKey/{processKey}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<AbstractProcessInstance>> getCurrentUserProcessInstancesByKey(@PathVariable String processKey) {
        List<AbstractProcessInstance> instances = this.processEngineService.getCurrentUserProcessInstancesByKey(processKey);
        return ResponseEntity.ok(instances);
    }


    @GetMapping("/current-task/{processInstanceId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<TaskDTO> getCurrentTask(@PathVariable String processInstanceId) {
        log.info("getCurrentTask processInstanceId: {}", processInstanceId);
        TaskDTO currentTask = this.processEngineService.getCurrentTaskByProcessInstanceId(
            processInstanceId);
        return ResponseEntity.ok(currentTask);
    }

    @GetMapping("/current-task/by-id/{id}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable String id) {
        log.info("getTaskById: {}", id);
        TaskDTO currentTask = this.processEngineService.getCurrentTaskById(id);
        return ResponseEntity.ok(currentTask);
    }

    @GetMapping("/tasks-by-instance/{processInstanceId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<TaskDTO>> getTasksByInstanceId(@PathVariable String processInstanceId) {
        try {
            List<TaskDTO> tasks = this.processEngineService.getTasksByInstanceId(processInstanceId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
        }
    }


    @PostMapping("/validate-task/{taskId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<TaskDTO> validateAndMoveToNextTask(
        @PathVariable String taskId,
        @RequestBody Map<String, Object> variables) {
        TaskDTO taskDTO = null;
        try {
            taskDTO = this.processEngineService.validateAndMoveToNextTask(taskId, variables);
            return ResponseEntity.ok(taskDTO);
        } catch (ProcessEngineException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(taskDTO);
        }
    }

    @GetMapping("/historic-tasks-by-user/{userId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<AbstractTask>> getHistoricTasksByUserId(@PathVariable String userId, Pageable pageable) {
        log.info("REST request to get historic tasks by userId: {}", userId);

        Page<AbstractTask> tasks = this.processEngineService.getHistoricTasksByUserId(userId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), tasks);

        log.info("Found {} historicTasks ", tasks.getTotalElements());
        return ResponseEntity.ok().headers(headers).body(tasks.getContent());
    }

    @GetMapping("/active-tasks-by-user")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Page<AbstractTask>> getActiveTasksByUserId(Pageable pageable) {
        log.info("REST request to get active tasks by user");

        Page<AbstractTask> tasks = this.processEngineService.getActiveTasksByUserId(pageable);
        log.info("Found {} active tasks matching the criteria", tasks.getTotalElements());

        return ResponseEntity.ok(tasks);
    }

    // --- Repository Management ---

    @PostMapping("/repository/deploy")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<DeployDTO> deployProcessDefinition(@RequestBody Map<String, String> processData) {
        try {
            if (processData == null) {
                throw new IllegalArgumentException("processData cannot be null or empty");

            }
            String name = processData.get("name");
            String xml = processData.get("xml");
            Deployment deployment = this.processEngineService.deployProcessDefinition(name, xml);
            DeployDTO deployDto = DeployMapper.INSTANCE.toDto(deployment);
            return ResponseEntity.ok(deployDto);
        } catch (Exception e) {
            log.error("Error creating the process", e);
            throw e;
        }
    }

    //Deploy dmn
    @PostMapping("/repository/deploy-dmn")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<DeployDTO> deployDecisionDefinition(@RequestBody XmlFileDto dmnData) {
        log.info("Received request to deploy DMN with name: {}", dmnData);
        DeployDTO deployDto = this.dmnService.deployDecisionDefinition(dmnData);
        return ResponseEntity.ok(deployDto);
    }


    @GetMapping("/history/tasks/{processInstanceId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<HistoricTaskInstanceDTO>> getHistoricTasksByProcessInstanceId(@PathVariable String processInstanceId) {
        log.debug("REST API to get all historic tasks by process instance Id: {}", processInstanceId);

        List<HistoricTaskInstanceDTO> historicTasks = this.processEngineService.getHistoricTaskInstancesByProcessInstanceId(processInstanceId);
        return ResponseEntity.ok(historicTasks);
    }


    @GetMapping("/current-task-by-userId/{processInstanceId}/{userId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<TaskDTO> getCurrentTask(@PathVariable("processInstanceId") String processInstanceId,
                                                  @PathVariable("userId") String userId) {
        TaskDTO currentTask = this.processEngineService.getCurrentTaskByUserId(processInstanceId, userId);
        return ResponseEntity.ok(currentTask);
    }

    @GetMapping("/fetch-all-decision-definition")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<DmnActivityDecisionDefinitionDTO>> getAllDecisionDefinition() {
        log.info("Request to fetch all dmn decision definition");
        return ResponseEntity.ok(this.dmnService.findAllDmmActivityDecisionDefinition());
    }

    @GetMapping("/fetch-decision-definition-by-id/{id}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<String> getDecisionDefinitionById(@PathVariable("id") String id) {
        log.info("Request to fetch dmn decision definition by id = {} ", id);
        return ResponseEntity.ok(this.dmnService.findDmmActivityDecisionDefinitionById(id));
    }

    @GetMapping("/process/variables")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<ObjectNode>> getProcessVariablesByProcessInstanceId(
        @RequestParam String processInstanceId) {
        log.info("Request to get all variables from process instance by process instance id = {}",
            processInstanceId);
        return ResponseEntity.ok(
            this.processEngineService.getProcessVariablesByProcessInstanceId(processInstanceId));
    }

    @PostMapping("/start-process-once/{processKey}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ProcessInstanceDTO> startProcessOnceByKey(@PathVariable String processKey,
                                                                    @RequestBody Map<String, String> variables) {
        ProcessInstanceDTO processInstanceDTO = this.processEngineService.startProcessOnceByKey(
            processKey,
            variables);
        return ResponseEntity.ok(processInstanceDTO);
    }

    @PostMapping("/start-process-once-and-get-started-instance/{processKey}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ProcessInstanceDTO> startProcessOnceByKeyAndGetStartedInstance(
        @PathVariable String processKey,
        @RequestBody Map<String, String> variables) {
        ProcessInstanceDTO processInstanceDTO = this.processEngineService.startProcessOnceByKeyOrGetStartedProcessInstance(
            processKey,
            variables);
        return ResponseEntity.ok(processInstanceDTO);
    }

    @PostMapping("/list-process")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Page<ProcessDefinitionDTO>> getDeployedProcessesByCriteria(Pageable pageable, @RequestBody ProcessDefinitionCriteria criteria) {
        Page<ProcessDefinitionDTO> page = this.repositoryService.getDeployedProcessesByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page);
    }

    @PostMapping("/process-instances/{processDefinitionId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Page<AbstractProcessInstance>> getProcessInstancesByCriteria(Pageable pageable, @RequestBody ProcessInstanceCriteria criteria, @PathVariable String processDefinitionId) {
        Page<AbstractProcessInstance> instances = this.processEngineService.getProcessInstancesByCriteria(criteria, processDefinitionId, pageable);
        return ResponseEntity.ok(instances);
    }

    @PostMapping("/fetch-all-decision-definition")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Page<DmnActivityDecisionDefinitionDTO>> getAllDecisionDefinitionByCriteria(Pageable pageable, @RequestBody DecisionDefinitionCriteria criteria) {
        log.info("Request to fetch all dmn decision definition");
        return ResponseEntity.ok(this.dmnService.findAllDmmActivityDecisionDefinitionByCriteria(criteria, pageable));
    }

    @PostMapping("/historic-tasks-by-user/{userId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Page<HistoricTaskInstanceDTO>> getHistoricTasksByUserIdAndCriteria(@PathVariable String userId, @RequestBody TaskCriteria criteria, Pageable pageable) {
        log.info("REST request to get historic tasks by userId: {} and criteria: {}", userId, criteria);

        Page<HistoricTaskInstanceDTO> tasks = this.processEngineService.getHistoricTasksByUserIdAndCriteria(userId, criteria, pageable);
        log.info("Found {} historicTasks ", tasks.getTotalElements());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/process-definition/{id}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ProcessDefinitionDTO> getDeployedProcessById(@PathVariable String id) {
        log.info("REST request to get process definition by id: {}", id);
        ProcessDefinitionDTO processDefinition = this.repositoryService.getDeployedProcessById(id);
        return ResponseEntity.ok(processDefinition);
    }

    @PostMapping("/active-tasks-by-user")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Page<TaskDTO>> getActiveTasksByUserIdAndCriteria(@RequestBody TaskCriteria criteria, Pageable pageable) {
        log.info("REST request to get active tasks by user and criteria {}", criteria);

        Page<TaskDTO> tasks = this.processEngineService.getActiveTasksByUserIdAndCriteria(criteria, pageable);

        log.info("Found {} active tasks matching the criteria", tasks.getTotalElements());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/process-by-category")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<List<ProcessDefinitionDTO>> getProcessByCategory(@RequestParam String category) {
        List<ProcessDefinitionDTO> processDefinitionDTOS = this.processEngineService.getProcessByCategory(category);
        return ResponseEntity.ok(processDefinitionDTOS);
    }

    @GetMapping("/check-user-assignment/{processInstanceId}/{userId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or hasRole(\""+ AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<Boolean> isUserAssignedToProcess(
        @PathVariable String processInstanceId,
        @PathVariable String userId
    ) {
        log.debug("REST request to check if user {} is assigned to process instance {}", userId,
            processInstanceId);
        boolean isAssigned = this.processEngineService.isUserAssignedToProcessInstance(userId,
            processInstanceId);
        return ResponseEntity.ok(isAssigned);
    }

    @PostMapping("/start-unique-process/{processKey}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<ProcessInstanceDTO> startUniqueProcessInstanceByKey(@PathVariable String processKey,
                                                                              @RequestBody Map<String, String> variables) {
        ProcessInstanceDTO processInstanceDTO = this.processEngineService.startUniqueProcessInstanceByKey(
            processKey,
            variables);
        return ResponseEntity.ok(processInstanceDTO);
    }

    @PostMapping("/process-instances-userId/{userId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_TRACK_REQUESTS + "\")")
    public ResponseEntity<Page<HistoricProcessInstanceWithTaskDTO>> getProcessInstancesByUserId(@PathVariable UUID userId, Pageable pageable, @RequestBody HistoricProcessInstanceTaskCriteria criteria) {
        Page<HistoricProcessInstanceWithTaskDTO> instances = this.processEngineService.getProcessInstancesByUserId(userId, pageable, criteria);
        return ResponseEntity.ok(instances);
    }

    @PostMapping("/start")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")
    public ResponseEntity<?> startProcessByVariable(@RequestBody StartProcessRequestDTO request) {
        return ResponseEntity.ok(
            processEngineService.startProcessByProcessInstanceId(
                request.getProcessInstanceId(),
                request.getVariables(),
                request.getProcessDefinitionKey()
            )
        );
    }


    @GetMapping(value = "/process-instance/{userId}")
    //@PreAuthorize("hasRole(\"" + AuthoritiesConstants.BS_USER + "\") or  hasRole(\"" + AuthoritiesConstants.BS_ADMIN + "\")")

    public ResponseEntity<HistoricTaskInstanceDTO> getProcessInstanceByUserId(@PathVariable String userId, @RequestParam String processDefinitionKey ) {

        HistoricTaskInstanceDTO dto = new HistoricTaskInstanceDTO();
        processEngineService.getLastFinishedProcessInstanceIdByUserId(userId, processDefinitionKey)
            .ifPresent(dto::setProcessInstanceId);

        return ResponseEntity.ok(dto);
    }
}



