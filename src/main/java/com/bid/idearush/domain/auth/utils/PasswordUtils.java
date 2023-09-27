package com.bid.idearush.domain.auth.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordUtils {
    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String parsePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean validatePassword(String expectPassword, String actualPassword){
        return passwordEncoder.matches(expectPassword, actualPassword);
    }
}
