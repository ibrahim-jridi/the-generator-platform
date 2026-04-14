package com.pfe.dto;


import com.pfe.domain.AbstractTask;
import com.pfe.domain.enumeration.TaskStatus;

import java.time.ZonedDateTime;

public class TaskDTO extends AbstractTask {
    public TaskDTO() {
    }

    public TaskDTO(String id, String processInstanceId, String executionId, String processDefinitionId, String parentTaskId, String taskDefinitionKey, String owner, String assignee, String description, int priority, String name, FormDTO form, TaskStatus status, int revision, String delegationState, ZonedDateTime createTime, ZonedDateTime dueZonedDateTime, String category, int suspensionState, String tenantId, String formKey, UserDTO userDTO, String businessKey) {
        super(id, processInstanceId, executionId, processDefinitionId, parentTaskId, taskDefinitionKey, owner, assignee, description, priority, name, form, status, businessKey);
        this.revision = revision;
        this.delegationState = delegationState;
        this.createTime = createTime;
        this.dueZonedDateTime = dueZonedDateTime;
        this.category = category;
        this.suspensionState = suspensionState;
        this.tenantId = tenantId;
        this.formKey = formKey;
        this.userDTO = userDTO;
    }

    private int revision;
    private String delegationState;
    private ZonedDateTime createTime;
    private ZonedDateTime dueZonedDateTime;
    private String category;
    private int suspensionState;
    private String tenantId;
    private String formKey;
    private UserDTO userDTO;


    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public int getRevision() {
        return revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public String getDelegationState() {
        return delegationState;
    }

    public void setDelegationState(String delegationState) {
        this.delegationState = delegationState;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getDueZonedDateTime() {
        return dueZonedDateTime;
    }

    public void setDueZonedDateTime(ZonedDateTime dueZonedDateTime) {
        this.dueZonedDateTime = dueZonedDateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSuspensionState() {
        return suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFormKey() {
        return formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }
}
