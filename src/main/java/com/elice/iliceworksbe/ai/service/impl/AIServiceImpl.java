package com.elice.iliceworksbe.ai.service.impl;

import com.elice.iliceworksbe.ai.service.AIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class AIServiceImpl implements AIService {

    private static final String FLASK_URL = "http://34.22.92.60:5000";

    @Override
    public String generateSchedule(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_URL + "/generate_schedule", request, String.class);
        log.info("prompt: " + prompt);
        return response.getBody();
    }

    @Override
    public String findFreeTime(Map<String, Object> calendarData) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(calendarData, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(FLASK_URL + "/find_free_time", request, String.class);

        log.info("Find Free Time Request: {}", calendarData);
        return response.getBody();
    }
}
