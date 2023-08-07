package com.bid.idearush.domain.chat.model.reponse;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        String senderName,
        String msg,
        LocalDateTime createdAt
) {

    public static ChatMessageResponse from(ChatLog chatLog){
        return new ChatMessageResponse(
                chatLog.getUsers().getNickname(),
                chatLog.getMsg(),
                chatLog.getCreatedAt()
        );
    }

}
