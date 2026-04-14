package com.pfe.rest;

import java.util.Map;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/redirect")
public class RedirectController {

    private static final Logger log = LoggerFactory.getLogger(RedirectController.class);

    @Value("${bs-app.keycloak.auth-server-url}")
    private String keycloakServerUrl;

    @Value("${bs-app.keycloak.realm}")
    private String realm;

    @Value("${bs-app.keycloak.resource}")
    private String clientId;

    @Value("${bs-app.keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${ectd-app.keycloak.resource}")
    private String ectdClientId;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/ectd")
    public Mono<ResponseEntity<Map<String, String>>> redirectToEctd(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        log.info("Received request for token exchange with Keycloak");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("No Authorization header or invalid format — returning 401");
            return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build());
        }

        String bsAccessToken = authorizationHeader.substring(7);

        log.info("Using bsAccessToken (truncated): {}...", bsAccessToken.substring(0, Math.min(10, bsAccessToken.length())));

        String tokenEndpoint = this.keycloakServerUrl + "realms/" + this.realm + "/protocol/openid-connect/token";
        log.info("Token exchange endpoint: {}", tokenEndpoint);
        log.info("Target audience (eCTD client_id): {}", this.ectdClientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "urn:ietf:params:oauth:grant-type:token-exchange");
        body.add("subject_token", bsAccessToken);
        body.add("subject_token_type", "urn:ietf:params:oauth:token-type:access_token");
        body.add("requested_token_type", "urn:ietf:params:oauth:token-type:access_token");
        body.add("audience", this.ectdClientId);
        body.add("client_id", this.clientId);
        body.add("client_secret", this.clientSecret);

        return Mono.fromCallable(() -> {
            try {
                log.info("Sending token exchange request to Keycloak...");

                ResponseEntity<Map> kcResponse = this.restTemplate.exchange(
                    tokenEndpoint,
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class
                );

                log.info("Keycloak responded with HTTP status: {}", kcResponse.getStatusCode());

                if (!kcResponse.getStatusCode().is2xxSuccessful() || kcResponse.getBody() == null) {
                    log.error("Token exchange failed — no valid response body");
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
                }

                String ectdAccessToken = (String) kcResponse.getBody().get("access_token");

                if (ectdAccessToken == null) {
                    log.error("Keycloak response missing 'access_token'");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                }

                log.info("Token exchange successful — returning eCTD access_token (truncated): {}...", ectdAccessToken.substring(0, 10));

                return ResponseEntity.ok(Map.of("access_token", ectdAccessToken));

            } catch (HttpClientErrorException e) {
                log.error("Keycloak returned an HTTP error: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
            } catch (Exception e) {
                log.error("Internal server error during token exchange: {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        });
    }
}
