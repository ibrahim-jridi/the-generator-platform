package com.pfe.delegate;

import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.dto.WaitingListDTO;
import com.pfe.feignServices.WaitingListService;
import com.pfe.security.SecurityUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component

public class ChangeCategoryDelegate implements JavaDelegate {
    private final WaitingListService waitingListService;

    private static final Logger log = LoggerFactory.getLogger(ChangeCategoryDelegate.class);


    public ChangeCategoryDelegate(WaitingListService waitingListService) {
        this.waitingListService = waitingListService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> vars = delegateExecution.getVariables();

        try {
            String userId = SecurityUtils.getUserIdFromCurrentUser();
            log.debug("Retrieved user ID from security context: {}", userId);

            UUID userUuid = UUID.fromString(userId);
            log.debug("Converted user ID to UUID: {}", userUuid);

            WaitingListDTO waitingListDTO = new WaitingListDTO();

            if (searchNestedMap("INSCRIPTION_WAITING_LIST_SUBMIT_FORM_PHARMACY_CATEGORY", vars) != null) {
                String category = searchNestedMap("INSCRIPTION_WAITING_LIST_SUBMIT_FORM_PHARMACY_CATEGORY", vars).toString();

                if (category.contains("categorieA")) {
                    waitingListDTO.setCategory(Category.CATEGORY_A);

                    if (searchNestedMap("LIST_SUBMIT_FORM_DELEGATION", vars) != null) {
                        waitingListDTO.setDelegation(searchNestedMap("LIST_SUBMIT_FORM_DELEGATION", vars).toString());
                    } else {
                        for (int i = 1; i <= 24; i++) {
                            String key = "LIST_SUBMIT_FORM_DELEGATION" + i;
                            Object value = searchNestedMap(key, vars);
                            if (value != null) {
                                waitingListDTO.setDelegation(value.toString());
                                break;
                            }
                        }
                    }

                    waitingListDTO.setMunicipality(null);

                } else {
                    waitingListDTO.setCategory(Category.CATEGORY_B);

                    if (searchNestedMap("LIST_SUBMIT_FORM_COMMUNES", vars) != null) {
                        waitingListDTO.setMunicipality(searchNestedMap("LIST_SUBMIT_FORM_COMMUNES", vars).toString());
                    } else {
                        for (int i = 1; i <= 23; i++) {
                            String key = "LIST_SUBMIT_FORM_COMMUNES" + i;
                            Object value = searchNestedMap(key, vars);
                            if (value != null) {
                                waitingListDTO.setMunicipality(value.toString());
                                break;
                            }
                        }
                    }

                    waitingListDTO.setDelegation(null);
                }
            }


            if (searchNestedMap("LIST_SUBMIT_FORM_GOVERNORATE_LIST", vars) != null) {
                String governorateForm = searchNestedMap("LIST_SUBMIT_FORM_GOVERNORATE_LIST", vars).toString();
                Governorate governorateEnum = Governorate.valueOf(governorateForm.toUpperCase());
                waitingListDTO.setGovernorate(governorateEnum);
            }

            this.waitingListService.renewUserCategory(userUuid,waitingListDTO);
            log.info("Successfully renewed category for user [{}]", userUuid);


        } catch (Exception e) {
            log.error("Error in ChangeCategoryDelegate: {}", e.getMessage(), e);
            throw e;
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
