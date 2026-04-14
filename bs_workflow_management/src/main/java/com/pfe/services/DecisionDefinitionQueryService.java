package com.pfe.services;

import com.pfe.config.Constants;
import com.pfe.dto.DmnActivityDecisionDefinitionDTO;
import com.pfe.mapper.DmnDecisionDefinitionMapper;
import com.pfe.services.criteria.DecisionDefinitionCriteria;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.DecisionDefinitionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DecisionDefinitionQueryService {

    private static final Logger log = LoggerFactory.getLogger(DecisionDefinitionQueryService.class);

    private final RepositoryService repositoryService;

    private final DmnDecisionDefinitionMapper dmnDecisionDefinitionMapper;


    public DecisionDefinitionQueryService(RepositoryService repositoryService, DmnDecisionDefinitionMapper dmnDecisionDefinitionMapper) {
        this.repositoryService = repositoryService;
        this.dmnDecisionDefinitionMapper = dmnDecisionDefinitionMapper;
    }

    @Transactional(readOnly = true)
    public Page<DmnActivityDecisionDefinitionDTO> findByCriteria(DecisionDefinitionCriteria criteria, Pageable pageable){
        log.debug("find by criteria : {}, page: {}", criteria, pageable);
        DecisionDefinitionQuery query= repositoryService.createDecisionDefinitionQuery();
        if (criteria.id()!=null){
            query.decisionDefinitionId(criteria.id());
        }
        if (criteria.name()!=null){
            query.decisionDefinitionNameLike(Constants.PERCENTAGE + criteria.name() + Constants.PERCENTAGE);
        }
        if (criteria.version()!=null){
            query.decisionDefinitionVersion(criteria.version());
        }
        if (criteria.key()!=null){
            query.decisionDefinitionKeyLike(Constants.PERCENTAGE + criteria.key() + Constants.PERCENTAGE);
        }
        query.orderByDeploymentTime().desc();
        long total = query.count();
        if(pageable.getOffset() >= total){
            pageable= PageRequest.of(0,pageable.getPageSize());
        }
        List<DmnActivityDecisionDefinitionDTO> processDefinitions=query.listPage((int) pageable.getOffset(), pageable.getPageSize()).stream().map(dmnDecisionDefinitionMapper::toDto).toList();
        return new PageImpl<>(processDefinitions, pageable, total);
    }
}
