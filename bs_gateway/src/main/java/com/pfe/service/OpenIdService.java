package com.pfe.service;

import org.springframework.util.MultiValueMap;

import java.util.Map;

public interface OpenIdService {
    Map<String, String> getEhouweyaConfig();
    Map<String, Object> exchangeCodeAndFetchUser(String code, String codeVerifier) throws Exception;
}
