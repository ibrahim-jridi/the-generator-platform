package com.pfe.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DecisionValueLocalExecutionListner implements ExecutionListener {
    public static final String DECISION = "decisionLocale";
    public static final String DECISION_LIST = "decisionLocalList";


    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        Map<String, Object> vars = delegateExecution.getVariables();
        Object decisionLocale = searchNestedMap(DECISION, vars);
        List<String> responsableList = (List<String>)  delegateExecution.getVariable(
            ProcessConstants.LIST_OF_LOCAL);

        boolean decisionLocaleValeur = false;

        if (decisionLocale != null) {

            List<String> decisionLocalList = (List<String>) delegateExecution.getVariable(DECISION_LIST);

            if (decisionLocalList == null || (decisionLocalList != null && decisionLocalList.size() >= responsableList.size())) {
                decisionLocalList = new ArrayList<>();
            }

            decisionLocalList.add(decisionLocale.toString());

            delegateExecution.setVariable(DECISION_LIST, decisionLocalList);

            decisionLocaleValeur = decisionLocalList.stream()
                .anyMatch(d -> "rejeter".equalsIgnoreCase(d));

            if(decisionLocaleValeur == true){
                delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_EMAIL);
                delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_ID);
                delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_ID);
                delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_EMAIL);
            }
        }


        delegateExecution.setVariable(ProcessConstants.DECISION_LOCAL_VALEUR, decisionLocaleValeur);

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
