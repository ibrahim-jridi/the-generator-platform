package com.pfe.delegate;

import com.pfe.config.Constants;
import com.pfe.dto.request.SaveDesignationRequest;
import com.pfe.feignServices.UserService;

import java.util.*;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaveDesignationDelegate implements JavaDelegate {

  private static final Logger log = LoggerFactory.getLogger(SaveDesignationDelegate.class);
  private final UserService userService;


  private static final String BS_PM_REPRI_LABO = Constants.BS_ROLE_PM_REPRI_LABO;
  private static final String BS_PM_DELEGUEE_MEDICAL = Constants.BS_ROLE_PM_DELEGUEE_MEDICAL;
  private static final String BS_PM_VISITOR_MEDICAL = Constants.BS_ROLE_PM_VISITOR_MEDICAL;
  private static final String BS_PM_PHARMA_ECHANTILLONS = Constants.BS_ROLE_PM_PHARMA_ECHANTILLONS;
  private static final String BS_PM_RESP_CRO = Constants.BS_PM_RESP_CRO;
  private static final String BS_PM_INVISTG_CRO = Constants.BS_PM_INVISTG_CRO;
  private static final String BS_PM_TECH_IMPORT_EXPORT = Constants.BS_PM_TECH_IMPORT_EXPORT;
  private static final String BS_PM_LEGAL_IMPORT_EXPORT = Constants.BS_PM_LEGAL_IMPORT_EXPORT;
  private static final String BS_RESP_AGENCE_PROMOT = Constants.BS_ROLE_RESP_AGENCE_PROMOT;
  public SaveDesignationDelegate(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void execute(DelegateExecution delegateExecution) throws Exception {

    String PmKeycloakId = null;
    if(delegateExecution.getVariable("PmKeycloakId")!=null){
        PmKeycloakId = delegateExecution.getVariable("PmKeycloakId").toString();
    }
      UUID pmIdDesignation = null;
    if(delegateExecution.getVariable("starter") != null){
         pmIdDesignation = extractUserId(delegateExecution.getVariable("starter"));
    }

    Map<String, List<Object>> responsibleLists = createResponsibleLists(delegateExecution);
    log.info("Generated responsibleLists: {}", responsibleLists);

    String responsableAgenceValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE));
      List<String> responsableAgenceLabValue = extractUsernames(
        responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_LAB));
      List<String> delegueMedicalValue = extractUsernames(responsibleLists.get(ProcessConstants.LIST_OF_DELEGUE_MEDICAL));
      List<String> pharmacienVesiteurValue = extractUsernames(responsibleLists.get(ProcessConstants.LIST_OF_PHARMACIEN_VISITOR));
    String pharmacienEchantillionValue = getValueFromList(
        responsibleLists.get(ProcessConstants.LIST_OF_PHARMACIEN_ECHANTILLION));
    String responsableCROValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_CRO));
    String responsableInvestigationCROValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_INVESTIGATION_CRO));
    String responsableTechImportExportValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_TECHNIQUE));
    String responsableLegalImportExportValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_LEGAL));
    String responsableTechniqueAgencePCTValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_AGENCE_PCT));
    String responsableTechniqueFilialePCTValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_FILIALE_PCT));
    String responsableTechniquePCTValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_PCT));
    List<String> labValue = extractUsernames(responsibleLists.get(ProcessConstants.LIST_OF_LAB));
    List<String> labLocalValue = extractUsernames(responsibleLists.get(ProcessConstants.LIST_OF_LAB_LOCAL));
    String responsablePRTIndustrieValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_PRT_INDUSTRIE));
    String responsableCQIndustrieValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_QUALITY_CONTROL_INDUSTRIE));
    String responsablePRODIndustrieValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_PRODUCTION_OPERATION_INDUSTRIE));
    String responsableAffaireIndustrieValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_AFFAIRE_INDUSTRIE));
    String responsableCreateGrossisteValue = getValueFromList(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_CREATE_GROSSISTE));
    List<String> responsableAgenceLabLocValue = extractUsernames(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_LAB_LOC));
    List<String> responsableAgenceLabEtrValue = extractUsernames(responsibleLists.get(ProcessConstants.LIST_OF_RESPONSABLE_LAB_ETR));

if(PmKeycloakId!=null) {
    if (responsableAgenceValue != null && !responsableAgenceValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableAgenceValue);
        saveDesignationRequest.setRole(BS_RESP_AGENCE_PROMOT);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_RESP_AGENCE_PROMOT);
    }
    // responsableAgenceLabValue (LabLoc+LabEtr) with pm id
//    if (!responsableAgenceLabValue.isEmpty()) {
//        for (String username : responsableAgenceLabValue) {
//            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
//            saveDesignationRequest.setOwner(PmKeycloakId);
//            saveDesignationRequest.setDesignated(username);
//            saveDesignationRequest.setRole(BS_PM_REPRI_LABO);
//            this.userService.saveDesignation(saveDesignationRequest);
//            log.info("Successfully saved designation for role: {}", BS_PM_REPRI_LABO);
//        }
//    }
    if (!responsableAgenceLabLocValue.isEmpty()) {

        for (int i = 0; i < responsableAgenceLabLocValue.size(); i++) {
            String labLocUsername = labLocalValue.get(i);
            String responsableUsername = responsableAgenceLabLocValue.get(i);
            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(PmKeycloakId);
            saveDesignationRequest.setDesignated(responsableUsername);
            saveDesignationRequest.setRole(BS_PM_REPRI_LABO);
            saveDesignationRequest.setLaboratoryId(labLocUsername);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Saved designation: Representative={} with Laboratory={}", labLocUsername);
        }
    }
    if (!responsableAgenceLabEtrValue.isEmpty()) {

        for (int i = 0; i < responsableAgenceLabEtrValue.size(); i++) {
            String labEtrUsername = labValue.get(i);
            String responsableUsername = responsableAgenceLabEtrValue.get(i);

            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(PmKeycloakId);
            saveDesignationRequest.setDesignated(responsableUsername);
            saveDesignationRequest.setRole(BS_PM_REPRI_LABO);
            saveDesignationRequest.setLaboratoryId(labEtrUsername);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Saved designation: Representative={} with Laboratory={}", labEtrUsername);

        }
    }
    if (!labValue.isEmpty()) {
        for (String username : labValue) {
            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(PmKeycloakId);
            saveDesignationRequest.setDesignated(username);
            saveDesignationRequest.setRole(Constants.BS_ROLE_PM_LABO_ETRANGER);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_LABO_ETRANGER);

        }
    }
    if (!labLocalValue.isEmpty()) {
        for (String username : labLocalValue) {
            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(PmKeycloakId);
            saveDesignationRequest.setDesignated(username);
            saveDesignationRequest.setRole(Constants.BS_ROLE_PM_LABO_LOCAL);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_LABO_LOCAL);

        }
    }

    if (!delegueMedicalValue.isEmpty()) {
        for (String username : delegueMedicalValue) {
            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(PmKeycloakId);
            saveDesignationRequest.setDesignated(username);
            saveDesignationRequest.setRole(BS_PM_DELEGUEE_MEDICAL);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Successfully saved designation for role: {}", BS_PM_DELEGUEE_MEDICAL);

        }
    }
    if (!pharmacienVesiteurValue.isEmpty()) {
        for (String username : pharmacienVesiteurValue) {
            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(PmKeycloakId);
            saveDesignationRequest.setDesignated(username);
            saveDesignationRequest.setRole(BS_PM_VISITOR_MEDICAL);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Successfully saved designation for role: {}", BS_PM_VISITOR_MEDICAL);

        }
    }
    if (pharmacienEchantillionValue != null && !pharmacienEchantillionValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(pharmacienEchantillionValue);
        saveDesignationRequest.setRole(BS_PM_PHARMA_ECHANTILLONS);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_PHARMA_ECHANTILLONS);

    }

    if (responsableCROValue != null && !responsableCROValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableCROValue);
        saveDesignationRequest.setRole(BS_PM_RESP_CRO);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_RESP_CRO);

    }
    if (responsableInvestigationCROValue != null && !responsableInvestigationCROValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableInvestigationCROValue);
        saveDesignationRequest.setRole(BS_PM_INVISTG_CRO);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_INVISTG_CRO);

    }

    if (responsableTechImportExportValue != null && !responsableTechImportExportValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableTechImportExportValue);
        saveDesignationRequest.setRole(BS_PM_TECH_IMPORT_EXPORT);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_TECH_IMPORT_EXPORT);

    }

    if (responsableLegalImportExportValue != null && !responsableLegalImportExportValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableLegalImportExportValue);
        saveDesignationRequest.setRole(BS_PM_LEGAL_IMPORT_EXPORT);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_LEGAL_IMPORT_EXPORT);

    }
    if (responsableTechniqueAgencePCTValue != null && !responsableTechniqueAgencePCTValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableTechniqueAgencePCTValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
    if (responsableTechniqueFilialePCTValue != null && !responsableTechniqueFilialePCTValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableTechniqueFilialePCTValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
    if (responsableTechniquePCTValue != null && !responsableTechniquePCTValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableTechniquePCTValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
    if (responsablePRTIndustrieValue != null && !responsablePRTIndustrieValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsablePRTIndustrieValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
    if (responsableCQIndustrieValue != null && !responsableCQIndustrieValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableCQIndustrieValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_RESP_CTRL_QA);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_RESP_CTRL_QA);
    }
    if (responsablePRODIndustrieValue != null && !responsablePRODIndustrieValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsablePRODIndustrieValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_PROD);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_PROD);
    }
    if (responsableAffaireIndustrieValue != null && !responsableAffaireIndustrieValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableAffaireIndustrieValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_RESP_REG_AFF);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_RESP_REG_AFF);
    }
    if (responsableCreateGrossisteValue != null && !responsableCreateGrossisteValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(PmKeycloakId);
        saveDesignationRequest.setDesignated(responsableCreateGrossisteValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
}
else {
    String pmKeycloackIdAddDesignation = userService.getuser(pmIdDesignation).getBody().getKeycloakId().toString();
    if (responsableAgenceValue != null && !responsableAgenceValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableAgenceValue);
        saveDesignationRequest.setRole(BS_RESP_AGENCE_PROMOT);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_RESP_AGENCE_PROMOT);
    }
    // responsableAgenceLabValue (LabLoc+LabEtr) Without pmId

//    if (!responsableAgenceLabValue.isEmpty()) {
//        for (String username : responsableAgenceLabValue) {
//            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
//            saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
//            saveDesignationRequest.setDesignated(username);
//            saveDesignationRequest.setRole(BS_PM_REPRI_LABO);
//            saveDesignationRequest.setPmUserId(pmIdDesignation);
//            this.userService.saveDesignation(saveDesignationRequest);
//            log.info("Successfully saved designation for role: {}", BS_PM_REPRI_LABO);
//        }
//    }
    if (!responsableAgenceLabLocValue.isEmpty()) {

        for (int i = 0; i < responsableAgenceLabLocValue.size(); i++) {
            String labLocUsername = labLocalValue.get(i);
            String responsableUsername = responsableAgenceLabLocValue.get(i);

            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
            saveDesignationRequest.setDesignated(responsableUsername);
            saveDesignationRequest.setRole(BS_PM_REPRI_LABO);
            saveDesignationRequest.setPmUserId(pmIdDesignation);
            saveDesignationRequest.setLaboratoryId(labLocUsername);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Saved designation: Representative={} with Laboratory={}", responsableUsername, labLocUsername);
        }
    }

    if (!responsableAgenceLabEtrValue.isEmpty()) {

        for (int i = 0; i < responsableAgenceLabEtrValue.size(); i++) {
            String labEtrUsername = labValue.get(i);
            String responsableUsername = responsableAgenceLabEtrValue.get(i);

            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
            saveDesignationRequest.setDesignated(responsableUsername);
            saveDesignationRequest.setRole(BS_PM_REPRI_LABO);
            saveDesignationRequest.setPmUserId(pmIdDesignation);
            saveDesignationRequest.setLaboratoryId(labEtrUsername);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Saved designation: Representative={} with Laboratory={}", responsableUsername, labEtrUsername);
        }
    }

    if (!labValue.isEmpty()) {
        for (String username : labValue) {
            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
            saveDesignationRequest.setDesignated(username);
            saveDesignationRequest.setRole(Constants.BS_ROLE_PM_LABO_ETRANGER);
            saveDesignationRequest.setPmUserId(pmIdDesignation);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_LABO_ETRANGER);

        }
    }
    if (!labLocalValue.isEmpty()) {
        for (String username : labLocalValue) {
            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
            saveDesignationRequest.setDesignated(username);
            saveDesignationRequest.setRole(Constants.BS_ROLE_PM_LABO_LOCAL);
            saveDesignationRequest.setPmUserId(pmIdDesignation);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_LABO_LOCAL);

        }
    }

    if (!delegueMedicalValue.isEmpty()) {
        for (String username : delegueMedicalValue) {
            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
            saveDesignationRequest.setDesignated(username);
            saveDesignationRequest.setRole(BS_PM_DELEGUEE_MEDICAL);
            saveDesignationRequest.setPmUserId(pmIdDesignation);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Successfully saved designation for role: {}", BS_PM_DELEGUEE_MEDICAL);

        }
    }
    if (!pharmacienVesiteurValue.isEmpty()) {
        for (String username : pharmacienVesiteurValue) {
            SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
            saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
            saveDesignationRequest.setDesignated(username);
            saveDesignationRequest.setRole(BS_PM_VISITOR_MEDICAL);
            saveDesignationRequest.setPmUserId(pmIdDesignation);
            this.userService.saveDesignation(saveDesignationRequest);
            log.info("Successfully saved designation for role: {}", BS_PM_VISITOR_MEDICAL);

        }
    }
    if (pharmacienEchantillionValue != null && !pharmacienEchantillionValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(pharmacienEchantillionValue);
        saveDesignationRequest.setRole(BS_PM_PHARMA_ECHANTILLONS);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_PHARMA_ECHANTILLONS);

    }

    if (responsableCROValue != null && !responsableCROValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableCROValue);
        saveDesignationRequest.setRole(BS_PM_RESP_CRO);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_RESP_CRO);

    }
    if (responsableInvestigationCROValue != null && !responsableInvestigationCROValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableInvestigationCROValue);
        saveDesignationRequest.setRole(BS_PM_INVISTG_CRO);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_INVISTG_CRO);

    }

    if (responsableTechImportExportValue != null && !responsableTechImportExportValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableTechImportExportValue);
        saveDesignationRequest.setRole(BS_PM_TECH_IMPORT_EXPORT);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_TECH_IMPORT_EXPORT);

    }

    if (responsableLegalImportExportValue != null && !responsableLegalImportExportValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableLegalImportExportValue);
        saveDesignationRequest.setRole(BS_PM_LEGAL_IMPORT_EXPORT);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", BS_PM_LEGAL_IMPORT_EXPORT);

    }
    if (responsableTechniqueAgencePCTValue != null && !responsableTechniqueAgencePCTValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableTechniqueAgencePCTValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
    if (responsableTechniqueFilialePCTValue != null && !responsableTechniqueFilialePCTValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableTechniqueFilialePCTValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
    if (responsableTechniquePCTValue != null && !responsableTechniquePCTValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableTechniquePCTValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
    if (responsablePRTIndustrieValue != null && !responsablePRTIndustrieValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsablePRTIndustrieValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
    if (responsableCQIndustrieValue != null && !responsableCQIndustrieValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableCQIndustrieValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_RESP_CTRL_QA);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_RESP_CTRL_QA);
    }
    if (responsablePRODIndustrieValue != null && !responsablePRODIndustrieValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsablePRODIndustrieValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_PROD);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_PROD);
    }
    if (responsableAffaireIndustrieValue != null && !responsableAffaireIndustrieValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableAffaireIndustrieValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_RESP_REG_AFF);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_RESP_REG_AFF);
    }
    if (responsableCreateGrossisteValue != null && !responsableCreateGrossisteValue.equals("[]")) {
        SaveDesignationRequest saveDesignationRequest = new SaveDesignationRequest();
        saveDesignationRequest.setOwner(pmKeycloackIdAddDesignation);
        saveDesignationRequest.setDesignated(responsableCreateGrossisteValue);
        saveDesignationRequest.setRole(Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
        saveDesignationRequest.setPmUserId(pmIdDesignation);
        this.userService.saveDesignation(saveDesignationRequest);
        log.info("Successfully saved designation for role: {}", Constants.BS_ROLE_PM_PHARMA_RESP_TECH);
    }
}
  }



  public static List<Object> searchAllNestedMap(String targetKey, Object container) {
    log.debug("Searching for key '{}' in nested map", targetKey);
    List<Object> results = new ArrayList<>();
    if (container == null || targetKey == null) {
      log.warn("Invalid search parameters: targetKey={}, container={}", targetKey, container);
      return results;
    }

    if (container instanceof Map<?, ?>) {
      Map<?, ?> map = (Map<?, ?>) container;
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        if (entry.getKey().equals(targetKey)) {
          results.add(entry.getValue());
        }
        Object value = entry.getValue();
        results.addAll(searchAllNestedMap(targetKey, value));
      }
    } else if (container instanceof List<?>) {
      List<?> list = (List<?>) container;
      for (Object element : list) {
        results.addAll(searchAllNestedMap(targetKey, element));
      }
    }
    log.debug("Found {} results for key '{}'", results.size(), targetKey);
    return results;
  }

  private Map<String, List<Object>> createResponsibleLists(DelegateExecution delegateExecution) {
    Map<String, List<Object>> responsibleLists = new HashMap<>();

    List<Object> responsableAgence = new ArrayList<>();
    responsableAgence.add(
        searchAllNestedMap(ProcessConstants.RESPONSABLE_AGENCE_ID, delegateExecution.getVariables()));
    responsableAgence.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_AGENCE_EMAIL,
        delegateExecution.getVariables()));
    responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE, responsableAgence);

    List<Object> responsableAgenceLab = new ArrayList<>();
    responsableAgenceLab.add(searchAllNestedMap(ProcessConstants.LOCAL_AGENCE_ID,
        delegateExecution.getVariables()));
    responsableAgenceLab.add(searchAllNestedMap(ProcessConstants.LOCAL_AGENCE_EMAIL,
        delegateExecution.getVariables()));
    responsableAgenceLab.add(searchAllNestedMap(ProcessConstants.STRANGER_AGENCE_ID,
        delegateExecution.getVariables()));
    responsableAgenceLab.add(searchAllNestedMap(ProcessConstants.STRANGER_AGENCE_EMAIL,
        delegateExecution.getVariables()));
    responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_LAB, responsableAgenceLab);

      List<Object> agenceLabStranger = new ArrayList<>();
      agenceLabStranger.add(searchAllNestedMap(ProcessConstants.LIST_LAB_STRANGER_TO_CREATE,
          delegateExecution.getVariables()));
      agenceLabStranger.add(searchAllNestedMap(ProcessConstants.LAB_STRANGER,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_LAB, agenceLabStranger);

      List<Object> agenceLabLocal = new ArrayList<>();
      agenceLabLocal.add(searchAllNestedMap(ProcessConstants.LAB_LOCAL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_LAB_LOCAL, agenceLabLocal);

    List<Object> delegueMedical = new ArrayList<>();
    delegueMedical.add(
        searchAllNestedMap(ProcessConstants.DELEGATE_AGENCE_ID, delegateExecution.getVariables()));
    delegueMedical.add(
        searchAllNestedMap(ProcessConstants.DELGATE_AGENCE_EMAIL, delegateExecution.getVariables()));
    responsibleLists.put(ProcessConstants.LIST_OF_DELEGUE_MEDICAL, delegueMedical);

    List<Object> pharmacienVesiteur = new ArrayList<>();
    pharmacienVesiteur.add(
        searchAllNestedMap(ProcessConstants.VISITOR_AGENCE_ID, delegateExecution.getVariables()));
    pharmacienVesiteur.add(
        searchAllNestedMap(ProcessConstants.VISITOR_AGENCE_EMAIL, delegateExecution.getVariables()));
    responsibleLists.put(ProcessConstants.LIST_OF_PHARMACIEN_VISITOR, pharmacienVesiteur);

    List<Object> pharmacienEchantillion = new ArrayList<>();
    pharmacienEchantillion.add(searchAllNestedMap(ProcessConstants.PHARMACY_AGENCE_ID,
        delegateExecution.getVariables()));
    pharmacienEchantillion.add(searchAllNestedMap(ProcessConstants.PHARMACY_AGENCE_EMAIL,
        delegateExecution.getVariables()));
    responsibleLists.put(ProcessConstants.LIST_OF_PHARMACIEN_ECHANTILLION, pharmacienEchantillion);


    List<Object> responsableCRO = new ArrayList<>();
    responsableCRO.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_CRO_ID,
        delegateExecution.getVariables()));
    responsableCRO.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_CRO_EMAIL,
        delegateExecution.getVariables()));
    responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_CRO, responsableCRO);

    List<Object> responsableInvestigationCRO = new ArrayList<>();
    responsableInvestigationCRO.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_ID,
        delegateExecution.getVariables()));
    responsableInvestigationCRO.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_EMAIL,
        delegateExecution.getVariables()));
    responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_INVESTIGATION_CRO, responsableInvestigationCRO);

    List<Object> responsableExport = new ArrayList<>();
    responsableExport.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_ID,
        delegateExecution.getVariables()));
    responsableExport.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_EMAIL,
        delegateExecution.getVariables()));
    responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_TECHNIQUE, responsableExport);

    List<Object> responsableInvestigationExport = new ArrayList<>();
    responsableInvestigationExport.add(searchAllNestedMap(ProcessConstants.REPRESENTANT_LEGAL_IMPORT_EXPORT_ID,
        delegateExecution.getVariables()));
    responsableInvestigationExport.add(searchAllNestedMap(ProcessConstants.REPRESENTANT_LEGAL_IMPORT_EXPORT_EMAIL,
        delegateExecution.getVariables()));
    responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_LEGAL, responsableInvestigationExport);

      List<Object> responsablePCT = new ArrayList<>();
      responsablePCT.add(
          searchAllNestedMap(ProcessConstants.RESPONSABLE_PCT_ID, delegateExecution.getVariables()));
      responsablePCT.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_PCT_EMAIL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_PCT , responsablePCT);

      List<Object> responsableAgencePCT = new ArrayList<>();
      responsableAgencePCT.add(
          searchAllNestedMap(ProcessConstants.RESPONSABLE_AGENCE_PCT_ID, delegateExecution.getVariables()));
      responsableAgencePCT.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_AGENCE_PCT_EMAIL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_AGENCE_PCT , responsableAgencePCT);

      List<Object> responsableFilialePCT = new ArrayList<>();
      responsableFilialePCT.add(
          searchAllNestedMap(ProcessConstants.RESPONSABLE_FILIALE_PCT_ID, delegateExecution.getVariables()));
      responsableFilialePCT.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_FILIALE_PCT_EMAIL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_FILIALE_PCT , responsableFilialePCT);

      List<Object> responsablePRTIndustrie = new ArrayList<>();
      responsablePRTIndustrie.add(
          searchAllNestedMap(ProcessConstants.PRT_INDUSTRIE_HUMAN_ID, delegateExecution.getVariables()));
      responsablePRTIndustrie.add(searchAllNestedMap(ProcessConstants.PRT_INDUSTRIE_HUMAN_EMAIL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_PRT_INDUSTRIE , responsablePRTIndustrie);

      List<Object> responsableCQIndustrie = new ArrayList<>();
      responsableCQIndustrie.add(
          searchAllNestedMap(ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_ID, delegateExecution.getVariables()));
      responsableCQIndustrie.add(searchAllNestedMap(ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_EMAIL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_QUALITY_CONTROL_INDUSTRIE , responsableCQIndustrie);

      List<Object> responsablePRODIndustrie = new ArrayList<>();
      responsablePRODIndustrie.add(
          searchAllNestedMap(ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_ID, delegateExecution.getVariables()));
      responsablePRODIndustrie.add(searchAllNestedMap(ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_EMAIL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_PRODUCTION_OPERATION_INDUSTRIE , responsablePRODIndustrie);

      List<Object> responsableAffaireIndustrie = new ArrayList<>();
      responsableAffaireIndustrie.add(
          searchAllNestedMap(ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_ID, delegateExecution.getVariables()));
      responsableAffaireIndustrie.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_EMAIL,
          delegateExecution.getVariables()));
      responsableAffaireIndustrie.add(searchAllNestedMap(ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_IDENTIFIER,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_AFFAIRE_INDUSTRIE , responsableAffaireIndustrie);

      List<Object> responsablePharmacienGrossisste = new ArrayList<>();
      responsablePharmacienGrossisste.add(
          searchAllNestedMap(ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_ID, delegateExecution.getVariables()));
      responsablePharmacienGrossisste.add(searchAllNestedMap(ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_EMAIL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_CREATE_GROSSISTE , responsablePharmacienGrossisste);

      log.info("Responsible lists created: {}", responsibleLists);
      List<Object> responsableAgenceLabEtr = new ArrayList<>();
      responsableAgenceLabEtr.add(searchAllNestedMap(ProcessConstants.STRANGER_AGENCE_ID,
          delegateExecution.getVariables()));
      responsableAgenceLabEtr.add(searchAllNestedMap(ProcessConstants.STRANGER_AGENCE_EMAIL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_LAB_ETR, responsableAgenceLabEtr);

      List<Object> responsableAgenceLabLoc = new ArrayList<>();
      responsableAgenceLabLoc.add(searchAllNestedMap(ProcessConstants.LOCAL_AGENCE_ID,
          delegateExecution.getVariables()));
      responsableAgenceLabLoc.add(searchAllNestedMap(ProcessConstants.LOCAL_AGENCE_EMAIL,
          delegateExecution.getVariables()));
      responsibleLists.put(ProcessConstants.LIST_OF_RESPONSABLE_LAB_LOC, responsableAgenceLabLoc);

    return responsibleLists;
  }

  private String getValueFromList(List<Object> list) {
    if (list == null || list.isEmpty()) {
      log.debug("List is empty or null.");
      return null;
    }
    log.debug("Processing list: {}", list);

    Object firstElement = list.get(0);
    log.debug("First element: {}", firstElement);

    if (firstElement instanceof List && ((List<?>) firstElement).isEmpty()) {
      return list.size() > 1 ?
          (list.get(1) != null ? list.get(1).toString() : null) :
          null;
    } else if (firstElement == null || firstElement.toString().isEmpty()) {
      return list.size() > 1 ?
          (list.get(1) != null ? list.get(1).toString() : null) :
          null;
    } else {
      log.debug("Returning first element as string: {}", firstElement.toString());
      return firstElement.toString();
    }
  }
    private List<String> extractUsernames(List<Object> list) {
        List<String> usernames = new ArrayList<>();
        if (list == null) return usernames;

        for (Object item : list) {
            if (item instanceof List<?>) {
                for (Object subItem : (List<?>) item) {
                    if (subItem instanceof List<?>) {
                        for (Object subSubItem : (List<?>) subItem) {
                            if (subSubItem instanceof String && !((String) subSubItem).isEmpty()) {
                                usernames.add((String) subSubItem);
                            }
                        }
                    } else if (subItem instanceof String && !((String) subItem).isEmpty()) {
                        usernames.add((String) subItem);
                    }
                }
            } else if (item instanceof String && !((String) item).isEmpty()) {
                usernames.add((String) item);
            }
        }

        return usernames;
    }

    private UUID extractUserId(Object variable) {
        if (variable instanceof UUID) {
            return (UUID) variable;
        }
        if (variable instanceof String) {
            try {
                return UUID.fromString((String) variable);
            } catch (IllegalArgumentException e) {
                log.error("⚠️ UUID invalide : " + variable);
            }
        }
        return null;
    }
}
