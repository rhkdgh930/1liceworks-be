package com.elice.iliceworksbe.calendar.repository;

import com.elice.iliceworksbe.calendar.entity.Calendar;
import com.elice.iliceworksbe.calendar.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e " +
            "WHERE ((e.dtStartTime BETWEEN :startDate AND :endDate) " +
            "OR (e.dtEndTime BETWEEN :startDate AND :endDate))" +
            "AND (e.calendar = :calendar)")
    List<Event> findEventsWithinThreeMonthsByCalendar(@Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate,
                                                      @Param("calendar") Calendar calendar);


    @Query("SELECT e FROM Event e " +
            "WHERE e.title LIKE %:keyword% " +
            "AND e.calendar = :calendar ")
    List<Event> findByTitleContainingAndCalendar(
            @Param("keyword") String keyword,
            @Param("calendar") Calendar calendar);

    @Query("SELECT e FROM Event e " +
            "WHERE e.title LIKE %:keyword% " +
            "AND e.calendar = :calendar " +
            "AND e.privacy <> 'PRIVATE'")
    List<Event> findByTitleContainingAndCalendarAndNotPrivate(
            @Param("keyword") String keyword,
            @Param("calendar") Calendar calendar);
}
