package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.CaseService;
import org.camunda.bpm.engine.runtime.CaseInstance;
import org.springframework.stereotype.Service;

@Service
public class CaseServiceImpl implements com.pfe.services.CaseService {


    private final CaseService caseService;

    public CaseServiceImpl(CaseService caseService) {
        this.caseService = caseService;
    }

    @Override
    public CaseInstance createCaseInstance(String caseDefinitionKey) {
        return caseService.createCaseInstanceByKey(caseDefinitionKey);
    }

    @Override
    public void manuallyStartCaseExecution(String caseExecutionId) {
        caseService.manuallyStartCaseExecution(caseExecutionId);
    }

    @Override
    public void completeCaseExecution(String caseExecutionId) {
        caseService.completeCaseExecution(caseExecutionId);
    }
}

