package com.bid.idearush.domain.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Slf4j(topic = "채팅 로그")
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        MessageHeaders header = message.getHeaders();
        MultiValueMap<String, String> map = header.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("웹 소켓 접속함." + map.get("Authorization"));
            //TODO: JWT 토큰 검증 처리.
        }
        return message;
    }

}
