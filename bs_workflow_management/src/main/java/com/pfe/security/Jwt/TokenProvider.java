package com.pfe.security.Jwt;

import com.pfe.security.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class TokenProvider {
    private static final String AUTHORITY_PREFIX = "SCOPE_";
    private static final String AUTHORITIES_KEY = "auth";
    private final JwtParser jwtParser;
    private final long tokenValidityInMilliseconds;
    private final long tokenValidityInMillisecondsForRememberMe;

    public TokenProvider() {
        jwtParser = Jwts.parserBuilder().build();
        this.tokenValidityInMilliseconds = 1000 * 300;
        this.tokenValidityInMillisecondsForRememberMe = 1000 * 300;
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date validity;
        if (rememberMe) {
            validity = new Date(now + this.tokenValidityInMillisecondsForRememberMe);
        } else {
            validity = new Date(now + this.tokenValidityInMilliseconds);
        }

        return Jwts
            .builder()
            .setSubject(authentication.getName())
            .claim(AUTHORITIES_KEY, authorities)
            .signWith(null, "PS512")
            .setExpiration(validity)
            .compact();
    }

    public Authentication getAuthentication(String token) {
        int i = token.lastIndexOf('.');
        String withoutSignature = token.substring(0, i + 1);
        Claims claims = jwtParser.parseClaimsJwt(withoutSignature).getBody();

        List<GrantedAuthority> authorities = SecurityUtils.extractAuthorityFromClaims(claims);

        OAuth2AuthenticatedPrincipal auth2AuthenticatedPrincipal = getAuth2AuthenticatedPrincipal(claims);

        Instant iat = auth2AuthenticatedPrincipal.getAttribute(OAuth2TokenIntrospectionClaimNames.IAT);

        Instant exp = auth2AuthenticatedPrincipal.getAttribute(OAuth2TokenIntrospectionClaimNames.EXP);

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, token, iat, exp);
        return new BearerTokenAuthentication(auth2AuthenticatedPrincipal, accessToken, authorities);
    }

    private List<SimpleGrantedAuthority> addPermissionMatrix(List<GrantedAuthority> authorities) {
        //to do add permissions of roles
        List<SimpleGrantedAuthority> lstPermissions = new ArrayList<>();
//        String role = "ROLE_ADMIN";
//        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
//        lstPermissions.add(authority);
        //filter some authoritie and add it if you need
        return lstPermissions;
    }

    private OAuth2AuthenticatedPrincipal getAuth2AuthenticatedPrincipal(Claims claims) {
        return convertClaimsSet(claims);
    }

    private OAuth2AuthenticatedPrincipal convertClaimsSet(Map<String, Object> claims) {
        claims.computeIfPresent(
            OAuth2TokenIntrospectionClaimNames.AUD,
            (k, v) -> {
                if (v instanceof String) {
                    return Collections.singletonList(v);
                }
                return v;
            }
        );
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.CLIENT_ID, (k, v) -> v.toString());
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.EXP, (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.IAT, (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.ISS, (k, v) -> v.toString());
        claims.computeIfPresent(OAuth2TokenIntrospectionClaimNames.NBF, (k, v) -> Instant.ofEpochSecond(((Number) v).longValue()));
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        claims.computeIfPresent(
            OAuth2TokenIntrospectionClaimNames.SCOPE,
            (k, v) -> {
                if (v instanceof String) {
                    Collection<String> scopes = Arrays.asList(((String) v).split(" "));
                    for (String scope : scopes) {
                        authorities.add(new SimpleGrantedAuthority(AUTHORITY_PREFIX + scope));
                    }
                    return scopes;
                }
                return v;
            }
        );
        return new OAuth2IntrospectionAuthenticatedPrincipal(claims, authorities);
    }

    public boolean validateToken(String authToken) {
        return true;
    }
}
