package com.elice.iliceworksbe.ai.web;

import com.elice.iliceworksbe.ai.dto.GenerateScheduleRequestDto;
import com.elice.iliceworksbe.ai.dto.GenerateScheduleResponseDto;
import com.elice.iliceworksbe.ai.service.AIService;
import com.elice.iliceworksbe.auth.model.UserDetailsImpl;
import com.elice.iliceworksbe.calendar.dto.request.PostMyEventRequestDto;
import com.elice.iliceworksbe.calendar.service.EventService;
import com.elice.iliceworksbe.common.exception.BaseResponse;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
@Tag(name = "AI", description = "AI 기능 관련 API 입니다.")
public class AIController {

    private final AIService aiService;
    private final EventService eventService;

    /**
     * TODO
     * 검증 로직 보완
     */
    @Operation(summary = "AI로 생성된 캘린더 일정 조회", description = "AI가 생성한 내 일정을 조회합니다.")
    @PostMapping("/generate_schedule")
    public BaseResponse<GenerateScheduleResponseDto> generateSchedule(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody GenerateScheduleRequestDto requestDto) {
        GenerateScheduleResponseDto result = aiService.generateSchedule(requestDto);
        return new BaseResponse<>(result);
    }

    @Operation(summary = "AI로 캘린더 일정 생성", description = "AI가 해당하는 캘린더ID에서 내 일정을 생성합니다.")
    @PostMapping("/generate_schedule/ok")
    public BaseResponse<GenerateScheduleResponseDto> generateScheduleOk(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody GenerateScheduleRequestDto requestDto) {
        GenerateScheduleResponseDto result = aiService.generateSchedule(requestDto);
        eventService.postMyEvent(userDetails.getUserId(), PostMyEventRequestDto.from(result));
        return new BaseResponse<>(result);
    }

    /**
     * TODO
     * 아직 개발 완료 안했습니다!
     */
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
