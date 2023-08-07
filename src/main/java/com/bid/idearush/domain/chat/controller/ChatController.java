package com.bid.idearush.domain.chat.controller;

import com.bid.idearush.domain.chat.model.reponse.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations template;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageResponse chatMessage) {
        template.convertAndSend("/", chatMessage);
    }

}
