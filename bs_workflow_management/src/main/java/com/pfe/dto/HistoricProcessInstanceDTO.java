package com.pfe.dto;

import java.time.ZonedDateTime;
import java.util.Objects;

public class HistoricProcessInstanceDTO extends AbstractProcessInstance {

    private Integer processDefinitionVersion;
    private String startUserId;


    public HistoricProcessInstanceDTO() {
    }

    public HistoricProcessInstanceDTO(String id, String processInstanceId, String businessKey, String processDefinitionId, ZonedDateTime startTime, ZonedDateTime endTime, Long duration, String deleteReason, String tenantId, String state, Integer processDefinitionVersion, String processDefinitionName, String startUserId) {
        super(id, processInstanceId, businessKey, processDefinitionId, startTime, endTime, duration,
            deleteReason, tenantId, state, processDefinitionName);
        this.processDefinitionVersion = processDefinitionVersion;
        this.startUserId = startUserId;
    }

    public Integer getProcessDefinitionVersion() {
        return processDefinitionVersion;
    }

    public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
        this.processDefinitionVersion = processDefinitionVersion;
    }

    public String getStartUserId() {
        return startUserId;
    }

    public void setStartUserId(String startUserId) {
        this.startUserId = startUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricProcessInstanceDTO that = (HistoricProcessInstanceDTO) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
