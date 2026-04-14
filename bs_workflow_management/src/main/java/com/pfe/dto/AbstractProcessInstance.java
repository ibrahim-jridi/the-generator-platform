package com.pfe.dto;

import java.time.ZonedDateTime;

public abstract class AbstractProcessInstance {

    private String id;
    private String processInstanceId;
    private String businessKey;
    private String processDefinitionId;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private Long duration;
    private String deleteReason;
    private String tenantId;
    private String state;
    private String processDefinitionName;

    public AbstractProcessInstance() {
    }

    public AbstractProcessInstance(String id, String processInstanceId, String businessKey,
        String processDefinitionId, ZonedDateTime startTime, ZonedDateTime endTime, Long duration,
        String deleteReason, String tenantId, String state, String processDefinitionName) {
        this.id = id;
        this.processInstanceId = processInstanceId;
        this.businessKey = businessKey;
        this.processDefinitionId = processDefinitionId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
        this.deleteReason = deleteReason;
        this.tenantId = tenantId;
        this.state = state;
        this.processDefinitionName = processDefinitionName;
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

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
        this.processDefinitionId = processDefinitionId;
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

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }



    public String getDeleteReason() {
        return deleteReason;
    }

    public void setDeleteReason(String deleteReason) {
        this.deleteReason = deleteReason;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProcessDefinitionName() {
        return processDefinitionName;
    }

    public void setProcessDefinitionName(String processDefinitionName) {
        this.processDefinitionName = processDefinitionName;
    }

}
