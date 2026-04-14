package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Service;

@Service
public class RuntimeServiceImpl implements com.pfe.services.RuntimeService {


    private final RuntimeService runtimeService;

    public RuntimeServiceImpl(RuntimeService runtimeService){
        this.runtimeService = runtimeService;
    }

    @Override
    public ProcessInstance startProcess(String processKey) {
        return runtimeService.startProcessInstanceByKey(processKey);
    }

    @Override
    public ProcessInstance startProcessById(String processId) {
        return runtimeService.startProcessInstanceById(processId);
    }

    @Override
    public void signalExecution(String executionId) {
        runtimeService.signal(executionId);
    }

    @Override
    public ProcessInstance findProcessInstanceById(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }
}
