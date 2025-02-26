package com.elice.iliceworksbe.calendar.service.impl;

import com.elice.iliceworksbe.auth.entity.User;
import com.elice.iliceworksbe.auth.repository.UserRepository;
import com.elice.iliceworksbe.calendar.dto.response.GetAccessibleCalendarsResponseDto;
import com.elice.iliceworksbe.calendar.dto.response.GetCalendarEventsResponseDto;
import com.elice.iliceworksbe.calendar.dto.request.PostMyEventRequestDto;
import com.elice.iliceworksbe.calendar.dto.request.PostTeamEventRequestDto;
import com.elice.iliceworksbe.calendar.entity.Calendar;
import com.elice.iliceworksbe.calendar.entity.Event;
import com.elice.iliceworksbe.calendar.entity.EventParticipant;
import com.elice.iliceworksbe.calendar.repository.CalendarRepository;
import com.elice.iliceworksbe.calendar.repository.EventParticipantRepository;
import com.elice.iliceworksbe.calendar.repository.EventRepository;
import com.elice.iliceworksbe.calendar.service.EventService;
import com.elice.iliceworksbe.common.constant.CalendarType;
import com.elice.iliceworksbe.common.exception.BaseException;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import com.elice.iliceworksbe.notification.dto.request.NotificationRequestDto;
import com.elice.iliceworksbe.notification.entity.EventReminder;
import com.elice.iliceworksbe.notification.repository.EventReminderRepository;
import com.elice.iliceworksbe.notification.service.NotificationService;
import com.elice.iliceworksbe.notification.utils.NotificationMessages;
import com.elice.iliceworksbe.team.entity.Team;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CalendarRepository calendarRepository;
    private final UserRepository userRepository;
    private final EventReminderRepository eventReminderRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void postTeamEvent(Long userId, Long calendarId, PostTeamEventRequestDto postTeamEventRequestDto) {

        // 1. 현재 유저의 팀 조회
        Team team = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER))
                .getTeam();

        // 2. CalendarId가 해당 유저의 팀 캘린더가 맞는지 조회
        Calendar teamCalendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_CALENDAR));

        if (!(teamCalendar.getId().equals(team.getId()) && teamCalendar.getType().equals(CalendarType.TEAM))) {
            throw new BaseException(ErrorCode.NOT_FOUND_CALENDAR);
        }

        // 3. 해당 팀 캘린더에 대해 일정 생성 및 추가
        Event teamEvent = Event.of(postTeamEventRequestDto, teamCalendar);
        eventRepository.save(teamEvent);

        // 4. 생성된 EventReminder 추가
        List<EventReminder> eventReminders = postTeamEventRequestDto.eventReminders()
                .stream()
                .map(eR -> EventReminder.of(eR, teamEvent))
                .toList();
        eventReminderRepository.saveAll(eventReminders);

        // 5. 일정 참석자 추가
        // 5-1. 일정 인원 조회
        List<Long> participantIds = postTeamEventRequestDto.eventParticipants().stream()
                .map(PostTeamEventRequestDto.EventParticipantDto::userId)
                .toList();

        List<User> users = userRepository.findAllById(participantIds);

        // 5-2. 현재 사용자가 일정 참석자 리스트에 포함되어 있지 않다면 users 리스트에 추가
        if (!participantIds.contains(userId)) {
            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
            users.add(currentUser);
        }

        // 5-2. 일정 인원 토대로 일정 참석자 추가 (일정 참석자가 해당 팀원인지 확인하는 로직추가)
        List<EventParticipant> eventParticipants = users.stream()
                .filter(participant -> participant.checkMyTeam(team))
                .map(participant -> EventParticipant.of(teamEvent, participant)).toList();
        eventParticipantRepository.saveAll(eventParticipants);


        // 6. 해당 팀의 모든 구성원에게 일정이 생성됨을 알림
        // 6-1. 해당 팀의 모든 구성원 조회
        List<User> teamUsers = userRepository.findByTeam(team);

        // 6-2. 해당 팀의 모든 구성원들에게 알림 보냄
        teamUsers.forEach(
                m -> notificationService.sendNotification(
                                NotificationRequestDto.builder()
                                        .userId(m.getId())
                                        .message(NotificationMessages.CREATE_TEAM.getMessage())
                                        .build()
                        )
        );
    }

    @Override
    public void postMyEvent(Long userId, PostMyEventRequestDto postMyEventRequestDto) {

        // 1. 현재 유저의 캘린더 조회
        User currentUser = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER));
        Calendar myCalendar = calendarRepository.findByTypeAndTypeId(CalendarType.MEMBER, userId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_CALENDAR));

        // 2. 해당 유저의 일정 추가
        Event myEvent = Event.of(postMyEventRequestDto, myCalendar);
        eventRepository.save(myEvent);

        // 3. 생성된 EventReminder 추가
        List<EventReminder> eventReminders = postMyEventRequestDto.eventReminders()
                .stream()
                .map(eR -> EventReminder.of(eR, myEvent))
                .toList();
        eventReminderRepository.saveAll(eventReminders);

        // 4. 일정 참석자 추가(자신)
        EventParticipant eventParticipant = EventParticipant.builder()
                .user(currentUser)
                .event(myEvent)
                .build();
        eventParticipantRepository.save(eventParticipant);
    }

    @Override
    public GetCalendarEventsResponseDto getCalendarEvents(Long requestingUserId, Long targetUserId, Long calendarId, int targetMonth, int targetYear, CalendarType calendarType) {

        log.info("CalendarType {}", calendarType);

        // 0. 현재 조회 시도하는 유저가 어느 팀에 존재하는지 확인
        Team queryingTeam = userRepository.findById(requestingUserId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).getTeam();

        // 1. 선택한 캘린더에 대해 조회할 수 있는 권한이 있는지 확인. TypeId=-1은 법정공휴일 캘린더를 의미한다. (권한 상관없이 조회 가능)
        Calendar targetCalendar = calendarRepository.findById(calendarId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_CALENDAR));

        if (!(targetCalendar.getTypeId() != -1 && queryingTeam.equals(targetCalendar.getTeam()))) {
            throw new BaseException(ErrorCode.NOT_FOUND_CALENDAR);
        }

        // 2. 조회할 일정 날짜 범위 구하기 (기준연월 기준 +- 1개월)
        EventDateRange eventDateRange = getEventDateRange(targetMonth, targetYear);

        switch (calendarType) {

            case MEMBER:
                log.info("Get Member Calendar {}", targetUserId);

                // 2. 해당 캘린더가 해당 유저의 캘린더인지 확인
                if (!(targetCalendar.getType().equals(calendarType) && targetCalendar.getTypeId().equals(targetUserId))) {
                    throw new BaseException(ErrorCode.NOT_FOUND_CALENDAR);
                }

                // 3. targetYear와 targetMonth를 기준으로 앞뒤로 1개월씩에 해당하는 모든 Event 조회
                List<Event> memberEvents = eventRepository.findEventsWithinThreeMonthsByCalendar(eventDateRange.startDateTime, eventDateRange.endDateTime, targetCalendar);

                // 4. 변환 후 전달

                // 4-1. 자신인 경우, 그대로 리턴
                if (targetUserId.equals(requestingUserId)) {
                    return new GetCalendarEventsResponseDto(calendarId, memberEvents.stream().map(GetCalendarEventsResponseDto.EventDto::from).toList());
                }

                // 4-2. 타인의 경우, 필터링하여 리턴
                return new GetCalendarEventsResponseDto(calendarId, memberEvents.stream().map(GetCalendarEventsResponseDto.EventDto::fromForMember).toList());

            case TEAM:
                log.info("Get Team Calendar {}", queryingTeam.getId());

                // 2. 해당 캘린더가 해당 팀의 캘린더인지 확인
                if (!(targetCalendar.getType().equals(calendarType) && targetCalendar.getTypeId().equals(queryingTeam.getId()))) {
                    throw new BaseException(ErrorCode.NOT_FOUND_CALENDAR);
                }

                // 3. targetYear와 targetMonth를 기준으로 앞뒤로 1개월씩에 해당하는 모든 Event 조회
                List<Event> teamEvents = eventRepository.findEventsWithinThreeMonthsByCalendar(eventDateRange.startDateTime, eventDateRange.endDateTime, targetCalendar);

                // 4. 변환 후 전달
                return new GetCalendarEventsResponseDto(calendarId, teamEvents.stream().map(GetCalendarEventsResponseDto.EventDto::from).toList());

            case OTHER:
                log.info("Get Other Calendar");

                // 2. 해당 캘린더가 법정 공휴일 캘린더인지 확인
                if (!(targetCalendar.getType().equals(calendarType) && targetCalendar.getTypeId().equals(-1L))) {
                    throw new BaseException(ErrorCode.NOT_FOUND_CALENDAR);
                }

                // 3. targetYear와 targetMonth를 기준으로 앞뒤로 1개월씩에 해당하는 모든 Event 조회
                List<Event> otherEvents = eventRepository.findEventsWithinThreeMonthsByCalendar(eventDateRange.startDateTime, eventDateRange.endDateTime, targetCalendar);

                // 4. 변환 후 전달
                return new GetCalendarEventsResponseDto(calendarId, otherEvents.stream().map(GetCalendarEventsResponseDto.EventDto::from).toList());

            default:
                throw new BaseException(ErrorCode.NOT_FOUND_CALENDAR);
        }

    }

    @Override
    public List<GetAccessibleCalendarsResponseDto> getAccessibleCalendars(Long userId) {

        // 0. 현재 조회 시도하는 유저가 어느 팀에 존재하는지 확인
        Team queryingTeam = userRepository.findById(userId).orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND_USER)).getTeam();

        // 1. 해당 팀의 캘린더들 조회
        List<Calendar> teamAccessibleCalendars = calendarRepository.findByTeam(queryingTeam);

        // 2. 그 밖의 캘린더들 조회
        List<Calendar> otherCalendars = calendarRepository.findByTypeId(-1L);

        List<Calendar> allCalendars = new ArrayList<>();
        allCalendars.addAll(teamAccessibleCalendars);
        allCalendars.addAll(otherCalendars);

        return allCalendars.stream().map(GetAccessibleCalendarsResponseDto::from).toList();
    }

    private EventDateRange getEventDateRange(int targetMonth, int targetYear) {
        // 기준 월의 첫날과 마지막 날
        YearMonth targetYM = YearMonth.of(targetYear, targetMonth);

        // 이전 월의 첫날
        YearMonth prevYM = targetYM.minusMonths(1);
        LocalDate startOfPrevMonth = prevYM.atDay(1);

        // 다음 월의 마지막 날
        YearMonth nextYM = targetYM.plusMonths(1);
        LocalDate endOfNextMonth = nextYM.atEndOfMonth();

        // 최종 검색 범위
        LocalDateTime startDate = startOfPrevMonth.atStartOfDay();  // 2024-12-01 00:00:00
        LocalDateTime endDate = endOfNextMonth.atTime(23, 59, 59);  // 2025-02-28 23:59:59

        return new EventDateRange(startDate, endDate);
    }

    private record EventDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime){}
}
