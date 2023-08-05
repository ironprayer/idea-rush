package com.bid.idearush.domain.auth.model.reponse;

import java.time.LocalDateTime;

public record TokenResponse(

        String accessToken,
        LocalDateTime expiredAt

) {}
