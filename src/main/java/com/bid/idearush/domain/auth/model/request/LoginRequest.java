package com.bid.idearush.domain.auth.model.request;

import jakarta.validation.constraints.Size;

public record LoginRequest(

        @Size(max = 16)
        String userAccountId,
        @Size(max = 32)
        String password

) {}
