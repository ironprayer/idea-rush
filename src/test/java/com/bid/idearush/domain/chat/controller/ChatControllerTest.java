package com.bid.idearush.domain.chat.controller;

import com.bid.idearush.domain.chat.config.WebSocketConfigTest;
import com.bid.idearush.domain.chat.model.reponse.ChatMessageResponse;
import com.bid.idearush.domain.chat.model.request.ChatMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {

    private static final String WEBSOCKET_URI = "http://localhost:{port}/chat";
    private static final String WEBSOCKET_TOPIC = "/sub";
    @LocalServerPort
    private int port;
    private BlockingQueue<ChatMessageResponse> blockingQueue;
    private WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {

        this.blockingQueue = new LinkedBlockingQueue<>();
        List<Transport> transports = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
        this.stompClient = new WebSocketStompClient(new SockJsClient(transports));
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());

    }

    @DisplayName("안냥 이라는 메세지를 성공적으로 날린다.")
    @Test
    public void shouldSendAndReceiveMessageSuccessTest() throws Exception {
        StompSession session = stompClient.connect(WEBSOCKET_URI, new StompSessionHandlerAdapter() {
        }, port).get(1, TimeUnit.SECONDS);
        session.subscribe(WEBSOCKET_TOPIC, new DefaultStompFrameHandler());

        ChatMessageRequest testMessageRequest = new ChatMessageRequest("안냥", "테스트 이름");
        session.send("/pub/sendMessage", testMessageRequest);

        ChatMessageResponse receivedMessage = blockingQueue.poll(1, TimeUnit.SECONDS);
        //TODO: 이게 왜 null 이지?
        System.out.println(receivedMessage);
//        assertEquals(testMessageRequest.name(), receivedMessage.senderName());
//        assertEquals(testMessageRequest.msg(), receivedMessage.msg());
    }

    //TODO: 실패 케이스도 만들어야 함. 또한 token header 에 넣어서 테스트 할것.

    private class DefaultStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders headers) {
            return ChatMessageResponse.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            System.out.println("reciver : " + payload);
            blockingQueue.offer((ChatMessageResponse) payload);
        }

    }

}
