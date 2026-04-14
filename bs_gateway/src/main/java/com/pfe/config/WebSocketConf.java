package com.pfe.config;


import feign.Client;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.socket.client.StandardWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import org.springframework.web.reactive.socket.server.WebSocketService;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.upgrade.TomcatRequestUpgradeStrategy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Configuration
public class WebSocketConf {

  @Bean
  @Primary
  public WebSocketClient tomcatWebSocketClient() {

    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    container.setDefaultMaxBinaryMessageBufferSize(1024 * 1024 * 2);  // 2MB
    container.setDefaultMaxTextMessageBufferSize(1024 * 1024 * 2);    // 2MB
    return new StandardWebSocketClient(container);
  }

  @Bean
  @Primary
  public WebSocketService getWebSocketService() {
    TomcatRequestUpgradeStrategy strategy = new TomcatRequestUpgradeStrategy();
    strategy.setMaxSessionIdleTimeout(0L);  // No timeout
    strategy.setMaxBinaryMessageBufferSize(1024 * 1024 * 2);  // 2MB
    strategy.setMaxTextMessageBufferSize(1024 * 1024 * 2);    // 2MB

    return new HandshakeWebSocketService(strategy);
  }
    @Bean
    public Client feignClient() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        return new Client.Default(sslContext.getSocketFactory(), (hostname, session) -> true);
    }
}
