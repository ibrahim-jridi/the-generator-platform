package com.pfe.delegate;
import com.pfe.dto.ProfileDTO;
import com.pfe.dto.UserDTO;
import com.pfe.feignServices.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SavePMDelegate implements JavaDelegate {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(SavePMDelegate.class);

    public SavePMDelegate(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

       String PmKeycloakId = delegateExecution.getVariable("PmKeycloakId").toString();

        List<String> keys = List.of(ProcessConstants.COMPANY_NAME, ProcessConstants.URL_WEB_SITE, ProcessConstants.IMPORT_EXPORT_PRODUCT_CATEGORY, ProcessConstants.ANOTHER_CATEGORIES);
        Map<String, String> profileData = new HashMap<>();

        for (String key : keys) {
            Object value = delegateExecution.getVariable(key);
            if (value != null) {
                profileData.put(key, value.toString());
            }
        }

        if (profileData.isEmpty()) {
            log.error("No manager profile data found in form submission for user {}", PmKeycloakId);
            throw new RuntimeException("Manager profile data cannot be null or empty");
        }

        List<String> selectedCategories = new ArrayList<>();
        Object rawCategory = delegateExecution.getVariable(ProcessConstants.IMPORT_EXPORT_PRODUCT_CATEGORY);
        if (rawCategory instanceof Map<?, ?> rawMap) {
            for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key instanceof String keyStr && value != null && "true".equalsIgnoreCase(value.toString())) {
                    String label = convertCamelCaseToLabel(keyStr);
                    selectedCategories.add(label);
                }
            }
        }

        String companyName = profileData.get(ProcessConstants.COMPANY_NAME);
        String webSite = profileData.get(ProcessConstants.URL_WEB_SITE);
        String categoryString = String.join(",", selectedCategories);
        String anotherCategories = profileData.get(ProcessConstants.ANOTHER_CATEGORIES);

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setKeycloakId(UUID.fromString(PmKeycloakId));
        profileDTO.setCompanyName(companyName);
        profileDTO.setWebSite(webSite);
        profileDTO.setProductCategory(categoryString);
        profileDTO.setAnotherCategories(anotherCategories);

        profileData.put(ProcessConstants.IMPORT_EXPORT_PRODUCT_CATEGORY, categoryString);
        profileDTO.setProfileData(profileData);

        ResponseEntity<UserDTO> response = userService.saveDataPm(PmKeycloakId,profileDTO);
        log.info("Manager Profile Data saved successfully for user {}: {}", PmKeycloakId, response);

    }


    private String convertCamelCaseToLabel(String input) {
        String spaced = input.replaceAll("([a-z])([A-Z])", "$1 $2");
        return Arrays.stream(spaced.split(" "))
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
            .collect(Collectors.joining(" "));
    }
}

