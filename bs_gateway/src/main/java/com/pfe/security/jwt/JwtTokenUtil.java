package com.pfe.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class JwtTokenUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);
    @Value("${jhipster.security.authentication.jwt.base64-secret}")
    private String jwtSecret;

    private Key key;

    @PostConstruct
    public void init() {
        log.info("JWT Secret (Base64):{} ", this.jwtSecret); // Debugging line
        if (this.jwtSecret == null || this.jwtSecret.isEmpty()) {
            throw new IllegalStateException("JWT secret is not set.");
        }
        try {
            byte[] decodedKey = Base64.getDecoder().decode(this.jwtSecret);
            this.key = Keys.hmacShaKeyFor(decodedKey);
            if (this.key == null) {
                throw new IllegalStateException("Signing key cannot be null.");
            }
            log.info("Signing key initialized.");
        } catch (Exception e) {
            System.err.println("Error initializing JWT key: " + e.getMessage());
        }
    }

    public String generateToken(UUID userId, long validityDuration) {
        return Jwts.builder()
            .setSubject(String.valueOf(userId))
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + validityDuration))
            .signWith(this.key, SignatureAlgorithm.HS512)
            .compact();
    }

    public Key getKey() {
        if (this.key == null) {
            System.err.println("Signing key is not initialized.");
            throw new IllegalStateException("Signing key is not initialized.");
        }
        return this.key;
    }

    public Claims extractAllClaims(String token) {
        Key key = getKey();
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean isTokenExpired(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getExpiration().before(new Date());
    }
}
