package com.pfe.services;

import org.camunda.bpm.engine.runtime.ProcessInstance;

public interface RuntimeService {
    ProcessInstance startProcess(String processKey);
    ProcessInstance startProcessById(String processKey);

    void signalExecution(String executionId);

    ProcessInstance findProcessInstanceById(String processInstanceId);
}
