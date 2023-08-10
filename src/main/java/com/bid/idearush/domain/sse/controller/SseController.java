package com.bid.idearush.domain.sse.controller;

import com.bid.idearush.domain.sse.service.SseService;
import com.bid.idearush.domain.sse.type.SseConnect;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse/connect")
@RequiredArgsConstructor
public class SseController {

    private final SseService sseService;

    @GetMapping("/user/{id}")
    @ResponseBody
    public SseEmitter connectUser(
            @PathVariable(name = "id") Long userId,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return sseService.connect(SseConnect.NOTIFICATION, userId, lastEventId);
    }

    @GetMapping("/idea/{id}")
    @ResponseBody
    public SseEmitter connectIdea(
            @PathVariable(name = "id") Long ideaId,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return sseService.connect(SseConnect.BID, ideaId, lastEventId);
    }
}
