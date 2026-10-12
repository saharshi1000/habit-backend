package com.habit.tracker.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "habit_logs",
       uniqueConstraints = @UniqueConstraint(columnNames = {"habit_id", "log_date"}))
public class HabitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long habitId;
    private LocalDate logDate;
    private boolean completed;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getHabitId() { return habitId; }
    public void setHabitId(Long habitId) { this.habitId = habitId; }
    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}