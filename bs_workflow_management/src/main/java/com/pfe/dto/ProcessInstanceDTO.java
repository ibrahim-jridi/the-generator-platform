package com.pfe.dto;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Objects;

public class ProcessInstanceDTO extends AbstractProcessInstance {
    private String startActivityId;
    private String endActivityId;
    private String superProcessInstanceId;
    Map<String, Object> retrievedVariables;
    private String startUserId;
    private String businessKey;


    public ProcessInstanceDTO() {
    }

    public ProcessInstanceDTO(String id, String processInstanceId, String businessKey,
        String processDefinitionId, ZonedDateTime startTime, ZonedDateTime endTime, Long duration,
        String deleteReason, String tenantId, String state, String startActivityId,
        String endActivityId, String superProcessInstanceId, Map<String, Object> retrievedVariables,
        String startUserId, String processDefinitionName) {
        super(id, processInstanceId, businessKey, processDefinitionId, startTime, endTime, duration,
            deleteReason, tenantId, state, processDefinitionName);
        this.startActivityId = startActivityId;
        this.endActivityId = endActivityId;
        this.superProcessInstanceId = superProcessInstanceId;
        this.retrievedVariables = retrievedVariables;
        this.startUserId = startUserId;
    }

    public String getStartActivityId() {
        return startActivityId;
    }

    public void setStartActivityId(String startActivityId) {
        this.startActivityId = startActivityId;
    }

    public String getEndActivityId() {
        return endActivityId;
    }

    public void setEndActivityId(String endActivityId) {
        this.endActivityId = endActivityId;
    }

    public String getSuperProcessInstanceId() {
        return superProcessInstanceId;
    }

    public void setSuperProcessInstanceId(String superProcessInstanceId) {
        this.superProcessInstanceId = superProcessInstanceId;
    }

    public Map<String, Object> getRetrievedVariables() {
        return retrievedVariables;
    }

    public void setRetrievedVariables(Map<String, Object> retrievedVariables) {
        this.retrievedVariables = retrievedVariables;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    @Override
    public String getBusinessKey() {
        return businessKey;
    }

    @Override
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProcessInstanceDTO that = (ProcessInstanceDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
