package com.elice.iliceworksbe.ai.web;

import com.elice.iliceworksbe.ai.service.AIService;
import com.elice.iliceworksbe.common.exception.BaseResponse;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    @PostMapping("/generate_schedule")
    public BaseResponse<Map<String, Object>> generateSchedule(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String resultJson = aiService.generateSchedule(prompt);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> result = objectMapper.readValue(resultJson, Map.class);
            return new BaseResponse<>(result);
        } catch (Exception e) {
            return new BaseResponse<>(ErrorCode.FAILED_TO_JSON_PARSING);
        }
    }

    @PostMapping("/find_free_time")
    public BaseResponse<Map<String, Object>> findFreeTime(@RequestBody Map<String, Object> request) {
        String resultJson = aiService.findFreeTime(request);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> result = objectMapper.readValue(resultJson, Map.class);
            return new BaseResponse<>(result);
        } catch (Exception e) {
            return new BaseResponse<>(ErrorCode.FAILED_TO_JSON_PARSING);
        }
    }
}
