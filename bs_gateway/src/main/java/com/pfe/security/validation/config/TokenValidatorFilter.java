package com.pfe.security.validation.config;

import com.pfe.security.validation.error.JwtAuthenticationEntryPoint;
import com.pfe.security.validation.error.TokenValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


public class TokenValidatorFilter implements WebFilter {

  private final Logger logger = LoggerFactory.getLogger(TokenValidatorFilter.class);

  private JwtAuthenticationEntryPoint authenticationEntryPoint;
  private final String urlBaseKeycloak;
  private final int cacheTokenDelay;
  private final int cacheCertsKeysDelay;

  public TokenValidatorFilter(
      JwtAuthenticationEntryPoint authenticationEntryPoint,
      String urlBaseKeycloak,
      int cacheTokenDelay,
      int cacheCertsKeysDelay
  ) {
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.urlBaseKeycloak = urlBaseKeycloak;
    this.cacheTokenDelay = cacheTokenDelay;
    this.cacheCertsKeysDelay = cacheCertsKeysDelay;
  }


  /**
   * Process the Web request and (optionally) delegate to the next {@code WebFilter} through the
   * given {@link WebFilterChain}.
   *
   * @param exchange the current server exchange
   * @param chain    provides a way to delegate to the next filter
   * @return {@code Mono<Void>} to indicate when request processing is complete
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

    ServerHttpRequest requestHttp = exchange.getRequest();
    try {
      String authorization =
          requestHttp.getHeaders().get(HttpHeaders.AUTHORIZATION) != null ? requestHttp.getHeaders()
              .get(HttpHeaders.AUTHORIZATION).toString() : null;

      if (this.logger.isDebugEnabled()) {
        this.logger.debug("Token to be checked is: [{}]", authorization);
      }

      if (authorization != null && authorization.length() > 7) {
        this.logger.debug("Start Check Security JWT Token By TokenValidatorFilter");

        TokenValidatorManager tokenValidatorManager = TokenValidatorManager.getInstance(
            this.urlBaseKeycloak,
            this.cacheTokenDelay,
            this.cacheCertsKeysDelay
        );
        String jwtToken = TokenValidatorUtils.getTokenFromAuthorization(authorization);

        if (jwtToken == null || jwtToken.length() == 0) {
          throw new TokenValidatorException("No Bearer token found");
        }

        tokenValidatorManager.startValidateToken(jwtToken);

        this.logger.debug("Valid Token");
      } else {
        this.logger.debug("Request Ignored no Authorization Headers found");
      }
      //continue
      chain.filter(exchange);

    } catch (Exception ex) {
      this.logger.error("Error Validation token " + ex.getMessage());
      return this.authenticationEntryPoint.commence(
          exchange,
          ex instanceof TokenValidatorException ? (TokenValidatorException) ex
              : new TokenValidatorException(ex.getMessage())
      );

    } finally {
      this.logger.debug("End Check Security JWT Token By TokenValidatorFilter");
    }
    return chain.filter(exchange);
  }

}
