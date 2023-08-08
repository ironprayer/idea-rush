package com.bid.idearush.domain.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        MessageHeaders header = message.getHeaders();
        MultiValueMap<String, String> map = header.get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("웹 소켓 접속함." + map.get("Authorization"));
        }
        return message;
    }

}
