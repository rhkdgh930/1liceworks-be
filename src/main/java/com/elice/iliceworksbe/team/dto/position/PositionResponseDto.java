package com.elice.iliceworksbe.team.dto.position;


import com.elice.iliceworksbe.team.entity.Position;

public record PositionResponseDto(Long id, String name) {
    public static PositionResponseDto from(Position position) {
        return new PositionResponseDto(position.getId(), position.getName());
    }
}
