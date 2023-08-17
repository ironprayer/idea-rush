package com.bid.idearush.domain.auth.service;

import com.bid.idearush.domain.auth.model.request.SignupRequest;
import com.bid.idearush.domain.user.model.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

}