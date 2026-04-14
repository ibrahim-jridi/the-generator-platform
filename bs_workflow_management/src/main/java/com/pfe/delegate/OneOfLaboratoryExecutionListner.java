package com.pfe.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class OneOfLaboratoryExecutionListner implements ExecutionListener {

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        if( delegateExecution.getVariable(ProcessConstants.LIST_OF_LOCAL).toString().equals("[]") && delegateExecution.getVariable(
            ProcessConstants.LIST_OF_STRANGER).toString().equals("[]")){
            delegateExecution.setVariable("labExist", false);

    } else {
            delegateExecution.setVariable("labExist", true);
        }

    }

}
