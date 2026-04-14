package com.pfe.security.validation.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfe.security.validation.cache.MapCache;
import com.pfe.security.validation.error.TokenValidatorException;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.keycloak.RSATokenVerifier;
import org.keycloak.jose.jws.JWSHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

public class TokenValidatorManager {

  private final Logger logger = LoggerFactory.getLogger(TokenValidatorManager.class);
  private static TokenValidatorManager instance = null;
  private final String urlBaseKeycloak;
  private final MapCache<String, PublicKey> certsKeysCache;
  private final MapCache<String, String> tokensCache;

  private TokenValidatorManager(String urlBaseKeycloak, int delayCacheToken,
      int delayCacheCertsKeys) {
    this.urlBaseKeycloak = urlBaseKeycloak;
    this.certsKeysCache = new MapCache<>(delayCacheCertsKeys);
    this.tokensCache = new MapCache<>(delayCacheToken);
  }

  public static synchronized TokenValidatorManager getInstance(String urlBaseKeycloak,
      int delayCacheToken, int delayCacheCertsKeys) {
    if (TokenValidatorManager.instance == null) {
      instance = new TokenValidatorManager(urlBaseKeycloak, delayCacheToken, delayCacheCertsKeys);
    }
    return instance;
  }

  public void startValidateToken(String token) {
    String tokenCached = this.tokensCache.get(token);
    if (tokenCached != null) {
      //token valid we found it from cache, go to check token expiration
      if (tokenExpired(token)) {
        //remove token from cache and go to validate it
        this.tokensCache.remove(token);
        throw new TokenValidatorException("Token expired");
      }
      //no exception token not expired, token is OK
    } else {
      //token not found in cache or is death from cache go to check it
      validateToken(token);
      //token OK put it in cache
      this.tokensCache.put(token, token);
    }
  }

  private void validateToken(String jwtToken) {
    //check token format and expiration
    DecodedJWT jwt = JWT.decode(jwtToken);
    PublicKey publicKey = retrivePublicKeyCached(jwt);
    if (publicKey == null) {
      throw new TokenValidatorException("token not valid");
    }
    JwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey((RSAPublicKey) publicKey).build();
    try {
      jwtDecoder.decode(jwtToken);
    } catch (BadJwtException failed) {
      this.logger.debug("Failed to authenticate since the JWT was invalid");
      throw new TokenValidatorException(failed.getMessage(), failed);
    } catch (JwtException failed) {
      this.logger.debug("Failed to authenticate JWT token");
      throw new TokenValidatorException(failed.getMessage(), failed);
    }
    //no exception token is ok
  }

  private boolean tokenExpired(String token) {
    DecodedJWT jwt = JWT.decode(token);
    return jwt.getExpiresAt().before(new Date());
  }

  private PublicKey retrivePublicKeyCached(DecodedJWT jwtToken) {
    PublicKey publicKeyCached = this.certsKeysCache.get(getRealmName(jwtToken));
    if (publicKeyCached == null) {
      PublicKey publicKey = retrievePublicKeyNotCached(jwtToken);
      insertIntoCache(jwtToken, publicKey);
      return publicKey;
    } else {
      return this.certsKeysCache.get(getRealmName(jwtToken));
    }
  }

  private static String getRealmName(DecodedJWT jwtToken) {
    return jwtToken.getIssuer().substring(jwtToken.getIssuer().indexOf("/realms") + 8);
  }

  private PublicKey retrievePublicKeyNotCached(DecodedJWT jwtToken) {
    try {
      RSATokenVerifier verifier = RSATokenVerifier.create(jwtToken.getToken());
      return getRealmPublicKey(verifier.getHeader(), this.urlBaseKeycloak, getRealmName(jwtToken));
    } catch (Exception ex) {
      this.logger.debug("Can't retrieve Certs: " + ex.getMessage());
      return null;
    }
  }

  private PublicKey getRealmPublicKey(JWSHeader jwsHeader, String serverUrl, String realmId) {
    return retrievePublicKeyFromCertsEndpoint(jwsHeader, serverUrl, realmId);
  }

  private PublicKey retrievePublicKeyFromCertsEndpoint(JWSHeader jwsHeader, String serverUrl,
      String realmId) {
    try {
      ObjectMapper om = new ObjectMapper();

      Map<String, Object> certInfos = om.readValue(
          new URL(getRealmCertsUrl(serverUrl, realmId)).openStream(), Map.class);

      List<Map<String, Object>> keys = (List<Map<String, Object>>) certInfos.get("keys");

      Map<String, Object> keyInfo = null;
      for (Map<String, Object> key : keys) {
        String kid = (String) key.get("kid");

        if (jwsHeader.getKeyId().equals(kid)) {
          keyInfo = key;
          break;
        }
      }

      if (keyInfo == null) {
        return null;
      }

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      String modulusBase64 = (String) keyInfo.get("n");
      String exponentBase64 = (String) keyInfo.get("e");

      Decoder urlDecoder = Base64.getUrlDecoder();
      BigInteger modulus = new BigInteger(1, urlDecoder.decode(modulusBase64));
      BigInteger publicExponent = new BigInteger(1, urlDecoder.decode(exponentBase64));

      return keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));
    } catch (Exception ex) {
      this.logger.error("Token PublicKeys error: {0}" + ex.getMessage());
    }
    return null;
  }

  private String getRealmCertsUrl(String serverUrl, String realmId) {
    return getRealmUrl(serverUrl, realmId) + "/protocol/openid-connect/certs";
  }

  private String getRealmUrl(String serverUrl, String realmId) {
    return serverUrl + "/realms/" + realmId;
  }

  private void insertIntoCache(DecodedJWT jwtToken, PublicKey publicKey) {
    if (publicKey != null) {
      this.certsKeysCache.put(getRealmName(jwtToken), publicKey);
    }
  }
}
