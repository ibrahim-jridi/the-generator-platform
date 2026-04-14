package com.pfe.service;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

public class KeycloakPublicKeyUtil {

  public static PublicKey getPublicKeyFromKeycloak(String keycloakUrl, String realm)
      throws Exception {
    String url = String.format("%s/realms/%s/protocol/openid-connect/certs", keycloakUrl, realm);
    RestTemplate restTemplate = new RestTemplate();
    String response = restTemplate.getForObject(url, String.class);

    // Parse the public key from the response
    JSONObject json = new JSONObject(response);
    JSONArray keys = json.getJSONArray("keys");
    String publicKeyPem = keys.getJSONObject(0).getJSONArray("x5c").getString(0);

    // Convert the public key to a usable format
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    byte[] decodedKey = Base64.getDecoder().decode(publicKeyPem);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
    return keyFactory.generatePublic(keySpec);
  }
}
