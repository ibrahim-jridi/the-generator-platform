package com.pfe.services.servicesImplementation;

import com.pfe.dto.DeployDTO;
import com.pfe.dto.DmnActivityDecisionDefinitionDTO;
import com.pfe.dto.XmlFileDto;
import com.pfe.mapper.DeployMapper;
import com.pfe.mapper.DmnDecisionDefinitionMapper;
import com.pfe.services.DecisionDefinitionQueryService;
import com.pfe.services.IDmnService;
import com.pfe.services.criteria.DecisionDefinitionCriteria;
import com.pfe.web.rest.errors.ProcessEngineException;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DmnServiceImpl implements IDmnService {
    private static final Logger log = LoggerFactory.getLogger(DmnServiceImpl.class);

    private final ProcessEngine processEngine;
    private final DmnDecisionDefinitionMapper dmnDecisionDefinitionMapper;
    private final DeployMapper deployMapper;

    private final DecisionDefinitionQueryService decisionDefinitionQueryService;

    public DmnServiceImpl(ProcessEngine processEngine, DmnDecisionDefinitionMapper dmnDecisionDefinitionMapper, DeployMapper deployMapper, DecisionDefinitionQueryService decisionDefinitionQueryService) {
        this.processEngine = processEngine;
        this.dmnDecisionDefinitionMapper = dmnDecisionDefinitionMapper;
        this.deployMapper = deployMapper;
        this.decisionDefinitionQueryService = decisionDefinitionQueryService;
    }

    @Override
    public List<DmnActivityDecisionDefinitionDTO> findAllDmmActivityDecisionDefinition() {
        log.debug("Fetching all dmn decision definition");
        return processEngine.getRepositoryService().createDecisionDefinitionQuery()
            .orderByDeploymentTime()
            .desc()
            .list()
            .stream()
            .map(dmnDecisionDefinitionMapper::toDto)
            .toList();
    }

    @Override
    public List<DmnActivityDecisionDefinitionDTO> findDmmActivityDecisionDefinitionByKey(String key) {
        log.debug("Fetching all dmn decision definition by key ={}", key);
        return processEngine.getRepositoryService().createDecisionDefinitionQuery()
            .decisionDefinitionKey(key)
            .list()
            .stream()
            .map(dmnDecisionDefinitionMapper::toDto)
            .toList();
    }

    @Override
    public String findDmmActivityDecisionDefinitionById(String id) {
        log.debug("Fetching dmn decision definition by id = {}", id);
        try {
            DmnModelInstance modelInstance = processEngine.getRepositoryService()
                .getDmnModelInstance(id);
            return Dmn.convertToString(modelInstance);
        } catch (Exception e) {
            throw new ProcessEngineException("Failed to get BPMN XML for process definition ID: " + id, e);
        }
    }

    @Override
    public DeployDTO deployDecisionDefinition(XmlFileDto dmnData) {
        try {
            Deployment dmnDeployment = processEngine.getRepositoryService()
                .createDeployment()
                .name(dmnData.getName())
                .addString(dmnData.getName() + ".dmn", dmnData.getXml())
                .deploy();
            log.debug("Deployment successful for DMN: {}", dmnData);
            return deployMapper.toDto(dmnDeployment);
        } catch (Exception e) {
            log.error("Failed to deploy decision definition: {}", dmnData);
            throw new ProcessEngineException("Failed to deploy decision definition: " + dmnData, e);
        }
    }

    @Override
    public Page<DmnActivityDecisionDefinitionDTO> findAllDmmActivityDecisionDefinitionByCriteria(
        DecisionDefinitionCriteria criteria, Pageable pageable) {
        log.debug("Fetching all dmn decision definition by criteria {} ",criteria);
        return decisionDefinitionQueryService.findByCriteria(criteria,pageable);
    }
}
