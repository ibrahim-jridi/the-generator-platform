package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements com.pfe.services.AuthorizationService {

    private final AuthorizationService authorizationService;

    public AuthorizationServiceImpl(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void createAuthorization(String userId, String resourceId, Resources resourceType, Permissions permission) {
        Authorization authorization = authorizationService.createNewAuthorization(Authorization.AUTH_TYPE_GRANT);
        authorization.setUserId(userId);
        authorization.setResource(resourceType);
        authorization.setResourceId(resourceId);
        authorization.addPermission(permission);
        authorizationService.saveAuthorization(authorization);
    }

    @Override
    public Authorization getAuthorization(String authorizationId) {
        return authorizationService.createAuthorizationQuery().authorizationId(authorizationId).singleResult();
    }

    @Override
    public void deleteAuthorization(String authorizationId) {
        authorizationService.deleteAuthorization(authorizationId);
    }
}


