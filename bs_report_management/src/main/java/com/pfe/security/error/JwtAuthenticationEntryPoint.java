package com.pfe.security.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Initiates the authentication flow
     *
     * @param exchange
     * @param ex
     * @return {@code Mono<Void>} to indicate when the request for authentication is complete
     */
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException authException) {
        String jsonResponse;
        if (authException instanceof AuthenticationException) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("type", exchange.getRequest().getURI());
            responseBody.put("titre", "401, Vous n'êtes pas autorisé à accéder à cette ressource.");
            responseBody.put("statut", HttpStatus.UNAUTHORIZED.value());
            responseBody.put("detail", authException.getMessage());
            responseBody.put("instance", exchange.getRequest().getURI() + "#TokenValidator");

            try {
                jsonResponse = objectMapper.writeValueAsString(responseBody);
            } catch (Exception ex) {
                jsonResponse = "{\"status\": 401, \"message\": \"Erreur lors de la génération de la réponse JSON.\"}";
            }
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);

        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            jsonResponse = "{\"status\": 401, \"message\": \"401, Vous n'êtes pas autorisé à accéder à cette ressource.\"}";
        }

        // Configurer la réponse HTTP
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes(
            StandardCharsets.UTF_8));
        // Écrire la réponse JSON

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}
