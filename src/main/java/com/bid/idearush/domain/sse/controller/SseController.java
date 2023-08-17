package com.bid.idearush.domain.sse.controller;

import com.bid.idearush.domain.sse.service.SseService;
import com.bid.idearush.domain.sse.type.SseConnect;
import com.bid.idearush.global.security.AuthPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse/connect")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping("/user")
    public SseEmitter connectUser(
            @AuthenticationPrincipal AuthPayload authPayload,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return sseService.connect(SseConnect.NOTIFICATION, authPayload.userId(), lastEventId);
    }

    @GetMapping("/idea/{id}")
    public SseEmitter connectIdea(
            @PathVariable(name = "id") Long ideaId,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return sseService.connect(SseConnect.BID, ideaId, lastEventId);
    }
}
