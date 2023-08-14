package com.bid.idearush.domain.auth.model.request;

import jakarta.validation.constraints.Size;

public record LoginRequest(
        String userAccountId,
        String password
) {}