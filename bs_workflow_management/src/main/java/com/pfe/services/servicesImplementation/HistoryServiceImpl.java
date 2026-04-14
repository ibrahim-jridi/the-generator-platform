package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements com.pfe.services.HistoryService {

    private final HistoryService historyService;

    public HistoryServiceImpl(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Override
    public List<HistoricProcessInstance> getCompletedProcessInstances(String processDefinitionKey) {
        return historyService.createHistoricProcessInstanceQuery()
            .processDefinitionKey(processDefinitionKey)
            .finished()
            .list();
    }

    @Override
    public HistoricProcessInstance getProcessInstanceHistory(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
    }

    @Override
    public long getHistoricTaskInstanceCount() {
        return historyService.createHistoricTaskInstanceQuery().count();
    }
}


