package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.entity.Routine;
import com.gyonnmi.myroutine.service.RoutineService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
public class HomeController {

    private final RoutineService routineService;

    public HomeController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Long userId = 1L;

        List<Routine> routines = routineService.getTodayRoutines(userId);

        int totalCount = routines.size();
        int completedCount = routineService.getCompletedCount(userId);
        int achievementRate = routineService.getAchievementRate(userId);

        String today = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy年M月d日（E）", Locale.JAPANESE));

        System.out.println("=================================");
        System.out.println("조회된 루틴 개수 : " + routines.size());
        System.out.println("조회된 루틴 목록 : " + routines);
        System.out.println("=================================");

        model.addAttribute("today", today);
        model.addAttribute("routines", routines);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("achievementRate", achievementRate);

        return "home";
    }
}