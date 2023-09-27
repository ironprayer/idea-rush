package com.bid.idearush.domain.auth.service;

import com.bid.idearush.domain.auth.controller.request.LoginRequest;
import com.bid.idearush.domain.auth.controller.request.SignupRequest;
import com.bid.idearush.domain.user.entity.Users;
import com.bid.idearush.domain.user.repository.UserRepository;
import com.bid.idearush.global.exception.UserFindException;
import com.bid.idearush.global.exception.errortype.UserFindErrorCode;
import com.bid.idearush.global.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.bid.idearush.global.exception.errortype.UserFindErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public void signup(SignupRequest signupRequest) {
        boolean isDupUserAccountId = userRepository.findByUserAccountId(signupRequest.userAccountId()).isPresent();
        boolean isDupNickname = userRepository.findByNickname(signupRequest.nickname()).isPresent();

        if(isDupUserAccountId) {
            throw new UserFindException(USER_ID_DUPLICATE);
        } else if(isDupNickname) {
            throw new UserFindException(USER_NICKNAME_DUPLICATE);
        }

        Users user = signupRequest.toUsers(passwordEncoder);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public String login(LoginRequest loginRequest) {
        Users user = userRepository.findByUserAccountId(loginRequest.userAccountId())
                .orElseThrow(() -> {
                    throw new UserFindException(USER_ACCOUNT_ID_EMPTY);
                });

        if(!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UserFindException(USER_PASSWORD_WRONG);
        }

        return jwtUtils.generateToken(user.getId());
    }

}