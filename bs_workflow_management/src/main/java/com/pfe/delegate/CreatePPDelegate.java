package com.pfe.delegate;

import com.pfe.dto.request.SignUpRequest;
import com.pfe.feignServices.AuthenticateService;
import com.pfe.feignServices.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CreatePPDelegate implements JavaDelegate {

    private final AuthenticateService authenticateService;
    private final UserService userService;

    public CreatePPDelegate(AuthenticateService authenticateService, UserService userService) {
        this.authenticateService = authenticateService;
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> vars = delegateExecution.getVariables();
        List  listVide = new ArrayList();
        delegateExecution.setVariable(ProcessConstants.LIST_OF_DELEGATE, listVide);
        delegateExecution.setVariable(ProcessConstants.LIST_OF_VISITOR, listVide);
        delegateExecution.setVariable(ProcessConstants.LIST_OF_LOCAL, listVide);
        delegateExecution.setVariable(ProcessConstants.LIST_OF_STRANGER, listVide);
        delegateExecution.setVariable("localToCreateList", listVide);
        delegateExecution.setVariable("strangerToCreateList", listVide);
        delegateExecution.setVariable("delegateToCreateList", listVide);
        delegateExecution.setVariable("visitorToCreateList", listVide);

        String validationVide ="";


        if(searchNestedMap("VALIDATION_EXISTENCE_ACCOUNT_PROMOTION_AGENCY_RESPONSABLE", vars) == null){
            delegateExecution.setVariable("VALIDATION_EXISTENCE_ACCOUNT_PROMOTION_AGENCY_RESPONSABLE", validationVide);
        }
        if(searchNestedMap("VALIDATION_EXISTENCE_ACCOUNT_PHARMACIST_RESPONSIBLE_HOLDING", vars) == null){
             delegateExecution.setVariable("VALIDATION_EXISTENCE_ACCOUNT_PHARMACIST_RESPONSIBLE_HOLDING", validationVide);
        }
        if (searchNestedMap("CONFIRMATION_ACTIVITY_MEDICAL_PRODUCTS", vars) == null){
            delegateExecution.setVariable("CONFIRMATION_ACTIVITY_MEDICAL_PRODUCTS", validationVide);
        }


        if (searchNestedMap(ProcessConstants.DECISION_PHARAMACIE_AGENCE, vars) == null){
            delegateExecution.setVariable(ProcessConstants.DECISION_PHARAMACIE_AGENCE, validationVide);
        }
        if (searchNestedMap(ProcessConstants.DECISION_VISITEUR_VALEUR , vars) == null){
            delegateExecution.setVariable(ProcessConstants.DECISION_VISITEUR_VALEUR , false);
        }
        if (searchNestedMap(ProcessConstants.DECISION_LOCAL_VALEUR, vars) == null){
            delegateExecution.setVariable(ProcessConstants.DECISION_LOCAL_VALEUR, false);
        }
        if (searchNestedMap(ProcessConstants.DECISION_DELEGATE_VALEUR, vars) == null){
            delegateExecution.setVariable(ProcessConstants.DECISION_DELEGATE_VALEUR, false);
        }
        if (searchNestedMap(ProcessConstants.DECISION_ETRANGER_VALEUR, vars) == null){
            delegateExecution.setVariable(ProcessConstants.DECISION_ETRANGER_VALEUR, false);
        }
        if (searchNestedMap(ProcessConstants.DECISION_RESPONSABLE_AGENCE, vars) == null){
            delegateExecution.setVariable(ProcessConstants.DECISION_RESPONSABLE_AGENCE, false);
        }

        if (searchNestedMap(ProcessConstants.DECISION_RESPONSABLE_INVESTIGATION_CRO, vars) == null){
            delegateExecution.setVariable(ProcessConstants.DECISION_RESPONSABLE_INVESTIGATION_CRO, validationVide);
        }


        if (searchNestedMap(ProcessConstants.RESPONSABLE_AGENCE_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.RESPONSABLE_AGENCE_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.RESPONSABLE_AGENCE_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE,result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.RESPONSABLE_AGENCE_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_AGENCE_ID, vars) != null) {
            String username = searchNestedMap(ProcessConstants.RESPONSABLE_AGENCE_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);

            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE,id);
        }

        if (!searchAllNestedMap(ProcessConstants.LOCAL_AGENCE_EMAIL, vars).isEmpty() ||
            !searchAllNestedMap(ProcessConstants.LOCAL_AGENCE_ID, vars).isEmpty()) {

            List<Object> emailList = searchAllNestedMap(ProcessConstants.LOCAL_AGENCE_EMAIL, vars);
            List<Object> idList = searchAllNestedMap(ProcessConstants.LOCAL_AGENCE_ID, vars);

            List<String> combinedList = new ArrayList<>();
            List<String> combinedListToCreateLocal = new ArrayList<>();
            List<String> localIdsToCreate = new ArrayList<>();

            if (!emailList.isEmpty()) {
                List<String> emailIds = emailList.stream()
                    .map(Object::toString)
                    .map(email -> {
                        SignUpRequest signUpRequest = new SignUpRequest();
                        signUpRequest.setEmail(email);
                        Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(signUpRequest).getBody();
                        localIdsToCreate.add(result.get("id").toString());
                        return result.get("username").toString();
                    })
                    .collect(Collectors.toList());
                combinedList.addAll(emailIds);
                combinedListToCreateLocal.addAll(localIdsToCreate);
                delegateExecution.setVariable("localToCreateList", combinedListToCreateLocal);
                delegateExecution.removeVariable(ProcessConstants.LOCAL_AGENCE_EMAIL);
            }

            if (!idList.isEmpty()) {
                List<String> idStrings = idList.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
                combinedList.addAll(idStrings);
            }

            if (!combinedList.isEmpty()) {
                List<String> uniqueResponsableList = combinedList.stream().distinct().collect(Collectors.toList());
                delegateExecution.setVariable(ProcessConstants.LIST_OF_LOCAL, uniqueResponsableList);
            }

        }

        if (!searchAllNestedMap(ProcessConstants.STRANGER_AGENCE_EMAIL, vars).isEmpty() ||
            !searchAllNestedMap(ProcessConstants.STRANGER_AGENCE_ID, vars).isEmpty()) {
            List<Object> emailList2 = searchAllNestedMap(ProcessConstants.STRANGER_AGENCE_EMAIL, vars);
            List<Object> idList2 = searchAllNestedMap(ProcessConstants.STRANGER_AGENCE_ID, vars);

            List<String> combinedList2 = new ArrayList<>();
            List<String> combinedListToCreateStranger = new ArrayList<>();
            List<String> strangerIdsToCreate = new ArrayList<>();

            if (!emailList2.isEmpty()) {
                List<String> emailIds2 = emailList2.stream()
                    .map(Object::toString)
                    .map(email -> {
                        SignUpRequest signUpRequest = new SignUpRequest();
                        signUpRequest.setEmail(email);
                        Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(signUpRequest).getBody();
                        strangerIdsToCreate.add(result.get("id").toString());
                        return result.get("username").toString();
                    })
                    .collect(Collectors.toList());
                combinedList2.addAll(emailIds2);
                combinedListToCreateStranger.addAll(strangerIdsToCreate);
                delegateExecution.setVariable("strangerToCreateList", combinedListToCreateStranger);
                delegateExecution.removeVariable(ProcessConstants.STRANGER_AGENCE_EMAIL);
            }

            if (!idList2.isEmpty()) {
                List<String> idStrings2 = idList2.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
                combinedList2.addAll(idStrings2);
            }

            if (!combinedList2.isEmpty()) {
                List<String> uniqueResponsableEtrangerList = combinedList2.stream().distinct().collect(Collectors.toList());
                delegateExecution.setVariable(ProcessConstants.LIST_OF_STRANGER, uniqueResponsableEtrangerList);
            }
        }

        if (!searchAllNestedMap(ProcessConstants.VISITOR_AGENCE_EMAIL, vars).isEmpty() ||
            !searchAllNestedMap(ProcessConstants.VISITOR_AGENCE_ID, vars).isEmpty()) {

            List<Object> visitorIdList = searchAllNestedMap(ProcessConstants.VISITOR_AGENCE_ID, vars);
            List<Object> visitorEmailList = searchAllNestedMap(ProcessConstants.VISITOR_AGENCE_EMAIL, vars);

            List<String> combinedVisitorList = new ArrayList<>();
            List<String> combinedListToCreateVisitor = new ArrayList<>();
            List<String> visitorIdsToCreate = new ArrayList<>();

            if (!visitorIdList.isEmpty()) {
                List<String> visitorIds = visitorIdList.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
                combinedVisitorList.addAll(visitorIds);
            }

            if (!visitorEmailList.isEmpty()) {
                List<String> visitorEmails = visitorEmailList.stream()
                    .map(Object::toString)
                    .map(email -> {
                        SignUpRequest signUpRequest = new SignUpRequest();
                        signUpRequest.setEmail(email);
                        Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(signUpRequest).getBody();
                        visitorIdsToCreate.add(result.get("id").toString());
                        return result.get("username").toString();
                    })
                    .collect(Collectors.toList());
                combinedVisitorList.addAll(visitorEmails);
                combinedListToCreateVisitor.addAll(visitorIdsToCreate);
                delegateExecution.setVariable("visitorToCreateList", combinedListToCreateVisitor);
                delegateExecution.removeVariable(ProcessConstants.VISITOR_AGENCE_EMAIL);
            }

            if (!combinedVisitorList.isEmpty()) {
                List<String> uniqueVisitorList = combinedVisitorList.stream().distinct().collect(Collectors.toList());
                delegateExecution.setVariable(ProcessConstants.LIST_OF_VISITOR, uniqueVisitorList);
            }
        }

        if (!searchAllNestedMap(ProcessConstants.DELGATE_AGENCE_EMAIL, vars).isEmpty() ||
            !searchAllNestedMap(ProcessConstants.DELEGATE_AGENCE_ID, vars).isEmpty()) {
            List<Object> medicalDelegateEmailList = searchAllNestedMap(
                ProcessConstants.DELGATE_AGENCE_EMAIL, vars);
            List<Object> medicalDelegateIdList = searchAllNestedMap(
                ProcessConstants.DELEGATE_AGENCE_ID, vars);

            List<String> combinedMedicalDelegateList = new ArrayList<>();
            List<String> combinedListDelegateToCreate = new ArrayList<>();

            List<String> medicalDelegateIdsToCreate = new ArrayList<>();

            if (!medicalDelegateEmailList.isEmpty()) {
                List<String> medicalDelegateEmails = medicalDelegateEmailList.stream()
                    .map(Object::toString)
                    .map(email -> {
                        SignUpRequest signUpRequest = new SignUpRequest();
                        signUpRequest.setEmail(email);
                        Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(signUpRequest).getBody();
                        medicalDelegateIdsToCreate.add(result.get("id").toString());
                        return result.get("username").toString();
                    })
                    .collect(Collectors.toList());
                combinedMedicalDelegateList.addAll(medicalDelegateEmails);
                combinedListDelegateToCreate.addAll(medicalDelegateIdsToCreate);
                delegateExecution.setVariable("delegateToCreateList", combinedListDelegateToCreate);
                delegateExecution.removeVariable(ProcessConstants.DELGATE_AGENCE_EMAIL);
            }

            if (!medicalDelegateIdList.isEmpty()) {
                List<String> medicalDelegateIds = medicalDelegateIdList.stream()
                    .map(Object::toString)
                    .collect(Collectors.toList());
                combinedMedicalDelegateList.addAll(medicalDelegateIds);
            }

            if (!combinedMedicalDelegateList.isEmpty()) {
                List<String> uniqueAssigneeList = combinedMedicalDelegateList.stream().distinct().collect(Collectors.toList());
                delegateExecution.setVariable(ProcessConstants.LIST_OF_DELEGATE, uniqueAssigneeList);
            }

        }

        if (searchNestedMap(ProcessConstants.PHARMACY_AGENCE_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.PHARMACY_AGENCE_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.PHARMACY_AGENCE_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_PHARAMACY,result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.PHARMACY_AGENCE_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.PHARMACY_AGENCE_ID, vars) != null) {
            String username = searchNestedMap(ProcessConstants.PHARMACY_AGENCE_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_PHARAMACY,id);
        }


        if (searchNestedMap(ProcessConstants.RESPONSABLE_AGENCE_PCT_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.RESPONSABLE_AGENCE_PCT_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.RESPONSABLE_AGENCE_PCT_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_AGENCE_PCT,result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.RESPONSABLE_AGENCE_PCT_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_AGENCE_PCT_ID, vars) != null) {
            String username = searchNestedMap(ProcessConstants.RESPONSABLE_AGENCE_PCT_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_AGENCE_PCT,id);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_PCT_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.RESPONSABLE_PCT_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.RESPONSABLE_PCT_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_PCT,result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.RESPONSABLE_PCT_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_PCT_ID, vars) != null) {
            String username = searchNestedMap(ProcessConstants.RESPONSABLE_PCT_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_PCT,id);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_FILIALE_PCT_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.RESPONSABLE_FILIALE_PCT_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.RESPONSABLE_FILIALE_PCT_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_FILIALE_PCT,result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.RESPONSABLE_FILIALE_PCT_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_FILIALE_PCT_ID, vars) != null) {
            String username = searchNestedMap(ProcessConstants.RESPONSABLE_FILIALE_PCT_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_FILIALE_PCT,id);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_CRO_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.RESPONSABLE_CRO_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.RESPONSABLE_CRO_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_CRO,result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.RESPONSABLE_CRO_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_CRO_ID, vars) != null) {
            String username = searchNestedMap(ProcessConstants.RESPONSABLE_CRO_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_CRO,id);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_INVESTIGATION_CRO,result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_ID, vars) != null) {
            String username = searchNestedMap(ProcessConstants.RESPONSABLE_INVESTIGATION_CRO_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_INVESTIGATION_CRO,id);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_EMAIL, vars) != null) {
            String email = searchNestedMap(
                ProcessConstants.RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_TECHNIQUE ,
                result.get("id"));
            delegateExecution.removeVariable(
                ProcessConstants.RESPONSABLE_TECHNIQUE_IMPORT_EXPORT_EMAIL);

        }
        if (searchNestedMap(ProcessConstants.REPRESENTANT_LEGAL_IMPORT_EXPORT_IDENTIFIER, vars) != null) {
            String username = searchNestedMap(
                ProcessConstants.REPRESENTANT_LEGAL_IMPORT_EXPORT_IDENTIFIER, vars).toString();
            delegateExecution.setVariable(ProcessConstants.REPRESENTANT_LEGAL_IMPORT_EXPORT_ID,
                username);
        }

        if (searchNestedMap(ProcessConstants.REPRESENTANT_LEGAL_IMPORT_EXPORT_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.REPRESENTANT_LEGAL_IMPORT_EXPORT_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.REPRESENTANT_LEGAL_IMPORT_EXPORT_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_LEGAL,
                result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.REPRESENTANT_LEGAL_IMPORT_EXPORT_EMAIL);
        }
        /*Industrie*/
        if (searchNestedMap(ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_QUALITY_CONTROL_INDUSTRIE,result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_ID, vars) != null) {
            String username = searchNestedMap(ProcessConstants.QUALITY_CONTROL_INDUSTRIE_HUMAN_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_QUALITY_CONTROL_INDUSTRIE,id);
        }

        if (searchNestedMap(ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_EMAIL, vars) != null) {
            String email = searchNestedMap(
                ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_PRODUCTION_OPERATION_INDUSTRIE,result.get("id"));
            delegateExecution.removeVariable(
                ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_ID, vars) != null) {
            String username = searchNestedMap(
                ProcessConstants.PRODUCTION_OPERATION_INDUSTRIE_HUMAN_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_PRODUCTION_OPERATION_INDUSTRIE,id);
        }



        if (searchNestedMap(ProcessConstants.PRT_INDUSTRIE_HUMAN_EMAIL, vars) != null) {
            String email = searchNestedMap(ProcessConstants.PRT_INDUSTRIE_HUMAN_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.PRT_INDUSTRIE_HUMAN_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_PRT_INDUSTRIE,result.get("id"));
            delegateExecution.removeVariable(ProcessConstants.PRT_INDUSTRIE_HUMAN_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.PRT_INDUSTRIE_HUMAN_ID, vars) != null) {
            String username = searchNestedMap(ProcessConstants.PRT_INDUSTRIE_HUMAN_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_PRT_INDUSTRIE,id);
        }


        if (searchNestedMap(ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_EMAIL, vars) != null) {
            String email = searchNestedMap(
                ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_AFFAIRE_INDUSTRIE,result.get("id"));
            delegateExecution.removeVariable(
                ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_ID, vars) != null) {
            String username = searchNestedMap(
                ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_AFFAIRE_INDUSTRIE,id);
        }
        if (searchNestedMap(ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_IDENTIFIER, vars) != null) {
            String username = searchNestedMap(
                ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_IDENTIFIER, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.RESPONSABLE_AFFAIRE_INDUSTRIE_HUMAN_ID,username);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_AFFAIRE_INDUSTRIE,id);
        }
        if (searchNestedMap(ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_EMAIL, vars) != null) {
            String email = searchNestedMap(
                ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_EMAIL, vars).toString();
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setEmail(email);
            Map<String, Object> result = (Map<String, Object>) this.authenticateService.signupByEmail(
                signUpRequest).getBody();
            delegateExecution.setVariable(
                ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_ID,
                result.get("username"));
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_CREATE_GROSSISTE,result.get("id"));
            delegateExecution.removeVariable(
                ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_EMAIL);
        }

        if (searchNestedMap(ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_ID, vars) != null) {
            String username = searchNestedMap(
                ProcessConstants.RESPONSBALE_PHARAMCIEN_CREATION_GROSSISTE_ID, vars).toString();
            UUID id = Optional.ofNullable(userService.findUserByUserName(username).getBody())
                .map(user -> user.getId())
                .orElse(null);
            delegateExecution.setVariable(ProcessConstants.LIST_OF_RESPONSABLE_CREATE_GROSSISTE,id);
        }
        List<Map<String, Object>> dataGridLocalEtranger = (List<Map<String, Object>>) delegateExecution.getVariable("customdatagrid5");
        List<Map<String, Object>> dataGridDelegate = (List<Map<String, Object>>) delegateExecution.getVariable("customdatagrid");

        List<Map<String, Object>> dataGridVisitor = (List<Map<String, Object>>) delegateExecution.getVariable("customdatagrid1");

        boolean localToCreate = false;
        boolean strangerToCreate = false;
        boolean delegateToCreate = false;
        boolean visitorToCreate = false;
        boolean labStrangerToCreate = false;


        if (dataGridLocalEtranger != null) {
            localToCreate = dataGridLocalEtranger.stream()
                .anyMatch(ligne -> "non".equals(ligne.get("VALIDATION_EXISTENCE_ACCOUNT_PROMOTION_LAB_RESPONSABLE")));
            strangerToCreate = dataGridLocalEtranger.stream()
                .anyMatch(ligne -> "non".equals(ligne.get("VALIDATION_EXISTENCE_ACCOUNT_PROMOTION_LAB_RESPONSABLE2")));
            labStrangerToCreate = dataGridLocalEtranger.stream()
                .anyMatch(ligne -> "non".equals(ligne.get("VALIDATION_EXISTENCE_ACCOUNT_PROMOTION_FOREIGN_PHARMACEUTICAL_LAB")));

        }
        if (dataGridDelegate != null) {
            delegateToCreate = dataGridDelegate.stream()
                .anyMatch(ligne -> "non".equals(ligne.get("VALIDATION_EXISTENCE_ACCOUNT_MEDICAL_DELEGATE")));
               }
        if (dataGridVisitor != null) {
            visitorToCreate = dataGridVisitor.stream()
                .anyMatch(ligne -> "non".equals(ligne.get("VALIDATION_EXISTENCE_ACCOUNT_PHARMACEUTICAL_VISITOR")));
        }

        delegateExecution.setVariable("localToCreate", localToCreate);
        delegateExecution.setVariable("etrangerToCreate", strangerToCreate);
        delegateExecution.setVariable("delegateToCreate", delegateToCreate);
        delegateExecution.setVariable("visitorToCreate", visitorToCreate);
        delegateExecution.setVariable("labStrangerToCreate", labStrangerToCreate);
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

    public static List<Object> searchAllNestedMap(String targetKey, Object container) {
        List<Object> results = new ArrayList<>();
        if (container == null || targetKey == null) {
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
        return results;
    }
}
