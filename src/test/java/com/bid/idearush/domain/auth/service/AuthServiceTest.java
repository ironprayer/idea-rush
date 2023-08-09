package com.bid.idearush.domain.auth.service;

import com.bid.idearush.domain.auth.model.request.LoginRequest;
import com.bid.idearush.domain.auth.model.request.SignupRequest;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.exception.UserFindException;
import com.bid.idearush.global.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void initJwtUtils() {
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.init();
    }

    @Nested
    @DisplayName("회원가입 테스트")
    class Signup {

        SignupRequest signupRequest =
                new SignupRequest("a123", "aaaa", "1234");

        @Test
        @DisplayName("회원가입 성공 테스트")
        void signupSuccessTest() {
            given(userRepository.findByUserAccountId(signupRequest.userAccountId()))
                    .willReturn(Optional.empty());
            given(userRepository.findByNickname(signupRequest.nickname()))
                    .willReturn(Optional.empty());

            authService.signup(signupRequest);

            verify(userRepository, times(1)).save(any(Users.class));
        }

        @Test
        @DisplayName("유저 아이디가 중복되어 회원가입에 실패하는 경우 테스트")
        void UserAccountIdDupSignupFailTest() {
            given(userRepository.findByUserAccountId(signupRequest.userAccountId()))
                    .willReturn(Optional.of(Users.builder().build()));

            IllegalStateException ex = assertThrows(IllegalStateException.class, ()->
                    authService.signup(signupRequest));

            assertEquals("유저 아이디가 중복됩니다.", ex.getMessage());
        }

        @Test
        @DisplayName("닉네임이 중복되어 회원가입에 실패하는 경우 테스트")
        void NicknameDupSignupFailTest() {
            given(userRepository.findByNickname(signupRequest.nickname()))
                    .willReturn(Optional.of(Users.builder().build()));

            IllegalStateException ex = assertThrows(IllegalStateException.class, ()->
                    authService.signup(signupRequest));

            assertEquals("닉네임이 중복됩니다.", ex.getMessage());
        }

    }

    @Nested
    @DisplayName("로그인 테스트")
    class Login {

        LoginRequest loginRequest = new LoginRequest("a123", "1234");

        @Test
        @DisplayName("로그인 성공 테스트")
        void loginSuccessTest() {
            Users user = Users.builder().id(1L).build();
            given(userRepository.findByUserAccountId(loginRequest.userAccountId()))
                    .willReturn(Optional.of(user));
            given(passwordEncoder.matches(loginRequest.password(), user.getPassword())).willReturn(true);

            String accessToken = authService.login(loginRequest);

            assertNotNull(accessToken);
        }

        @Test
        @DisplayName("로그인 시 유저 아이디가 존재하지 않은 경우 테스트")
        void loginNotUserAccountIdTest() {
            given(userRepository.findByUserAccountId(loginRequest.userAccountId())).willReturn(Optional.empty());

            UserFindException ex = assertThrows(UserFindException.class, () ->
                    authService.login(loginRequest));

            assertEquals(ex.getMessage(), "유저 아이디가 존재하지 않습니다.");
        }

        @Test
        @DisplayName("로그인 시 비밀번호가 유효하지 않은 경우 테스트")
        void loginInvalidPasswordTest() {
            Users user = Users.builder().build();
            given(userRepository.findByUserAccountId(loginRequest.userAccountId())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(loginRequest.password(), user.getPassword())).willReturn(false);

            UserFindException ex = assertThrows(UserFindException.class, () ->
                    authService.login(loginRequest));

            assertEquals(ex.getMessage(), "잘못된 비밀번호입니다.");
        }

    }

}
