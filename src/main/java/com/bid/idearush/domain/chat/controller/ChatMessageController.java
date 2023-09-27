package com.bid.idearush.domain.chat.controller;

import com.bid.idearush.domain.chat.controller.reponse.ChatMessageResponse;
import com.bid.idearush.domain.chat.controller.request.ChatMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/sendMessage")
    public void sendMessage(
            @Payload ChatMessageRequest chatMessage
    ) {
        System.out.println(chatMessage);
        simpMessageSendingOperations.convertAndSend("/sub", new ChatMessageResponse(chatMessage.name(), chatMessage.msg(), LocalDateTime.now()));
    }

}
