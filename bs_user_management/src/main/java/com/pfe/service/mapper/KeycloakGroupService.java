package com.pfe.service.mapper;

import com.pfe.service.dto.GroupDTO;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;
import org.keycloak.representations.idm.GroupRepresentation;

public interface KeycloakGroupService {

  UUID createGroup(GroupDTO groupName);

  void updateGroup(GroupDTO groupDTO);

  GroupRepresentation getGroupById(UUID groupId);

  boolean isGroupNameUnique(String groupName);

  void assignUserToGroup(String userId, String groupId);

  List<GroupRepresentation> getUserGroups(String userId);

  void removeUserFromGroup(String userId, String groupId);

  Response addSubgroup(String parentGroupId, String subGroupName);

  List<GroupRepresentation> getSubGroups(String groupId);

  void printGroupHierarchy(GroupRepresentation group, String indent);

  void addCustomAttributeToGroup(GroupRepresentation group, String attributeName,
      String attributeValue);

}
