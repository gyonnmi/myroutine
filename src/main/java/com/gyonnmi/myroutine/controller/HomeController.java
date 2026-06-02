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

    // 생성자 주입 방식으로 RoutineService를 주입받음
    public HomeController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping("/")
    public String home(Model model) {
        Long userId = 1L; // 실제 애플리케이션에서는 인증된 사용자 ID를 가져와야 함. 임시 데이터
    
        List<Routine> routines = routineService.getTodayRoutines(userId); // 오늘의 루틴 목록을 가져옴
    
        int totalCount = routines.size(); // 오늘의 루틴 총 개수
        int completedCount = routineService.getCompletedCount(userId); // 오늘 완료된 루틴 개수
        int achievementRate = routineService.getAchievementRate(userId); // 오늘 달성률 계산

        // 오늘 날짜를 "yyyy年M月d日（E）" 형식으로 포맷팅
        String today = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyy年M月d日（E）", Locale.JAPANESE));

        // 모델에 데이터를 추가하여 뷰로 전달
        model.addAttribute("today", today);
        model.addAttribute("routines", routines);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("achievementRate", achievementRate);

        return "home";
    }
}