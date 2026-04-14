package com.pfe.security.jwt;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

  /**
   * Attempts to authenticate the provided {@link Authentication}
   *
   * @param authentication the {@link Authentication} to test
   * @return if authentication is successful an {@link Authentication} is returned. If
   * authentication cannot be determined, an empty Mono is returned. If authentication fails, a Mono
   * error is returned.
   */
  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    if (authentication instanceof BearerTokenAuthentication) {
      authentication.setAuthenticated(true);
    }
    return Mono.just(authentication);
  }
}
