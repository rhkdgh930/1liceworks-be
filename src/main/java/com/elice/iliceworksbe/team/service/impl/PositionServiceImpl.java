package com.elice.iliceworksbe.team.service.impl;

import com.elice.iliceworksbe.common.exception.BaseException;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import com.elice.iliceworksbe.team.dto.position.PositionRequestDto;
import com.elice.iliceworksbe.team.dto.position.PositionResponseDto;
import com.elice.iliceworksbe.team.dto.position.PositionUpdateDto;
import com.elice.iliceworksbe.team.entity.Position;
import com.elice.iliceworksbe.team.repository.PositionRepository;
import com.elice.iliceworksbe.team.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PositionServiceImpl implements PositionService {

    private final PositionRepository positionRepository;

    @Override
    public PositionResponseDto postPosition(PositionRequestDto positionRequestDto) {
        Position savedPosition = positionRepository.save(Position.from(positionRequestDto));
        return PositionResponseDto.from(savedPosition);
    }

    @Transactional(readOnly = true)
    @Override
    public PositionResponseDto getPosition(Long positionId) {
        Position findedPosition = positionRepository.findById(positionId)
                .orElseThrow(() -> new BaseException(ErrorCode.POSITION_NOT_FOUND));
        return PositionResponseDto.from(findedPosition);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PositionResponseDto> getAllPositions() {
        return positionRepository.findAll()
                .stream()
                .map(PositionResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public PositionResponseDto patchPosition(Long positionId, PositionUpdateDto positionUpdateDto) {
        Position findedPosition = positionRepository.findById(positionId)
                .orElseThrow(() -> new BaseException(ErrorCode.POSITION_NOT_FOUND));

        findedPosition.update(positionUpdateDto);

        Position updatedPosition = positionRepository.save(findedPosition);
        return PositionResponseDto.from(updatedPosition);
    }

    @Override
    public void deletePosition(Long positionId) {
        positionRepository.deleteById(positionId);
    }

    @PostConstruct
    public void init() {
        Position position = Position.builder()
                .name("none")
                .build();

        positionRepository.save(position);
    }
}
