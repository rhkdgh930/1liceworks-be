package com.elice.iliceworksbe.notification.service;

import com.elice.iliceworksbe.auth.entity.User;
import com.elice.iliceworksbe.auth.repository.UserRepository;
import com.elice.iliceworksbe.calendar.entity.Calendar;
import com.elice.iliceworksbe.calendar.repository.CalendarRepository;
import com.elice.iliceworksbe.common.constant.CalendarType;
import com.elice.iliceworksbe.common.constant.ContentType;
import com.elice.iliceworksbe.common.constant.Role;
import com.elice.iliceworksbe.common.constant.Status;
import com.elice.iliceworksbe.common.exception.BaseException;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import com.elice.iliceworksbe.notification.dto.request.WebhookRequestDto;
import com.elice.iliceworksbe.notification.dto.response.WebhookResponseDto;
import com.elice.iliceworksbe.notification.entity.Webhook;
import com.elice.iliceworksbe.notification.repository.WebhookRepository;
import com.elice.iliceworksbe.notification.service.impl.WebhookServiceImpl;
import com.elice.iliceworksbe.team.constant.Industry;
import com.elice.iliceworksbe.team.constant.Scale;
import com.elice.iliceworksbe.team.entity.Team;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@SpringBootTest
@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
class WebhookServiceTest {

    @InjectMocks
    private WebhookServiceImpl webhookService;

    @Mock
    private CalendarRepository calendarRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebhookRepository webhookRepository;

    private Long teamId = 1L;
    private Long calendarId = 1L;
    private Long userId = 1L;

    Team iliceTeamBE = Team.builder()
            .id(teamId)
            .companyName("일리스웍스 컴퍼니")
            .teamName("일리스 BE팀")
            .domainName("threadly.ilice-works.com")
            .hasPrivateDomain(true)
            .industry(Industry.CONSTRUCTION)
            .scale(Scale.TEN_TO_NINETEEN)
            .build();

    Calendar calendarBE = Calendar.builder()
            .id(calendarId)
            .name("일리스 BE팀 캘린더")
            .type(CalendarType.TEAM)
            .typeId(iliceTeamBE.getId())
            .team(iliceTeamBE)
            .build();

    User mockUser = User.builder()
            .id(userId)
            .accountId("hi563@threadly.ilice-works.com")
            .username("Test User")
            .privateEmail("hi563@naver.com")
            .role(Role.LEADER)
            .status(Status.ACTIVE)
            .team(iliceTeamBE)
            .isTeamCreated(true)
            .build();

    Webhook iliceTeamBE_webhook = Webhook.builder()
            .payloadUrl("https://discord.com/api/webhooks/1342348107215798306/bYtBuXjzlkFdsj2saJ03Mo5GvZUCPaGRFB7EsK7no01urQ61OnPMqvZnY2XJkewuRTcn")
            .contentType(ContentType.APPLICATION_JSON)
            .calendar(calendarBE)
            .build();

    WebhookRequestDto requestDto = new WebhookRequestDto(calendarId, "https://discord.com/api/webhooks/1342348107215798306/bYtBuXjzlkFdsj2saJ03Mo5GvZUCPaGRFB7EsK7no01urQ61OnPMqvZnY2XJkewuRTcn", ContentType.APPLICATION_JSON);


    @Test
    @DisplayName("웹훅 저장 성공")
    void testPostWebhook_Success() {

        // given
        given(calendarRepository.findById(calendarId)).willReturn(Optional.of(calendarBE));
        given(userRepository.findTeamIdByUserId(userId)).willReturn(Optional.of(iliceTeamBE.getId()));
        given(webhookRepository.findByCalendarId(calendarId)).willReturn(Optional.empty());
        given(webhookRepository.save(any(Webhook.class))).willReturn(iliceTeamBE_webhook);
        log.info("테스트 코드에서 calendarId: {}", calendarId);
        log.info("서비스 코드에서 requestDto.calendarId(): {}", requestDto.calendarId());

        // when
        WebhookResponseDto response = webhookService.postWebhook(userId, requestDto);

        // then
        assertNotNull(response);
        assertEquals(requestDto.payloadUrl(), response.payloadUrl());
        verify(webhookRepository, times(1)).save(any(Webhook.class));
    }

    @Test
    @DisplayName("웹훅 저장 실패 - 1. 존재하지 않는 캘린더")
    void testPostWebhook_CalendarNotFound() {

        // given
        given(calendarRepository.findById(calendarId)).willReturn(Optional.empty());

        // when & then
        BaseException thrown = assertThrows(BaseException.class,
                () -> webhookService.postWebhook(userId, requestDto)
        );

        assertEquals(ErrorCode.NOT_FOUND_CALENDAR, thrown.getStatus());
    }

    @Test
    @DisplayName("웹훅 저장 실패 - 2. 사용자의 팀이 존재하지 않을 때")
    void testPostWebhook_TeamNotFound() {
        // given
        given(calendarRepository.findById(calendarId)).willReturn(Optional.of(calendarBE));
        given(userRepository.findTeamIdByUserId(userId)).willReturn(Optional.empty());

        // when & then
        BaseException thrown = assertThrows(BaseException.class,
                () -> webhookService.postWebhook(userId, requestDto)
        );

        assertEquals(ErrorCode.NOT_FOUND_TEAM, thrown.getStatus());
    }

    @Test
    @DisplayName("웹훅 저장 실패 - 3. member가 웹훅을 등록하려 할 때")
    void testPostWebhook_InvalidAuthorization() {
        // given
        User memberUser = User.builder()
                .id(2L)
                .accountId("member@threadly.ilice-works.com")
                .username("member User")
                .privateEmail("member@naver.com")
                .role(Role.MEMBER)
                .status(Status.ACTIVE)
                .team(iliceTeamBE)
                .isTeamCreated(true)
                .build();

        Calendar memberCalendar = Calendar.builder()
                .id(2L)
                .name("member1 / 직급1")
                .type(CalendarType.MEMBER)
                .typeId(memberUser.getId())
                .team(iliceTeamBE)
                .build();
        WebhookRequestDto requestDto = new WebhookRequestDto(memberCalendar.getId(), "https://discord.com/api/webhooks/1342348107215798306/bYtBuXjzlkFdsj2saJ03Mo5GvZUCPaGRFB7EsK7no01urQ61OnPMqvZnY2XJkewuRTcn", ContentType.APPLICATION_JSON);

        given(calendarRepository.findById(memberCalendar.getId())).willReturn(Optional.of(memberCalendar));
        given(userRepository.findTeamIdByUserId(memberUser.getId())).willReturn(Optional.of(teamId));

        // when & then
        BaseException thrown = assertThrows(BaseException.class,
                () -> webhookService.postWebhook(memberUser.getId(), requestDto)
        );

        assertEquals(ErrorCode.INVALID_AUTHORIZATION, thrown.getStatus());
    }

    @Test
    @DisplayName("웹훅 저장 실패 - 이미 존재하는 웹훅")
    void testPostWebhook_DuplicatedWebhook() {
        // Given
        given(calendarRepository.findById(calendarId)).willReturn(Optional.of(calendarBE));
        given(userRepository.findTeamIdByUserId(userId)).willReturn(Optional.of(teamId));
        given(webhookRepository.findByCalendarId(calendarId)).willReturn(Optional.of(iliceTeamBE_webhook));
        WebhookRequestDto requestDto = new WebhookRequestDto(calendarId, "https://discord.com/api/webhooks/1342348107215798306/bYtBuXjzlkFdsj2saJ03Mo5GvZUCPaGRFB7EsK7no01urQ61OnPMqvZnY2XJkewuRTcn", ContentType.APPLICATION_JSON);

        // When & Then
        BaseException thrown = assertThrows(BaseException.class,
                () -> webhookService.postWebhook(userId, requestDto)
        );
        log.info("thrown={}",thrown.getStatus());

        assertEquals(ErrorCode.DUPLICATED_WEBHOOK, thrown.getStatus());
    }


    @Test
    void postWebhook() {
    }

    @Test
    void sendWebhookMessage() {
    }

    @Test
    void getWebhook() {
    }

    @Test
    void patchWebhook() {
    }

    @Test
    void deleteWebhook() {
    }
}