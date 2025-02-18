package com.elice.iliceworksbe.notification.service.impl;


import com.elice.iliceworksbe.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * SSE 연결
     *
     * @param username
     * @return
     */
    @Override
    public SseEmitter createEmitter(String username) {
        SseEmitter emitter = new SseEmitter(60 * 1000L); // 1분 유지
        configureEmitter(username, emitter);
        emitters.put(username, emitter);

        return emitter;
    }

    private void configureEmitter(String username, SseEmitter emitter) {
        emitter.onCompletion(() -> {
            log.info("SSE 연결 종료: {}", username);
            emitters.remove(username);
        });

        emitter.onTimeout(() -> {
            log.info("SSE 연결 타임아웃: {}", username);
            emitters.remove(username);
        });

        emitter.onError(e -> {
            log.error("SSE 에러 발생 ({}): {}", username, e.getMessage());
            emitters.remove(username);
            try {
                //클라이언트에게 error 이벤트 전송
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data("서버 오류가 발생했습니다."));
            } catch (IOException ioException) {
                log.error("클라이언트로 에러 메시지 전송 실패: {}", ioException.getMessage());
            }
            emitter.complete();

        });
    }
}
