package com.pfe.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class FixVariablePPForDMNDelegate implements JavaDelegate {
    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> vars = delegateExecution.getVariables();

        if (searchNestedMap("LEVEL_STUDY", vars) != null) {
            String delegation = searchNestedMap("LEVEL_STUDY", vars).toString();
            delegateExecution.setVariable("LEVEL_STUDY",
                delegation);
        }
        if (searchNestedMap("DIPLOMA_OBTAINED2", vars) != null) {
            String delegation = searchNestedMap("DIPLOMA_OBTAINED2", vars).toString();
            delegateExecution.setVariable("DIPLOMA_OBTAINED2",
                delegation);
        }
        if (searchNestedMap("DIPLOMA_OBTAINED", vars) != null) {
            String delegation = searchNestedMap("DIPLOMA_OBTAINED", vars).toString();

            delegateExecution.setVariable("DIPLOMA_OBTAINED2",
                delegation);
        }
        if (searchNestedMap("TYPE_DIPLOMA", vars) != null) {
            String delegation = searchNestedMap("TYPE_DIPLOMA", vars).toString();
            delegateExecution.setVariable("TYPE_DIPLOMA",
                delegation);
        }
        if (searchNestedMap("TYPE_DIPLOMA2", vars) != null) {
            String delegation = searchNestedMap("TYPE_DIPLOMA2", vars).toString();
            delegateExecution.setVariable("TYPE_DIPLOMA2",
                delegation);
        }
        if (searchNestedMap("FIELD_STUDY4", vars) != null ) {
            String delegation = searchNestedMap("FIELD_STUDY4", vars).toString();
            delegateExecution.setVariable("FIELD_STUDY4",
                delegation);
        }
        if (searchNestedMap("FIELD_STUDY3", vars) != null) {
            String delegation = searchNestedMap("FIELD_STUDY3", vars).toString();
            delegateExecution.setVariable("FIELD_STUDY4",
                delegation);
        }
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
