package com.bid.idearush.domain.chat.controller;

import com.bid.idearush.domain.chat.service.ChatService;
import com.bid.idearush.global.security.AuthPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/getUserName")
    public String getUserNickName(
            @AuthenticationPrincipal AuthPayload authPayload
    ) {
        return chatService.getUserNickName(authPayload.userId());
    }

}
