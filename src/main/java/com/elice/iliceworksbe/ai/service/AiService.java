package com.elice.iliceworksbe.ai.service;

import java.util.Map;

public interface AiService {
    String generateSchedule(String prompt);
    String findFreeTime(Map<String, Object> calendarData);
}
