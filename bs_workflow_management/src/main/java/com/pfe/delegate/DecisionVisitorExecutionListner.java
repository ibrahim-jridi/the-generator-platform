package com.pfe.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.pfe.delegate.ProcessConstants.*;


@Component
public class DecisionVisitorExecutionListner implements ExecutionListener {
    public static final String DECISION = "decisionVisiteur";
    public static final String DECISION_LIST = "decisionVisiteurList";


    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {

        Map<String, Object> vars = delegateExecution.getVariables();
        Object decisionVisiteur = searchNestedMap(DECISION, vars);
        List<String> visitorList = (List<String>)  delegateExecution.getVariable(LIST_OF_VISITOR);
        boolean decisionVisiteurValeur =false;

        if (decisionVisiteur != null) {

            List<String> decisionVisiteurList = (List<String>) delegateExecution.getVariable(DECISION_LIST);

            if (decisionVisiteurList == null || (decisionVisiteurList != null && decisionVisiteurList.size() >= visitorList.size())) {
                decisionVisiteurList = new ArrayList<>();
            }

            decisionVisiteurList.add(decisionVisiteur.toString());

            delegateExecution.setVariable(DECISION_LIST, decisionVisiteurList);

            decisionVisiteurValeur = decisionVisiteurList.stream()
                .anyMatch(d -> "rejeter".equalsIgnoreCase(d));

            if(decisionVisiteurValeur== true){
                delegateExecution.removeVariable(VISITOR_AGENCE_ID);
                delegateExecution.removeVariable(VISITOR_AGENCE_EMAIL);
                delegateExecution.removeVariable(DELEGATE_AGENCE_ID);
                delegateExecution.removeVariable(DELGATE_AGENCE_EMAIL);
            }

        }

        delegateExecution.setVariable(DECISION_VISITEUR_VALEUR, decisionVisiteurValeur);
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
