package com.pfe.services.criteria;

import com.pfe.domain.enumeration.ProcessInstanceStatus;

import java.util.Date;

public record HistoricProcessInstanceTaskCriteria(String id, ProcessInstanceStatus state, String processDefinitionName, Date startTime, String currentTaskName, Date endTime) {
}
