package com.bid.idearush.domain.auth.controller;

import com.bid.idearush.domain.auth.model.request.SignupRequest;
import com.bid.idearush.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
    }

}
