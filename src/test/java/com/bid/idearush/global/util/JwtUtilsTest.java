package com.bid.idearush.global.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        setField(jwtUtils, "secretKey", "qbUUJ7SZOV1Az2zNzHtRQHSZuMgi9fsSFuwxLPDhSeM=");
        jwtUtils.init();
    }

    @Test
    @DisplayName("토큰 생성 성공 테스트")
    void succeedGenerateTokenTest() {
        Long userId = 1L;

        String accessToken = jwtUtils.generateToken(userId);

        assertNotNull(accessToken);
    }

    @Test
    @DisplayName("userId parsing 및 가져오기 성공 테스트")
    void succeedGetParseUserIdTest() {
        String accessToken = jwtUtils.generateToken(1L);

        Long userId = jwtUtils.parseUserId(accessToken);

        assertEquals(1L, userId);
    }

    @Test
    @DisplayName("accessToken 유효성 검사 성공 테스트")
    void succeedValidateTokenTest() {
        String accessToken = jwtUtils.generateToken(1L);

        boolean isValidateToken = jwtUtils.validateToken(accessToken);

        assertTrue(isValidateToken);
    }

    @Test
    @DisplayName("유효하지 않은 accessToken으로 인한 유효성 검사 실패 테스트")
    void failValidateTokenInvalidTest() {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTY5MTQyMDYwOCwiaWF0IjoxNjkxNDE3MDA4fQ." +
                "Cr6CsoDVGYUoS98S8a-lOWtmi46Wy83P5UrYDCLYojI";

        boolean isValidateInvalidToken = jwtUtils.validateToken(accessToken);

        assertFalse(isValidateInvalidToken);
    }

    @Test
    @DisplayName("accessToken claims 빈 값으로 인한 유효성 검사 실패 테스트")
    void failValidateTokenClaimsEmptyTest() {
        String accessToken = "";

        boolean isValidateClaimsEmptyToken = jwtUtils.validateToken(accessToken);

        assertFalse(isValidateClaimsEmptyToken);
    }

    @Test
    @DisplayName("만료된 accessToken 으로 인한 유효성 검사 실패 테스트")
    void failValidateTokenExpiredTest() {
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTY5MTQyMDYwOCwiaWF0IjoxNjkxNDE3MDA4fQ." +
                "Cr6CsoDVGYUoS98S8a-lOWtmi46Wy83P5UrYDCLYojI";

        boolean isValidateExpiredToken = jwtUtils.validateToken(accessToken);

        assertFalse(isValidateExpiredToken);
    }

}