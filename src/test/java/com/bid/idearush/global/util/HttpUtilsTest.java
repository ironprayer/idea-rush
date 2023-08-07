package com.bid.idearush.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class HttpUtilsTest {

    MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

    @Test
    @DisplayName("jwt 가져오기 성공 테스트")
    void getJwtSuccessTest() {
        mockHttpServletRequest.addHeader(
                "Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTY5MTQyMDYwOCwi" +
                        "aWF0IjoxNjkxNDE3MDA4fQ.Cr6CsoDVGYUoS98S8a-lOWtmi46Wy83P5UrYDCLYojI");

        String token = HttpUtils.getJwtFromRequest(mockHttpServletRequest);

        assertEquals(token, "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTY5MTQyMDYwOCwiaWF0IjoxNjkxNDE3MD" +
                "A4fQ.Cr6CsoDVGYUoS98S8a-lOWtmi46Wy83P5UrYDCLYojI");
    }

    @Test
    @DisplayName("Authorization의 value 값이 빈 경우 jwt 가져오기 실패 테스트")
    void getJwtNotValueFailTest() {
        mockHttpServletRequest.addHeader("Authorization", "");

        String token = HttpUtils.getJwtFromRequest(mockHttpServletRequest);

        assertNull(token);
    }

    @Test
    @DisplayName("토큰 값 앞에 Bearer이 붙지 않은 경우 jwt 가져오기 실패 테스트")
    void getJwtNotBearerFailTest(){
        mockHttpServletRequest.addHeader(
                "Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImV4cCI6MTY5MTQyMDYwOCwiaWF0Ijox" +
                        "NjkxNDE3MDA4fQ.Cr6CsoDVGYUoS98S8a-lOWtmi46Wy83P5UrYDCLYojI");

        String token = HttpUtils.getJwtFromRequest(mockHttpServletRequest);

        assertNull(token);
    }

}
