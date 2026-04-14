package com.pfe.delegate;

import com.pfe.config.Constants;
import com.pfe.domain.enumeration.Nationality;
import com.pfe.dto.UserDTO;
import com.pfe.feignServices.RoleService;
import com.pfe.feignServices.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.el.FixedValue;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.util.*;

@Component
public class VerifyEligibilityCriteriaListner implements ExecutionListener {


    private final UserService userService;
    private final RoleService roleService;

    public static final String ROLE_PHARMACIEN = Constants.BS_ROLE_PP_PHARMACIEN;
    public static final String ROLE_MEDECIN = Constants.BS_ROLE_PP_MEDECIN;
    public static final String ROLE_DENTISTE = Constants.BS_ROLE_PP_MEDECIN_DENTISTE;
    public static final String ROLE_VETERINAIRE = Constants.BS_ROLE_PP_MEDECIN_VETERINAIRE;
    public static final String ROLE_CONSULTING = Constants.BS_ROLE_PM_SOCIETE_CONSULTING;


    private static final Logger log = LoggerFactory.getLogger(VerifyEligibilityCriteriaListner.class);
    private FixedValue taskNameResponsable;
    private FixedValue taskNamePharmacie;
    private FixedValue taskNameLocale;
    private FixedValue taskNameStranger;
    private FixedValue taskNameDelegate;
    private FixedValue taskNameVisitor;
    private FixedValue taskNameResponsableCro;
    private FixedValue taskNameResponsableInvestigationCro;
    private FixedValue taskNameAgencePCT;
    private FixedValue taskNamePCT;
    private FixedValue taskNameFilialePCT;
    private FixedValue taskNamePRTIndustrie;
    private FixedValue taskNameQualityControlIndustrie;
    private FixedValue taskNameProductionOperationIndustrie;
    private FixedValue taskNameResponsableAffiareIndustrie;
    private FixedValue taskNameResponsableGrossiste;

    public VerifyEligibilityCriteriaListner( UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {



        if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && (((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.AGENCE_PROMO_MODEL_LTS) || ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.CREATE_DESIGNATION_AGENCE_PROMO))) {
            if (taskNameResponsable != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_RESPONSABLE) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_RESPONSABLE));

                if (isEligibleResponsable(userId)) {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_RESPONSABLE_AGENCE, "true");
                } else {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_RESPONSABLE_AGENCE, "false");
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_AGENCE_ID, null);
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_AGENCE_EMAIL, null);
                }
                taskNameResponsable = null;
            }
            if (taskNamePharmacie != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_PHARAMACY) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_PHARAMACY));

                if (isEligiblePharmacie(userId)) {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_PHARMACIE, "true");
                } else {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_PHARMACIE, "false");
                    delegateExecution.setVariable(ProcessConstants.PHARMACY_AGENCE_EMAIL, null);
                    delegateExecution.setVariable(ProcessConstants.PHARMACY_AGENCE_ID, null);
                }
                taskNamePharmacie = null;
            }
            if (taskNameLocale != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_LOCAL) != null) {
                List<String> responsableList = (List<String>)  delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_LOCAL);
                boolean allMatch = true;
                for (String username : responsableList) {
                    if (username.startsWith("ext-")){
                        continue;
                    }
                    if (username.startsWith("pm-")) {
                        UUID userId = userService.findUserByUserName(username).getBody().getId();
                        if (!isEligibleLocaleStranger(userId)) {
                            allMatch = false;
                            delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_EMAIL);
                            delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_ID);
                            break;
                        }

                    }
                    else {
                        allMatch = false;
                        delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_EMAIL);
                        delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_ID);
                        break;
                    }

                }
                delegateExecution.setVariable(ProcessConstants.CRITERE_LOCALE, allMatch ? "true" : "false");
                taskNameLocale = null;
            }
            if (taskNameStranger != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_STRANGER) != null) {
                List<String> responsableEtrangerList = (List<String>)  delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_STRANGER);
                boolean allMatchStranger = true;
                for (String username : responsableEtrangerList) {
                    if (username.startsWith("ext-")){
                        continue;
                    }
                    if (username.startsWith("pm-")) {
                        UUID userId = userService.findUserByUserName(username).getBody().getId();
                        if (!isEligibleLocaleStranger(userId)) {
                            allMatchStranger = false;
                            delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_ID);
                            delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_EMAIL);
                            break;
                        }

                    }
                    else {
                        allMatchStranger = false;
                        delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_ID);
                        delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_EMAIL);
                        break;
                    }
                }
                delegateExecution.setVariable(
                    ProcessConstants.CRITERE_STRANGER, allMatchStranger ? "true" : "false");
                taskNameStranger = null;
            }
            if (taskNameDelegate != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_DELEGATE) != null) {
                List<String> assigneeList = (List<String>)  delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_DELEGATE);
                boolean allEligible = true;
                for (String username : assigneeList) {

                        UUID userId = userService.findUserByUserName(username).getBody().getId();
                        if (!isEligibleResponsable(userId)) {
                            allEligible = false;
                            delegateExecution.removeVariable(ProcessConstants.DELEGATE_AGENCE_ID);
                            delegateExecution.removeVariable(ProcessConstants.DELGATE_AGENCE_EMAIL);
                            break;
                        }
                }
                delegateExecution.setVariable(
                    ProcessConstants.CRITERE_DELEGATE, allEligible ? "true" : "false");
                taskNameDelegate = null;
            }
            if (taskNameVisitor != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_VISITOR) != null) {
                List<String> visitorList = (List<String>)  delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_VISITOR);
                boolean allEligibleVisitor = true;
                for (String username : visitorList) {

                    UUID userId = userService.findUserByUserName(username).getBody().getId();
                    if (!isEligibleVisitor(userId)) {
                        allEligibleVisitor = false;
                        delegateExecution.removeVariable(ProcessConstants.VISITOR_AGENCE_ID);
                        delegateExecution.removeVariable(ProcessConstants.VISITOR_AGENCE_EMAIL);
                        break;
                    }
                }
                delegateExecution.setVariable(
                    ProcessConstants.CRITERE_VISITOR, allEligibleVisitor ? "true" : "false");
            }
            taskNameVisitor = null;
        }

        if (delegateExecution.getProcessDefinitionId() != null
                && !delegateExecution.getProcessDefinitionId().isEmpty()
                && (((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.CRO_MODEL_LTS)|| ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.CREATE_DESIGNATION_CRO))){
            if (taskNameResponsableCro != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_RESPONSABLE_CRO) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_RESPONSABLE_CRO));

                if (isEligibleResponsableCRO(userId)) {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_RESPONSABLE_CRO, "true");
                } else {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_RESPONSABLE_CRO, "false");
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_CRO_ID, null);
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_CRO_EMAIL, null);
                }
                taskNameResponsableCro = null;
            }
            if (taskNameResponsableInvestigationCro != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_RESPONSABLE_INVESTIGATION_CRO) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_RESPONSABLE_INVESTIGATION_CRO));

                if (isEligiblePharmacie(userId)) {
                    delegateExecution.setVariable(
                        ProcessConstants.CRITERE_RESPONSABLE_INVESTIGATION_CRO, "true");
                } else {
                    delegateExecution.setVariable(
                        ProcessConstants.CRITERE_RESPONSABLE_INVESTIGATION_CRO, "false");
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_ID, null);
                    delegateExecution.setVariable(
                        ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_EMAIL, null);
                }
                taskNameResponsableInvestigationCro = null;
            }

        }

        if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
                && (((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.AGENCE_PCT_MODEL_LTS) || ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.CREATE_DESIGNATION_AGENCE_PCT))){

            if (taskNameAgencePCT != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_RESPONSABLE_AGENCE_PCT) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_RESPONSABLE_AGENCE_PCT));

                if (isEligiblePharmacie(userId)) {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_AGENCE_PCT, "true");
                } else {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_AGENCE_PCT, "false");
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_AGENCE_PCT_EMAIL, null);
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_AGENCE_PCT_ID, null);
                }
                taskNameAgencePCT = null;
            }
        }

        if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && (((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.CREATE_PCT_MODEL_LTS) || ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.CREATE_DESIGNATION_CREATE_PCT))){

            if (taskNamePCT != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_RESPONSABLE_PCT) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_RESPONSABLE_PCT));

                if (isEligiblePharmacie(userId)) {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_PCT, "true");
                } else {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_PCT, "false");
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_PCT_EMAIL, null);
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_PCT_ID, null);
                }
                taskNamePCT = null;
            }
        }

        if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && (((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.FILIALE_PCT_MODEL_LTS) || ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.CREATE_DESIGNATION_FILIALE_PCT))){

            if (taskNameFilialePCT != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_RESPONSABLE_FILIALE_PCT) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_RESPONSABLE_FILIALE_PCT));

                if (isEligiblePharmacie(userId)) {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_FILIALE_PCT, "true");
                } else {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_FILIALE_PCT, "false");
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_FILIALE_PCT_EMAIL, null);
                    delegateExecution.setVariable(ProcessConstants.RESPONSABLE_FILIALE_PCT_ID, null);
                }
                taskNameFilialePCT = null;
            }
        }


        if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.INDUSTRIE_HUMAIN_MODEL_LTS)){
            if (taskNamePRTIndustrie != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_PRT_INDUSTRIE) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_PRT_INDUSTRIE));

                if (isEligiblePharmacie(userId)) {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_PRT_INDUSTRIE, "true");
                } else {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_PRT_INDUSTRIE, "false");
                    delegateExecution.setVariable(ProcessConstants.PRT_INDUSTRIE_HUMAN_ID, null);
                    delegateExecution.setVariable(ProcessConstants.PRT_INDUSTRIE_HUMAN_EMAIL, null);
                }
                taskNamePRTIndustrie = null;
            }

            if (taskNameQualityControlIndustrie != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_QUALITY_CONTROL_INDUSTRIE) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_QUALITY_CONTROL_INDUSTRIE));

                if (isEligiblePharmacie(userId)) {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_QUALITY_CONTROL_INDUSTRIE, "true");
                } else {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_QUALITY_CONTROL_INDUSTRIE, "false");
                    delegateExecution.setVariable(
                        ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_ID, null);
                    delegateExecution.setVariable(
                        ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_EMAIL, null);
                }
                taskNameQualityControlIndustrie = null;
            }
            if (taskNameProductionOperationIndustrie != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_PRODUCTION_OPERATION_INDUSTRIE) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_PRODUCTION_OPERATION_INDUSTRIE));

                if (isEligiblePharmacie(userId)) {
                    delegateExecution.setVariable(
                        ProcessConstants.CRITERE_PRODUCTION_OPERATION_INDUSTRIE, "true");
                } else {
                    delegateExecution.setVariable(
                        ProcessConstants.CRITERE_PRODUCTION_OPERATION_INDUSTRIE, "false");
                    delegateExecution.setVariable(
                        ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_ID, null);
                    delegateExecution.setVariable(
                        ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_EMAIL, null);
                }
                taskNameProductionOperationIndustrie = null;
            }

        }

        if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.INDUSTRIE_VETERINAIRE_MODEL_LTS)) {
            if (taskNamePRTIndustrie != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_PRT_INDUSTRIE) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_PRT_INDUSTRIE));

                if (isEligiblePharmacieAndExperience(userId)) {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_PRT_INDUSTRIE, "true");
                } else {
                    delegateExecution.setVariable(ProcessConstants.CRITERE_PRT_INDUSTRIE, "false");
                    delegateExecution.setVariable(ProcessConstants.PRT_INDUSTRIE_HUMAN_ID, null);
                    delegateExecution.setVariable(ProcessConstants.PRT_INDUSTRIE_HUMAN_EMAIL, null);
                }
                taskNamePRTIndustrie = null;
            }
            if (taskNameResponsableAffiareIndustrie != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_RESPONSABLE_AFFAIRE_INDUSTRIE) != null) {
                UUID userIdUUID = extractUserId( delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_RESPONSABLE_AFFAIRE_INDUSTRIE));

                String username = userService.getuser(userIdUUID).getBody().getUsername();

                boolean allMatchAffaire = true;

                if (username.startsWith("ext-")) {
                    UUID userId = extractUserId(delegateExecution.getVariable(
                        ProcessConstants.LIST_OF_RESPONSABLE_AFFAIRE_INDUSTRIE));

                    if (!isEligiblePharmacieAndExperience(userId)) {
                        allMatchAffaire = false;
                        delegateExecution.removeVariable(
                            ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_ID);
                        delegateExecution.removeVariable(
                            ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_EMAIL);
                        delegateExecution.removeVariable(
                            ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_IDENTIFIER);

                    }
                }
                else if (username.startsWith("pm-")) {
                    UUID userId = userService.findUserByUserName(username).getBody().getId();
                    if (!isEligibleLocaleStranger(userId)) {
                        allMatchAffaire = false;
                        delegateExecution.removeVariable(
                            ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_ID);
                        delegateExecution.removeVariable(
                            ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_EMAIL);
                        delegateExecution.removeVariable(
                            ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_IDENTIFIER);

                    }

                }
                else {
                    allMatchAffaire = false;
                    delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_EMAIL);
                    delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_ID);
                    delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_ID);
                    delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_EMAIL);

                }

                delegateExecution.setVariable(ProcessConstants.CRITERE_RESPONSBALE_AFFAIRE_INDUSTRIE, allMatchAffaire ? "true" : "false");

                taskNameResponsableAffiareIndustrie = null;
            }
        }

        if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && (((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.CREATION_GROSSISTE_MODEL_LTS) || ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(
            ProcessConstants.CREATE_DESIGNATION_CREATION_GROSSISTE))){

            if (taskNameResponsableGrossiste != null && delegateExecution.getVariable(
                ProcessConstants.LIST_OF_RESPONSABLE_CREATE_GROSSISTE) != null) {
                UUID userId = extractUserId(delegateExecution.getVariable(
                    ProcessConstants.LIST_OF_RESPONSABLE_CREATE_GROSSISTE));

                if (isEligiblePharmacie(userId)) {
                    delegateExecution.setVariable(
                        ProcessConstants.CRITERE_RESPONSABME_CREATE_GROSSISTE, "true");
                } else {
                    delegateExecution.setVariable(
                        ProcessConstants.CRITERE_RESPONSABME_CREATE_GROSSISTE, "false");
                    delegateExecution.setVariable(
                        ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_EMAIL, null);
                    delegateExecution.setVariable(
                        ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_ID, null);
                }
                taskNameResponsableGrossiste = null;
            }
        }
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

    private boolean isEligibleResponsable(UUID userId) {
        List<UUID> eligibleRoles = List.of(
            roleService.getRoleByLabel(ROLE_PHARMACIEN).getBody().getId(),
            roleService.getRoleByLabel(ROLE_MEDECIN).getBody().getId(),
            roleService.getRoleByLabel(ROLE_DENTISTE).getBody().getId(),
            roleService.getRoleByLabel(ROLE_VETERINAIRE).getBody().getId()
        );

        boolean hasEligibleRole = eligibleRoles.stream()
            .anyMatch(roleId -> Boolean.TRUE.equals(userService.existsUserByRole(userId, roleId).getBody()));

        boolean hasNoDesignations = Boolean.FALSE.equals(userService.existsDesignationsListByDesignatedUserId(userId).getBody());
        boolean hasOrdreProfile = Boolean.TRUE.equals(userService.doesProfileKeyExist(userId, "InscritAuConseilDeLordreProfession").getBody());
        boolean isOrdreOui = false;

        if (hasOrdreProfile) {
            String ordreValue = userService.getProfileValue(userId, "InscritAuConseilDeLordreProfession").getBody();
            isOrdreOui = "oui".equalsIgnoreCase(ordreValue);
        }

        return hasEligibleRole && hasNoDesignations && hasOrdreProfile && isOrdreOui;
    }

    private boolean isEligiblePharmacie(UUID userId) {
        UUID pharmacienRoleId = roleService.getRoleByLabel(ROLE_PHARMACIEN).getBody().getId();

        boolean hasPharmacienRole = Boolean.TRUE.equals(userService.existsUserByRole(userId, pharmacienRoleId).getBody());


        return hasPharmacienRole;
    }

    private boolean isEligibleLocaleStranger(UUID userId) {
        UUID consultingRoleId = roleService.getRoleByLabel(ROLE_CONSULTING).getBody().getId();
        return Boolean.TRUE.equals(userService.existsUserByRole(userId, consultingRoleId).getBody());
    }
    private boolean isEligibleVisitor(UUID userId) {


        boolean hasNoDesignations = Boolean.FALSE.equals(userService.existsDesignationsListByDesignatedUserId(userId).getBody());

        boolean hasOrdreProfile = Boolean.TRUE.equals(userService.doesProfileKeyExist(userId, "LEVEL_STUDY").getBody());
        boolean isOrdreOui = false;

        if (hasOrdreProfile) {
            String ordreValue = userService.getProfileValue(userId, "LEVEL_STUDY").getBody();
            isOrdreOui = "UNIVERSITAIRE".equalsIgnoreCase(ordreValue);
        }
        boolean hasDiplomaLicenceProfile = Boolean.TRUE.equals(userService.doesProfileKeyExist(userId, "TYPE_DIPLOMA").getBody());
        boolean isLicenceOui = false;
        boolean isDiplomaDoctoratOui = false;
        if (hasDiplomaLicenceProfile) {
            String diplomaType  = userService.getProfileValue(userId, "TYPE_DIPLOMA").getBody();
            isLicenceOui = "LICENCE_OU_EQUIVALENT".equalsIgnoreCase(diplomaType);
            isDiplomaDoctoratOui = "DOCTORAT_DEXERCICE".equalsIgnoreCase(diplomaType);
        }
        boolean hasDiplomaProfile = Boolean.TRUE.equals(userService.doesProfileKeyExist(userId, "DIPLOMA_OBTAINED").getBody());
        boolean isDiplomaOui = false;

        if (hasDiplomaProfile) {
            String diplomaObtained = userService.getProfileValue(userId, "DIPLOMA_OBTAINED").getBody();
            isDiplomaOui = "oui".equalsIgnoreCase(diplomaObtained);
        }
        boolean hasStudyProfile = Boolean.TRUE.equals(userService.doesProfileKeyExist(userId, "FIELD_STUDY").getBody());
        boolean isStudyOui = false;

        if (hasStudyProfile) {
            String fieldStudy = userService.getProfileValue(userId, "FIELD_STUDY").getBody();
            isStudyOui = "ETUDES_UNIVERSITAIRES_PHARMACEUTIQUES_MEDICALES_BIOMEDICALES".equalsIgnoreCase(fieldStudy);

        }
        boolean hasFieldStudyProfile = Boolean.TRUE.equals(userService.doesProfileKeyExist(userId, "FIELD_STUDY4").getBody());
        boolean isFieldStudyPharmacieOui = false;
        boolean isFieldStudyNonMedicalOui = false;
        boolean isFieldStudyDentisteOui = false;
        boolean isFieldStudyVetOui = false;
        if (hasFieldStudyProfile) {
            String fieldStudy4  = userService.getProfileValue(userId, "FIELD_STUDY4").getBody();
            fieldStudy4 = fieldStudy4 != null
                ? fieldStudy4.replaceAll("\\s+", "") // supprime tous les espaces (même \t, \n)
                : "";
            isFieldStudyPharmacieOui = "Pharmacie".equalsIgnoreCase(fieldStudy4);
            isFieldStudyNonMedicalOui = "AUTRES_ETUDES_NON_MEDICALES".equalsIgnoreCase(fieldStudy4);
            isFieldStudyDentisteOui = "medecineDentaire".equalsIgnoreCase(fieldStudy4);
            isFieldStudyVetOui = "medecineVeterinaire".equalsIgnoreCase(fieldStudy4);

        }
        boolean hasFieldStudyOtherProfile = Boolean.TRUE.equals(userService.doesProfileKeyExist(userId, "FIELD_STUDY7").getBody());
        boolean isFieldStudy3Oui = false;
        boolean isFieldStudy4Oui = false;
        boolean isFieldStudy5Oui = false;
        boolean isFieldStudyInternatOui = false;
        boolean isFieldStudyTheseOui = false;
        if (hasFieldStudyOtherProfile) {
            String fieldStudy7  = userService.getProfileValue(userId, "FIELD_STUDY7").getBody();
            isFieldStudy3Oui = "3EME_ANNEE".equalsIgnoreCase(fieldStudy7 );
            isFieldStudy4Oui = "4EmeAnnee".equalsIgnoreCase(fieldStudy7 );
            isFieldStudy5Oui = "5EmeAnneeEnInternatEnInstanceDeTheseDexercice".equalsIgnoreCase(fieldStudy7 );
            isFieldStudyInternatOui = "EnInternat".equalsIgnoreCase(fieldStudy7 );
            isFieldStudyTheseOui = "EnInstanceDeTheseDexercice".equalsIgnoreCase(fieldStudy7 );

        }
        boolean pathLicence = isLicenceOui  && isDiplomaOui && isStudyOui;
        boolean pathDoctorat = !isDiplomaOui && isDiplomaDoctoratOui &&(isFieldStudyPharmacieOui || isFieldStudyNonMedicalOui || isFieldStudyDentisteOui || isFieldStudyVetOui) && (isFieldStudy3Oui || isFieldStudy4Oui || isFieldStudy5Oui || isFieldStudyInternatOui || isFieldStudyTheseOui);

        return   hasNoDesignations && isOrdreOui && (pathLicence || pathDoctorat) ;
    }
    private boolean isEligibleResponsableCRO(UUID userId) {
        List<UUID> eligibleRoles = List.of(
            roleService.getRoleByLabel(ROLE_PHARMACIEN).getBody().getId(),
            roleService.getRoleByLabel(ROLE_MEDECIN).getBody().getId(),
            roleService.getRoleByLabel(ROLE_DENTISTE).getBody().getId(),
            roleService.getRoleByLabel(ROLE_VETERINAIRE).getBody().getId()
        );

        boolean hasEligibleRole = eligibleRoles.stream()
            .anyMatch(roleId -> Boolean.TRUE.equals(userService.existsUserByRole(userId, roleId).getBody()));

        UserDTO user = userService.getuser(userId).getBody();
        boolean countryIsTunisie = "Tunisie".equalsIgnoreCase(user.getCountry());
        boolean nationalityIsTunisian = Nationality.TUNISIAN.equals(user.getNationality());

        boolean hasEnoughExperience = hasMinimumExperience(userId, "YEARS_OF_EXPERIENCE")
            || hasMinimumExperience(userId, "YEARS_OF_EXPERIENCE3");

        return hasEligibleRole && (nationalityIsTunisian || (!nationalityIsTunisian && countryIsTunisie)) && hasEnoughExperience;
    }
    private boolean isEligiblePharmacieAndExperience(UUID userId) {
        UUID pharmacienRoleId = roleService.getRoleByLabel(ROLE_PHARMACIEN).getBody().getId();

        boolean hasPharmacienRole = Boolean.TRUE.equals(userService.existsUserByRole(userId, pharmacienRoleId).getBody());
        boolean hasEnoughExperience = hasMinimumExperienceIndustrie(userId, "YEARS_OF_EXPERIENCE")
            || hasMinimumExperience(userId, "YEARS_OF_EXPERIENCE3");

        return hasPharmacienRole && hasEnoughExperience;
    }
    private boolean hasMinimumExperience(UUID userId, String profileKey) {
        if (Boolean.TRUE.equals(userService.doesProfileKeyExist(userId, profileKey).getBody())) {
            String experienceValue = userService.getProfileValue(userId, profileKey).getBody();
            try {
                double experience = Double.parseDouble(experienceValue);
                return experience >= 5;
            } catch (NumberFormatException e) {
                log.error("Valeur d'expérience invalide pour la clé {} : {}", profileKey, experienceValue);
            }
        }
        return false;
    }
    private boolean hasMinimumExperienceIndustrie(UUID userId, String profileKey) {
        if (Boolean.TRUE.equals(userService.doesProfileKeyExist(userId, profileKey).getBody())) {
            String experienceValue = userService.getProfileValue(userId, profileKey).getBody();
            try {
                double experience = Double.parseDouble(experienceValue);
                return experience >= 2;
            } catch (NumberFormatException e) {
                log.error("Valeur d'expérience invalide pour la clé {} : {}", profileKey, experienceValue);
            }
        }
        return false;
    }

}
