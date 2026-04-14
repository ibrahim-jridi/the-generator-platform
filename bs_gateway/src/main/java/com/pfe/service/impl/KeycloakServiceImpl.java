package com.pfe.service.impl;

import com.pfe.config.Constants;
import com.pfe.security.jwt.JwtTokenUtil;
import com.pfe.security.keycloak.KeycloakUtil;
import com.pfe.service.KeycloakService;
import com.pfe.service.NotificationService;
import com.pfe.service.dto.request.SignInRequest;
import com.pfe.service.dto.request.SignUpRequest;
import com.pfe.service.dto.response.SignInResponse;
import io.jsonwebtoken.Claims;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class KeycloakServiceImpl implements KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;


    @Value("${token.validity-duration}")
    private Long validityDuration;

    private final Keycloak keycloak;

    private final KeycloakUtil keycloakUtil;

    private final JwtTokenUtil jwtTokenUtil;
    private final NotificationService notificationService;
    private static final Logger log = LoggerFactory.getLogger(KeycloakServiceImpl.class);

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public KeycloakServiceImpl(Keycloak keycloak, JwtTokenUtil jwtTokenUtil,
        KeycloakUtil keycloakUtil, NotificationService notificationService) {
        this.keycloak = keycloak;
        this.jwtTokenUtil = jwtTokenUtil;
        this.keycloakUtil = keycloakUtil;
        this.notificationService = notificationService;
    }


    @Override
    public UserRepresentation createUser(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = generatePassword();
        String firstName = signUpRequest.getFirstName();
        String lastName = signUpRequest.getLastName();

        RealmResource realmResource = this.keycloak.realm(this.realm);
        UsersResource usersResource = realmResource.users();
        String username = "EXT-" + generateUniqueNumber();
        UserRepresentation user = initUserRepresentation(signUpRequest,
            email, password, firstName, lastName, username);
        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            UUID userId = UUID.fromString(
                response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"));

            ClientsResource clientsResource = realmResource.clients();
            List<ClientRepresentation> clients = clientsResource.findByClientId(this.clientId);

            if (clients.isEmpty()) {
                throw new RuntimeException(
                    "Client with clientId '" + this.clientId + "' not found");
            }
            assignAuthorityToUser(this.keycloak, String.valueOf(userId), Constants.BS_USER_AUTHORITY);
            assignAuthorityToUser(this.keycloak, String.valueOf(userId),
                Constants.BS_TASKS_AUTHORITY);
            assignAuthorityToUser(this.keycloak, String.valueOf(userId),
                Constants.BS_VIEW_DECLARATION_INTERESTS);

            String validityToken = this.jwtTokenUtil.generateToken(userId, validityDuration);
            this.notificationService.sendEmailVerification(signUpRequest.getEmail(), userId,
                username,
                validityToken, password);
        } else {
            throw new RuntimeException(
                "Failed to create user: " + response.getStatusInfo().getReasonPhrase());
        }

        String location = response.getHeaderString("Location");
        String userId = location.substring(location.lastIndexOf("/") + 1);
        UserRepresentation createdUser = usersResource.get(userId).toRepresentation();

        return createdUser;
    }

    @Override
    public UserRepresentation createOrUpdateUser(SignUpRequest signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = generatePassword();
        String firstName = signUpRequest.getFirstName();
        String lastName = signUpRequest.getLastName();

        RealmResource realmResource = this.keycloak.realm(this.realm);
        UsersResource usersResource = realmResource.users();
        String username = "EXT-" + generateUniqueNumber();
        if (signUpRequest.getId() == null) {
            throw new IllegalStateException("User ID must not be null");
        }
        UserRepresentation keycloakUser = usersResource
            .get(String.valueOf(signUpRequest.getId()))
            .toRepresentation();

        if (keycloakUser == null) {
            throw new IllegalStateException("UserRepresentation introuvable pour l'ID : " + signUpRequest.getId());
        }

        if (Boolean.TRUE.equals(keycloakUser.isEmailVerified())) {
            log.error("User already exists for email {}", email);
            throw new ResponseStatusException(  HttpStatus.CONFLICT, "This activation link has already been used");
        }
        if (signUpRequest.getId() != null) {
            UserRepresentation user = initUserRepresentation(signUpRequest, email, password,
                firstName,
                lastName, null);
            try {
                assignAuthorityToUser(this.keycloak, String.valueOf(signUpRequest.getId()),
                    Constants.BS_USER_AUTHORITY);
                assignAuthorityToUser(this.keycloak, String.valueOf(signUpRequest.getId()),
                    Constants.BS_TASKS_AUTHORITY);
                assignAuthorityToUser(this.keycloak, String.valueOf(signUpRequest.getId()),
                    Constants.BS_VIEW_DECLARATION_INTERESTS);
                String validityToken = this.jwtTokenUtil.generateToken(signUpRequest.getId(),
                    validityDuration);

                UserResource userResource = usersResource.get(
                    String.valueOf(signUpRequest.getId()));
                userResource.update(user);
                UserRepresentation updatedUser = userResource.toRepresentation();
                this.notificationService.sendEmailVerification(signUpRequest.getEmail(),
                    signUpRequest.getId(),
                    updatedUser.getUsername(),
                    validityToken, password);
                return updatedUser;
            } catch (NotFoundException e) {
                throw new RuntimeException("User with ID " + signUpRequest.getId() + " not found");
            } catch (ClientErrorException e) {
                throw new RuntimeException(
                    "Failed to update user: " + e.getResponse().getStatusInfo().getReasonPhrase());
            }
        } else {
            UserRepresentation user = initUserRepresentation(signUpRequest, email, password,
                firstName,
                lastName, username);
            Response response = usersResource.create(user);
            if (response.getStatus() == 201) {
                UUID newUserId = UUID.fromString(
                    response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"));

                ClientsResource clientsResource = realmResource.clients();
                List<ClientRepresentation> clients = clientsResource.findByClientId(this.clientId);

                if (clients.isEmpty()) {
                    throw new RuntimeException(
                        "Client with clientId '" + this.clientId + "' not found");
                }
                assignAuthorityToUser(this.keycloak, String.valueOf(newUserId),
                    Constants.BS_USER_AUTHORITY);
                assignAuthorityToUser(this.keycloak, String.valueOf(newUserId),
                    Constants.BS_TASKS_AUTHORITY);
                assignAuthorityToUser(this.keycloak, String.valueOf(newUserId),
                    Constants.BS_VIEW_DECLARATION_INTERESTS);

                String validityToken = this.jwtTokenUtil.generateToken(newUserId, validityDuration);
                this.notificationService.sendEmailVerification(signUpRequest.getEmail(),
                    UUID.fromString(newUserId.toString()),
                    username,
                    validityToken, password);
            } else {
                throw new RuntimeException(
                    "Failed to create user: " + response.getStatusInfo().getReasonPhrase());
            }

            String location = response.getHeaderString("Location");
            String newUserId = location.substring(location.lastIndexOf("/") + 1);
            UserRepresentation createdUser = usersResource.get(newUserId).toRepresentation();

            return createdUser;
        }
    }

    @Override
    public UserRepresentation createUser(String email) {
        log.debug("Creating user: {}", email);
        if (email == null || email.isEmpty()) {
            log.error("Email is null or empty");
            throw new RuntimeException("Email cannot be null or empty");
        }
        String password = generatePassword();
        RealmResource realmResource = this.keycloak.realm(this.realm);
        UsersResource usersResource = realmResource.users();
        String username = "EXT-" + generateUniqueNumber();
        UserRepresentation user = initUserRepresentation(null,
            email, password, null, null, username);
        Response response = usersResource.create(user);
        if (response.getStatus() == 201) {
            UUID userId = UUID.fromString(
                response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1"));

            ClientsResource clientsResource = realmResource.clients();
            List<ClientRepresentation> clients = clientsResource.findByClientId(this.clientId);

            if (clients.isEmpty()) {
                log.error("Client with clientId '" + this.clientId + "' not found");
                throw new RuntimeException(
                    "Client with clientId '" + this.clientId + "' not found");
            }
            assignAuthorityToUser(this.keycloak, String.valueOf(userId),
                Constants.BS_USER_AUTHORITY);
            assignAuthorityToUser(this.keycloak, String.valueOf(userId),
                Constants.BS_TASKS_AUTHORITY);

            String validityToken = this.jwtTokenUtil.generateToken(userId, validityDuration);
            this.notificationService.sendEmailCompleteAccount(email, userId,
                username,
                validityToken, password);
        } else {
            log.error("Failed to create user: " + response.getStatusInfo().getReasonPhrase());
            throw new RuntimeException(
                "Failed to create user: " + response.getStatusInfo().getReasonPhrase());
        }

        String location = response.getHeaderString("Location");
        String userId = location.substring(location.lastIndexOf("/") + 1);
        UserRepresentation createdUser = usersResource.get(userId).toRepresentation();

        return createdUser;
    }


    private UserRepresentation initUserRepresentation(SignUpRequest signUpRequest, String email,
        String password, String firstName, String lastName, String username) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);

        Map<String, String> customAttributes = Map.of();
        if (signUpRequest != null) {
            customAttributes = addUserCustomAttributes(signUpRequest);
        }

        if (!customAttributes.isEmpty()) {
            customAttributes.forEach((attributeName, attributeValue) -> {
                addCustomAttributeToUser(user, attributeName, attributeValue);
            });
        }
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        user.setCredentials(Collections.singletonList(credential));
        return user;
    }

    private static Map<String, String> addUserCustomAttributes(SignUpRequest signUpRequest) {
        Map<String, String> customAttributes = new HashMap<>();
        if (signUpRequest.getAddress() != null && !signUpRequest.getAddress().isEmpty()) {
            customAttributes.put("address", signUpRequest.getAddress());
        }
        if (signUpRequest.getAge() != null) {
            customAttributes.put("age", signUpRequest.getAge());
        }
        if (signUpRequest.getBirthDate() != null) {
            customAttributes.put("birthDate", signUpRequest.getBirthDate().toString());
        }
        if (signUpRequest.getNationalId() != null && !signUpRequest.getNationalId().isEmpty()) {
            customAttributes.put("nationalId", signUpRequest.getNationalId());
        }
        if (signUpRequest.getCountry() != null && !signUpRequest.getCountry().isEmpty()) {
            customAttributes.put("country", signUpRequest.getCountry());
        }
        if (signUpRequest.geteBarid() != null && !signUpRequest.geteBarid().isEmpty()) {
            customAttributes.put("eBarid", signUpRequest.geteBarid());
        }
        if (signUpRequest.getGender() != null && !signUpRequest.getGender().isEmpty()) {
            customAttributes.put("gender", signUpRequest.getGender());
        }
        if (signUpRequest.getPassportAttachment() != null && !signUpRequest.getPassportAttachment()
            .isEmpty()) {
            customAttributes.put("passportAttachment", signUpRequest.getPassportAttachment());
        }
        if (signUpRequest.getPhoneNumber() != null && !signUpRequest.getPhoneNumber().isEmpty()) {
            customAttributes.put("phoneNumber", signUpRequest.getPhoneNumber());
        }

        customAttributes.put("profile_completed", String.valueOf(false));
        customAttributes.put("first_connection", String.valueOf(true));
        customAttributes.put("roles", Constants.DEFAULT_ROLE);
        customAttributes.put("groups", Constants.GLOBAL_GROUP);

        if (signUpRequest.getNationality() != null && !signUpRequest.getNationality().isEmpty()) {
            customAttributes.put("nationality", signUpRequest.getNationality());
        }
        return customAttributes;
    }

    private void assignAuthorityToUser(Keycloak keycloak, String userId, String roleName) {
        String clientId = this.getClientId();
        List<RoleRepresentation> clientRoles = keycloak.realm(this.realm).clients().get(clientId)
            .roles().list();

        RoleRepresentation roleToAssign = clientRoles.stream()
            .filter(role -> role.getName().equals(roleName)).findFirst().orElse(null);

        if (roleToAssign != null) {
            keycloak.realm(this.realm).users().get(userId).roles().clientLevel(clientId)
                .add(Collections.singletonList(roleToAssign));
        } else {
            log.error("no role to add");
        }
    }

    private String getClientId() {
        List<ClientRepresentation> clients = this.keycloak.realm(this.realm).clients()
            .findByClientId(this.clientId);
        if (clients.isEmpty()) {
            log.info("Client not found: {}", this.clientId);
            return null;
        }
        return clients.get(0).getId();
    }

    private int generateUniqueNumber() {
        long timestamp = Instant.now().toEpochMilli();
        int uniqueNumber = (int) (timestamp % 10000000);
        if (uniqueNumber < 1000000) {
            uniqueNumber += 1000000;
        }
        return uniqueNumber;
    }

    private static String generatePassword() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@$&*-+?";
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder password = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            password.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }

    private void addCustomAttributeToUser(UserRepresentation user, String attributeName,
        String attributeValue) {
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }
        attributes.put(attributeName, Collections.singletonList(attributeValue));
        user.setAttributes(attributes);
    }

    @Override
    public SignInResponse authenticateUser(SignInRequest signInRequest) {
        Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl(this.keycloakServerUrl)
            .realm(this.realm)
            .clientId(this.clientId)
            .clientSecret(this.clientSecret)
            .username(signInRequest.getUsername())
            .password(signInRequest.getPassword())
            .grantType(OAuth2Constants.PASSWORD)
            .build();
        AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();

        ModelMapper modelMapper = new ModelMapper();

        UUID userId = getUserIdFromToken(tokenResponse);

        UserResource userResource = this.keycloak.realm(this.realm).users()
            .get(String.valueOf(userId));

        UserRepresentation userRepresentation = userResource.toRepresentation();

        SignInResponse signInResponse = mapUserToSignInResponse(userRepresentation, tokenResponse);

        sendVerificationEmail(userId);

        signInResponse.setUserId(userId);

        Boolean isEmailVerified = extractClaim(tokenResponse, "email_verified", Boolean.class);
        signInResponse.setEmailVerified(isEmailVerified);

        Boolean isProfileCompleted = extractClaim(tokenResponse, "profile_completed",
            Boolean.class);
        signInResponse.setProfileCompleted(isProfileCompleted);

        return signInResponse;


    }

    private String calculateAge(UserRepresentation userRepresentation) {
        log.debug("calculateAge {}", userRepresentation);
        if (userRepresentation.getAttributes().get("birthDate") != null
            && userRepresentation.getAttributes().get("birthDate").get(0) != null) {
            LocalDate birthDate = LocalDate.parse(
                userRepresentation.getAttributes().get("birthDate").get(0),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return String.valueOf(Period.between(birthDate, LocalDate.now()).getYears());
        }
        return null;

    }


    private SignInResponse mapUserToSignInResponse(UserRepresentation userRepresentation,
        AccessTokenResponse tokenResponse) {
        ModelMapper modelMapper = new ModelMapper();

        SignInResponse signInResponse = modelMapper.map(tokenResponse, SignInResponse.class);

        signInResponse.setEmail(userRepresentation.getEmail());
        signInResponse.setFirstName(userRepresentation.getFirstName());
        signInResponse.setLastName(userRepresentation.getLastName());
        signInResponse.setUsername(userRepresentation.getUsername());
        signInResponse.setAge(calculateAge(userRepresentation));

        setCustomAttribute(userRepresentation, "gender", signInResponse::setGender);
        setCustomAttribute(userRepresentation, "nationality", signInResponse::setNationality);
        setCustomAttribute(userRepresentation, "country", signInResponse::setCountry);
        setCustomAttribute(userRepresentation, "nationalId", signInResponse::setCin);
        setCustomAttribute(userRepresentation, "passportNumber", signInResponse::setPassportNumber);
        setCustomAttribute(userRepresentation, "phoneNumber", signInResponse::setPhoneNumber);
        setCustomAttribute(userRepresentation, "address", signInResponse::setAddress);
        setCustomAttribute(userRepresentation, "age", signInResponse::setAge);
        setCustomAttribute(userRepresentation, "birthDate", signInResponse::setBirthDate);

        return signInResponse;
    }

    private void setCustomAttribute(UserRepresentation userRepresentation, String attributeKey,
        Consumer<String> setter) {
        Map<String, List<String>> attributes = userRepresentation.getAttributes();

        if (attributes != null && attributes.containsKey(attributeKey)) {
            List<String> values = attributes.get(attributeKey);
            if (values != null && !values.isEmpty()) {
                setter.accept(values.get(0));
            }
        }
    }


    @Override
    public String sendVerificationEmail() {

        UUID userId = getUserIdFromToken();
        return this.jwtTokenUtil.generateToken(userId, validityDuration);
    }

    private Map<String, List<String>> getUserAttributes(String userId) {
        try {
            UsersResource usersResource = this.keycloak.realm(this.realm).users();
            UserRepresentation user = usersResource.get(userId).toRepresentation();
            return user.getAttributes();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user attributes", e);
        }
    }


    private UUID getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof OAuth2IntrospectionAuthenticatedPrincipal) {
            OAuth2IntrospectionAuthenticatedPrincipal principal =
                (OAuth2IntrospectionAuthenticatedPrincipal) authentication.getPrincipal();

            return UUID.fromString(principal.getName());
        }
        return null;
    }

    @Override
    public Map<String, Object> refreshAccessToken(String refreshToken) {
        String tokenEndpoint =
            this.keycloakServerUrl + "/realms/" + this.realm + "/protocol/openid-connect/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", this.clientId);
        body.add("client_secret", this.clientSecret);
        body.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = this.restTemplate.exchange(
            tokenEndpoint,
            HttpMethod.POST,
            requestEntity,
            Map.class
        );

        return response.getBody();
    }


    private <T> T extractClaim(AccessTokenResponse tokenResponse, String claimKey,
        Class<T> claimType) {
        try {
            log.debug("Extracting claim {} from token", claimKey);
            Claims claims = this.keycloakUtil.getClaimsFromToken(tokenResponse);
            return claims.get(claimKey, claimType);
        } catch (Exception e) {
            log.error("Error extracting claim {} from token", claimKey);
            throw new RuntimeException("Failed to extract claim: " + claimKey, e);
        }
    }


    private UUID getUserIdFromToken(AccessTokenResponse tokenResponse) {
        try {
            Claims claims = this.keycloakUtil.getClaimsFromToken(tokenResponse);

            return UUID.fromString(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user id.", e);
        }
    }



    @Override
    public UserRepresentation findByEmailAndUsername(String email, String username) {
        RealmResource realmResource = this.keycloak.realm(this.realm);
        List<UserRepresentation> users = realmResource.users()
            .search(username, null, null, email, 0, 1);
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    private void sendVerificationEmail(UUID userId) {
        String validityToken = this.jwtTokenUtil.generateToken(userId, validityDuration);
        log.info("Validity token: {}", validityToken);
    }

}
