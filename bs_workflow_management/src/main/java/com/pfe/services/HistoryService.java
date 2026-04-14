package com.pfe.services;

import org.camunda.bpm.engine.history.HistoricProcessInstance;

import java.util.List;

public interface HistoryService {
    List<HistoricProcessInstance> getCompletedProcessInstances(String processDefinitionKey);

    HistoricProcessInstance getProcessInstanceHistory(String processInstanceId);

    long getHistoricTaskInstanceCount();
}
