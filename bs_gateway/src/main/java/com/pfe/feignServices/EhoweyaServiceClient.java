package com.pfe.feignServices;

import com.pfe.config.FeignClientConfig;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@FeignClient( name = "feignClientApplication", url = "${ehoweya.ehoweya-url}", configuration = FeignClientConfig.class)
public interface EhoweyaServiceClient {

    @PostMapping(value = "/oauth2/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    ResponseEntity<Map<String, Object>> getAccessToken(
        @RequestParam("code") String code,
        @RequestParam("grant_type") String grantType,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("client_id") String clientId,
        @RequestParam("redirect_uri") String redirectUri,
        @RequestParam("code_verifier") String codeVerifier
    );

    @GetMapping("/userinfo")
    ResponseEntity<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String authorization);
}
