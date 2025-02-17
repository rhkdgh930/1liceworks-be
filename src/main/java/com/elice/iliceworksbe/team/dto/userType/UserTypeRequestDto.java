package com.elice.iliceworksbe.team.dto.userType;

import com.elice.iliceworksbe.team.entity.UserType;

public record UserTypeRequestDto(String name) {
    public UserType from() {
        return UserType.from(this);
    }
}
