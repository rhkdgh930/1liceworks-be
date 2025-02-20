package com.elice.iliceworksbe.team.web;

import com.elice.iliceworksbe.auth.model.UserDetailsImpl;
import com.elice.iliceworksbe.common.exception.BaseResponse;
import com.elice.iliceworksbe.team.dto.team.*;
import com.elice.iliceworksbe.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    @PreAuthorize("hasAuthority('LEADER')")
    @PostMapping("/member")
    public BaseResponse<TeamMemberResponseDto> postMember(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody TeamMemberRequestDto teamMemberRequestDto) {
        TeamMemberResponseDto teamMemberResponseDto = teamService.addMember(userDetails.getUserId(), teamMemberRequestDto);
        return new BaseResponse<>(teamMemberResponseDto);
    }

    @PreAuthorize("hasAuthority('LEADER')")
    @PatchMapping("/member/{memberId}")
    public BaseResponse<TeamMemberDetailResponseDto> patchMember(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long memberId,
            @RequestBody TeamMemberInfoUpdateDto teamMemberInfoUpdateDto) {
        TeamMemberDetailResponseDto teamMemberDetailResponseDto = teamService.updateMemberInfo(userDetails.getUserId(), memberId, teamMemberInfoUpdateDto);
        return new BaseResponse<>(teamMemberDetailResponseDto);
    }

    @PreAuthorize("hasAuthority('LEADER')")
    @PatchMapping("/{teamId}")
    public BaseResponse<TeamResponseDto> patchTeam(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long teamId,
            @RequestBody TeamInfoUpdateDto teamInfoUpdateDto) {
        TeamResponseDto teamResponseDto = teamService.updateTeamInfo(userDetails.getUserId(), teamId, teamInfoUpdateDto);
        return new BaseResponse<>(teamResponseDto);
    }
}
