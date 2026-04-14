package com.pfe.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

  @Value("${keycloak.auth-server-url}")
  private String keycloakServerUrl;

  @Value("${keycloak.realm}")
  private String realm;

  @Value("${keycloak.resource}")
  private String clientId;

  @Value("${keycloak.credentials.secret}")
  private String clientSecret;

  @Value("${keycloak-admin.username}")
  private String adminUsername;

  @Value("${keycloak-admin.password}")
  private String adminPassword;

  @Bean
  public Keycloak keycloakAdminClient() {
    return KeycloakBuilder.builder()
        .serverUrl(this.keycloakServerUrl)
        .realm(this.realm)
        .clientId(this.clientId)
        .clientSecret(this.clientSecret)
        .username(this.adminUsername)
        .password(this.adminPassword)
        .grantType(OAuth2Constants.PASSWORD)
        .build();
  }


}
