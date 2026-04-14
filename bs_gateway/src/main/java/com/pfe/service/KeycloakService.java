package com.pfe.service;

import com.pfe.service.dto.request.SignInRequest;
import com.pfe.service.dto.request.SignUpRequest;
import com.pfe.service.dto.response.SignInResponse;
import java.util.Map;
import org.keycloak.representations.idm.UserRepresentation;

public interface KeycloakService {

  UserRepresentation createUser(SignUpRequest signUpRequest);

  UserRepresentation createUser(String email);

  SignInResponse authenticateUser(SignInRequest signInRequest);

  String sendVerificationEmail();

  Map<String, Object> refreshAccessToken(String refreshToken);

  UserRepresentation findByEmailAndUsername(String email, String username);

  UserRepresentation createOrUpdateUser(SignUpRequest signUpRequest);
}
