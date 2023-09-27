package com.bid.idearush.domain.auth.controller.request;

import com.bid.idearush.domain.auth.utils.PasswordUtils;
import com.bid.idearush.domain.user.entity.Users;
import jakarta.validation.constraints.Size;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.bid.idearush.domain.auth.utils.PasswordUtils.parsePassword;

public record SignupRequest(
        @Size(max = 16)
        String userAccountId,
        @Size(max = 16)
        String nickname,
        @Size(max = 32)
        String password
) {

    public Users toUser() {
        Users user = Users.builder()
                .userAccountId(userAccountId())
                .nickname(nickname())
                .password(parsePassword(password))
                .build();
        return user;
    }

}