package com.pfe.services;

import org.camunda.bpm.engine.identity.User;

public interface IdentityService {
    void createUser(String userId, String firstName, String lastName, String email);

    User findUserById(String userId);

    void createGroup(String groupId, String groupName);

    void addUserToGroup(String userId, String groupId);
}
