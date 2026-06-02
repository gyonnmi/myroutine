package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.service.RoutineService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoutineController {

    private final RoutineService routineService;

    // 생성자 주입 방식으로 RoutineService를 주입받음
    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping("/routines") 
    // 새로운 루틴을 추가하는 메서드
    public String addRoutine(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String repeatDays
    ) {
        Long userId = 1L; // 실제 애플리케이션에서는 인증된 사용자 ID를 가져와야 함. 임시 데이터

        routineService.addRoutine(userId, title, description, repeatDays);

        return "redirect:/";
    }
}