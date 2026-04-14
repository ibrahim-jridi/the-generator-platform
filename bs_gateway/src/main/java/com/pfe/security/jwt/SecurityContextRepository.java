package com.pfe.security.jwt;

import com.pfe.security.validation.config.TokenValidatorUtils;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {


  private final TokenProvider tokenProvider;

  public SecurityContextRepository(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  /**
   * Saves the SecurityContext
   *
   * @param exchange the exchange to associate to the SecurityContext
   * @param context  the SecurityContext to save
   * @return a completion notification (success or error)
   */
  @Override
  public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
    return null;
  }

  /**
   * Loads the SecurityContext associated with the {@link ServerWebExchange}
   *
   * @param exchange the exchange to look up the {@link SecurityContext}
   * @return the {@link SecurityContext} to lookup or empty if not found. Never null
   */
  @Override
  public Mono<SecurityContext> load(ServerWebExchange exchange) {

    List<String> bearHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);

    String jwt = TokenValidatorUtils.getTokenFromAuthorization(
        bearHeader != null ? bearHeader.toString() : null);
    if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
      Authentication authentication = this.tokenProvider.getAuthentication(jwt);
      return Mono.just(new SecurityContextImpl(authentication));
    } else {
      return Mono.empty();
    }
  }
}
