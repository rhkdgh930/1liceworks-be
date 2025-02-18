package com.elice.iliceworksbe.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {
    SseEmitter createEmitter(String username);
}