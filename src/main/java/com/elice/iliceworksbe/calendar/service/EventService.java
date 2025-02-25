package com.elice.iliceworksbe.calendar.service;

import com.elice.iliceworksbe.calendar.dto.request.PostMyEventRequestDto;
import com.elice.iliceworksbe.calendar.dto.request.PostTeamEventRequestDto;

public interface EventService {
    void postTeamEvent(Long userId, Long calendarId, PostTeamEventRequestDto postTeamEventRequestDto);
    void postMyEvent(Long userId, PostMyEventRequestDto postMyEventRequestDto);

    void deleteTeamEvent(Long userId, Long calendarId, Long eventId);
    void deleteMyEvent(Long userId, Long eventId);
}
