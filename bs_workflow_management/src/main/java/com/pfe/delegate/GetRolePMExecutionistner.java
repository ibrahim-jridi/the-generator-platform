package com.pfe.delegate;

import com.pfe.config.Constants;
import com.pfe.dto.RoleDTO;
import com.pfe.feignServices.UserService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



import java.util.Set;
import java.util.UUID;

@Component
public class GetRolePMExecutionistner implements ExecutionListener {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(GetRolePMExecutionistner.class);

    public GetRolePMExecutionistner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void notify(DelegateExecution delegateExecution) throws Exception {
        UUID userId = extractUserId(delegateExecution.getVariable("starter"));
        delegateExecution.setVariable("pmDesignation", delegateExecution.getVariable("starter"));
      Set<RoleDTO> roleDTOS = userService.getuser(userId).getBody().getRoles();

        String roleVariable = "autre";

        for (RoleDTO roleDTO : roleDTOS) {
            String roleName = roleDTO.getLabel();

            if (Constants.BS_ROLE_PM_CRO.equals(roleName)) {
                roleVariable = "cro";
                break;
            } else if (Constants.BS_ROLE_PM_AGENCE_PROMOTION.equals(roleName)) {
                roleVariable = "agencePromotion";
                break;
            } else if (roleName.equals(Constants.BS_ROLE_PM_SOCIETE_CONSULTING) ||
                roleName.equals(Constants.BS_ROLE_PM_EXPORT_IMPORT) ||
                roleName.equals(Constants.BS_ROLE_PM_TRANSITAIRE) ||
                roleName.equals(Constants.BS_ROLE_PM_ASSOCIATION) ||
                roleName.equals(Constants.BS_ROLE_PM_STRUCT_DIPLOM) ||
                roleName.equals(Constants.BS_ROLE_PM_SOCIETE_PHARMA) ||
                roleName.equals(Constants.BS_ROLE_PM_DEFAULT)) {
                roleVariable = "importExport";
                break;
            } else if (Constants.BS_ROLE_PM_AGENCE_PCT.equals(roleName)) {
                roleVariable = "agencePCT";
                break;
            } else if (Constants.BS_ROLE_PM_FILIALE_PCT.equals(roleName)) {
                roleVariable = "filialePCT";
                break;
            } else if (Constants.BS_ROLE_PM_PCT.equals(roleName)) {
                roleVariable = "createPCT";
                break;
            }
            else if (Constants.BS_ROLE_PM_GROSSISTE.equals(roleName)) {
                roleVariable = "grossiste";
                break;
            }
        }

        delegateExecution.setVariable("role", roleVariable);

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
