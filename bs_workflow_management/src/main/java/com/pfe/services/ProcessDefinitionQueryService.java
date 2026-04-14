package com.pfe.services;

import com.pfe.config.Constants;
import com.pfe.services.criteria.ProcessDefinitionCriteria;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
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
public class ProcessDefinitionQueryService {
    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionQueryService.class);

    private final RepositoryService repositoryService;

    public ProcessDefinitionQueryService(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Transactional(readOnly = true)
    public Page<ProcessDefinition> findByCriteria(ProcessDefinitionCriteria criteria, Pageable pageable){
        log.debug("find by criteria : {}, page: {}", criteria, pageable);
        ProcessDefinitionQuery query= repositoryService.createProcessDefinitionQuery();
        if (criteria.id()!=null){
            query.processDefinitionId(criteria.id());
        }
        if (criteria.name()!=null){
            query.processDefinitionNameLike(Constants.PERCENTAGE + criteria.name() + Constants.PERCENTAGE);
        }
        if (criteria.version()!=null){
            query.processDefinitionVersion(criteria.version());
        }
        if (criteria.key()!=null){
            query.processDefinitionKeyLike(Constants.PERCENTAGE + criteria.key() + Constants.PERCENTAGE);

        }

        query.orderByDeploymentTime().desc();
        long total = query.count();
        if(pageable.getOffset() >= total){
            pageable= PageRequest.of(0,pageable.getPageSize());
        }
        List<ProcessDefinition> processDefinitions=query.listPage((int) pageable.getOffset(), pageable.getPageSize());
        return new PageImpl<>(processDefinitions, pageable, total);
    }

}
