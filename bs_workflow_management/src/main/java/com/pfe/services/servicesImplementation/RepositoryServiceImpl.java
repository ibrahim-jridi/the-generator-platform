package com.pfe.services.servicesImplementation;

import com.pfe.dto.ProcessDefinitionDTO;
import com.pfe.mapper.ProcessDefinitionMapper;
import com.pfe.services.ProcessDefinitionQueryService;
import com.pfe.services.criteria.ProcessDefinitionCriteria;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RepositoryServiceImpl implements com.pfe.services.RepositoryService {


    private final RepositoryService repositoryService;
    private final ProcessDefinitionMapper processDefinitionMapper;

    private final ProcessDefinitionQueryService processDefinitionQueryService;

    public RepositoryServiceImpl(RepositoryService repositoryService, ProcessDefinitionMapper processDefinitionMapper, ProcessDefinitionQueryService processDefinitionQueryService) {
        this.repositoryService = repositoryService;
        this.processDefinitionMapper = processDefinitionMapper;
        this.processDefinitionQueryService = processDefinitionQueryService;
    }

    @Override
    public Deployment deployProcessDefinition(Map<String, String> processData) {

        try {
            String processXml = processData.get("xml");
            String processName = processData.get("name");

            if (processXml == null || processName == null) {
                throw new IllegalArgumentException("Invalid process data. Required properties: xml, name.");
            }

            String modifiedXml = processXml.replaceFirst("<bpmn:process", "<bpmn:process camunda:historyTimeToLive=\"180\"");

            return repositoryService.createDeployment()
                .name(processName)
                .addString(processName + ".bpmn", modifiedXml)
                .deploy();
        } catch (Exception e) {
            System.err.println("Deployment failed: " + e.getMessage());
            throw new RuntimeException("Failed to deploy process definition.", e);
        }
    }

    @Override
    public void deleteDeployment(String deploymentId) {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Override
    public long getProcessDefinitionCount() {
        return repositoryService.createProcessDefinitionQuery().count();
    }

    @Override
    public Page<ProcessDefinitionDTO> getDeployedProcesses(Pageable pageable) {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
            .orderByDeploymentTime().desc()
            .listPage((int) pageable.getOffset(), pageable.getPageSize());

        long total = repositoryService.createProcessDefinitionQuery().count();

        List<ProcessDefinitionDTO> response = processDefinitions.stream()
            .map(processDefinitionMapper::toDto)
            .collect(Collectors.toList());


        return new PageImpl<>(response, pageable, total);
    }

    @Override
    public Page<ProcessDefinitionDTO> getDeployedProcessesByCriteria(
        ProcessDefinitionCriteria criteria, Pageable pageable) {
        return this.processDefinitionMapper.toDto(processDefinitionQueryService.findByCriteria(criteria, pageable));
    }

    @Override
    public ProcessDefinitionDTO getDeployedProcessById(String id) {
        return this.processDefinitionMapper.toDto(repositoryService.getProcessDefinition(id));
    }
}


