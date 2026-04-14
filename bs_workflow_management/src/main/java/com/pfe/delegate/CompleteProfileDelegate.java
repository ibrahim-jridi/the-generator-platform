package com.pfe.delegate;

import com.pfe.config.Constants;
import com.pfe.dto.RoleDTO;
import com.pfe.dto.ProfileDTO;
import com.pfe.dto.UserDTO;
import com.pfe.dto.request.UpdateProfileRequest;
import com.pfe.feignServices.RoleService;
import com.pfe.feignServices.UserService;

import java.util.*;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CompleteProfileDelegate implements JavaDelegate {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(CompleteProfileDelegate.class);
    public static final String DEFAULT_ROLE = Constants.DEFAULT_ROLE;
    private final RoleService roleService;
    public static final String BS_ROLE_ECTD = Constants.BS_ROLE_ECTD;


    public CompleteProfileDelegate(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String userId = (String) delegateExecution.getVariable("starter");
        UUID userUUID = UUID.fromString(userId);

        Map<String, Object> vars = delegateExecution.getVariables();
        Map<String, String> profileData = new HashMap<>();
        for (Map.Entry<String, Object> entry : vars.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) continue;

            if (isSimpleType(value)) {
                String val = value.toString();

                if (val.contains("=") && !key.matches("[A-Z_]+")) {
                    String[] parts = val.split("=", 2);
                    if (parts.length == 2 && parts[0] != null && parts[1] != null) {
                        profileData.put(parts[0].trim(), parts[1].trim());
                    }
                }
                if (key.equalsIgnoreCase("profileData") && val.contains("=")) {
                    String[] parts = val.split("=", 2);
                    if (parts.length == 2 && parts[0] != null && parts[1] != null) {
                        profileData.put(parts[0].trim(), parts[1].trim());
                    }
                }else {
                    profileData.put(key, val);
                }
            }

            else if (value instanceof List<?>) {
                List<?> list = (List<?>) value;
                for (Object item : list) {
                    if (item instanceof Map<?, ?> map) {
                        if (map.containsKey("name") && map.containsKey("url") && map.containsKey("type")) {
                            profileData.put(key, map.toString());
                        } else {

                            for (Map.Entry<?, ?> cell : map.entrySet()) {
                                if (cell.getKey() != null && cell.getValue() != null) {
                                    profileData.put(cell.getKey().toString(), cell.getValue().toString());
                                }
                            }
                        }
                    }
                }
            }
        }


        if (profileData.isEmpty()) {
            log.error("No profile data found in form submission for user {}", userUUID);
            throw new RuntimeException("Profile data cannot be null or empty");
        }

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setProfileData(profileData);

        log.info("Request to complete user profile with id : {}", userUUID);

        Object roleObject = delegateExecution.getVariable("role");

        if (roleObject instanceof java.util.List) {
            java.util.List<?> roleList = (java.util.List<?>) roleObject;
            if (!roleList.isEmpty() && roleList.get(0) instanceof java.util.Map) {
                java.util.Map<?, ?> roleMap = (java.util.Map<?, ?>) roleList.get(0);
                String roleValue = (String) roleMap.get("role");

                if (!roleValue.contains(DEFAULT_ROLE)) {
                    Set<RoleDTO> roleDTOSet = new HashSet<>();
                    UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
                    UserDTO userUpdate = userService.getuser(userUUID).getBody();
                    RoleDTO roleDTO = roleService.getRoleByLabel(roleValue).getBody();
                    RoleDTO defaultRole = roleService.getRoleByLabel(DEFAULT_ROLE).getBody();
                    RoleDTO ectdRole = roleService.getRoleByLabel(BS_ROLE_ECTD).getBody();
                    boolean pmHasEctd = userService.doesPmHaveEctd(userUUID).getBody();
                    roleDTOSet.add(roleDTO);
                    roleDTOSet.add(defaultRole);
                    if (pmHasEctd) {
                    roleDTOSet.add(ectdRole);
                    }
                    userUpdate.setRoles(roleDTOSet);
                    userUpdate.setProfileCompleted(true);
                    updateProfileRequest.setUserDTO(userUpdate);
                    updateProfileRequest.setProfileDTO(profileDTO);
                    ResponseEntity<UserDTO> updatedUser = userService.updateAndCompletedProfile(updateProfileRequest);
                    log.info("User profile completed : {}", updatedUser);
                } else {

                    ResponseEntity<UserDTO> completedUserProfile = this.userService.saveAndCompleteMyProfile(
                        profileDTO);
                    log.info("User profile completed : {}" ,completedUserProfile);
                }
            }
        }
    }
    private boolean isSimpleType(Object value) {
        return value instanceof String || value instanceof Number || value instanceof Boolean;
    }


}
