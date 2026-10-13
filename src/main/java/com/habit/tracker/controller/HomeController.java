package com.habit.tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Streakly Habit Tracker API is running. Open the app: https://habit-frontend-rouge.vercel.app";
    }
}