package com.pfe.delegate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.config.Constants;
import com.pfe.domain.enumeration.RegistryStatus;
import com.pfe.dto.ActivityTypeDTO;
import com.pfe.dto.RoleDTO;
import com.pfe.dto.UserDTO;
import com.pfe.dto.request.CreateCompanyAccountRequest;
import com.pfe.feignServices.RoleService;
import com.pfe.feignServices.UserService;

import java.util.*;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.pfe.delegate.ProcessConstants.*;

@Component
public class CreateCompanyDelegate implements JavaDelegate {

    private final UserService userService;
    private final RoleService roleService;
    private static final Logger log = LoggerFactory.getLogger(CreateCompanyDelegate.class);


    public CreateCompanyDelegate(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> vars = delegateExecution.getVariables();
        String address = getVariableAsString(delegateExecution, "COMPANY_ADDRESS");
        String phoneNumber = getVariableAsString(delegateExecution, "PHONE_NUMBER");
        String email = getVariableAsString(delegateExecution, "E_MAIL", "EMAIL_ADDRESS",
            "MAIL_ADDRESS1");
        String taxRegistration = getVariableAsString(delegateExecution, "matriculeFiscalRne");
        String socialReason = getVariableAsString(delegateExecution, "TRADE_NAME");
        String legalStatus = getVariableAsString(delegateExecution, "LEGAL_FORM");
        HashSet<ActivityTypeDTO> activities = buildActivityTypeSetFromDataUsingSearch(vars);
        String fileStatus = getFileName(delegateExecution.getVariable("statutDeLaSociete"));
        String filePatent = getFileName(delegateExecution.getVariable("patente"));
        String denomination = getVariableAsString(delegateExecution, "COMPANY_NAME");
        RegistryStatus registryStatus = getRegistryStatus(getVariableAsString(delegateExecution, "REGISTRATION_STATUS"));

        CreateCompanyAccountRequest createCompanyAccountRequest = new CreateCompanyAccountRequest();
        createCompanyAccountRequest.setAddress(address);
        createCompanyAccountRequest.setPhoneNumber(phoneNumber);
        createCompanyAccountRequest.setEmail(email);
        createCompanyAccountRequest.setTaxRegistration(taxRegistration);
        createCompanyAccountRequest.setSocialReason(socialReason);
        createCompanyAccountRequest.setLegalStatus(legalStatus);
        createCompanyAccountRequest.setActivitiesType(activities);
        createCompanyAccountRequest.setFileStatus(fileStatus);
        createCompanyAccountRequest.setFilePatent(filePatent);
        createCompanyAccountRequest.setRegistryStatus(registryStatus);
        createCompanyAccountRequest.setDenomination(denomination);
        RoleDTO roleDTO = new RoleDTO();
        RoleDTO ectdRole = null;
        if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(AGENCE_PROMO_MODEL_LTS)) {
            ectdRole = new RoleDTO();
            roleDTO.setLabel(Constants.BS_ROLE_PM_AGENCE_PROMOTION);
            roleDTO.setId(roleService.getRoleByLabel(Constants.BS_ROLE_PM_AGENCE_PROMOTION).getBody().getId());
            ectdRole.setId(roleService.getRoleByLabel(Constants.BS_ROLE_ECTD).getBody().getId());
            ectdRole.setLabel(Constants.BS_ROLE_ECTD);
        } else if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(CRO_MODEL_LTS)) {
            roleDTO.setLabel(Constants.BS_ROLE_PM_CRO);
        }
        else if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            &&((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(IMPORT_EXPORT_MODEL_LTS)) {
            Object roleObject = delegateExecution.getVariable("rolePM");

            if (roleObject instanceof java.util.List) {
                java.util.List<?> roleList = (java.util.List<?>) roleObject;
                if (!roleList.isEmpty() && roleList.get(0) instanceof java.util.Map) {
                    java.util.Map<?, ?> roleMap = (java.util.Map<?, ?>) roleList.get(0);
                    String roleValue = (String) roleMap.get("rolePM");
                    roleDTO.setLabel(roleValue);
                    roleDTO.setId(roleService.getRoleByLabel(roleValue).getBody().getId());
                }
            }


        }
        else if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(AGENCE_PCT_MODEL_LTS)) {
            roleDTO.setLabel(Constants.BS_ROLE_PM_AGENCE_PCT);
        }
        else if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(FILIALE_PCT_MODEL_LTS)) {
            roleDTO.setLabel(Constants.BS_ROLE_PM_FILIALE_PCT);
        }
        else if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(CREATE_PCT_MODEL_LTS)) {
            roleDTO.setLabel(Constants.BS_ROLE_PM_PCT);
        }
        else if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && (((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(INDUSTRIE_HUMAIN_MODEL_LTS) || delegateExecution.getProcessDefinitionId().contains(INDUSTRIE_VETERINAIRE_MODEL_LTS))) {
            ectdRole = new RoleDTO();
            roleDTO.setLabel(Constants.BS_ROLE_PM_INDUSTRIEL);
            roleDTO.setId(roleService.getRoleByLabel(Constants.BS_ROLE_PM_INDUSTRIEL).getBody().getId());
            ectdRole.setId(roleService.getRoleByLabel(Constants.BS_ROLE_ECTD).getBody().getId());
            ectdRole.setLabel(Constants.BS_ROLE_ECTD);
        }
        else if (delegateExecution.getProcessDefinitionId() != null
            && !delegateExecution.getProcessDefinitionId().isEmpty()
            && ((ExecutionEntity) delegateExecution).getProcessDefinition().getKey().contains(CREATION_GROSSISTE_MODEL_LTS)) {
            roleDTO.setLabel(Constants.BS_ROLE_PM_GROSSISTE);
        }
        else {
            roleDTO.setLabel(Constants.DEFAULT_ROLE);
        }
        if (ectdRole != null) {
            createCompanyAccountRequest.setRoles(Set.of(roleDTO,ectdRole));
        }
        else{
            createCompanyAccountRequest.setRoles(Set.of(roleDTO));
        }
        log.info("Company Creation Data : {}", createCompanyAccountRequest);
        ResponseEntity<UserDTO> companyUser = userService.createCompanyUser(createCompanyAccountRequest);
        delegateExecution.setVariable("PmKeycloakId",companyUser.getBody().getId());

        log.info("Created Company : {}", companyUser);
    }


    private String getVariableAsString(DelegateExecution delegateExecution, String... variableNames) {
        for (String variableName : variableNames) {
            Object variable = delegateExecution.getVariable(variableName);
            if (variable != null) {
                return variable.toString();
            }
        }
        return null;
    }

    private String getFileName(Object file) throws JsonProcessingException {
        if (file == null) {
            return null;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(file);
        List<Map<String, Object>> list = objectMapper.readValue(json, new TypeReference<>() {});
        return (String) list.get(0).get("name");
    }

    private RegistryStatus getRegistryStatus(String registryStatus) {
        if ("actif".equals(registryStatus)) {
            return RegistryStatus.ACTIVE;
        } else if ("suspendu".equals(registryStatus)) {
            return RegistryStatus.SUSPENDED;
        } else {
            return RegistryStatus.IN_STOP;
        }
    }


    private HashSet<ActivityTypeDTO> buildActivityTypeSetFromDataUsingSearch(Map<String, Object> activityFormData) {
        HashSet<ActivityTypeDTO> activityDomain = new HashSet<>();

        List<Object> primaryActivities = searchAllNestedMap("PRIMARY_ACTIVITY", activityFormData);
        List<Object> primaryCodes = searchAllNestedMap("PRIMARY_ACTIVITY_CODE", activityFormData);

        int maxSize = Math.max(primaryActivities.size(), primaryCodes.size());
        for (int i = 0; i < maxSize; i++) {
            String activity = (i < primaryActivities.size() && primaryActivities.get(i) != null) ? primaryActivities.get(i).toString() : null;
            String code = (i < primaryCodes.size() && primaryCodes.get(i) != null) ? primaryCodes.get(i).toString() : null;

            if (activity != null || code != null) {
                ActivityTypeDTO activityTypeDTO = new ActivityTypeDTO();
                activityTypeDTO.setName(activity);
                activityTypeDTO.setCode(code);
                activityDomain.add(activityTypeDTO);
            }
        }

        return activityDomain;
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
