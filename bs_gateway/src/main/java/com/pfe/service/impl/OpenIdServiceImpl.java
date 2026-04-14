package com.pfe.service.impl;

import com.pfe.config.RestTemplateConfig;
import com.pfe.feignServices.EhoweyaServiceClient;
import com.pfe.service.OpenIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Service
public class OpenIdServiceImpl implements OpenIdService {
    private static final Logger log = LoggerFactory.getLogger(OpenIdServiceImpl.class);
    @Value("${ehoweya.responseType}")
    private String  responseType;
    @Value("${ehoweya.clientId}")
    private String clientId;
    @Value("${ehoweya.scope}")
    private String scope ;
    @Value("${ehoweya.redirectUri}")
    private String redirectUri ;
    @Value("${ehoweya.codeChallengeMethod}")
    private String codeChallengeMethod ;
    @Value("${ehoweya.grantType}")
    private String grantType ;
    @Value("${ehoweya.clientSecret}")
    private String clientSecret ;
    public String codeChallenge;
    private final EhoweyaServiceClient ehoweyaServiceClient;
    private String codeVerifier ;
    @Value("${ehoweya.ehoweya-url}")
    private String ehoweyaUrl;
    public OpenIdServiceImpl(EhoweyaServiceClient ehoweyaServiceClient) {
        this.ehoweyaServiceClient = ehoweyaServiceClient;
    }

    public String generateCodeVerifier() {
        if (this.codeVerifier != null) {
            return this.codeVerifier;
        }
        SecureRandom random = new SecureRandom();
        byte[] code = new byte[32];
        random.nextBytes(code);
        return  Base64.getUrlEncoder().withoutPadding().encodeToString(code);

    }

    public static String generateCodeChallenge(String codeVerifier) {
        byte[] bytes = codeVerifier.getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }
    @Override
    public Map<String, String> getEhouweyaConfig() {
        this.codeVerifier = generateCodeVerifier();
        codeChallenge = generateCodeChallenge(this.codeVerifier);
        Map<String, String> ehoweyaConfig = new HashMap<>();
        ehoweyaConfig.put("response_type", responseType);
        ehoweyaConfig.put("client_id", clientId);
        ehoweyaConfig.put("scope", scope);
        ehoweyaConfig.put("redirect_uri", redirectUri);
        ehoweyaConfig.put("code_challenge", codeChallenge);
        ehoweyaConfig.put("code_challenge_method", codeChallengeMethod);
        ehoweyaConfig.put("code_verifier", this.codeVerifier);
        return ehoweyaConfig;
    }

    @Override
    public Map<String, Object> exchangeCodeAndFetchUser(String code, String codeVerifier) {
        try {
            log.info("start to get token from e-howeya service");
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("grant_type", grantType);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", redirectUri);
            params.add("code_verifier", codeVerifier);

            RestTemplate restTemplate = new RestTemplateConfig().createRestTemplateTrustAllCerts();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(ehoweyaUrl + "/oauth2/token", request, Map.class);
            log.debug("response from e-howeya service is : {}", response);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                return Map.of(
                    "error", "Failed to get token",
                    "details", response.getBody()
                );
            }

            ResponseEntity<Map> userInfoResponse = getUserInfoResponse(restTemplate, response);
            return userInfoResponse.getBody();
        } catch (Exception ex) {
            return Map.of(
                "error", "Internal error",
                "message", ex.getMessage()
            );
        }
    }

        private ResponseEntity<Map> getUserInfoResponse(RestTemplate restTemplate, ResponseEntity<Map> response) {
        String token = (String) response.getBody().get("access_token");
        log.info("start to get user data from e-howeya service");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
            ehoweyaUrl + "/userinfo",
            HttpMethod.GET,
            entity,
            Map.class
        );
        if (!userInfoResponse.getStatusCode().is2xxSuccessful() || userInfoResponse.getBody() == null) {
            return ResponseEntity.status(userInfoResponse.getStatusCode())
                .body(Map.of(
                    "error", "Failed to get user info",
                    "details", userInfoResponse.getBody()
                ));
        }
        log.debug("user data recieved from e-howeya service: {}", userInfoResponse);
        return userInfoResponse;
    }
}
