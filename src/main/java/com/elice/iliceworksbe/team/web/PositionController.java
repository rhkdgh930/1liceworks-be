package com.elice.iliceworksbe.team.web;

import com.elice.iliceworksbe.common.exception.BaseResponse;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import com.elice.iliceworksbe.team.dto.position.PositionRequestDto;
import com.elice.iliceworksbe.team.dto.position.PositionResponseDto;
import com.elice.iliceworksbe.team.dto.position.PositionUpdateDto;
import com.elice.iliceworksbe.team.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/position")
public class PositionController {

    private final PositionService positionService;

    @PostMapping
    public BaseResponse<PositionResponseDto> postPosition(@RequestBody PositionRequestDto positionRequestDto) {
        PositionResponseDto postResponseDto = positionService.postPosition(positionRequestDto);
        return new BaseResponse<>(postResponseDto);
    }

    @GetMapping
    public BaseResponse<List<PositionResponseDto>> getAllPositions() {
        List<PositionResponseDto> getResponseDtos = positionService.getAllPositions();
        return new BaseResponse<>(getResponseDtos);
    }

    @GetMapping("/{positionId}")
    public BaseResponse<PositionResponseDto> getPosition(@PathVariable Long positionId) {
        PositionResponseDto getResponseDto = positionService.getPosition(positionId);
        return new BaseResponse<>(getResponseDto);
    }

    @PatchMapping("/{positionId}")
    public BaseResponse<PositionResponseDto> patchPosition(
            @PathVariable Long positionId,
            @RequestBody PositionUpdateDto positionUpdateDto) {
        PositionResponseDto patchResponseDto = positionService.patchPosition(positionId, positionUpdateDto);
        return new BaseResponse<>(patchResponseDto);
    }

    @DeleteMapping("/{positionId}")
    public BaseResponse<String> deletePosition(@PathVariable Long positionId) {
        positionService.deletePosition(positionId);
        return new BaseResponse<>(ErrorCode.NO_CONTENT);
    }
}
