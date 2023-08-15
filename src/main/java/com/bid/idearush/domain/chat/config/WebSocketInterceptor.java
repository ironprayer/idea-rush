package com.bid.idearush.domain.chat.config;

import com.bid.idearush.global.util.JwtUtils;
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

    private final JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        MessageHeaders header = message.getHeaders();
        MultiValueMap<String, String> map = header.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);
        System.out.println("헤더 : "+map);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            if(!jwtUtils.validateToken(map.get("Authorization").get(0))) return null;
        }
        return message;
    }

}
