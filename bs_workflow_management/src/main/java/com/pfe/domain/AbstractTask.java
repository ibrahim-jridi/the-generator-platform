package com.pfe.domain;

import com.pfe.domain.enumeration.TaskStatus;
import com.pfe.dto.FormDTO;


public abstract class AbstractTask {

    private String id;
    private String processInstanceId;
    private String executionId;
    private String processDefinitionId;
    private String parentTaskId;
    private String taskDefinitionKey;
    private String owner;
    private String assignee;
    private String description;
    private int priority;
    private String name;
    private FormDTO form;
    private TaskStatus status;
    private String businessKey;


    public AbstractTask() {
    }

    public AbstractTask(String id, String processInstanceId, String executionId, String processDefinitionId, String parentTaskId, String taskDefinitionKey, String owner, String assignee, String description, int priority, String name, FormDTO form, TaskStatus status, String businessKey) {
        this.id = id;
        this.processInstanceId = processInstanceId;
        this.executionId = executionId;
        this.processDefinitionId = processDefinitionId;
        this.parentTaskId = parentTaskId;
        this.taskDefinitionKey = taskDefinitionKey;
        this.owner = owner;
        this.assignee = assignee;
        this.description = description;
        this.priority = priority;
        this.name = name;
        this.form = form;
        this.status = status;
        this.businessKey = businessKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FormDTO getForm() {
        return form;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
}

