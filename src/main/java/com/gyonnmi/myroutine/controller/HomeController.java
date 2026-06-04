package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.entity.Routine;
import com.gyonnmi.myroutine.entity.User;
import com.gyonnmi.myroutine.repository.UserRepository;
import com.gyonnmi.myroutine.service.RoutineService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
public class HomeController {

    // 필드
    private final RoutineService routineService; // 루틴 관련 비즈니스 로직을 처리하는 서비스
    private final UserRepository userRepository; // 사용자 정보를 데이터베이스에서 조회하기 위한 리포지토리

    // 생성자
    public HomeController(
            RoutineService routineService,
            UserRepository userRepository) {

        this.routineService = routineService;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        String username = authentication.getName(); // 현재 로그인한 사용자의 username(ID) 가져오기

        User user = userRepository.findByUsername(username)
                .orElseThrow(); // username으로 User 엔티티 조회, 없으면 예외 발생

        Long userId = user.getId(); // User 엔티티에서 사용자 ID 가져오기

        model.addAttribute("nickname", user.getNickname()); // 모델에 사용자 닉네임 추가하여 뷰로 전달

        List<Routine> routines = routineService.getTodayRoutines(userId); // 오늘의 루틴 목록을 가져옴
        List<Long> completedRoutineIds = routineService.getCompletedRoutineIds(userId); // 오늘 완료된 루틴의 ID 목록을 가져옴

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
        model.addAttribute("completedRoutineIds", completedRoutineIds);

        return "home";
    }
}