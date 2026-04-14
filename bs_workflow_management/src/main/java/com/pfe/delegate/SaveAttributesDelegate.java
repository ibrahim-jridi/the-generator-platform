package com.pfe.delegate;


import com.pfe.feignServices.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SaveAttributesDelegate implements JavaDelegate {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(SaveAttributesDelegate.class);


    public SaveAttributesDelegate(UserService userService) {

        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        UUID pmIdDesignation = null;

        if(delegateExecution.getVariable("starter") != null){
            pmIdDesignation = extractUserId(delegateExecution.getVariable("starter"));
            String username = userService.getUsernameById(pmIdDesignation).getBody();

            try {
                userService.saveDesignationsUsersAttributes(username);
                log.info("Successfully saved user designation attributes for username: {}", username);
            } catch (Exception e) {
                log.error("Error while saving user designation attributes for username: {}", username, e);
                throw e;
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




}
