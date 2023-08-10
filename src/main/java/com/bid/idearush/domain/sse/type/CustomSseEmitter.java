package com.bid.idearush.domain.sse.type;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public record CustomSseEmitter (
        Long id,
        SseEmitter sseEmitter
) {}
