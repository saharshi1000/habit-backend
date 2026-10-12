package com.habit.tracker.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.habit.tracker.model.HabitLog;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
    List<HabitLog> findByHabitIdOrderByLogDateDesc(Long habitId);
    Optional<HabitLog> findByHabitIdAndLogDate(Long habitId, LocalDate logDate);
    List<HabitLog> findByHabitIdInAndLogDateBetween(List<Long> habitIds, LocalDate from, LocalDate to);
    void deleteByHabitId(Long habitId);
}