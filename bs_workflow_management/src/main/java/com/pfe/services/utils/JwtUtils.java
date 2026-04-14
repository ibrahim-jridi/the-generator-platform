package com.pfe.services.utils;


import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public final class JwtUtils {

    private final JwtParser jwtParser;
    public static final String CLAIMS_NAMESPACE = "https://www.jhipster.tech/";
    public static final String PREFERRED_USERNAME = "preferred_username";

    public JwtUtils(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    public UUID getConnectedUserId() {
        String token = getCurrentUserToken();
        if (token == null || token.equals("")) {
            return null ;
        }
        int i = token.lastIndexOf('.');
        String withoutSignature = token.substring(0, i + 1);
        Claims claims = jwtParser.parseClaimsJwt(withoutSignature).getBody();
        String clientId = (String) claims.get("sub");

        return UUID.fromString(clientId);
    }

    public static String getCurrentUserToken() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication != null) {
            return ((BearerTokenAuthentication) authentication).getToken().getTokenValue();
        }
        return null;
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication instanceof BearerTokenAuthentication) {
            Map<String, Object> tokenAttributes = ((BearerTokenAuthentication) authentication).getTokenAttributes();
            if (tokenAttributes.containsKey(PREFERRED_USERNAME)) {
                return (String) tokenAttributes.get(PREFERRED_USERNAME);
            }
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication instanceof JwtAuthenticationToken) {
            return (String) ((JwtAuthenticationToken) authentication).getToken().getClaims().get(PREFERRED_USERNAME);
        } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
            Map<String, Object> attributes = ((DefaultOidcUser) authentication.getPrincipal()).getAttributes();
            if (attributes.containsKey(PREFERRED_USERNAME)) {
                return (String) attributes.get(PREFERRED_USERNAME);
            }
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
}



