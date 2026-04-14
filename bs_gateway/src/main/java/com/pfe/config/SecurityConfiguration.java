package com.pfe.config;

import com.pfe.security.jwt.*;
import com.pfe.security.jwt.AuthenticationManager;
import com.pfe.security.jwt.SecurityContextRepository;
import com.pfe.security.validation.config.TokenValidatorFilter;
import com.pfe.security.validation.error.*;
import com.pfe.security.validation.error.CustomAccessDeniedHandler;
import com.pfe.security.validation.error.JwtAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.web.server.*;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;


@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfiguration {


  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;
  private final SecurityContextRepository securityContextRepository;
  private final AuthenticationManager authenticationManager;

  @Value("${tokenvalidator.security.url_auth_server}")
  private String urlBaseKeycloak;

  @Value("${tokenvalidator.security.cache_token_delay}")
  private int delayCachetoken;

  @Value("${tokenvalidator.security.cache_certs_keys_delay}")
  private int delayCacheCertsKeys;

  public SecurityConfiguration(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
      CustomAccessDeniedHandler customAccessDeniedHandler,
      AuthenticationManager authenticationManager,
      SecurityContextRepository securityContextRepository) {
    this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    this.customAccessDeniedHandler = customAccessDeniedHandler;
    this.authenticationManager = authenticationManager;

    this.securityContextRepository = securityContextRepository;
  }


  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    {
      TokenValidatorFilter tokenValidatorFilter = new TokenValidatorFilter(
          this.jwtAuthenticationEntryPoint, this.urlBaseKeycloak, this.delayCachetoken,
          this.delayCacheCertsKeys);

      http
          .csrf(csrf -> csrf.disable())
          .authorizeExchange(authz ->
              // prettier-ignore
              authz
                  .pathMatchers(HttpMethod.OPTIONS)
                  .permitAll()
                  .pathMatchers(("/open-api/**"))
                  .permitAll()
                  .pathMatchers(("/api/v1/redirect/**"))
                  .permitAll()
                  .pathMatchers(("/api/v1/oauth/**")).permitAll()
                  .pathMatchers(("/api/v1/send-email/**")).permitAll()
                  .pathMatchers("/redirect/**").permitAll()
                  .pathMatchers(("/api/**")).authenticated()
                  .pathMatchers(("/notification-management/websocket/**")).permitAll()
                  .pathMatchers(("/notification-management/api/v1/open-api/**")).permitAll()
                  .pathMatchers(("/user-management/api/v1/open-api/**")).permitAll()
                  .pathMatchers(("/file-management/api/v1/open-api/**")).permitAll()
                  .anyExchange().authenticated()
          )
          .httpBasic(basic -> basic.disable())
          .exceptionHandling(ex -> ex.authenticationEntryPoint(this.jwtAuthenticationEntryPoint)
              .accessDeniedHandler(this.customAccessDeniedHandler))
          .addFilterAt(tokenValidatorFilter, SecurityWebFiltersOrder.FIRST)
          .authenticationManager(this.authenticationManager)
          .securityContextRepository(this.securityContextRepository);

      return http.build();
    }
  }

  @Bean
  @Order(2)
  SecurityWebFilterChain browserChain(ServerHttpSecurity http) {

    http
        .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/redirect/**", "/login/oauth2/**", "/oauth2/authorization/**"))
        .csrf(c -> c.disable())
        .authorizeExchange(ex -> ex.anyExchange().authenticated())
        .oauth2Login(Customizer.withDefaults())
        .oauth2Client(Customizer.withDefaults());

    return http.build();
  }
  @Bean
  GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults("");
  }

}


