package com.bid.idearush.domain.auth.service;

import com.bid.idearush.domain.auth.model.request.SignupRequestDto;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Nested
    @DisplayName("회원가입 테스트")
    class Signup {

        @Test
        @DisplayName("회원가입 성공 테스트")
        void signupSuccessTest() {
            // given
            SignupRequestDto signupRequestDto =
                    new SignupRequestDto("a123", "aaaa", "1234");
            AuthService authService = new AuthService(userRepository, passwordEncoder);

            given(userRepository.findByUserAccountId(signupRequestDto.userAccountId()))
                    .willReturn(Optional.empty());
            given(userRepository.findByNickname(signupRequestDto.nickname()))
                    .willReturn(Optional.empty());

            // when
            authService.signup(signupRequestDto);

            // then
            verify(userRepository, times(1)).save(any(Users.class));
        }

        @Test
        @DisplayName("회원가입 시 유저 아이디가 중복되는 경우 테스트")
        void signupUserAccountIdDupTest() {
            // given
            SignupRequestDto signupRequestDto =
                    new SignupRequestDto("a123", "aaaa", "1234");
            AuthService authService = new AuthService(userRepository, passwordEncoder);
            Users user = Users.builder().build();

            given(userRepository.findByUserAccountId(signupRequestDto.userAccountId()))
                    .willReturn(Optional.of(user));

            // when
            Exception exception = assertThrows(IllegalStateException.class, ()->
                    authService.signup(signupRequestDto));

            // then
            assertEquals("유저 아이디가 중복됩니다.", exception.getMessage());
            verify(userRepository, times(0)).save(any(Users.class));
        }

        @Test
        @DisplayName("회원가입 시 닉네임이 중복되는 경우 테스트")
        void signupNicknameDupTest() {
            // given
            SignupRequestDto signupRequestDto =
                    new SignupRequestDto("a123", "aaaa", "1234");
            AuthService authService = new AuthService(userRepository, passwordEncoder);
            Users user = Users.builder().build();

            given(userRepository.findByNickname(signupRequestDto.nickname()))
                    .willReturn(Optional.of(user));

            // when
            Exception exception = assertThrows(IllegalStateException.class, ()->
                    authService.signup(signupRequestDto));

            // then
            assertEquals("닉네임이 중복됩니다.", exception.getMessage());
            verify(userRepository, times(0)).save(any(Users.class));
        }
    }
}
