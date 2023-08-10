package com.bid.idearush.domain.sse.service;

import com.bid.idearush.domain.sse.type.CustomSseEmitter;
import com.bid.idearush.domain.sse.type.SseConnect;
import com.bid.idearush.domain.sse.type.SseEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SseService {
    private static final Long DEFAULT_TIMEOUT = 1000 * 60 * 30L;
    private static final List<CustomSseEmitter> noticeEmitters = new ArrayList<>();
    private static final List<CustomSseEmitter> bidEmitters = new ArrayList<>();

    public SseEmitter connect(SseConnect type, Long id, String lastEventId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        List<CustomSseEmitter> currentEmitters = type == SseConnect.NOTIFICATION ? noticeEmitters : bidEmitters;
        String initMessage = type == SseConnect.NOTIFICATION ? "connect user : " + id : "connect idea : " + id;

        CustomSseEmitter customSseEmitter = new CustomSseEmitter(id, emitter);
        currentEmitters.add(customSseEmitter);

        emitter.onCompletion(() -> currentEmitters.remove(customSseEmitter));
        emitter.onTimeout(() -> currentEmitters.remove(customSseEmitter));

        //TODO 503 에러 문제 해결하기 위해서 첫 연길시 메시지 보냄. 정확한 원인에 대해 나중에 이야기가 되어야 할듯.
        try {
            emitter.send(initMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return emitter;
    }

    public void send(SseConnect type, SseEvent event, Long id, Object data) {
        List<CustomSseEmitter> currentEmitters = type == SseConnect.NOTIFICATION ? noticeEmitters : bidEmitters;
        List<CustomSseEmitter> sendEmitters = currentEmitters.stream()
                .filter((customSseEmitter) -> customSseEmitter.id() == id).toList();

        for(CustomSseEmitter sendEmitter : sendEmitters) {
            try {
                sendEmitter.sseEmitter()
                        .send(SseEmitter.event()
                                .name(event.toString())
                                .data(data));
            } catch (IOException exception) {
                currentEmitters.remove(sendEmitter);
                throw new RuntimeException("연결 오류!");
            }
        }
    }
}
