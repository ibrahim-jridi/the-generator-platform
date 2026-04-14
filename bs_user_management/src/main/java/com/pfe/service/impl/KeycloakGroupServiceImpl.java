package com.pfe.service.impl;

import com.pfe.service.dto.GroupDTO;
import com.pfe.service.mapper.KeycloakGroupService;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.GroupResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeycloakGroupServiceImpl implements KeycloakGroupService {

    private final Keycloak keycloak;

    @Value("${bs-app.keycloak.realm}")
    private String realm;

    public KeycloakGroupServiceImpl(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    private RealmResource getRealmResource() {
        return this.keycloak.realm(this.realm);
    }

    public List<GroupRepresentation> getAllGroups() {
        return getRealmResource().groups().groups();
    }

    @Override
    public UUID createGroup(GroupDTO groupDTO) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(groupDTO.getLabel());
        if (groupDTO.getParent() != null) {
            group.setParentId(String.valueOf(groupDTO.getParent().getId()));
        }
        addCustomAttributeToGroup(group, "description", groupDTO.getDescription());
        addCustomAttributeToGroup(group, "createdBy", String.valueOf(groupDTO.getCreatedBy()));

        Response response = getRealmResource().groups().add(group);
        if (response.getStatus() == 201) {
            URI location = response.getLocation();
            if (location != null) {
                String path = location.getPath();
                String groupId = path.substring(path.lastIndexOf("/") + 1);
                return UUID.fromString(groupId);
            }
        }
        response.close();
        return null;
    }

    @Override
    public void updateGroup(GroupDTO groupDTO) {
        GroupResource groupResource = getRealmResource().groups()
            .group(String.valueOf(groupDTO.getId()));
        GroupRepresentation groupRepresentation = groupResource.toRepresentation();
        if (!groupDTO.getLabel().equals(groupRepresentation.getName())) {
            groupRepresentation.setName(groupDTO.getLabel());
        }
        if (groupDTO.getParent() != null && groupDTO.getParent().getId() != UUID.fromString(
            groupRepresentation.getParentId())) {
            groupRepresentation.setParentId(String.valueOf(groupDTO.getParent().getId()));
        }
        if (!groupDTO.getDescription()
            .equals(groupRepresentation.getAttributes().get("description"))) {
            addCustomAttributeToGroup(groupRepresentation, "description",
                groupDTO.getDescription());
        }
        if (!groupDTO.getDescription()
            .equals(groupRepresentation.getAttributes().get("description"))) {
            addCustomAttributeToGroup(groupRepresentation, "description",
                groupDTO.getDescription());
        }
        groupResource.update(groupRepresentation);
    }

    @Override
    public GroupRepresentation getGroupById(UUID groupId) {
        GroupResource groupResource = getRealmResource().groups().group(String.valueOf(groupId));
        return groupResource.toRepresentation();
    }


    @Override
    public boolean isGroupNameUnique(String groupName) {
        List<GroupRepresentation> groups = getRealmResource().groups().groups();
        for (GroupRepresentation group : groups) {
            if (group.getName().equals(groupName)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void assignUserToGroup(String userId, String groupId) {
        UserResource userResource = getRealmResource().users().get(userId);
        GroupResource groupResource = getRealmResource().groups().group(groupId);
        userResource.joinGroup(groupResource.toRepresentation().getId());
    }

    @Override
    public List<GroupRepresentation> getUserGroups(String userId) {
        return getRealmResource().users().get(userId).groups();
    }

    @Override
    public void removeUserFromGroup(String userId, String groupId) {
        UserResource userResource = getRealmResource().users().get(userId);
        userResource.leaveGroup(groupId);
    }

    @Override
    public Response addSubgroup(String parentGroupId, String subGroupName) {
        GroupRepresentation subGroup = new GroupRepresentation();
        subGroup.setName(subGroupName);
        GroupResource parentGroup = getRealmResource().groups().group(parentGroupId);
        return parentGroup.subGroup(subGroup);
    }

    @Override
    public List<GroupRepresentation> getSubGroups(String groupId) {
        GroupResource groupResource = getRealmResource().groups().group(groupId);
        return groupResource.toRepresentation().getSubGroups();
    }

    @Override
    public void printGroupHierarchy(GroupRepresentation group, String indent) {
        if (group.getSubGroups() != null && !group.getSubGroups().isEmpty()) {
            for (GroupRepresentation subGroup : group.getSubGroups()) {
                printGroupHierarchy(subGroup, indent + "    ");
            }
        }
    }

    @Override
    public void addCustomAttributeToGroup(GroupRepresentation group, String attributeName,
        String attributeValue) {
        Map<String, List<String>> attributes = group.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(attributeName, Collections.singletonList(attributeValue));
        group.setAttributes(attributes);
    }

}
