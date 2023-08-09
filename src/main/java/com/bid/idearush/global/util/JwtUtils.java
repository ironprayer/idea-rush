package com.bid.idearush.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;
    private static Key SIGNING_KEY;
    private static final Integer ACCESS_TOKEN_DURATION_SECONDS = 60 * 60;

    @PostConstruct
    private void init() {
        SIGNING_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(Long userId) {
        Instant now = Instant.now();
        Instant expiryDateOfAccessToken = now.plusSeconds(ACCESS_TOKEN_DURATION_SECONDS);

        String accessToken = Jwts.builder()
                .setClaims(Map.of(
                        "userId", userId,
                        "iat", now.getEpochSecond(),
                        "exp", expiryDateOfAccessToken.getEpochSecond()
                ))
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256)
                .compact();

        System.out.println(accessToken);
        return accessToken;
    }

    public static boolean validateToken(String token) {
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

    public static Long parseUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

}
