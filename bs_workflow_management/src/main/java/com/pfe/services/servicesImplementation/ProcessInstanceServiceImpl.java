package com.pfe.services.servicesImplementation;

import com.pfe.dto.ProcessInstanceDTO;
import com.pfe.mapper.ProcessInstanceMapper;
import com.pfe.services.ProcessInstanceService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {

    private final ProcessEngine processEngine;
    private final ProcessInstanceMapper processInstanceMapper;
    public ProcessInstanceServiceImpl(ProcessEngine processEngine,ProcessInstanceMapper processInstanceMapper){
        this.processEngine=processEngine;
        this.processInstanceMapper=processInstanceMapper;
    }
    @Override
    public Map<String, Map<String, Long>> getProcessInstanceCounts() {
        Map<String, Map<String, Long>> result = new HashMap<>();

        // Get all process definitions
        List<String> processDefinitionIds = processEngine.getRepositoryService()
            .createProcessDefinitionQuery()
            .list()
            .stream()
            .map(definition -> definition.getId())
            .toList();

        for (String processDefinitionId : processDefinitionIds) {
            Map<String, Long> instanceCounts = new HashMap<>();

            long activeCount = processEngine.getRuntimeService()
                .createProcessInstanceQuery()
                .processDefinitionId(processDefinitionId)
                .active()
                .count();
            instanceCounts.put("active", activeCount);

            long suspendedCount = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery()
                .processDefinitionId(processDefinitionId)
                .suspended()
                .count();
            instanceCounts.put("suspended", suspendedCount);

            result.put(processDefinitionId, instanceCounts);
        }

        return result;
    }
    @Override
    public List<ProcessInstanceDTO> getAllProcessInstances(String processDefinitionKey) {
        ProcessInstanceQuery query = processEngine.getRuntimeService()
            .createProcessInstanceQuery()
            .processDefinitionId(processDefinitionKey);
        List<ProcessInstance> processInstances = query.list();
        return processInstances.stream()
            .map(ProcessInstanceMapper.INSTANCE::toDto)
            .collect(Collectors.toList());
    }


public List<ProcessInstanceDTO> getAllActiveInstances(String processDefinitionId) {
    ProcessInstanceQuery activeInstancesQuery = processEngine.getRuntimeService()
        .createProcessInstanceQuery()
        .processDefinitionId(processDefinitionId);

    List<ProcessInstance> processInstances = activeInstancesQuery.list();

    return processInstances.stream()
        .map(ProcessInstanceMapper.INSTANCE::toDto)
        .collect(Collectors.toList());
}

//    private ProcessInstanceDTO mapToProcessInstanceDto(ProcessInstance processInstance) {
//        ProcessInstanceDTO dto = new ProcessInstanceDTO();
//        dto.setId(processInstance.getId());
//        dto.setProcessInstanceId(processInstance.getProcessInstanceId());
//        dto.setProcessDefinitionId(processInstance.getProcessDefinitionId());
//        dto.setBusinessKey(processInstance.getBusinessKey());
//        dto.setTenantId(processInstance.getTenantId());
//        dto.setState(processInstance.isSuspended() ? "SUSPENDED" : "ACTIVE");
//
//        return dto;
//    }


    public List<HistoricProcessInstance> getAllHistoricInstances(String processDefinitionId) {
        HistoricProcessInstanceQuery historicInstancesQuery = processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processDefinitionId(processDefinitionId).finished();

        return historicInstancesQuery.list();
    }
}
