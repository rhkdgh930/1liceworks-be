package com.elice.iliceworksbe.notification.service;

import com.elice.iliceworksbe.notification.dto.request.NotificationRequestDto;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


public interface NotificationService {
    SseEmitter createEmitter(Long userId);
    void sendNotification(NotificationRequestDto notificationRequestDto);
}
