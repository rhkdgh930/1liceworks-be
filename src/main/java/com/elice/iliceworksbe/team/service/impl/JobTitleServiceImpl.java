package com.elice.iliceworksbe.team.service.impl;

import com.elice.iliceworksbe.common.exception.BaseException;
import com.elice.iliceworksbe.common.exception.ErrorCode;
import com.elice.iliceworksbe.team.dto.jobTitle.JobTitleRequestDto;
import com.elice.iliceworksbe.team.dto.jobTitle.JobTitleResponseDto;
import com.elice.iliceworksbe.team.dto.jobTitle.JobTitleUpdateDto;
import com.elice.iliceworksbe.team.entity.JobTitle;
import com.elice.iliceworksbe.team.repository.JobTitleRepository;
import com.elice.iliceworksbe.team.service.JobTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobTitleServiceImpl implements JobTitleService {

    private final JobTitleRepository jobTitleRepository;

    @Transactional
    @Override
    public JobTitleResponseDto postJobTitle(JobTitleRequestDto jobTitleRequestDto) {
        JobTitle savedJobTitle = jobTitleRepository.save(JobTitle.from(jobTitleRequestDto));
        return JobTitleResponseDto.from(savedJobTitle);
    }

    @Override
    public JobTitleResponseDto getJobTitle(Long jobTitleId) {
        JobTitle findedJobTitle = jobTitleRepository.findById(jobTitleId)
                .orElseThrow(() -> new BaseException(ErrorCode.JOB_TITLE_NOT_FOUND));
        return JobTitleResponseDto.from(findedJobTitle);
    }

    @Override
    public List<JobTitleResponseDto> getAllJobTitles() {
        return jobTitleRepository.findAll()
                .stream()
                .map(JobTitleResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public JobTitleResponseDto patchJobTitle(Long jobTitleId, JobTitleUpdateDto jobTitleUpdateDto) {
        JobTitle findedJobTitle = jobTitleRepository.findById(jobTitleId)
                .orElseThrow(() -> new BaseException(ErrorCode.JOB_TITLE_NOT_FOUND));

        findedJobTitle.update(jobTitleUpdateDto);

        JobTitle updatedJobTitle = jobTitleRepository.save(findedJobTitle);
        return JobTitleResponseDto.from(updatedJobTitle);
    }

    @Transactional
    @Override
    public void deleteJobTitle(Long jobTitleId) {
        jobTitleRepository.deleteById(jobTitleId);
    }

    @Override
    public JobTitle getJobTileByName(String name) {
        return jobTitleRepository.findByName(name)
                .orElseThrow(() -> new BaseException(ErrorCode.JOB_TITLE_NOT_FOUND));
    }

    @PostConstruct
    public void init() {
        JobTitle jobTitle1 = JobTitle.builder()
                .name("없음")
                .build();

        JobTitle jobTitle2 = JobTitle.builder()
                .name("사장")
                .build();

        JobTitle jobTitle3 = JobTitle.builder()
                .name("이사")
                .build();

        JobTitle jobTitle4 = JobTitle.builder()
                .name("부장")
                .build();

        JobTitle jobTitle5 = JobTitle.builder()
                .name("과장")
                .build();

        JobTitle jobTitle6 = JobTitle.builder()
                .name("대리")
                .build();

        JobTitle jobTitle7 = JobTitle.builder()
                .name("사원")
                .build();

        jobTitleRepository.save(jobTitle1);
        jobTitleRepository.save(jobTitle2);
        jobTitleRepository.save(jobTitle3);
        jobTitleRepository.save(jobTitle4);
        jobTitleRepository.save(jobTitle5);
        jobTitleRepository.save(jobTitle6);
        jobTitleRepository.save(jobTitle7);

    }
}
