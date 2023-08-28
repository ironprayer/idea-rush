package com.bid.idearush.domain.sse.service;

import com.bid.idearush.domain.sse.type.CustomSseEmitter;
import com.bid.idearush.domain.sse.type.SseConnect;
import com.bid.idearush.domain.sse.type.SseEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;


@Service
@Slf4j
public class SseService {
    private static final Long DEFAULT_TIMEOUT = 1000 * 60 * 30L;
    private static final ConcurrentLinkedDeque<CustomSseEmitter> noticeEmitters = new ConcurrentLinkedDeque<>();
    private static final ConcurrentLinkedDeque<CustomSseEmitter> bidEmitters = new ConcurrentLinkedDeque<>();
    private static int count = 0;

    public SseEmitter connect(SseConnect type, Long id, String lastEventId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        ConcurrentLinkedDeque<CustomSseEmitter> currentEmitters = type == SseConnect.NOTIFICATION ? noticeEmitters : bidEmitters;
        String initMessage = type == SseConnect.NOTIFICATION ? "connect user : " + id : "connect idea : " + id;

        CustomSseEmitter customSseEmitter = new CustomSseEmitter(id, emitter);
        currentEmitters.add(customSseEmitter);

        emitter.onCompletion(() -> currentEmitters.remove(customSseEmitter));
        emitter.onTimeout(() -> {
            customSseEmitter.sseEmitter().complete();
            currentEmitters.remove(customSseEmitter);
        });
        emitter.onError(e -> {
            emitter.completeWithError(e);
            currentEmitters.remove(customSseEmitter);
        });

        //TODO 503 에러 문제 해결하기 위해서 첫 연길시 메시지 보냄. 정확한 원인에 대해 나중에 이야기가 되어야 할듯.
        try {
//            System.out.println("connect" + lastEventId);
            emitter.send(initMessage);
        } catch (IOException e) {
            customSseEmitter.sseEmitter().completeWithError(e);
            currentEmitters.remove(customSseEmitter);
        }

        return emitter;
    }

    public void send(SseConnect type, SseEvent event, Long id, Object data) {
        ConcurrentLinkedDeque<CustomSseEmitter> currentEmitters = type == SseConnect.NOTIFICATION ? noticeEmitters : bidEmitters;
        List<CustomSseEmitter> sendEmitters = currentEmitters.stream()
                .filter((customSseEmitter) -> id.equals(customSseEmitter.id())).toList();

//        for(CustomSseEmitter sendEmitter : sendEmitters) {
//            try {
//                sendEmitter.sseEmitter()
//                        .send(SseEmitter.event()
//                                .name(event.toString())
//                                .data(data));
//            } catch (IOException | IllegalStateException e) {
//                sendEmitter.sseEmitter().completeWithError(e);
//                currentEmitters.remove(sendEmitter);
//            }
//        }
    }

}
