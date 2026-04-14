package com.pfe.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.el.FixedValue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


import static com.pfe.delegate.ProcessConstants.*;

@Component
public class FixVariblesExecutionListner implements ExecutionListener {

    private FixedValue videResponsableCRO;
    private FixedValue videResponsableInvestigationCRO;
    private FixedValue videResponsableAgence;
    private FixedValue videResponsablePharmacieAgence;
    private FixedValue videResponsableTechniqueImport;
    private FixedValue videResponsableLegalImport;
    private FixedValue videResponsableAgencePCT;
    private FixedValue videResponsableFilialePCT;
    private FixedValue videResponsablePCT;
    private FixedValue videPRTIndustrie;
    private FixedValue videQualityControlIndustrie;
    private FixedValue videProductionOperationIndustrie;
    private FixedValue videResponsableAffaireIndustrie;
    private FixedValue videResponsableGrossiste;


    @Override
    public void notify(DelegateExecution execution) {
        String processDefinitionId = execution.getProcessDefinitionId();
        Map<String, Object> vars = execution.getVariables();
        if (processDefinitionId != null && processDefinitionId.contains(CRO_MODEL_LTS)) {

            handleResponsableCRO(execution, vars);
            handleResponsableInvestigationCRO(execution, vars);
        }
        if (processDefinitionId != null && processDefinitionId.contains(AGENCE_PROMO_MODEL_LTS)) {

            handleResponsableAgence(execution, vars);
            handlePharamcieAgence(execution, vars);
        }
        if (processDefinitionId != null && processDefinitionId.contains(IMPORT_EXPORT_MODEL_LTS)) {

            handleResponsableTechnique(execution, vars);
            handleResponsableLegal(execution, vars);
        }

        if (processDefinitionId != null && processDefinitionId.contains(AGENCE_PCT_MODEL_LTS)) {

            handleResponsableAgencePCT(execution, vars);
        }
        if (processDefinitionId != null && processDefinitionId.contains(FILIALE_PCT_MODEL_LTS)) {

            handleResponsableFilialePCT(execution, vars);
        }
        if (processDefinitionId != null && processDefinitionId.contains(CREATE_PCT_MODEL_LTS)) {

            handleResponsablePCT(execution, vars);
        }
        if (processDefinitionId != null && processDefinitionId.contains(INDUSTRIE_HUMAIN_MODEL_LTS)) {

            handlePRTIndustrie(execution, vars);
            handleProductionOperationIndustrie(execution, vars);
            handleResponsableAffaireIndustrie(execution, vars);
            handleQualityControlIndustrie(execution, vars);
        }
        if (processDefinitionId != null && processDefinitionId.contains(CREATION_GROSSISTE_MODEL_LTS)) {

            handleResponsableGrossiste(execution, vars);
        }
    }

    private void handleResponsableCRO(DelegateExecution execution, Map<String, Object> vars) {
        if (videResponsableCRO == null) return;


        Object decision = searchNestedMap(DECISION_RESPONSABLE_CRO, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(RESPONSABLE_CRO_ID, null);
            execution.setVariable(RESPONSABLE_CRO_EMAIL, null);
            videResponsableCRO = null;
        }
    }

    private void handleResponsableInvestigationCRO(DelegateExecution execution, Map<String, Object> vars) {
        if (videResponsableInvestigationCRO == null) return;


        Object decision = searchNestedMap(DECISION_RESPONSABLE_INVESTIGATION_CRO, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(RESPONSABLE_INVESTIGATION_CRO_ID, null);
            execution.setVariable(RESPONSABLE_INVESTIGATION_CRO_EMAIL, null);
            videResponsableInvestigationCRO = null;
        }
    }
    private void handleResponsableAgence(DelegateExecution execution, Map<String, Object> vars) {
        if (videResponsableAgence == null) return;


        Object decision = searchNestedMap(DECISION_RESPONSABLE_AGENCE, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(RESPONSABLE_AGENCE_ID, null);
            execution.setVariable(RESPONSABLE_AGENCE_EMAIL, null);
            videResponsableAgence = null;
        }
    }
    private void handlePharamcieAgence(DelegateExecution execution, Map<String, Object> vars) {
        if (videResponsablePharmacieAgence == null) return;


        Object decision = searchNestedMap(DECISION_PHARAMACIE_AGENCE, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(PHARMACY_AGENCE_EMAIL, null);
            execution.setVariable(PHARMACY_AGENCE_ID, null);
            videResponsablePharmacieAgence = null;
        }
    }
    private void handleResponsableTechnique(DelegateExecution execution, Map<String, Object> vars) {
        if (videResponsableTechniqueImport == null) return;


        Object decision = searchNestedMap(DECISION_RESPONSABLE_TECHNIQUE_IMPORT, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_EMAIL, null);
            execution.setVariable(RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_ID, null);
            videResponsableTechniqueImport = null;
        }
    }
    private void handleResponsableLegal(DelegateExecution execution, Map<String, Object> vars) {
        if (videResponsableLegalImport== null) return;


        Object decision = searchNestedMap(DECISION_RESPONSABLE_LEGAL_IMPORT, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(REPRESENTANT_LEGAL_IMPORT_EXPORT_IDENTIFIER, null);
            execution.setVariable(REPRESENTANT_LEGAL_IMPORT_EXPORT_EMAIL, null);
            execution.setVariable(REPRESENTANT_LEGAL_IMPORT_EXPORT_ID, null);
            videResponsableLegalImport = null;
        }
    }
    private void handleResponsableAgencePCT(DelegateExecution execution, Map<String, Object> vars) {

        if (videResponsableAgencePCT == null) return;

        Object decision = searchNestedMap(DECISION_RESPONSABLE_AGENCE_PCT, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(RESPONSABLE_AGENCE_PCT_ID, null);
            execution.setVariable(RESPONSABLE_AGENCE_PCT_EMAIL, null);
            videResponsableAgencePCT = null;
        }
    }
    private void handleResponsableFilialePCT(DelegateExecution execution, Map<String, Object> vars) {

        if (videResponsableFilialePCT == null) return;

        Object decision = searchNestedMap(DECISION_RESPONSABLE_FILIALE_PCT, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(RESPONSABLE_FILIALE_PCT_ID, null);
            execution.setVariable(RESPONSABLE_FILIALE_PCT_EMAIL, null);
            videResponsableFilialePCT = null;
        }
    }
    private void handleResponsablePCT(DelegateExecution execution, Map<String, Object> vars) {

        if (videResponsablePCT == null) return;

        Object decision = searchNestedMap(DECISION_RESPONSABLE_PCT, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(RESPONSABLE_PCT_ID, null);
            execution.setVariable(RESPONSABLE_PCT_EMAIL, null);
            videResponsablePCT = null;
        }
    }
    private void handlePRTIndustrie(DelegateExecution execution, Map<String, Object> vars) {
        if (videPRTIndustrie == null) return;


        Object decision = searchNestedMap(DECISION_PRT_INDUSTRIE, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(PRT_INDUSTRIE_HUMAN_EMAIL, null);
            execution.setVariable(PRT_INDUSTRIE_HUMAN_ID, null);
            videPRTIndustrie = null;
        }
    }
    private void handleResponsableAffaireIndustrie(DelegateExecution execution, Map<String, Object> vars) {
        if (videResponsableAffaireIndustrie == null) return;


        Object decision = searchNestedMap(DECISION_RESPONSABLE_AFFAIRE_INDUSTRIE, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_EMAIL, null);
            execution.setVariable(RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_ID, null);
            execution.setVariable(RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_IDENTIFIER, null);
            videResponsableAffaireIndustrie = null;
        }
    }
    private void handleQualityControlIndustrie(DelegateExecution execution, Map<String, Object> vars) {
        if (videQualityControlIndustrie == null) return;


        Object decision = searchNestedMap(DECISION_QUALITY_CONTROL_INDUSTRIE, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(QUALITY_CONTROL_INDUSTRIE_HUMAN_EMAIL, null);
            execution.setVariable(QUALITY_CONTROL_INDUSTRIE_HUMAN_ID, null);
            videQualityControlIndustrie = null;
        }
    }
    private void handleProductionOperationIndustrie(DelegateExecution execution, Map<String, Object> vars) {
        if (videProductionOperationIndustrie == null) return;


        Object decision = searchNestedMap(DECISION_PRODUCTION_OPERATION_INDUSTRIE, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(PRODUCTION_OPERATION_INDUSTRIE_HUMAN_EMAIL, null);
            execution.setVariable(PRODUCTION_OPERATION_INDUSTRIE_HUMAN_ID, null);
            videProductionOperationIndustrie = null;
        }
    }
    private void handleResponsableGrossiste(DelegateExecution execution, Map<String, Object> vars) {
        if (videResponsableGrossiste == null) return;


        Object decision = searchNestedMap(DECISION_RESPONSABLE_CREATE_GROSSISTE, vars);

        if (isRejection(decision, "rejeter")) {
            execution.setVariable(RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_EMAIL, null);
            execution.setVariable(RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_ID, null);
            videResponsableGrossiste = null;
        }
    }
    private boolean isRejection(Object value, String expected) {
        return expected.equals(String.valueOf(value));
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
