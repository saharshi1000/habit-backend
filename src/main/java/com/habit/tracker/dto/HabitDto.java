package com.habit.tracker.dto;

public record HabitDto(Long id, String name, String description,
                       boolean completedToday, int streak) {}