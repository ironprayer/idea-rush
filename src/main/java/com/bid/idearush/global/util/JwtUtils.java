package com.bid.idearush.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key SIGNING_KEY;
    private final Integer ACCESS_TOKEN_DURATION_SECONDS = 60 * 60;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        SIGNING_KEY = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(Long userId) {
        Instant now = Instant.now();
        Instant expiryDateOfAccessToken = now.plusSeconds(ACCESS_TOKEN_DURATION_SECONDS);

        return Jwts.builder()
                .setClaims(Map.of(
                        "userId", userId,
                        "iat", now.getEpochSecond(),
                        "exp", expiryDateOfAccessToken.getEpochSecond()
                ))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public Long parseUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

}
