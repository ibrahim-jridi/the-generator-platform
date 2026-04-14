package com.pfe.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DecisionDelegateValueExecutionListner implements ExecutionListener {
    public static final String DECISION = "decision";
    public static final String DECISION_LIST = "decisionList";


    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        Map<String, Object> vars = delegateExecution.getVariables();
        Object decision = searchNestedMap(DECISION, vars);
        List<String> assigneeList = (List<String>)  delegateExecution.getVariable(
            ProcessConstants.LIST_OF_DELEGATE);
        boolean decisionDelegueValeur = false;
        if (decision != null) {

            List<String> decisionList = (List<String>) delegateExecution.getVariable(DECISION_LIST);

            if (decisionList == null || (decisionList != null && decisionList.size() >= assigneeList.size())) {
                decisionList = new ArrayList<>();
            }

            decisionList.add(decision.toString());

            delegateExecution.setVariable(DECISION_LIST, decisionList);

             decisionDelegueValeur = decisionList.stream()
                .anyMatch(d -> "rejeter".equalsIgnoreCase(d));

             if(decisionDelegueValeur == true){

                 delegateExecution.removeVariable(ProcessConstants.VISITOR_AGENCE_ID);
                 delegateExecution.removeVariable(ProcessConstants.VISITOR_AGENCE_EMAIL);
                 delegateExecution.removeVariable(ProcessConstants.DELEGATE_AGENCE_ID);
                 delegateExecution.removeVariable(ProcessConstants.DELGATE_AGENCE_EMAIL);
             }

        }

        delegateExecution.setVariable(ProcessConstants.DECISION_DELEGATE_VALEUR, decisionDelegueValeur);

    }
    public static Object searchNestedMap(String targetKey, Object container) {
        if (container == null || targetKey == null) {
            return null;
        }

        if (container instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) container;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (entry.getKey().equals(targetKey)) {
                    return entry.getValue();
                }
                Object value = entry.getValue();
                Object result = searchNestedMap(targetKey, value);
                if (result != null) {
                    return result;
                }
            }
        } else if (container instanceof List<?>) {
            List<?> list = (List<?>) container;
            for (Object element : list) {
                Object result = searchNestedMap(targetKey, element);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
