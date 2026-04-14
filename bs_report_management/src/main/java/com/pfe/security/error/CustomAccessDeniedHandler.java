package com.pfe.security.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
    // Créer l'objet JSON personnalisé
    Map<String, Object> response = new HashMap<>();
    response.put("type", exchange.getRequest().getURI());
    response.put("titre", "403, Vous n'êtes pas autorisé à accéder à cette ressource.");
    response.put("statut", HttpStatus.FORBIDDEN.value());
    response.put("detail", "Accès refusé. Vous n'avez pas les permissions nécessaires.");
    response.put("instance", exchange.getRequest().getURI() );

    // Convertir l'objet en JSON
    String jsonResponse;
    try {
      jsonResponse = objectMapper.writeValueAsString(response);
    } catch (Exception ex) {
      jsonResponse = "{\"status\": 403, \"message\": \"Erreur lors de la génération de la réponse JSON.\"}";
    }

    // Configurer la réponse HTTP
    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

    DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(jsonResponse.getBytes(
        StandardCharsets.UTF_8));

    // Écrire la réponse JSON
    return exchange.getResponse().writeWith(Mono.just(buffer));
  }
}
