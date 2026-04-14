package com.pfe.services;

import com.pfe.domain.enumeration.ProcessInstanceStatus;
import com.pfe.dto.HistoricProcessInstanceDTO;
import com.pfe.dto.ProcessInstanceDTO;
import com.pfe.mapper.HistoricProcessInstanceMapper;
import com.pfe.mapper.ProcessInstanceMapper;
import com.pfe.services.criteria.ProcessInstanceCriteria;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricProcessInstanceQuery;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class ProcessInstanceQueryService {

    private static final Logger log = LoggerFactory.getLogger(ProcessDefinitionQueryService.class);

    private final ProcessEngine processEngine;

    private final ProcessInstanceMapper processInstanceMapper;

    private final HistoricProcessInstanceMapper historicProcessInstanceMapper;

    public ProcessInstanceQueryService(ProcessEngine processEngine, ProcessInstanceMapper processInstanceMapper, HistoricProcessInstanceMapper historicProcessInstanceMapper) {
        this.processEngine = processEngine;
        this.processInstanceMapper = processInstanceMapper;
        this.historicProcessInstanceMapper = historicProcessInstanceMapper;
    }


    @Transactional(readOnly = true)
    public List<ProcessInstanceDTO> findProcessInstanceByCriteria(ProcessInstanceCriteria criteria, ProcessDefinition processDefinition) {
        log.debug("find by criteria : {}, processDefinitionId: {}", criteria, processDefinition.getId());

        if (processDefinition.getId() == null || criteria == null) {
            return Collections.emptyList();
        }

        ProcessInstanceQuery query = processEngine.getRuntimeService()
            .createProcessInstanceQuery()
            .processDefinitionId(processDefinition.getId());

        query=applyCriteriaToProcessInstanceQuery(query, criteria);

        if (query != null) {
            return query.list().stream()
                .map(procInstance -> {
                    ProcessInstanceDTO processInstanceDTO = processInstanceMapper.toDto(procInstance);
                    processInstanceDTO.setProcessDefinitionName(processDefinition.getName());
                    return processInstanceDTO;
                })
                .toList();
        } else {
            return Collections.emptyList();
        }
    }

    private ProcessInstanceQuery applyCriteriaToProcessInstanceQuery(ProcessInstanceQuery query, ProcessInstanceCriteria criteria) {
        if (criteria.id() != null) {
            query.processInstanceId(criteria.id());
        }

        if (criteria.processDefinitionId() != null) {
            query.processDefinitionId(criteria.processDefinitionId());
        }

        if (criteria.state() != null) {
            query = applyStatusToProcessInstanceQuery(query, criteria.state());
        }

        if (criteria.startTime() != null) {
            query = null;
        }
        return query;
    }

    private ProcessInstanceQuery applyStatusToProcessInstanceQuery(ProcessInstanceQuery query, String status) {

        String normalizedStatus = status.toUpperCase(Locale.ROOT);

        if (ProcessInstanceStatus.ACTIVE.name().contains(normalizedStatus)) {
            query.active();
        } else if (ProcessInstanceStatus.SUSPENDED.name().contains(normalizedStatus)) {
            query.suspended();
        } else {
            query = null;
        }
        return query;
    }

    @Transactional(readOnly = true)
    public List<HistoricProcessInstanceDTO> findHistoricProcessInstanceByCriteria(ProcessInstanceCriteria criteria, ProcessDefinition processDefinition) {
        log.debug("find historic (finished) process instances by criteria : {}, processDefinitionId: {}", criteria, processDefinition.getId());

        if (processDefinition.getId() == null || criteria == null) {
            return Collections.emptyList();
        }

        HistoricProcessInstanceQuery historicQuery = processEngine.getHistoryService()
            .createHistoricProcessInstanceQuery()
            .processDefinitionId(processDefinition.getId())
            .finished();

        historicQuery= applyCriteriaToHistoricQuery(historicQuery, criteria);

        if (historicQuery != null) {
            return historicQuery.list().stream()
                .map(historicProcInstance -> {
                    HistoricProcessInstanceDTO historicProcessInstanceDTO = historicProcessInstanceMapper.toDto(historicProcInstance);
                    historicProcessInstanceDTO.setProcessDefinitionName(processDefinition.getName());
                    return historicProcessInstanceDTO;
                })
                .toList();
        } else {
            return Collections.emptyList();
        }
    }

    private HistoricProcessInstanceQuery applyCriteriaToHistoricQuery(HistoricProcessInstanceQuery query, ProcessInstanceCriteria criteria) {
        if (criteria.id() != null) {
            query.processInstanceId(criteria.id());
        }

        if (criteria.processDefinitionId() != null) {
            query.processDefinitionId(criteria.processDefinitionId());
        }

        if (criteria.startTime() != null) {
            query.startDateOn(criteria.startTime());
        }
        if (criteria.state() != null && !ProcessInstanceStatus.COMPLETED.name().contains(criteria.state().toUpperCase(Locale.ROOT))) {
            query = null;
        }
        return query;
    }


}
