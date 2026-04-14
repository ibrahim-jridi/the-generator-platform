package com.pfe.service;

import com.pfe.security.RealmConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateTokenRequester {

  @Value("${spring.security.oauth2.client.login-user-admin}")
  private String adminLogin;

  @Value("${spring.security.oauth2.client.password-user-admin}")
  private String adminPassword;

  public TokenResponse requestAccessToken(RealmConfig realmConfig) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", realmConfig.getClientid());
    map.add("username", this.adminLogin);
    map.add("password", this.adminPassword);
    map.add("grant_type", "password");

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

    ResponseEntity<TokenResponse> response = restTemplate.exchange(
        realmConfig.getUrl() + "/protocol/openid-connect/token",
        HttpMethod.POST,
        entity,
        TokenResponse.class
    );

    return response.getBody();
  }

  public TokenResponse requestNewAccessToken(RealmConfig realmConfig, String refreshToken) {
    RestTemplate restTemplate = new RestTemplate();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", realmConfig.getClientid());
    map.add("grant_type", "refresh_token");
    map.add("refresh_token", refreshToken);

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

    ResponseEntity<TokenResponse> response = restTemplate.exchange(
        realmConfig.getUrl() + "/protocol/openid-connect/token",
        HttpMethod.POST,
        entity,
        TokenResponse.class
    );

    return response.getBody();
  }
}
