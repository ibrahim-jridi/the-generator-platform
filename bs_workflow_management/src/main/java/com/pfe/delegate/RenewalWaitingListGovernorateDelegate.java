package com.pfe.delegate;

import com.pfe.domain.enumeration.Category;
import com.pfe.domain.enumeration.Governorate;
import com.pfe.domain.enumeration.StatusWaitingList;
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
public class RenewalWaitingListGovernorateDelegate implements JavaDelegate {

    private final WaitingListService waitingListService;
    private static final Logger log = LoggerFactory.getLogger(RenewalWaitingListGovernorateDelegate.class);

    public RenewalWaitingListGovernorateDelegate(WaitingListService waitingListService) {
        this.waitingListService = waitingListService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        Map<String, Object> vars = delegateExecution.getVariables();
        WaitingListDTO waitingListDTO = new WaitingListDTO();
        String userId = SecurityUtils.getUserIdFromCurrentUser();
        UUID userUuid = UUID.fromString(userId);
        waitingListDTO.setIdUser(userUuid);
        waitingListDTO.setStatus(StatusWaitingList.REGISTRED);

        if (searchNestedMap("LIST_SUBMIT_FORM_PHARMACY_CATEGORY", vars) != null) {
            String category = searchNestedMap("LIST_SUBMIT_FORM_PHARMACY_CATEGORY", vars).toString();
            if(category.contains("categorieA")){
                waitingListDTO.setCategory(Category.CATEGORY_A);
            }
            else{
                waitingListDTO.setCategory(Category.CATEGORY_B);
            }
        }
        if (searchNestedMap("LIST_SUBMIT_FORM_GOVERNORATE_LIST", vars) != null) {
            String governorateForm = searchNestedMap("LIST_SUBMIT_FORM_GOVERNORATE_LIST", vars).toString();
            Governorate governorateEnum = Governorate.valueOf(governorateForm.toUpperCase());
            waitingListDTO.setGovernorate(governorateEnum);
        }
        if (searchNestedMap("LIST_SUBMIT_FORM_DELEGATION", vars) != null) {
            String delegation = searchNestedMap("LIST_SUBMIT_FORM_DELEGATION", vars).toString();
            waitingListDTO.setDelegation(delegation);
        }
        for (int i = 1; i <= 24; i++) {
            String key = "LIST_SUBMIT_FORM_DELEGATION" + i;
            Object value = searchNestedMap(key, vars);

            if (value != null) {
                waitingListDTO.setDelegation(value.toString());
                break;
            }
        }
        if (searchNestedMap("LIST_SUBMIT_FORM_COMMUNES", vars) != null) {
            String municipality = searchNestedMap("LIST_SUBMIT_FORM_COMMUNES", vars).toString();
            waitingListDTO.setMunicipality(municipality);
        }

        for (int i = 1; i <= 23; i++) {
            String key = "LIST_SUBMIT_FORM_COMMUNES" + i;
            Object value = searchNestedMap(key, vars);

            if (value != null) {
                waitingListDTO.setDelegation(value.toString());
                break;
            }
        }
        this.waitingListService.renewalWaitingListForGovernorate(userUuid,waitingListDTO);
        log.info("Waiting List Created : {}", waitingListDTO);
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
