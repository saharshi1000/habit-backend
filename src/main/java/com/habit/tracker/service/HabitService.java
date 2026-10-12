package com.habit.tracker.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.habit.tracker.dto.DayStat;
import com.habit.tracker.dto.HabitDto;
import com.habit.tracker.model.Habit;
import com.habit.tracker.model.HabitLog;
import com.habit.tracker.repository.HabitLogRepository;
import com.habit.tracker.repository.HabitRepository;

@Service
public class HabitService {

    private final HabitRepository habitRepo;
    private final HabitLogRepository logRepo;

    public HabitService(HabitRepository habitRepo, HabitLogRepository logRepo) {
        this.habitRepo = habitRepo;
        this.logRepo = logRepo;
    }

    /** All habits of a user with today's status and current streak. */
    public List<HabitDto> today(Long userId) {
        LocalDate today = LocalDate.now();
        return habitRepo.findByUserId(userId).stream().map(h -> {
            boolean done = logRepo.findByHabitIdAndLogDate(h.getId(), today)
                    .map(HabitLog::isCompleted).orElse(false);
            return new HabitDto(h.getId(), h.getName(), h.getDescription(), done, streak(h.getId()));
        }).toList();
    }

    /** Consecutive completed days ending today (or yesterday if today isn't done yet). */
    public int streak(Long habitId) {
        Set<LocalDate> dates = logRepo.findByHabitIdOrderByLogDateDesc(habitId).stream()
                .filter(HabitLog::isCompleted)
                .map(HabitLog::getLogDate)
                .collect(Collectors.toSet());
        LocalDate d = LocalDate.now();
        if (!dates.contains(d)) d = d.minusDays(1);
        int count = 0;
        while (dates.contains(d)) {
            count++;
            d = d.minusDays(1);
        }
        return count;
    }

    /** Tick / untick today's completion. */
    public HabitLog toggle(Long habitId) {
        LocalDate today = LocalDate.now();
        HabitLog log = logRepo.findByHabitIdAndLogDate(habitId, today).orElseGet(() -> {
            HabitLog l = new HabitLog();
            l.setHabitId(habitId);
            l.setLogDate(today);
            return l;
        });
        log.setCompleted(!log.isCompleted());
        return logRepo.save(log);
    }

    /** Last 7 days: how many habits were completed each day. */
    public List<DayStat> weekly(Long userId) {
        List<Long> ids = habitRepo.findByUserId(userId).stream().map(Habit::getId).toList();
        LocalDate today = LocalDate.now();
        List<HabitLog> logs = ids.isEmpty()
                ? List.of()
                : logRepo.findByHabitIdInAndLogDateBetween(ids, today.minusDays(6), today);
        List<DayStat> result = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            LocalDate d = today.minusDays(i);
            long c = logs.stream().filter(l -> l.isCompleted() && l.getLogDate().equals(d)).count();
            result.add(new DayStat(d.toString(), (int) c, ids.size()));
        }
        return result;
    }

    @Transactional
    public void delete(Long habitId) {
        logRepo.deleteByHabitId(habitId);
        habitRepo.deleteById(habitId);
    }
}