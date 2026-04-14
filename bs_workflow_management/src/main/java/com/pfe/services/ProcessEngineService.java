package com.pfe.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pfe.domain.AbstractTask;
import com.pfe.dto.*;
import com.pfe.dto.HistoricProcessInstanceWithTaskDTO;
import com.pfe.services.criteria.HistoricProcessInstanceTaskCriteria;
import com.pfe.services.criteria.ProcessInstanceCriteria;
import com.pfe.services.criteria.TaskCriteria;
import com.pfe.dto.AbstractProcessInstance;
import com.pfe.dto.HistoricTaskInstanceDTO;
import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.dto.ProcessInstanceDTO;
import com.pfe.dto.TaskDTO;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.externaltask.LockedExternalTask;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.camunda.bpm.engine.runtime.Job;
import org.camunda.bpm.engine.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ProcessEngineService {
    /*--------------------Process mgmt----------------*/
    ProcessInstanceDTO startProcessByKey(String processKey, Map<String, String> variables);

    ProcessInstanceDTO startProcessById(String processId, Map<String, String> variables);

    void signalExecution(String executionId);

    String getProcessXmlById(String processDefinitionId);

    boolean toggleProcessDefinitionState(String processDefinitionId);

    List<AbstractProcessInstance> getProcessInstances(String processDefinitionId);

    Page<AbstractProcessInstance> getProcessInstancesByCriteria(ProcessInstanceCriteria criteria, String processDefinitionId, Pageable pageable);

    List<AbstractProcessInstance> getCurrentUserProcessInstances(String processDefinitionId);

    List<AbstractProcessInstance> getCurrentUserProcessInstancesByKey(String processKey);

    List<AbstractProcessInstance> getCurrentUserProcessInstancesByUserId();

    /*--------------------task mgmt---------------------*/
    List<Task> getTasksByAssignee(String assignee);

    void completeTask(String taskId);

    TaskDTO getCurrentTaskByProcessInstanceId(String processInstanceId);

    TaskDTO getCurrentTaskById(String id);

    TaskDTO getCurrentTaskByAssignee(String processInstanceId, String assignee);

    /*                  external tasks mgmt                 */
    List<LockedExternalTask> fetchAndLockTasks(String workerId, String topicName, int maxTasks);

    void completeExternalTask(String taskId, String workerId);

    List<AbstractTask> getTasksByUserId(String userId);

    List<TaskDTO> getTasksByInstanceId(String processInstanceId);

    List<TaskDTO> getTasksByOwner(String owner);

    TaskDTO validateAndMoveToNextTask(String taskId, Map<String, Object> variables);

    /*                  user and group mgmt             */

    void createUser(String userId, String firstName, String lastName, String email);

    User findUserById(String userId);

    void createGroup(String groupId, String groupName);

    void addUserToGroup(String userId, String groupId);

    /*          repository mgmt            */

    Deployment deployProcessDefinition(String name, String xml);

    void deleteDeployment(String deploymentId);

    long getProcessDefinitionCount();

    /*          history mgmt           */

    List<HistoricProcessInstance> getCompletedProcessInstances(String processDefinitionKey);

    HistoricProcessInstance getProcessInstanceHistory(String processInstanceId);

    long getHistoricTaskInstanceCount();

    List<HistoricTaskInstanceDTO> getHistoricTaskInstancesByProcessInstanceId(String processInstanceId);

    List<HistoricTaskInstance> getHistoricTaskInstancesByAssignee(String assignee);

    /*           authorization mgmt                */
    void createAuthorization(String userId, String resourceId, Resources resourceType, Permission permission);

    Authorization getAuthorization(String authorizationId);

    void deleteAuthorization(String authorizationId);

    /*              job mgmt             */
    List<Job> getAllJobs();

    void executeJob(String jobId);

    void deleteJob(String jobId);

    long getJobCount();

    /*                    case mgmt                 */
    CaseInstance createCaseInstance(String caseDefinitionKey);

    void manuallyStartCaseExecution(String caseExecutionId);

    void completeCaseExecution(String caseExecutionId);

    /*              form mgmt             */
    TaskFormData getTaskFormData(String taskId);

    void submitTaskForm(String taskId, Map<String, Object> variables);

    TaskDTO getCurrentTaskByUserId(String processInstanceId, String userId);

    Page<AbstractTask> getHistoricTasksByUserId(String userId, Pageable pageable);

    Page<AbstractTask> getActiveTasksByUserId(Pageable pageable);

    List<ObjectNode> getProcessVariablesByProcessInstanceId(String processInstanceId);

    ProcessInstanceDTO startProcessOnceByKey(String processKey, Map<String, String> variables);

    ProcessInstanceDTO startProcessOnceByKeyOrGetStartedProcessInstance(String processKey,
        Map<String, String> variables);

    Page<HistoricTaskInstanceDTO> getHistoricTasksByUserIdAndCriteria(String userId, TaskCriteria criteria, Pageable pageable);

    Page<TaskDTO> getActiveTasksByUserIdAndCriteria(TaskCriteria criteria, Pageable pageable);

    List<ProcessDefinitionDTO> getProcessByCategory(String categoryProcessParam);

    boolean isUserAssignedToProcessInstance(String userId, String processInstanceId);
   ProcessInstanceDTO startUniqueProcessInstanceByKey(String processKey, Map<String, String> variables) ;

    Page<HistoricProcessInstanceWithTaskDTO> getProcessInstancesByUserId(UUID userId, Pageable pageable, HistoricProcessInstanceTaskCriteria criteria);
    ProcessInstanceDTO startProcessByProcessInstanceId(String processInstanceId, Map<String, String> variables, String processDefinitionKey);
     Optional<String> getLastFinishedProcessInstanceIdByUserId(String userId , String processDefinitionKey) ;

}
