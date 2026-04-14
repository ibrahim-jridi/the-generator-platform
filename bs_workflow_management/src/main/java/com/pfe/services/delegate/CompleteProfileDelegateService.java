package com.pfe.services.delegate;

import com.pfe.dto.UserDTO;
import com.pfe.feignServices.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CompleteProfileDelegateService implements JavaDelegate {

    private UserService userService;

    CompleteProfileDelegateService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        System.out.println("azer");
        //setProfileCompleted
        String starter = (String) delegateExecution.getVariable("starter");
        UUID starterId = UUID.fromString(starter);
        if (starterId != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setProfileCompleted(true);
            userDTO.setId(starterId);
            userService.completeUserProfile(starterId,userDTO);

        }
    }
}
