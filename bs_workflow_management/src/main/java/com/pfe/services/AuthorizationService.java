package com.pfe.services;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resources;
public interface AuthorizationService {
    void createAuthorization(String userId, String resourceId, Resources resourceType, Permissions permission);

    Authorization getAuthorization(String authorizationId);

    void deleteAuthorization(String authorizationId);
}
