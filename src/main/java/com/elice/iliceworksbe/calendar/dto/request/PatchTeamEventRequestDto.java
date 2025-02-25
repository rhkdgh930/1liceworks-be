package com.elice.iliceworksbe.calendar.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

public record PatchTeamEventRequestDto(
        @NotBlank(message = "일정 제목은 필수입니다.")
        String title,
        String description,
        LocalDateTime dtStartTime,
        LocalDateTime dtEndTime,
        @NotBlank(message = "종일 설정을 해주세요.")
        Boolean isAllDay,
        String location,
        List<EventReminderDto> eventReminders,
        List<EventParticipantDto> eventParticipants
) {

    public record EventReminderDto(
        LocalDateTime notifyTime
    ) {}

    public record EventParticipantDto(
       Long userId
    ) {}
}
