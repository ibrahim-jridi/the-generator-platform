package com.pfe.services;

import com.pfe.config.Constants;
import com.pfe.dto.HistoricTaskInstanceDTO;
import com.pfe.dto.TaskDTO;
import com.pfe.feignServices.UserService;
import com.pfe.mapper.HistoricTaskInstanceMapper;
import com.pfe.mapper.TaskMapper;
import com.pfe.services.criteria.TaskCriteria;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TaskQueryService {
    private static final Logger log = LoggerFactory.getLogger(TaskQueryService.class);

    private final ProcessEngine processEngine;

    private final HistoricTaskInstanceMapper historicTaskInstanceMapper;

    private final TaskMapper taskMapper;
    private final UserService userService;


    public TaskQueryService(ProcessEngine processEngine, HistoricTaskInstanceMapper historicTaskInstanceMapper, TaskMapper taskMapper, UserService userService) {
        this.processEngine = processEngine;
        this.historicTaskInstanceMapper = historicTaskInstanceMapper;
        this.taskMapper = taskMapper;
        this.userService = userService;
    }


    @Transactional(readOnly = true)
    public Page<HistoricTaskInstanceDTO> findHistoricTasksByCriteria(String userId, TaskCriteria criteria, Pageable pageable){
        log.debug("find by criteria : {}, page: {}", criteria, pageable);
        HistoricTaskInstanceQuery query= this.processEngine.getHistoryService()
            .createHistoricTaskInstanceQuery()
            .taskAssignee(userId)
            .finished();
        if (criteria.id()!=null){
            query.taskId(criteria.id());
        }
        if (criteria.startTime()!=null){
            query.startedAfter(criteria.startTime());
            query.startedBefore(new Date(criteria.startTime().getTime() + 24 * 60 * 60 * 1000 - 1));
        }
        if (criteria.name()!=null){
            query.taskNameLike(Constants.PERCENTAGE + criteria.name() + Constants.PERCENTAGE);
        }
        if (criteria.endTime()!=null){
            query.finishedAfter(criteria.endTime());
            query.finishedBefore(new Date(criteria.endTime().getTime() + 24 * 60 * 60 * 1000 - 1));
        }
        if (criteria.businessKey() != null) {
            query.processInstanceBusinessKeyLike(Constants.PERCENTAGE + criteria.businessKey() + Constants.PERCENTAGE);
        }

        query.orderByHistoricTaskInstanceEndTime().desc();
        long total = query.count();
        if(pageable.getOffset() >= total){
            pageable= PageRequest.of(0,pageable.getPageSize());
        }
        List<HistoricTaskInstance> historicTaskInstances=query.listPage((int) pageable.getOffset(), pageable.getPageSize());
        List<HistoricTaskInstanceDTO> historicTaskInstanceDTOs = historicTaskInstances.stream().map(historicTask -> {
            HistoricTaskInstanceDTO historicTaskDTO = historicTaskInstanceMapper.toDto(historicTask);
            String businessKey = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(historicTask.getProcessInstanceId())
                .singleResult()
                .getBusinessKey();
            String startedUserId = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processInstanceId(historicTask.getProcessInstanceId())
                .singleResult().getStartUserId();
            historicTaskDTO.setBusinessKey(businessKey);
            String ownerName = "Pas de user";
            if (startedUserId != null) {
                try {
                     ownerName = userService.findByKeycloakId(UUID.fromString(startedUserId))
                        .map(user -> user.getFirstName() + " " + user.getLastName())
                        .orElse("Pas de user");

                     if(ownerName.equalsIgnoreCase("Pas de user")){

                         ownerName = userService.getOptionalUser(UUID.fromString(startedUserId))
                             .map(user -> user.getFirstName() + " " + user.getLastName())
                             .orElse("Pas de user");
                     }

                } catch (Exception e) {

                    log.warn("Impossible de récupérer l'utilisateur avec l'ID {}", startedUserId, e);
                }
            }

            historicTaskDTO.setOwner(ownerName);
            return historicTaskDTO;
        }).toList();
        return new PageImpl<>(historicTaskInstanceDTOs, pageable, total);
    }

    @Transactional(readOnly = true)
    public Page<TaskDTO> findActiveTasksByCriteria(String userId, String username, TaskCriteria criteria, Pageable pageable) {
        log.debug("find by criteria : {}, page: {}", criteria, pageable);
        TaskQuery query= processEngine.getTaskService()
            .createTaskQuery()
            .orderByTaskCreateTime()
            .desc()
            .taskAssigneeIn(userId, username)
            .initializeFormKeys();
        if (criteria.id()!=null){
            query.taskId(criteria.id());
        }
        if (criteria.startTime()!=null){
            query.taskCreatedAfter(criteria.startTime());
            query.taskCreatedBefore(new Date(criteria.startTime().getTime() + 24 * 60 * 60 * 1000 - 1));
        }
        if (criteria.name()!=null){
            query.taskNameLike(Constants.PERCENTAGE + criteria.name() + Constants.PERCENTAGE);
        }
        if (criteria.businessKey() != null) {
            query.processInstanceBusinessKeyLike(Constants.PERCENTAGE + criteria.businessKey() + Constants.PERCENTAGE);
        }

        long total = query.count();
        if(pageable.getOffset() >= total){
            pageable= PageRequest.of(0,pageable.getPageSize());
        }
        List<Task> activeTasks=query.listPage((int) pageable.getOffset(), pageable.getPageSize());
        List<TaskDTO> activeTaskDTOs = activeTasks.stream().map(task -> {
            TaskDTO taskDTO = taskMapper.toDto(task);
            String businessKey = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .singleResult()
                .getBusinessKey();
            taskDTO.setBusinessKey(businessKey);
            return taskDTO;
        }).toList();
        return new PageImpl<>(activeTaskDTOs, pageable, total);
    }

}
