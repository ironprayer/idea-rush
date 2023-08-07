package com.bid.idearush.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JwtUtilsTest {

    @Test
    @DisplayName("토큰 생성 성공 테스트")
    void generateTokenSuccessTest() {
        Long userId = 1L;

        String accessToken = JwtUtils.generateToken(userId);

        assertNotNull(accessToken);
    }

    @Test
    @DisplayName("userId parsing 및 가져오기 성공 테스트")
    void parseUserIdGetSuccessTest() {
        String accessToken = JwtUtils.generateToken(1L);

        Long userId = JwtUtils.parseUserId(accessToken);

        assertEquals(1L, userId);
    }

    @Test
    @DisplayName("accessToken 유효성 검사 성공 테스트")
    void validateAccessTokenSuccessTest() {
        String accessToken = JwtUtils.generateToken(1L);

        boolean isValidateToken = JwtUtils.validateToken(accessToken);

        assertEquals(true, isValidateToken);
    }

    @Test
    @DisplayName("유효하지 않은 accessToken으로 인한 유효성 검사 실패 테스트")
    void validateTokenInvalidFailTest() {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTY5MTQyMDYwOCwiaWF0IjoxNjkxNDE3MDA4fQ." +
                "Cr6CsoDVGYUoS98S8a-lOWtmi46Wy83P5UrYDCLYojI";

        boolean isValidateToken = JwtUtils.validateToken(accessToken);

        assertEquals(false, isValidateToken);
    }

    @Test
    @DisplayName("accessToken claims 빈 값으로 인한 유효성 검사 실패 테스트")
    void validateTokenClaimsEmptyFailTest() {
        String accessToken = "";

        boolean isValidateToken = JwtUtils.validateToken(accessToken);

        assertEquals(false, isValidateToken);
    }

    @Test
    @DisplayName("만료된 accessToken 으로 인한 유효성 검사 실패 테스트")
    void validateTokenExpiredFailTest() {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTY5MTQyMDYwOCwiaWF0IjoxNjkxNDE3MDA4fQ." +
                "Cr6CsoDVGYUoS98S8a-lOWtmi46Wy83P5UrYDCLYojI";

        boolean isValidateToken = JwtUtils.validateToken(accessToken);

        assertEquals(false, isValidateToken);
    }
}
