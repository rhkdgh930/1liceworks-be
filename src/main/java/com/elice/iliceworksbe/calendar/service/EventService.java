package com.elice.iliceworksbe.calendar.service;

import com.elice.iliceworksbe.calendar.dto.response.GetAccessibleCalendarsResponseDto;
import com.elice.iliceworksbe.calendar.dto.response.GetCalendarEventsResponseDto;
import com.elice.iliceworksbe.calendar.dto.request.PostMyEventRequestDto;
import com.elice.iliceworksbe.calendar.dto.request.PostTeamEventRequestDto;
import com.elice.iliceworksbe.common.constant.CalendarType;

import java.util.List;

public interface EventService {
    void postTeamEvent(Long userId, Long calendarId, PostTeamEventRequestDto postTeamEventRequestDto);
    void postMyEvent(Long userId, PostMyEventRequestDto postMyEventRequestDto);

    GetCalendarEventsResponseDto getCalendarEvents(Long requestingUserId, Long targetUserId, Long calendarId, int targetMonth, int targetYear, CalendarType calendarType);

    List<GetAccessibleCalendarsResponseDto> getAccessibleCalendars(Long userId);
}
