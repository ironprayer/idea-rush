package com.bid.idearush.domain.chat.config;

import com.bid.idearush.domain.chat.model.reponse.ChatMessageResponse;
import com.bid.idearush.domain.chat.model.request.ChatMessageRequest;
import com.bid.idearush.global.util.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketConfigTest {

    private static final String WEBSOCKET_URI = "http://localhost:{port}/chat";
    private static final String WEBSOCKET_TOPIC = "/sub";
    @LocalServerPort
    private int port;
    private BlockingQueue<String> blockingQueue;
    private WebSocketStompClient stompClient;
    @Autowired
    private JwtUtils jwtUtils;

    @BeforeEach
    public void setup() {
        this.blockingQueue = new ArrayBlockingQueue<>(1);
        List<Transport> transports = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
        this.stompClient = new WebSocketStompClient(new SockJsClient(transports));
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    }

    @Test
    @DisplayName("웹 소켓 연결 테스트")
    public void shouldReceiveMessageSuccessTest() throws Exception {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.set("Authorization", jwtUtils.generateToken(1L));
        stompHeaders.add("heart-beat", "0,0");
        stompHeaders.add("accept-version", "1.1,1.2");
        stompHeaders.add("destination", "/sub");
        stompHeaders.add("id", "0");
        StompSession session = stompClient.connect(WEBSOCKET_URI, new WebSocketHttpHeaders(), stompHeaders, new StompSessionHandlerAdapter() {
        }, port).get(5, TimeUnit.SECONDS);
        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());
        String testMessage = "테스트";

        session.send(WEBSOCKET_TOPIC, testMessage);

        assertEquals(testMessage, blockingQueue.poll(1, TimeUnit.SECONDS));
    }

    //TODO: 실패 케이스도 만들어야 함. 또한 token header 에 넣어서 테스트 할것.

    private class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            blockingQueue.offer((String) payload);
        }
    }

}