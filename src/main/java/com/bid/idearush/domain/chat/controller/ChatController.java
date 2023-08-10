package com.bid.idearush.domain.chat.controller;

import com.bid.idearush.domain.chat.model.reponse.ChatMessageResponse;
import com.bid.idearush.domain.chat.model.request.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/sendMessage")
    public void sendMessage(
            @Payload ChatMessageRequest chatMessage
    ) {
        simpMessageSendingOperations.convertAndSend("/sub", new ChatMessageResponse(chatMessage.name(), chatMessage.msg(), LocalDateTime.now()));
    }

}
