package com.pfe.config;

import com.pfe.domain.enums.Topic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value(value = "${bs-app.endpoint.front-url}")
  private String frontUrl;

  @Value(value = "${bs-app.endpoint.gateway-url}")
  private String gatewayUrl;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // DEFINE HERE ALL TOPICs
    config.enableSimpleBroker(String.valueOf(Topic.WORKFLOW_EVENT_TO_USER), "/user");
    // DEFINE HERE TO IDENTIFY USERs VIA WEBSOCKET
    config.setUserDestinationPrefix("/user");
    config.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/websocket").setAllowedOrigins(this.frontUrl).withSockJS();
  }
}


