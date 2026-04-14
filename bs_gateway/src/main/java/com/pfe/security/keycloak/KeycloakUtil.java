package com.pfe.security.keycloak;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.ByteArrayInputStream;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KeycloakUtil {

  @Value("${keycloak.auth-server-url}")
  private String keycloakServerUrl;

  @Value("${keycloak.realm}")
  private String realm;

  public Claims getClaimsFromToken(AccessTokenResponse tokenResponse) {
    try {
      String jwksUri = this.keycloakServerUrl + "/realms/" + this.realm + "/protocol/openid-connect/certs";
      RestTemplate restTemplate = new RestTemplate();
      String jwksResponse = restTemplate.getForObject(jwksUri, String.class);

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode jsonNode = objectMapper.readTree(jwksResponse);
      String certificateString = jsonNode.get("keys").get(0).get("x5c").get(0).asText();

      byte[] certificateBytes = Base64.getDecoder().decode(certificateString);
      CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
      X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(
          new ByteArrayInputStream(certificateBytes));

      PublicKey publicKey = certificate.getPublicKey();

      Claims claims = Jwts.parserBuilder()
          .setSigningKey(publicKey)
          .build()
          .parseClaimsJws(tokenResponse.getToken())
          .getBody();

      return claims;
    } catch (Exception e) {
      throw new RuntimeException("Failed to return Claims.", e);
    }

  }
}
