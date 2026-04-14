package com.pfe.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DecisionValueStrangerExecutionListner implements ExecutionListener {
    public static final String DECISION = "decisionEtranger";
    public static final String DECISION_LIST = "decisionEtrangerList";


    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        Map<String, Object> vars = delegateExecution.getVariables();

        Object decisionEtranger = searchNestedMap(DECISION, vars);
        List<String> responsableEtrangerList = (List<String>)  delegateExecution.getVariable(
            ProcessConstants.LIST_OF_STRANGER);
        boolean decisionEtrangerValeur =false;
        if (decisionEtranger != null) {

            List<String> decisionEtrangerList = (List<String>) delegateExecution.getVariable(DECISION_LIST);

            if (decisionEtrangerList == null || (decisionEtrangerList != null && decisionEtrangerList.size() >= responsableEtrangerList.size())) {
                decisionEtrangerList = new ArrayList<>();
            }

            decisionEtrangerList.add(decisionEtranger.toString());

            delegateExecution.setVariable(DECISION_LIST, decisionEtrangerList);

            decisionEtrangerValeur = decisionEtrangerList.stream()
                .anyMatch(d -> "rejeter".equalsIgnoreCase(d));

            if(decisionEtrangerValeur == true){
                delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_EMAIL);
                delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_ID);
                delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_ID);
                delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_EMAIL);
            }
        }

        delegateExecution.setVariable(ProcessConstants.DECISION_ETRANGER_VALEUR, decisionEtrangerValeur);
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
