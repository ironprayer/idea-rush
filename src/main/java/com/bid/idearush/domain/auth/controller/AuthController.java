package com.bid.idearush.domain.auth.controller;

import com.bid.idearush.domain.auth.controller.request.LoginRequest;
import com.bid.idearush.domain.auth.controller.request.SignupRequest;
import com.bid.idearush.domain.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
    }

    // jwt 토큰을 헤더 , 쿠키 , 바디에 담아서 보내는 것에 차이가 있는가 ?
    @PostMapping("/login")
    public void login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String accessToken = authService.login(loginRequest);
        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
    }

}