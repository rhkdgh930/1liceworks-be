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

        Team iliceTeamBE = Team.builder()
                .companyName("일리스웍스 컴퍼니")
                .teamName("일리스 BE팀")
                .domainName("threadly.ilice-works.com")
                .hasPrivateDomain(true)
                .industry(Industry.CONSTRUCTION)
                .scale(Scale.TEN_TO_NINETEEN)
                .build();

        Team iliceTeamFE = Team.builder()
                .companyName("일리스웍스 컴퍼니")
                .teamName("일리스 FE팀")
                .domainName("threadly.ilice-works.com")
                .hasPrivateDomain(true)
                .industry(Industry.CONSTRUCTION)
                .scale(Scale.TEN_TO_NINETEEN)
                .build();

        teamRepository.save(iliceTeamBE);
        teamRepository.save(iliceTeamFE);

        Position staffPosition = Position.builder()
                .name("사원")
                .build();
        JobTitle generalJobTitle = JobTitle.builder()
                .name("일반직")
                .build();
        UserType regularUserType = UserType.builder()
                .name("정규직")
                .build();

        positionRepository.save(staffPosition);
        jobTitleRepository.save(generalJobTitle);
        userTypeRepository.save(regularUserType);

        User taeseungUser = User.builder()
                .accountId("hi563@threadly.ilice-works.com")
                .username("정태승")
                .password(passwordEncoder.encode("!a12345678"))
                .privateEmail("hi563@naver.com")
                .role(Role.LEADER)
                .status(Status.ACTIVE)
                .team(iliceTeamBE)
                .isTeamCreated(true)
                .build();

        User kwanghoUser = User.builder()
                .accountId("kwangho@threadly.ilice-works.com")
                .username("명광호")
                .password(passwordEncoder.encode("!a12345678"))
                .privateEmail("kwangho@naver.com")
                .role(Role.MEMBER)
                .status(Status.ACTIVE)
                .team(iliceTeamBE)
                .isTeamCreated(true)
                .build();

        User soogyeongUser = User.builder()
                .accountId("soogyeong@threadly.ilice-works.com")
                .username("엄수경")
                .password(passwordEncoder.encode("!a12345678"))
                .privateEmail("soogyeong@naver.com")
                .role(Role.MEMBER)
                .status(Status.ACTIVE)
                .team(iliceTeamBE)
                .isTeamCreated(true)
                .build();

        User hyerimUser = User.builder()
                .accountId("hyerim@threadly.ilice-works.com")
                .username("양혜림")
                .password(passwordEncoder.encode("!a12345678"))
                .privateEmail("hyerim@naver.com")
                .role(Role.LEADER)
                .status(Status.ACTIVE)
                .team(iliceTeamFE)
                .isTeamCreated(true)
                .build();

        User kyungjunUser = User.builder()
                .accountId("kyungjun@threadly.ilice-works.com")
                .username("정경준")
                .password(passwordEncoder.encode("!a12345678"))
                .privateEmail("kyungjun@naver.com")
                .role(Role.MEMBER)
                .status(Status.ACTIVE)
                .team(iliceTeamFE)
                .isTeamCreated(true)
                .build();

        Employee taeseungEmployee = Employee.builder()
                .employeeNumber("24-12345")
                .hireDate(LocalDateTime.now())
                .position(staffPosition)
                .jobTitle(generalJobTitle)
                .userType(regularUserType)
                .responsibility("백엔드 개발")
                .user(taeseungUser)
                .build();

        Employee kwanghoEmployee = Employee.builder()
                .employeeNumber("24-12347")
                .hireDate(LocalDateTime.now())
                .position(staffPosition)
                .jobTitle(generalJobTitle)
                .userType(regularUserType)
                .responsibility("백엔드 개발")
                .user(kwanghoUser)
                .build();


        Employee soogyeongEmployee = Employee.builder()
                .employeeNumber("24-12347")
                .hireDate(LocalDateTime.now())
                .position(staffPosition)
                .jobTitle(generalJobTitle)
                .userType(regularUserType)
                .responsibility("백엔드 개발")
                .user(soogyeongUser)
                .build();


        Employee hyerimEmployee = Employee.builder()
                .employeeNumber("24-12346")
                .hireDate(LocalDateTime.now())
                .position(staffPosition)
                .jobTitle(generalJobTitle)
                .userType(regularUserType)
                .responsibility("없음")
                .user(hyerimUser)
                .build();

        Employee kyungjunEmployee = Employee.builder()
                .employeeNumber("24-12347")
                .hireDate(LocalDateTime.now())
                .position(staffPosition)
                .jobTitle(generalJobTitle)
                .userType(regularUserType)
                .responsibility("없음")
                .user(kyungjunUser)
                .build();


        // user 저장
        userRepository.save(taeseungUser);
        userRepository.save(soogyeongUser);
        userRepository.save(kwanghoUser);
        userRepository.save(hyerimUser);
        userRepository.save(kyungjunUser);

        // employee 저장
        employeeRepository.save(taeseungEmployee);
        employeeRepository.save(kwanghoEmployee);
        employeeRepository.save(soogyeongEmployee);
        employeeRepository.save(hyerimEmployee);
        employeeRepository.save(kyungjunEmployee);


    }
}
