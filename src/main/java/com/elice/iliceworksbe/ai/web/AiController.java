package com.elice.iliceworksbe.ai.web;

import com.elice.iliceworksbe.ai.service.AiService;
import com.elice.iliceworksbe.common.exception.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    /**
     * TODO
     * 리턴타입 JSON 타입으로 반환시켜야 함!
     */

    @PostMapping("/generate_schedule")
    public BaseResponse<String> generateSchedule(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        String result = aiService.generateSchedule(prompt);
        return new BaseResponse<>(result);
    }

    @PostMapping("find_free_time")
    public BaseResponse<String> findFreeTime(@RequestBody Map<String, Object> request) {
        String result = aiService.findFreeTime(request);
        return new BaseResponse<>(result);
    }
}
