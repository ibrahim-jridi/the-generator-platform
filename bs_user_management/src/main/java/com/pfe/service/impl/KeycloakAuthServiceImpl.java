package com.pfe.service.impl;

import com.pfe.domain.enumeration.Gender;
import com.pfe.domain.enumeration.Nationality;
import com.pfe.domain.enumeration.RegistryStatus;
import com.pfe.domain.enumeration.UserType;
import com.pfe.repository.GroupRepository;
import com.pfe.repository.RoleRepository;
import com.pfe.security.Jwt.JwtTokenUtil;
import com.pfe.security.keycloak.KeycloakUtil;
import com.pfe.service.KeycloakAuthService;
import com.pfe.service.dto.ActivityTypeDTO;
import com.pfe.service.dto.GroupDTO;
import com.pfe.service.dto.RoleDTO;
import com.pfe.service.dto.UserDTO;
import com.pfe.service.mapper.GroupMapper;
import io.jsonwebtoken.Claims;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeycloakAuthServiceImpl implements KeycloakAuthService {


  private static final Logger log = LoggerFactory.getLogger(KeycloakAuthServiceImpl.class);

  private final RoleRepository roleRepository;
  private final Keycloak keycloak;
  private final KeycloakUtil keycloakUtil;
  private final JwtTokenUtil jwtTokenUtil;
  private final GroupRepository groupRepository;
  private final GroupMapper groupMapper;
  @Value("${bs-app.keycloak.auth-server-url}")
  private String keycloakServerUrl;
  @Value("${bs-app.keycloak.realm}")
  private String realm;
  @Value("${bs-app.keycloak.resource}")
  private String clientId;
  @Value("${bs-app.keycloak.credentials.secret}")
  private String clientSecret;


  KeycloakAuthServiceImpl(Keycloak keycloak, JwtTokenUtil jwtTokenUtil, KeycloakUtil keycloakUtil,
      RoleRepository roleRepository, GroupRepository groupRepository, GroupMapper groupMapper) {
    this.keycloak = keycloak;
    this.jwtTokenUtil = jwtTokenUtil;
    this.keycloakUtil = keycloakUtil;
    this.roleRepository = roleRepository;
    this.groupRepository = groupRepository;
    this.groupMapper = groupMapper;
  }


  @Override
  public boolean userExistsByUsername(String username) {
    RealmResource realmResource = this.keycloak.realm(this.realm);
    List<UserRepresentation> users = realmResource.users().search(username, null, null, null, 0, 1);
    return !users.isEmpty();
  }

  @Override
  public UserDTO getUserByToken(String token) {
    if (!this.jwtTokenUtil.isTokenExpired(token)) {
      UUID userId = UUID.fromString(this.jwtTokenUtil.extractAllClaims(token).getSubject());
      if (userId != null) {
        UserResource userResource = findUserById(userId);
        UserRepresentation user = userResource.toRepresentation();
        UserDTO userDTO = new UserDTO();
        if (user.getAttributes().containsKey("userType") && user.getAttributes().get("userType")
            .get(0).equals(UserType.COMPANY.toString())) {
          userDTO.setSocialReason(user.getLastName());
          userDTO.setUserType(UserType.COMPANY);
          userDTO.setRoles(getUserRoles(String.valueOf(userId)));
          userDTO.setGroups(getUserGroups(String.valueOf(userId)));
        } else {
          userDTO.setFirstName(user.getFirstName());
          userDTO.setLastName(user.getLastName());
          userDTO.setRoles(getUserRoles(String.valueOf(userId)));
          userDTO.setGroups(getUserGroups(String.valueOf(userId)));
          userDTO.setUserType(UserType.PHYSICAL);
        }

        userDTO.setId(userId);
        userDTO.setKeycloakId(userId);
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmailVerified(user.isEmailVerified());
        userDTO.setIsActive(true);

        if (user.getAttributes() != null) {

          if (user.getAttributes().containsKey("address")) {
            userDTO.setAddress(user.getAttributes().get("address").get(0));
          }
          if (user.getAttributes().containsKey("gender")) {

            switch (String.valueOf(user.getAttributes().get("gender").get(0))) {
              case "MALE":
                userDTO.setGender(Gender.MALE);
                break;
              case "FEMALE":
                userDTO.setGender(Gender.FEMALE);
            }

          }
          if (user.getAttributes().containsKey("birthDate")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(user.getAttributes().get("birthDate").get(0),
                formatter);
            userDTO.setBirthDate(birthDate);
          }
          if (user.getAttributes().containsKey("nationalId")) {
            userDTO.setNationalId(user.getAttributes().get("nationalId").get(0));
          }
          if (user.getAttributes().containsKey("nationality")) {
            String nationalityValue = user.getAttributes().get("nationality").get(0);
            if (nationalityValue != null && !nationalityValue.isEmpty()) {
              try {
                Nationality matchedNationality = Nationality.valueOf(
                    nationalityValue.toUpperCase());
                userDTO.setNationality(matchedNationality);
              } catch (IllegalArgumentException e) {
                log.warn("Invalid nationality value: {}", nationalityValue);
              }
            }
          }
          if (user.getAttributes().containsKey("country")) {
            userDTO.setCountry(user.getAttributes().get("country").get(0));
          }
          if (user.getAttributes().containsKey("eBarid")) {
            userDTO.seteBarid(user.getAttributes().get("eBarid").get(0));
          }
          if (user.getAttributes().containsKey("gender")) {
            String gender = user.getAttributes().get("gender").get(0);
            if (gender.equals("Femme")) {
              userDTO.setGender(Gender.FEMALE);
            } else if (gender == "Homme") {
              userDTO.setGender(Gender.MALE);
            }
          }
          if (user.getAttributes().containsKey("phoneNumber")) {
            userDTO.setPhoneNumber(user.getAttributes().get("phoneNumber").get(0));
          }
          if (user.getAttributes().containsKey("isProfileCompleted")) {
            if (user.getAttributes().get("isProfileCompleted").get(0).equals("false")) {
              userDTO.setProfileCompleted(false);
            } else if (user.getAttributes().get("isProfileCompleted").get(0).equals("true")) {
              userDTO.setProfileCompleted(true);
            }
          }
          if (user.getAttributes().containsKey("creationDate")) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate birthDate = LocalDate.parse(user.getAttributes().get("creationDate").get(0),
                formatter);
            userDTO.setBirthDate(birthDate);
          }
          if (user.getAttributes().containsKey("activityDomain")) {
            userDTO.setActivityDomain(user.getAttributes().get("activityDomain").get(0));
          }
          if (user.getAttributes().containsKey("legalStatus")) {
            userDTO.setLegalStatus(user.getAttributes().get("legalStatus").get(0));
          }
          if (user.getAttributes().containsKey("activitiesType")) {
            Set<ActivityTypeDTO> activities = user.getAttributes().get("activitiesType").stream()
                .map(activity -> {
                  String[] parts = activity.split(":");
                  ActivityTypeDTO dto = new ActivityTypeDTO();
                  dto.setCode(parts.length > 0 ? parts[0] : "");
                  dto.setName(parts.length > 1 ? parts[1] : "");
                  return dto;
                }).collect(Collectors.toSet());
            userDTO.setActivitiesType(activities);
          }

          if (user.getAttributes().containsKey("fileStatus")) {
            userDTO.setFileStatus(user.getAttributes().get("fileStatus").get(0));
          }
          if (user.getAttributes().containsKey("filePatent")) {
            userDTO.setFilePatent(user.getAttributes().get("filePatent").get(0));
          }
          if (user.getAttributes().containsKey("registryStatus")) {
            userDTO.setRegistryStatus(
                RegistryStatus.valueOf(user.getAttributes().get("registryStatus").get(0)));
          }
          if (user.getAttributes().containsKey("denomination")) {
            userDTO.setDenomination(user.getAttributes().get("denomination").get(0));
          }
          if (user.getAttributes().containsKey("taxRegistration")) {
            userDTO.setTaxRegistration(user.getAttributes().get("taxRegistration").get(0));
          }
            Map<String, String> profileMap = new HashMap<>();

            if (user.getAttributes().containsKey("IMPORT_EXPORT_PRODUCT_CATEGORY")) {
                List<String> dataPmAttribute = user.getAttributes().get("IMPORT_EXPORT_PRODUCT_CATEGORY");
                String dataPmValue = (dataPmAttribute != null && !dataPmAttribute.isEmpty()) ? dataPmAttribute.get(0) : "{}";
                profileMap.put("IMPORT_EXPORT_PRODUCT_CATEGORY", dataPmValue);
            }

            if (user.getAttributes().containsKey("COMPANY_NAME")) {
                List<String> dataPmAttribute = user.getAttributes().get("COMPANY_NAME");
                String dataPmValue = (dataPmAttribute != null && !dataPmAttribute.isEmpty()) ? dataPmAttribute.get(0) : "{}";
                profileMap.put("COMPANY_NAME", dataPmValue);
            }

            if (user.getAttributes().containsKey("ANOTHER_CATEGORIES")) {
                List<String> dataPmAttribute = user.getAttributes().get("ANOTHER_CATEGORIES");
                String dataPmValue = (dataPmAttribute != null && !dataPmAttribute.isEmpty()) ? dataPmAttribute.get(0) : "{}";
                profileMap.put("ANOTHER_CATEGORIES", dataPmValue);
            }

            if (user.getAttributes().containsKey("URL_WEB_SITE")) {
                List<String> dataPmAttribute = user.getAttributes().get("URL_WEB_SITE");
                String dataPmValue = (dataPmAttribute != null && !dataPmAttribute.isEmpty()) ? dataPmAttribute.get(0) : "{}";
                profileMap.put("URL_WEB_SITE", dataPmValue);
            }
            if (!profileMap.isEmpty()) {
                userDTO.setProfile(profileMap);
            }
        }
        return userDTO;
      }
    } else {
      throw new RuntimeException("Token expired ");
    }
    return null;
  }

  private Set<RoleDTO> getUserRoles(String userId) {
    UserRepresentation user = this.keycloak.realm(this.realm).users().get(userId)
        .toRepresentation();

    List<String> rolesFromAttribute = user.getAttributes().get("roles");
    Set<String> keycloakRoleLabels = new HashSet<>();
    List<RoleDTO> rolesFromDatabase = this.roleRepository.findAll().stream().map(role -> {
      RoleDTO roleDTO = new RoleDTO();
      roleDTO.setLabel(role.getLabel());
      roleDTO.setDescription(role.getDescription());
      roleDTO.setId(role.getId());
      return roleDTO;
    }).toList();

    if (rolesFromAttribute != null) {
      keycloakRoleLabels.addAll(rolesFromAttribute);
    }
    Set<RoleDTO> rolesToAssign = new HashSet<>();

    for (RoleDTO roleDTO : rolesFromDatabase) {
      // Check if the role label exists in the Keycloak role labels
      if (keycloakRoleLabels.contains(roleDTO.getLabel())) {
        rolesToAssign.add(roleDTO);
      }
    }

    return rolesToAssign;
  }

  private Set<GroupDTO> getUserGroups(String userId) {
    log.debug("Attempting to assign groups to user with id {}", userId);
    UserRepresentation user = this.keycloak.realm(this.realm).users().get(userId)
        .toRepresentation();
    List<String> groupsLabelsFromKeyCloak = user.getAttributes().get("groups");
    Set<GroupDTO> groups = new HashSet<>();
    if (!groupsLabelsFromKeyCloak.isEmpty()) {
      groups.addAll(this.groupRepository.findAllByLabelIn(groupsLabelsFromKeyCloak).stream()
          .map(this.groupMapper::toDto).collect(Collectors.toSet()));
    }
    return groups;
  }

  @Override
  public UUID getUserIdFromToken(AccessTokenResponse tokenResponse) {
    try {
      Claims claims = this.keycloakUtil.getClaimsFromToken(tokenResponse);

      return UUID.fromString(claims.getSubject());
    } catch (Exception e) {
      throw new RuntimeException("Failed to get user id.", e);
    }
  }

  @Override
  public void setEmailVerified(UUID userId) {
    UsersResource usersResource = this.keycloak.realm(this.realm).users();
    UserResource userResource = usersResource.get(String.valueOf(userId));
    UserRepresentation userRepresentation = userResource.toRepresentation();
    userRepresentation.setEmailVerified(true);
    userResource.update(userRepresentation);
  }

  @Override
  public void setEmailVerified(UserResource userResource) {
    UserRepresentation userRepresentation = userResource.toRepresentation();
    userRepresentation.setEmailVerified(true);
    userResource.update(userRepresentation);
  }


  public UserResource findUserById(UUID userId) {
    UsersResource usersResource = this.keycloak.realm(this.realm).users();
    return usersResource.get(String.valueOf(userId));

  }


  @Override
  public AccessTokenResponse authenticateUser(String username, String password) {
    try {
      // Create REST client using the JAX-RS API - this is the standard way
      jakarta.ws.rs.client.Client resteasyClient = jakarta.ws.rs.client.ClientBuilder.newBuilder()
          .build();

      Keycloak keycloak = KeycloakBuilder.builder()
          .serverUrl(keycloakServerUrl)
          .realm(realm)
          .clientId(clientId)
          .clientSecret(clientSecret)
          .username(username)
          .password(password)
          .grantType(OAuth2Constants.PASSWORD)
          .resteasyClient(resteasyClient)  // Works with the Client interface
          .build();

      return keycloak.tokenManager().getAccessToken();
    } catch (Exception e) {
      log.error("Failed to authenticate user {}: {}", username, e.getMessage(), e);
      throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
    }
  }

  @Override
  public void addCustomAttributeToUser(UserRepresentation user, String attributeName,
      String attributeValue) {
      Map<String, List<String>> attributes = user.getAttributes();
      if (attributes == null) {
          attributes = new HashMap<>();
      }
      attributes.put(attributeName, Collections.singletonList(attributeValue));
      user.setAttributes(attributes);
  }

  @Override
  public void addOrUpdateCustomAttributeToUser(UserRepresentation user, String attributeName,
      String attributeValue) {
    UsersResource usersResource = this.keycloak.realm(this.realm).users();
    UserResource userResource = usersResource.get(String.valueOf(user.getId()));
    Map<String, List<String>> attributes = user.getAttributes();
    if (attributes == null) {
      attributes = new HashMap<>();
    } else if (attributes.containsKey(attributeName)) {
      attributes.remove(attributeName);
    }
    attributes.put(attributeName, Collections.singletonList(attributeValue));
    user.setAttributes(attributes);
    userResource.update(user);
  }

  @Override
  public List<String> getAttributeFromOfUser(UserRepresentation user, String attributeName) {
    Map<String, List<String>> attributes = user.getAttributes();
    if (attributes == null) {
      return null;
    }
    return attributes.get(attributeName);
  }


  @Override
  public void addCustomAttributeListToUser(UserRepresentation user, String attributeName,
      List<String> attributeValueList) {
    Map<String, List<String>> attributes = user.getAttributes();
    if (attributes == null) {
      attributes = new HashMap<>();
    }
    attributes.put(attributeName, attributeValueList);
    user.setAttributes(attributes);
  }

  @Override
  public Claims extractClaims(String token) {
    return this.jwtTokenUtil.extractAllClaims(token);
  }


  private List<String> getAttributes(UUID keycloakId, String key) {
      if (keycloakId != null) {
        UserResource userResource = findUserById(keycloakId);
        UserRepresentation user = userResource.toRepresentation();
        if (user != null && user.getAttributes() != null) {
          Map<String, List<String>> attributes = user.getAttributes();
          if (attributes != null && attributes.containsKey(key)) {
            return attributes.get(key);
          }
        }
      }
    return null;
  }


  @Override
  public List<String> getEctdRole(UUID keycloakId) {
    List<String> roleEctd = getAttributes(keycloakId, "role_ectd");
    return roleEctd;
  }

  @Override
  public List<String> getApplicationsIds(UUID keycloakId) {
    List<String> rawList = getAttributes(keycloakId, "applications_Id");

    List<String> applicationsId = new ArrayList<>();

    if (rawList != null && !rawList.isEmpty()) {
      applicationsId = rawList.stream()
          .flatMap(s -> Arrays.stream(s.split(",")))
          .collect(Collectors.toList());
    }


    return applicationsId;
  }
}
