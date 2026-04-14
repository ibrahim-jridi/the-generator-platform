package com.pfe.config;

import java.io.IOException;
import java.net.Socket;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class TrustAllSSLSocketFactory extends SSLSocketFactory {

  private SSLSocketFactory factory;

  public TrustAllSSLSocketFactory() throws Exception {
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, new TrustManager[]{new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] xcs, String string) {
      }

      public void checkServerTrusted(X509Certificate[] xcs, String string) {
      }

      public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
      }
    }}, new java.security.SecureRandom());
    factory = sc.getSocketFactory();
  }

  public static SSLSocketFactory getDefault() {
    try {
      return new TrustAllSSLSocketFactory();
    } catch (Exception e) {
      throw new RuntimeException("Error on create TrustAllSSLSocketFactory", e);
    }
  }

  @Override
  public String[] getDefaultCipherSuites() {
    return factory.getDefaultCipherSuites();
  }

  @Override
  public String[] getSupportedCipherSuites() {
    return factory.getSupportedCipherSuites();
  }

  @Override
  public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
      throws IOException {
    return factory.createSocket(socket, host, port, autoClose);
  }

  @Override
  public Socket createSocket(String host, int port) throws IOException {
    return factory.createSocket(host, port);
  }

  @Override
  public Socket createSocket(String host, int port, java.net.InetAddress localHost, int localPort)
      throws IOException {
    return factory.createSocket(host, port, localHost, localPort);
  }

  @Override
  public Socket createSocket(java.net.InetAddress host, int port) throws IOException {
    return factory.createSocket(host, port);
  }

  @Override
  public Socket createSocket(java.net.InetAddress address, int port,
      java.net.InetAddress localAddress, int localPort) throws IOException {
    return factory.createSocket(address, port, localAddress, localPort);
  }
}
