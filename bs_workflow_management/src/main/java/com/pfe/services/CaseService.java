package com.pfe.services;

import org.camunda.bpm.engine.runtime.CaseInstance;

public interface CaseService {

    CaseInstance createCaseInstance(String caseDefinitionKey);

    void manuallyStartCaseExecution(String caseExecutionId);

    void completeCaseExecution(String caseExecutionId);
}
