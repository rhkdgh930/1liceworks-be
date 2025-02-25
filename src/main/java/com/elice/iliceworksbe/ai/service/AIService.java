package com.elice.iliceworksbe.ai.service;

import com.elice.iliceworksbe.ai.dto.GenerateScheduleRequestDto;
import com.elice.iliceworksbe.ai.dto.GenerateScheduleResponseDto;

import java.util.Map;

public interface AIService {
    GenerateScheduleResponseDto generateSchedule(GenerateScheduleRequestDto requestDto);
    String findFreeTime(Map<String, Object> calendarData);
}
