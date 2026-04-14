package com.pfe.rest;

import com.pfe.config.Constants;
import com.pfe.feignServices.UserManagementClient;
import com.pfe.service.KeycloakService;
import com.pfe.service.dto.enumeration.UserType;
import com.pfe.service.dto.request.SignInRequest;
import com.pfe.service.dto.request.SignUpRequest;
import com.pfe.service.dto.request.TokenRequest;
import com.pfe.service.dto.request.UserDTO;
import com.pfe.service.dto.response.SignInResponse;
import com.pfe.service.dto.response.TokenResponse;
import java.util.Map;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication. to be deleted
 */
@RestController
@RequestMapping("/api/v1/oauth")
public class AuthenticateResource {

  private static final Logger log = LoggerFactory.getLogger(AuthenticateResource.class);

  private final KeycloakService keycloakService;
  private final UserManagementClient userManagementClient;

  public AuthenticateResource(KeycloakService keycloakService,
      UserManagementClient userManagementClient) {
    this.keycloakService = keycloakService;
    this.userManagementClient = userManagementClient;
  }

  @PostMapping("/register")
  public ResponseEntity<?> signup(@RequestBody SignUpRequest request) {
    log.info("Signup request: {}", request);
    UserRepresentation userRepresentation = this.keycloakService.createUser(request);
    return ResponseEntity.ok(userRepresentation);
  }

  @PostMapping("/pre-register")
  public ResponseEntity<?> preSignup(@RequestBody SignUpRequest request) {
    log.info("Pre-signup request: {}", request);
    UserRepresentation userRepresentation = this.keycloakService.createOrUpdateUser(request);
    return ResponseEntity.ok(userRepresentation);
  }

  @PostMapping("/register/by-email")
  public ResponseEntity<?> signupByEmail(@RequestBody SignUpRequest request) {
    log.info("Signup request: {}", request);
    UserRepresentation userRepresentation = this.keycloakService.createUser(request.getEmail());
    return ResponseEntity.ok(userRepresentation);

  }


  @PostMapping("/token")
  public ResponseEntity<SignInResponse> login(@RequestBody SignInRequest signInRequest) {
    log.info("Login request: {}", signInRequest);
    UserDTO userDTO = userManagementClient.findUserByUserName(signInRequest.getUsername()).getBody();

    if(!userDTO.getIsActive()) {
      log.error("User is inactive");
      return ResponseEntity.status(401).build();
    }

    if (userDTO == null || UserType.COMPANY.equals(userDTO.getUserType())) {
      log.error("Username or password is incorrect");
      return ResponseEntity.status(401).build();
    }

    SignInResponse tokenResponse = this.keycloakService.authenticateUser(signInRequest);

    if (tokenResponse == null) {
      log.error("Failed to authenticate user: {}", signInRequest.getUsername());
      return ResponseEntity.status(401).build();
    }

    return ResponseEntity.ok(tokenResponse);
  }

  @PostMapping("/rne-token")
  public ResponseEntity<SignInResponse> rneLogin(@RequestBody SignInRequest signInRequest) {
    log.info("Rne login request: {}", signInRequest);
      UserDTO userDTO = getUserDto(signInRequest.getFiscalIdOrUsername());
    if (userDTO != null) {
      signInRequest.setUsername(userDTO.getUsername());
      SignInResponse tokenResponse = this.keycloakService.authenticateUser(signInRequest);
      if (tokenResponse != null) {
        return ResponseEntity.ok(tokenResponse);
      } else {
        log.error("Failed to authenticate user: {}", signInRequest.getUsername());
        return ResponseEntity.status(401).build();
      }
    } else {
      log.error("Failed to authenticate user: {}", signInRequest.getUsername());
      return ResponseEntity.status(401).build();
    }

  }
    public UserDTO getUserDto(String fiscalIdOrUsername){
      if(Constants.COMPANY_USER_PREFIX.equals(fiscalIdOrUsername.substring(0,3))){
          return  userManagementClient.findUserByUserName(fiscalIdOrUsername).getBody();
      }else{
          return userManagementClient.findUserByFiscalId(fiscalIdOrUsername)
              .getBody();
      }
    }

  @GetMapping("/send-verification-mail")
  public ResponseEntity<Boolean> sendVerificationMail() {
    log.info("Send verification mail request");
    String validityToken = this.keycloakService.sendVerificationEmail();
    return ResponseEntity.ok(true);
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refreshAccessToken(@RequestBody TokenRequest request) {
    log.info("Refresh request: {}", request);
    String refreshToken = request.getRefreshToken();

    Map<String, Object> refreshTokenResponse = this.keycloakService.refreshAccessToken(
        refreshToken);
    TokenResponse tokenResponse = new TokenResponse();
    if (refreshTokenResponse != null && refreshTokenResponse.get("access_token") != null
        && refreshTokenResponse.get("refresh_token") != null) {
      tokenResponse.setRefreshToken((String) refreshTokenResponse.get("refresh_token"));
      tokenResponse.setAccessToken((String) refreshTokenResponse.get("access_token"));
    } else {
      log.error("Failed to refresh access token");
      ResponseEntity.status(401).build();
    }
    return ResponseEntity.ok(tokenResponse);

  }

}
