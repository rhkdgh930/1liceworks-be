package com.elice.iliceworksbe.calendar.repository;

import com.elice.iliceworksbe.calendar.entity.Calendar;
import com.elice.iliceworksbe.common.constant.CalendarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    Optional<Calendar> findByTypeAndTypeId(CalendarType type, Long typeId);
}
