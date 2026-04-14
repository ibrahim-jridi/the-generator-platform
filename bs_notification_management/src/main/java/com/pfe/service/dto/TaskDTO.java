package com.pfe.service.dto;


import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class TaskDTO implements Serializable {

    private static final long serialVersionUID = 3613743713477301063L;

    @Size(max = 50)
    private String id;
    @Size(max = 50)
    private String executionId;
    @Size(max = 50)
    private String processInstanceId;
    @Size(max = 50)
    private String processDefinitionId;
    @Size(max = 50)
    private String parentTaskId;
    @Size(max = 50)
    private String description;
    @Size(max = 50)
    private String taskDefinitionKey;
    @Size(max = 50)
    private String owner;
    @Size(min = 3, max = 50)
    private String assignee;
    @Size(max = 50)
    private String delegationState;
    @Size(max = 50)
    private String category;

    @Size(max = 50)
    private String tenantId;
    @Size(min = 3, max = 50)
    private String formKey;
    private int suspensionState;
    private int priority;
    private int revision;
    private UUID createdBy;
    private Instant createdDate;
    private UUID lastModifiedBy;
    private Instant lastModifiedDate;
    private int version;
    private Boolean deleted;
    private String name;

    public TaskDTO(String id, String executionId, String processInstanceId,
        String processDefinitionId,
        String parentTaskId, String description, String taskDefinitionKey, String owner,
        String assignee, String delegationState, String category, String tenantId, String formKey,
        int suspensionState, int priority, int revision, UUID createdBy, Instant createdDate,
        UUID lastModifiedBy, Instant lastModifiedDate, int version, Boolean deleted) {
        this.id = id;
        this.executionId = executionId;
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
        this.parentTaskId = parentTaskId;
        this.description = description;
        this.taskDefinitionKey = taskDefinitionKey;
        this.owner = owner;
        this.assignee = assignee;
        this.delegationState = delegationState;
        this.category = category;
        this.tenantId = tenantId;
        this.formKey = formKey;
        this.suspensionState = suspensionState;
        this.priority = priority;
        this.revision = revision;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.version = version;
        this.deleted = deleted;
    }

    public TaskDTO() {
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRevision() {
        return this.revision;
    }

    public void setRevision(int revision) {
        this.revision = revision;
    }

    public String getExecutionId() {
        return this.executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public String getProcessInstanceId() {
        return this.processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getProcessDefinitionId() {
        return this.processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
    }



    public String getParentTaskId() {
        return this.parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskDefinitionKey() {
        return this.taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAssignee() {
        return this.assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getDelegationState() {
        return this.delegationState;
    }

    public void setDelegationState(String delegationState) {
        this.delegationState = delegationState;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSuspensionState() {
        return this.suspensionState;
    }

    public void setSuspensionState(int suspensionState) {
        this.suspensionState = suspensionState;
    }

    public String getTenantId() {
        return this.tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getFormKey() {
        return this.formKey;
    }

    public void setFormKey(String formKey) {
        this.formKey = formKey;
    }

    public UUID getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public UUID getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(UUID lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TaskDTO that)) {
            return false;
        }
        return Objects.equals(this.id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
            "id='" + id + '\'' +
            ", executionId='" + executionId + '\'' +
            ", processInstanceId='" + processInstanceId + '\'' +
            ", processDefinitionId='" + processDefinitionId + '\'' +
            ", parentTaskId='" + parentTaskId + '\'' +
            ", description='" + description + '\'' +
            ", taskDefinitionKey='" + taskDefinitionKey + '\'' +
            ", owner='" + owner + '\'' +
            ", assignee='" + assignee + '\'' +
            ", delegationState='" + delegationState + '\'' +
            ", category='" + category + '\'' +
            ", tenantId='" + tenantId + '\'' +
            ", formKey='" + formKey + '\'' +
            ", suspensionState=" + suspensionState +
            ", priority=" + priority +
            ", revision=" + revision +
            ", createdBy=" + createdBy +
            ", createdDate=" + createdDate +
            ", lastModifiedBy=" + lastModifiedBy +
            ", lastModifiedDate=" + lastModifiedDate +
            ", version=" + version +
            ", deleted=" + deleted +
            ", name='" + name + '\'' +
            '}';
    }
}
