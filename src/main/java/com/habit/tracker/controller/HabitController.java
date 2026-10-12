package com.habit.tracker.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.habit.tracker.dto.DayStat;
import com.habit.tracker.dto.HabitDto;
import com.habit.tracker.model.Habit;
import com.habit.tracker.model.HabitLog;
import com.habit.tracker.repository.HabitLogRepository;
import com.habit.tracker.repository.HabitRepository;
import com.habit.tracker.service.HabitService;

@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService service;
    private final HabitRepository habitRepo;
    private final HabitLogRepository logRepo;

    public HabitController(HabitService service, HabitRepository habitRepo, HabitLogRepository logRepo) {
        this.service = service;
        this.habitRepo = habitRepo;
        this.logRepo = logRepo;
    }

    @GetMapping("/today")
    public List<HabitDto> today(@RequestParam Long userId) {
        return service.today(userId);
    }

    @GetMapping("/weekly")
    public List<DayStat> weekly(@RequestParam Long userId) {
        return service.weekly(userId);
    }

    @PostMapping
    public Habit create(@RequestBody Habit h) {
        h.setId(null);
        return habitRepo.save(h);
    }

    @PostMapping("/{id}/toggle")
    public HabitLog toggle(@PathVariable Long id) {
        return service.toggle(id);
    }

    @GetMapping("/{id}/logs")
    public List<HabitLog> logs(@PathVariable Long id) {
        return logRepo.findByHabitIdOrderByLogDateDesc(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}