package com.bid.idearush.global.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUtils {

    private static final String secretKey = "IdeaRushdkfdifekdihgakdhjifekfdjkfjejdfkdfjeikd";
    private static final Key SIGNING_KEY = getSigningKey();
    private static final Integer ACCESS_TOKEN_DURATION_SECONDS = 60 * 60;
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

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

        return accessToken;
    }

    public static Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SIGNING_KEY).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty.");
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
