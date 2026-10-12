package com.habit.tracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.habit.tracker.model.Habit;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUserId(Long userId);
}