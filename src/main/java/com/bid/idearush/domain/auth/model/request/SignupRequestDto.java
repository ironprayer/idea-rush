package com.bid.idearush.domain.auth.model.request;

import jakarta.validation.constraints.Size;

public record SignupRequestDto (
    @Size(max = 16)
    String userAccountId,
    @Size(max = 16)
    String nickname,
    @Size(max = 32)
    String password
) {}