package com.elice.iliceworksbe.calendar.web;

import com.elice.iliceworksbe.auth.model.UserDetailsImpl;
import com.elice.iliceworksbe.calendar.dto.request.PatchMyEventRequestDto;
import com.elice.iliceworksbe.calendar.dto.request.PatchTeamEventRequestDto;
import com.elice.iliceworksbe.calendar.dto.request.PostMyEventRequestDto;
import com.elice.iliceworksbe.calendar.dto.request.PostTeamEventRequestDto;
import com.elice.iliceworksbe.calendar.service.EventService;
import com.elice.iliceworksbe.common.exception.BaseResponse;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


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

    @Operation(summary = "팀 캘린더 일정 삭제", description = "팀장이 해당하는 팀 캘린더ID에서 일정을 삭제합니다.")
    @PreAuthorize("hasAuthority('LEADER')")
    @DeleteMapping("/team-events")
    public BaseResponse<Void> deleteTeamEvent(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long calendarId, @RequestParam Long eventId) {

        eventService.deleteTeamEvent(userDetails.getUserId(), calendarId, eventId);
        return new BaseResponse<>(ErrorCode.NO_CONTENT);
    }

    @Operation(summary = "내 캘린더 일정 삭제", description = "내 캘린더ID에서 내 일정을 삭제합니다.")
    @DeleteMapping("/my-events")
    public BaseResponse<Void> deleteMyEvent(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long eventId) {

        eventService.deleteMyEvent(userDetails.getUserId(), eventId);
        return new BaseResponse<>(ErrorCode.NO_CONTENT);
    }

    @Operation(summary = "팀 캘린더 일정 수정", description = "해당하는 팀 캘린더ID에서 일정을 수정합니다.")
    @PreAuthorize("hasAuthority('LEADER')")
    @PatchMapping("/team-events")
    public BaseResponse<Void> patchTeamEvent(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam Long calendarId,
                                             @RequestParam Long eventId, @RequestBody PatchTeamEventRequestDto patchTeamEventRequestDto) {

        eventService.patchTeamEvent(userDetails.getUserId(), calendarId, eventId, patchTeamEventRequestDto);
        return new BaseResponse<>(ErrorCode.NO_CONTENT);
    }

    @Operation(summary = "내 캘린더 일정 수정", description = "내 캘린더ID에서 내 일정을 수정합니다.")
    @PatchMapping("/my-events")
    public BaseResponse<Void> patchMyEvent(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                             @RequestParam Long eventId, @RequestBody PatchMyEventRequestDto patchMyEventRequestDto) {

        eventService.patchMyEvent(userDetails.getUserId(), eventId, patchMyEventRequestDto);
        return new BaseResponse<>(ErrorCode.NO_CONTENT);
    }


}
