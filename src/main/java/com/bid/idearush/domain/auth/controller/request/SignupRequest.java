package com.bid.idearush.domain.auth.controller.request;

import com.bid.idearush.domain.user.entity.Users;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;

public record SignupRequest(
        @Size(max = 16)
        String userAccountId,
        @Size(max = 16)
        String nickname,
        @Size(max = 32)
        String password
) {

    public Users toUsers(PasswordEncoder passwordEncoder) {
        Users user = Users.builder()
                .userAccountId(userAccountId())
                .nickname(nickname())
                .password(passwordEncoder.encode(password()))
                .build();
        return user;
    }

}