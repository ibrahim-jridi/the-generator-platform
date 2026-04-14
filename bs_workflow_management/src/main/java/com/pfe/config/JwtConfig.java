package com.pfe.config;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public JwtParser jwtParser() {
        String secretKey = "OTM5ZDUwZjEzMzllY2Y5MmIwM2ZlNjUxNzk0ZTdlMDVhNTc4MzUxMmNkOTJkNzQzMzQxNTA3OWMzYTZiNjg5OWRmMTUyYzZkMjJhODM5NmZhM2ZkM2E5NjQ4MDQzYzBlMmMzYTJlYzllODM5MTdjNzhmNjM0ODBiZmQyNGM4YzM=";
        return Jwts.parser().setSigningKey(secretKey);
    }
}
