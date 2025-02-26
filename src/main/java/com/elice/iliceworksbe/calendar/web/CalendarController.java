package com.elice.iliceworksbe.calendar.web;

import com.elice.iliceworksbe.auth.model.UserDetailsImpl;
import com.elice.iliceworksbe.calendar.dto.response.GetCalendarEventsResponseDto;
import com.elice.iliceworksbe.calendar.dto.request.PostMyEventRequestDto;
import com.elice.iliceworksbe.calendar.dto.request.PostTeamEventRequestDto;
import com.elice.iliceworksbe.calendar.dto.response.GetAccessibleCalendarsResponseDto;
import com.elice.iliceworksbe.calendar.service.EventService;
import com.elice.iliceworksbe.common.constant.CalendarType;
import com.elice.iliceworksbe.common.exception.BaseException;
import com.elice.iliceworksbe.common.exception.BaseResponse;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
@Tag(name = "Calendar", description = "캘린더 기능 관련 API 입니다.")
public class CalendarController {

    private final EventService eventService;

    @Operation(summary = "팀 캘린더 일정 생성", description = "해당하는 팀 캘린더ID에서 일정을 생성합니다.")
    @PostMapping("/team-events")
    public BaseResponse<Void> postTeamEvent(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long calendarId,
                                            @RequestBody PostTeamEventRequestDto postTeamEventRequestDto) {

        eventService.postTeamEvent(userDetails.getUserId(), calendarId, postTeamEventRequestDto);
        return new BaseResponse<>(ErrorCode.NO_CONTENT);
    }

    @Operation(summary = "내 캘린더 일정 생성", description = "해당하는 캘린더ID에서 내 일정을 생성합니다.")
    @PostMapping("/my-events")
    public BaseResponse<Void> postMyEvent(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody PostMyEventRequestDto postMyEventRequestDto) {

        eventService.postMyEvent(userDetails.getUserId(), postMyEventRequestDto);
        return new BaseResponse<>(ErrorCode.NO_CONTENT);
    }

    @Operation(summary = "캘린더 일정 조회", description = "특정 년도와 특정 월을 바탕으로 해당 월 +1개월, -1개월 범위로, 해당 캘린더 ID를 가진, 모든 일정을 조회합니다. (예: 2025년 1월인 경우, 2024년 12월부터 2025년 1월까지의 모든 일정) calendarType은 member, team, other이 있습니다.")
    @GetMapping("/events/{calendarType}")
    public BaseResponse<GetCalendarEventsResponseDto> getCalendarEvents(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                        @RequestParam Long calendarId,
                                                                        @RequestParam(required = false) Long targetUserId,
                                                                        @RequestParam int targetMonth,
                                                                        @RequestParam int targetYear,
                                                                        @PathVariable String calendarType
                                                                        ) {

        if (targetMonth > 12  || targetMonth < 1 || targetYear < 0) {
            throw new BaseException(ErrorCode.INVALID_DATE);
        }

        return new BaseResponse<>(eventService.getCalendarEvents(userDetails.getUserId(), targetUserId, calendarId, targetMonth, targetYear, CalendarType.valueOf(calendarType.toUpperCase())));
    }

    @Operation(summary = "내가 조회 가능한 캘린더들 조회", description = "내 팀 캘린더의 calendarId, 내 팀 구성원들의 calendarId와 userId, 그 밖의 조회 가능한 calendarId를 조회합니다. typeId는 type이 member이면 userId, team이면 teamId를 의미합니다.")
    @GetMapping
    public BaseResponse<List<GetAccessibleCalendarsResponseDto>> getAccessibleCalendars(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        return new BaseResponse<>(eventService.getAccessibleCalendars(userDetails.getUserId()));
    }

}
