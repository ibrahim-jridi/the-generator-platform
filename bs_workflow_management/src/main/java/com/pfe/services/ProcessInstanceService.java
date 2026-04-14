package com.pfe.services;

import com.pfe.dto.ProcessInstanceDTO;
import org.camunda.bpm.engine.history.HistoricProcessInstance;

import java.util.List;
import java.util.Map;

public interface ProcessInstanceService {
    Map<String, Map<String, Long>> getProcessInstanceCounts();
    List<ProcessInstanceDTO> getAllProcessInstances(String processDefinitionKey);
    List<ProcessInstanceDTO> getAllActiveInstances(String processDefinitionId);
    List<HistoricProcessInstance> getAllHistoricInstances(String processDefinitionId);
}
