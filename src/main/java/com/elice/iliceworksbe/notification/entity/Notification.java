package com.elice.iliceworksbe.notification.entity;

import com.elice.iliceworksbe.notification.dto.request.NotificationRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "notifications") // MongoDB 컬렉션 이름 지정
public class Notification {

    @Id
    private String id; // MongoDB에서는 기본적으로 String(ObjectId) 사용

    @Field(name = "message")
    private String message;

    @Field(name = "notify_time")
    private LocalDateTime notifyTime;

    @Field(name = "is_read")
    private boolean isRead = false;

    @Field(name = "is_sent")
    private boolean isSent = false;

    @Field(name = "user_id") // @ManyToOne 대신 user의 ID만 저장
    private String userId;

    @CreatedDate  // 생성 시간 자동 기록
    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // 마지막 수정 시간 자동 기록
    @Field(name = "updated_at")
    private LocalDateTime updatedAt;

    public static Notification from(NotificationRequestDto requestDto) {
        return Notification.builder()
                .message(requestDto.message())
                .notifyTime(LocalDateTime.now())
                .isRead(false)
                .isSent(false)
                .userId(String.valueOf(requestDto.userId()))
                .build();
    }

}