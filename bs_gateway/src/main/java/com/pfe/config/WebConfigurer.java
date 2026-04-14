package com.pfe.config;


import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.config.WebFluxConfigurer;


/**
 * Configuration of web application with Servlet 3.0 APIs.
 */
@Configuration
public class WebConfigurer implements WebFluxConfigurer {

  private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

  @Value(value = "#{'${application.origins}'.split(',')}")
  private List<String> authorizedOrigins;

  public WebConfigurer() {

  }

  @Bean
  public HttpMessageConverters messageConverters() {
    return new HttpMessageConverters();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(this.authorizedOrigins);
    config.setAllowedMethods(List.of("*"));
    config.setAllowedHeaders(List.of("*"));
    config.setExposedHeaders(List.of("Authorization", "Link", "X-Total-Count", "X-Warning", "Location"));
    config.setAllowCredentials(true);
    if (!CollectionUtils.isEmpty(config.getAllowedOrigins()) || !CollectionUtils.isEmpty(
        config.getAllowedOriginPatterns())) {
      this.log.debug("Registering CORS filter");
      source.registerCorsConfiguration("/api/**", config);
      source.registerCorsConfiguration("/user-management/**", config);
      source.registerCorsConfiguration("/workflow-management/**", config);
      source.registerCorsConfiguration("/report-management/**", config);
      source.registerCorsConfiguration("/file-management/**", config);
      source.registerCorsConfiguration("/connector-management/**", config);
      source.registerCorsConfiguration("/form-management/**", config);
      source.registerCorsConfiguration("/notification-management/api/**", config);
      source.registerCorsConfiguration("/audit-management/**", config);
      source.registerCorsConfiguration("/payment-management/**", config);
      source.registerCorsConfiguration("/open-api/**", config);
      source.registerCorsConfiguration("/redirect/ectd", config);

    }
    return source;
  }
}
