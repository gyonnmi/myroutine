package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.service.RoutineService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
            @RequestParam String repeatDays) {
        Long userId = 1L; // 실제 애플리케이션에서는 인증된 사용자 ID를 가져와야 함. 임시 데이터

        routineService.addRoutine(userId, title, description, repeatDays);

        return "redirect:/";
    }

    // 기존 루틴을 수정하는 메서드
    @PostMapping("/routines/{id}/edit")
    public String updateRoutine(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam List<String> repeatDays) {
        String repeatDaysString = String.join(",", repeatDays);

        routineService.updateRoutine(id, title, description, repeatDaysString);

        return "redirect:/";
    }

    // 기존 루틴을 삭제하는 메서드
    @PostMapping("/routines/{id}/delete")
    public String deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);

        return "redirect:/";
    }

    // 루틴의 완료 상태를 업데이트하는 메서드
    @PostMapping("/routines/{id}/complete")
    @ResponseBody
    public void updateCompleted(
            @PathVariable Long id,
            @RequestParam boolean completed
    ) {
        routineService.updateRoutineCompleted(id, completed);
    }
}