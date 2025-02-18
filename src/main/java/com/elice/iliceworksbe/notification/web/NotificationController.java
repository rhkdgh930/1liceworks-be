package com.elice.iliceworksbe.notification.web;


import com.elice.iliceworksbe.common.exception.BaseResponse;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import com.elice.iliceworksbe.notification.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationServiceImpl notificationService;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseResponse<Void>> handleResponseStatusException(ResponseStatusException ex) {
        log.error("SSE 구독 요청 실패: {}", ex.getReason());

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new BaseResponse<>(ErrorCode.INVALID_USER_JWT));
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetails userDetails) {
        // 인증되지 않은 사용자 예외 처리
        if (userDetails == null || userDetails.getUsername() == null || userDetails.getUsername().isEmpty()) {
            log.warn("SSE 구독 요청 거부: 인증되지 않은 사용자");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없는 유저의 접근입니다.");

        }
        String username = userDetails.getUsername();
        log.info("SSE 구독 요청: username={}", username);
        return notificationService.createEmitter(username); // SseEmitter를 반환
    }
}