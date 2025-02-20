package com.elice.iliceworksbe.common.utils;

import com.elice.iliceworksbe.auth.entity.User;
import com.elice.iliceworksbe.auth.repository.UserRepository;
import com.elice.iliceworksbe.common.constant.Role;
import com.elice.iliceworksbe.common.constant.Status;
import com.elice.iliceworksbe.team.constant.Industry;
import com.elice.iliceworksbe.team.constant.Scale;
import com.elice.iliceworksbe.team.entity.*;
import com.elice.iliceworksbe.team.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/*
서버 데이터 초기화용
 */

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final PositionRepository positionRepository;
    private final JobTitleRepository jobTitleRepository;
    private final UserTypeRepository userTypeRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init(){

        Team iliceTeam = Team.builder()
                .companyName("일리스웍스 컴퍼니")
                .teamName("일리스")
                .domainName("threadly.ilice-works.com")
                .hasPrivateDomain(true)
                .industry(Industry.CONSTRUCTION)
                .scale(Scale.TEN_TO_NINETEEN)
                .build();

        teamRepository.save(iliceTeam);

        Position nonePosition = Position.builder()
                .name("없음")
                .build();
        JobTitle noneJobTitle = JobTitle.builder()
                .name("없음")
                .build();
        UserType noneUserType = UserType.builder()
                .name("없음")
                .build();

        positionRepository.save(nonePosition);
        jobTitleRepository.save(noneJobTitle);
        userTypeRepository.save(noneUserType);

        User taeseungUser = User.builder()
                .accountId("hi563@threadly.ilice-works.com")
                .username("정태승")
                .password(passwordEncoder.encode("!a12345678"))
                .privateEmail("hi563@naver.com")
                .role(Role.LEADER)
                .status(Status.ACTIVE)
                .team(iliceTeam)
                .isTeamCreated(true)
                .build();

        User hyerimUser = User.builder()
                .accountId("hyerim@threadly.ilice-works.com")
                .username("양혜림")
                .password(passwordEncoder.encode("!a12345678"))
                .privateEmail("hyerim@naver.com")
                .role(Role.MEMBER)
                .status(Status.ACTIVE)
                .team(iliceTeam)
                .isTeamCreated(true)
                .build();

        Employee employee = Employee.builder()
                .employeeNumber("24-12345")
                .hireDate(LocalDateTime.now())
                .position(nonePosition)
                .jobTitle(noneJobTitle)
                .userType(noneUserType)
                .responsibility("없음")
                .user(taeseungUser)
                .build();

        Employee hyerimEmployee = Employee.builder()
                .employeeNumber("24-12346")
                .hireDate(LocalDateTime.now())
                .position(nonePosition)
                .jobTitle(noneJobTitle)
                .userType(noneUserType)
                .responsibility("없음")
                .user(hyerimUser)
                .build();

        userRepository.save(taeseungUser);
        userRepository.save(hyerimUser);
        employeeRepository.save(employee);
        employeeRepository.save(hyerimEmployee);


    }
}
