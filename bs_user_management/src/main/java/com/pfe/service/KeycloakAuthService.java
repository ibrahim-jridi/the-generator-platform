package com.pfe.service;

import com.pfe.service.dto.UserDTO;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.UUID;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakAuthService {

  AccessTokenResponse authenticateUser(String username, String password);

  boolean userExistsByUsername(String email);

  UserDTO getUserByToken(String token);

  UUID getUserIdFromToken(AccessTokenResponse tokenResponse);

  void setEmailVerified(UUID userId);

  void setEmailVerified(UserResource userResource);

  void addCustomAttributeToUser(UserRepresentation user, String attributeName,
      String attributeValue);

  void addOrUpdateCustomAttributeToUser(UserRepresentation user, String attributeName,
      String attributeValue);

  List<String> getAttributeFromOfUser(UserRepresentation user, String attributeName);

  void addCustomAttributeListToUser(UserRepresentation user, String attributeName,
      List<String> attributeValueList);

  Claims extractClaims(String token);

  List<String> getEctdRole(UUID keycloakId);

  List<String> getApplicationsIds(UUID keycloakId);
}
