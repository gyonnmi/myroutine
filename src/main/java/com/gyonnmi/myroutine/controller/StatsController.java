package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.entity.User;
import com.gyonnmi.myroutine.repository.UserRepository;
import com.gyonnmi.myroutine.service.StatsService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatsController {

    // 필드
    private final StatsService statsService;
    private final UserRepository userRepository;

    // 생성자
    public StatsController(
            StatsService statsService,
            UserRepository userRepository) {
        this.statsService = statsService;
        this.userRepository = userRepository;
    }

    @GetMapping("/stats")
    public String stats(
            Model model,
            Authentication authentication) {
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        Long userId = user.getId();

        int weekAchievementRate = statsService.getWeekAchievementRate(userId);

        int monthAchievementRate = statsService.getMonthAchievementRate(userId);

        long totalCompletedCount = statsService.getTotalCompletedCount(userId);

        model.addAttribute("weekAchievementRate", weekAchievementRate);
        model.addAttribute("monthAchievementRate", monthAchievementRate);
        model.addAttribute("totalCompletedCount", totalCompletedCount);
        model.addAttribute(
                "calendarDays",
                statsService.getCalendarDays(userId));

        return "stats";
    }
}