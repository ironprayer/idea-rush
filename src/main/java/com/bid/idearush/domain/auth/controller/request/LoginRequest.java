package com.bid.idearush.domain.auth.controller.request;

public record LoginRequest(
        String userAccountId,
        String password
) {}