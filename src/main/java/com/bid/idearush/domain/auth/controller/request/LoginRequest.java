package com.bid.idearush.domain.auth.model.request;

public record LoginRequest(
        String userAccountId,
        String password
) {}