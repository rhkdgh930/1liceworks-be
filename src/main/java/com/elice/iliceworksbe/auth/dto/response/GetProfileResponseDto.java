package com.elice.iliceworksbe.auth.dto.response;

import lombok.Builder;

@Builder
public record GetProfileResponseDto(
        String username,
        String accountId,
        String profileImage,
        String phone,
        String privateEmail,
        String userType,
        String position,
        String jobTitle,
        String responsibility,
        String employeeNumber,
        String hireDate
) {}
