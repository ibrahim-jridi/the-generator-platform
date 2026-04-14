package com.pfe.dto;

import com.pfe.domain.AbstractTask;
import com.pfe.domain.enumeration.TaskStatus;

import java.time.ZonedDateTime;
import java.util.List;

public class HistoricTaskInstanceDTO extends AbstractTask {
    public HistoricTaskInstanceDTO() {
    }

    public HistoricTaskInstanceDTO(String id, String processInstanceId, String executionId, String processDefinitionId, String parentTaskId, String taskDefinitionKey, String owner, String assignee, String description, int priority, String name, FormDTO form, TaskStatus status, String rootProcessInstanceId, String processDefinitionKey, String processDefinitionName, Integer processDefinitionVersion, String caseInstanceId, String caseExecutionId, String caseDefinitionId, String caseDefinitionKey, String caseDefinitionName, String eventType, Integer sequenceCounter, ZonedDateTime startTime, ZonedDateTime endTime, Long durationInMillis, String taskId, ZonedDateTime dueDate, List<SubmissionDTO> submissionDTO, UserDTO userDTO, String businessKey) {
        super(id, processInstanceId, executionId, processDefinitionId, parentTaskId, taskDefinitionKey, owner, assignee, description, priority, name, form, status, businessKey);
        this.rootProcessInstanceId = rootProcessInstanceId;
        this.processDefinitionKey = processDefinitionKey;
        this.processDefinitionName = processDefinitionName;
        this.processDefinitionVersion = processDefinitionVersion;
        this.caseInstanceId = caseInstanceId;
        this.caseExecutionId = caseExecutionId;
        this.caseDefinitionId = caseDefinitionId;
        this.caseDefinitionKey = caseDefinitionKey;
        this.caseDefinitionName = caseDefinitionName;
        this.eventType = eventType;
        this.sequenceCounter = sequenceCounter;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationInMillis = durationInMillis;
        this.taskId = taskId;
        this.dueDate = dueDate;
        this.submissionDTO = submissionDTO;
        this.userDTO = userDTO;
    }

    private String rootProcessInstanceId;
    private String processDefinitionKey;
    private String processDefinitionName;
    private Integer processDefinitionVersion;
    private String caseInstanceId;
    private String caseExecutionId;
    private String caseDefinitionId;
    private String caseDefinitionKey;
    private String caseDefinitionName;
    private String eventType;
    private Integer sequenceCounter;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private Long durationInMillis;
    private String taskId;
    private ZonedDateTime dueDate;
    private List<SubmissionDTO> submissionDTO;
    private UserDTO userDTO;

    public String getRootProcessInstanceId() {
        return rootProcessInstanceId;
    }

    public void setRootProcessInstanceId(String rootProcessInstanceId) {
        this.rootProcessInstanceId = rootProcessInstanceId;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
        this.processDefinitionKey = processDefinitionKey;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getCaseInstanceId() {
        return caseInstanceId;
    }

    public void setCaseInstanceId(String caseInstanceId) {
        this.caseInstanceId = caseInstanceId;
    }

    public String getCaseExecutionId() {
        return caseExecutionId;
    }

    public void setCaseExecutionId(String caseExecutionId) {
        this.caseExecutionId = caseExecutionId;
    }

    public String getCaseDefinitionId() {
        return caseDefinitionId;
    }

    public void setCaseDefinitionId(String caseDefinitionId) {
        this.caseDefinitionId = caseDefinitionId;
    }

    public String getCaseDefinitionKey() {
        return caseDefinitionKey;
    }

    public void setCaseDefinitionKey(String caseDefinitionKey) {
        this.caseDefinitionKey = caseDefinitionKey;
    }

    public String getCaseDefinitionName() {
        return caseDefinitionName;
    }

    public void setCaseDefinitionName(String caseDefinitionName) {
        this.caseDefinitionName = caseDefinitionName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getSequenceCounter() {
        return sequenceCounter;
    }

    public void setSequenceCounter(Integer sequenceCounter) {
        this.sequenceCounter = sequenceCounter;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getDurationInMillis() {
        return durationInMillis;
    }

    public void setDurationInMillis(Long durationInMillis) {
        this.durationInMillis = durationInMillis;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public ZonedDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(ZonedDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public List<SubmissionDTO> getSubmissionDTO() {
        return submissionDTO;
    }

    public void setSubmissionDTO(List<SubmissionDTO> submissionDTO) {
        this.submissionDTO = submissionDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
