package com.pfe.services.servicesImplementation;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.stereotype.Service;
import org.camunda.bpm.engine.identity.Group;


@Service
public class IdentityServiceImpl implements com.pfe.services.IdentityService {


    private final IdentityService identityService;

    public IdentityServiceImpl(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public void createUser(String userId, String firstName, String lastName, String email) {
        User user = identityService.newUser(userId);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        identityService.saveUser(user);
    }

    @Override
    public User findUserById(String userId) {
        return identityService.createUserQuery().userId(userId).singleResult();
    }

    @Override
    public void createGroup(String groupId, String groupName) {
        Group group = identityService.newGroup(groupId);
        group.setName(groupName);
        identityService.saveGroup(group);
    }

    @Override
    public void addUserToGroup(String userId, String groupId) {
        identityService.createMembership(userId, groupId);
    }
}


