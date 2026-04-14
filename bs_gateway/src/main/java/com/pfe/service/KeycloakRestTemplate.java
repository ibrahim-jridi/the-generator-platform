package com.pfe.service;

import com.pfe.security.RealmConfig;
import com.pfe.security.SecurityUtils;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class KeycloakRestTemplate extends RestTemplate {

  public static final String AUTHORIZATION = "Authorization";
  public static final String BEARER = "Bearer ";

  private static Map<String, TokenResponse> tokenCache = new ConcurrentHashMap<>();

  public KeycloakRestTemplate(RestTemplateTokenRequester restTemplateTokenRequester) {
    this.getInterceptors()
        .add((request, body, execution) -> {
          RealmConfig realmConfig = SecurityUtils.getRealmFromConnectedUser();
          TokenResponse token;
          if (!tokenCache.containsKey(realmConfig.getRealm())) {
            token = restTemplateTokenRequester.requestAccessToken(realmConfig);
            tokenCache.put(realmConfig.getRealm(), token);
          } else {
            token = getToken(restTemplateTokenRequester, realmConfig);
          }
          request.getHeaders()
              .put(AUTHORIZATION, Collections.singletonList(BEARER.concat(token.getAccessToken())));
          request.getHeaders()
              .put(HttpHeaders.ACCEPT, Collections.singletonList("application/json"));
          request.getHeaders()
              .put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
          return execution.execute(request, body);
        });
  }

  private static TokenResponse getToken(RestTemplateTokenRequester restTemplateTokenRequester,
      RealmConfig realmConfig) {
    TokenResponse token;
    TokenResponse tokenTmp = tokenCache.get(realmConfig.getRealm());

    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.SECOND, -tokenTmp.getExpiresIn() + 60);

    if (calendar.getTime().before(tokenTmp.getTokenTime())) {
      //token is valid
      token = tokenTmp;
    } else {
      calendar = Calendar.getInstance();
      calendar.add(Calendar.SECOND, -tokenTmp.getRefreshExpiresIn() + 60);
      if (calendar.getTime().before(tokenTmp.getTokenTime())) {
        //refresh token is valid request new one
        token = restTemplateTokenRequester.requestNewAccessToken(realmConfig,
            tokenTmp.getRefreshToken());
        tokenCache.put(realmConfig.getRealm(), token);
      } else {
        //refresh token is not valid request new token
        token = restTemplateTokenRequester.requestAccessToken(realmConfig);
        tokenCache.put(realmConfig.getRealm(), token);
      }
    }
    return token;
  }
}
