package com.pfe.services;

import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.services.criteria.ProcessDefinitionCriteria;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface RepositoryService {

    Deployment deployProcessDefinition(Map<String, String> processData);

    void deleteDeployment(String deploymentId);

    long getProcessDefinitionCount();

    Page<ProcessDefinitionDTO> getDeployedProcesses(Pageable pageable);

    Page<ProcessDefinitionDTO> getDeployedProcessesByCriteria(ProcessDefinitionCriteria criteria, Pageable pageable);

    ProcessDefinitionDTO getDeployedProcessById(String id);


}
