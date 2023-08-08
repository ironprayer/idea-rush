package com.bid.idearush.domain.chat.model.reponse;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        String senderName,
        String msg,
        LocalDateTime createdAt
) {


}
