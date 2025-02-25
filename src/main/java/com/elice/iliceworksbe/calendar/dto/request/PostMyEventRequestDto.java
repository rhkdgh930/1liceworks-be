package com.elice.iliceworksbe.calendar.dto.request;

import com.elice.iliceworksbe.ai.dto.GenerateScheduleResponseDto;
import com.elice.iliceworksbe.common.constant.Availability;
import com.elice.iliceworksbe.common.constant.PrivacyType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record PostMyEventRequestDto(
        String title,
        String description,
        LocalDateTime dtStartTime,
        LocalDateTime dtEndTime,
        Boolean isAllDay,
        PrivacyType privacyType,
        Availability availability,
        String location,
        List<EventReminderDto> eventReminders
) {

    public record EventReminderDto(
        LocalDateTime notifyTime
    ) {}

    public static PostMyEventRequestDto from(GenerateScheduleResponseDto responseDto) {
        return PostMyEventRequestDto.builder()
                .title(responseDto.getTitle())
                .description(responseDto.getDescription())
                .dtStartTime(responseDto.getDtStartTime())
                .dtEndTime(responseDto.getDtEndTime())
                .isAllDay(responseDto.getIsAllDay())
                .privacyType(responseDto.getPrivacyType())
                .availability(responseDto.getAvailability())
                .location(responseDto.getLocation())
                .eventReminders(responseDto.getEventReminders() != null ?
                        responseDto.getEventReminders().stream()
                                .map(reminder -> new EventReminderDto(reminder.getNotifyTime()))
                                .collect(Collectors.toList())
                        : null)
                .build();
    }
}
