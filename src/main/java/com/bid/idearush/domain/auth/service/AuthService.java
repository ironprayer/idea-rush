package com.bid.idearush.domain.auth.service;

import com.bid.idearush.domain.auth.model.reponse.TokenResponse;
import com.bid.idearush.domain.auth.model.request.LoginRequest;
import com.bid.idearush.domain.auth.model.request.SignupRequest;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest signupRequest) {
        boolean isDupUserAccountId = userRepository.findByUserAccountId(signupRequest.userAccountId()).isPresent();
        boolean isDupNickname = userRepository.findByNickname(signupRequest.nickname()).isPresent();

        if(isDupUserAccountId) {
            throw new IllegalStateException("유저 아이디가 중복됩니다.");
        } else if(isDupNickname) {
            throw new IllegalStateException("닉네임이 중복됩니다.");
        }

        Users user = signupRequest.toUsers(passwordEncoder);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest loginRequest) {
        Users user = userRepository.findByUserAccountId(loginRequest.userAccountId()).orElseThrow(
                () -> new IllegalArgumentException("아이디를 다시 확인해 주세요."));

        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        return JwtUtils.createToken(user.getId());
    }

}
