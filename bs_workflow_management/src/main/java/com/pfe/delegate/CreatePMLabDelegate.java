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
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class CreatePMLabDelegate implements JavaDelegate {
    private static final Logger log = LoggerFactory.getLogger(CreatePMLabDelegate.class);
    private final UserService userService;
    private final RoleService roleService;

    public CreatePMLabDelegate(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> vars = delegateExecution.getVariables();
        List<String> labToCreate = new ArrayList<>();
        delegateExecution.setVariable(ProcessConstants.LIST_LAB_STRANGER_TO_CREATE, labToCreate);
        Boolean labStrangerToCreate = (Boolean) delegateExecution.getVariable("labStrangerToCreate");
       if(Boolean.TRUE.equals(labStrangerToCreate)){

           List<Object> emailList = searchAllNestedMap(ProcessConstants.EMAIL_STRANGER_LAB, vars);
           List<Object> taxList = searchAllNestedMap(ProcessConstants.TAX_REGISTRATION_STRANGER_LAB, vars);
        List<Object> denominationList = searchAllNestedMap(
            ProcessConstants.DENOMINATION_STRANGER_LAB, vars);
        List<Object> tradNameList = searchAllNestedMap(ProcessConstants.TRAD_NAME_STRANGER_LAB, vars);
        List<Object> companyAddressList = searchAllNestedMap(
            ProcessConstants.COMPANY_ADDRESS_STRANGER_LAB, vars);
        List<Object> legalFormList = searchAllNestedMap(ProcessConstants.LEGAL_FORM_STRANGER_LAB, vars);
        List<Object> registrationStatusList = searchAllNestedMap(
            ProcessConstants.REGISTRATION_STATUS_STRANGER_LAB, vars);
        List<Object> phoneNumebreList = searchAllNestedMap(
            ProcessConstants.PHONE_NUMBER_STRANGER_LAB, vars);
        List<Object> fileCompanyStatusList = searchAllNestedMap(
            ProcessConstants.FILE_COMPANY_STATUS_STRANGER_LAB, vars);
        List<Object> filePatenteList = searchAllNestedMap(ProcessConstants.FILE_PATENTE_STRANGER_LAB, vars);

        int size = emailList.size();

        HashSet<ActivityTypeDTO> activities = buildActivityTypeSetFromDataUsingSearch(vars);
        List<CreateCompanyAccountRequest> signUpRequests = IntStream.range(0, size)
            .mapToObj(i -> {
                CreateCompanyAccountRequest request = new CreateCompanyAccountRequest();
                request.setEmail(emailList.get(i).toString());
                request.setTaxRegistration(taxList.get(i).toString());
                request.setDenomination(denominationList.get(i).toString());
                request.setSocialReason(tradNameList.get(i).toString());
                request.setAddress(companyAddressList.get(i).toString());

                request.setActivitiesType(activities);
                request.setLegalStatus(legalFormList.get(i).toString());
                request.setPhoneNumber(phoneNumebreList.get(i).toString());

                RegistryStatus registryStatus = getRegistryStatus(registrationStatusList.get(i).toString());
                request.setRegistryStatus(registryStatus);

                try {
                    String fileStatusName = getFileName(fileCompanyStatusList.get(i));
                    String filePatentName = getFileName(filePatenteList.get(i));
                    request.setFileStatus(fileStatusName);
                    request.setFilePatent(filePatentName);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException("Error while extracting file names", e);
                }
                RoleDTO roleDTO = new RoleDTO();
                RoleDTO ectdRole = new RoleDTO();
                roleDTO.setLabel(Constants.BS_ROLE_PM_LABO_ETRANGER);
                roleDTO.setId(roleService.getRoleByLabel(Constants.BS_ROLE_PM_LABO_ETRANGER).getBody().getId());
                ectdRole.setId(roleService.getRoleByLabel(Constants.BS_ROLE_ECTD).getBody().getId());
                ectdRole.setLabel(Constants.BS_ROLE_ECTD);
                request.setRoles(Set.of(roleDTO,ectdRole));
                ResponseEntity<UserDTO> companyUser = userService.createCompanyUser(request);
                labToCreate.add(companyUser.getBody().getUsername());
                delegateExecution.setVariable(ProcessConstants.LIST_LAB_STRANGER_TO_CREATE, labToCreate);
                return request;
            })
            .collect(Collectors.toList());

       }
else {
           log.info("No Stranger Lab To Create");
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

        List<Object> primaryActivities = searchAllNestedMap(
            ProcessConstants.PRIMARY_ACTIVITY_STRANGER_LAB, activityFormData);
        List<Object> primaryCodes = searchAllNestedMap(
            ProcessConstants.PRIMARY_ACTIVITY_CODE_STRANGER_LAB, activityFormData);

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

}
