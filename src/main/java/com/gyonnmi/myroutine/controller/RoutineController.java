package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.entity.User;
import com.gyonnmi.myroutine.service.RoutineService;
import com.gyonnmi.myroutine.service.UserService;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoutineController {

    // 필드
    private final RoutineService routineService;
    private final UserService userService;

    // 생성자
    public RoutineController(
            RoutineService routineService,
            UserService userService) {

        this.routineService = routineService;
        this.userService = userService;
    }

    // 새로운 루틴을 추가하는 메서드
    @PostMapping("/routines")
    public String addRoutine( 
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String repeatDays,
            Authentication authentication) {

        User user = userService.getLoginUser(authentication);

        Long userId = user.getId(); // User 엔티티에서 사용자 ID 가져오기

        routineService.addRoutine( // 루틴 추가 메서드 호출
                userId,
                title,
                description,
                repeatDays);

        return "redirect:/";
    }

    // 기존 루틴을 수정하는 메서드
    @PostMapping("/routines/{id}/edit")
    public String updateRoutine( 
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam List<String> repeatDays) {
        String repeatDaysString = String.join(",", repeatDays); // 반복 요일 리스트를 문자열로 변환하여 저장

        routineService.updateRoutine(id, title, description, repeatDaysString); // 루틴 수정 메서드 호출

        return "redirect:/";
    }

    // 루틴 ID를 경로 변수로 받아서 삭제 처리하는 메서드
    @PostMapping("/routines/{id}/delete")
    public String deleteRoutine(@PathVariable Long id) { 
        routineService.deleteRoutine(id); // 루틴 ID를 전달하여 루틴 삭제

        return "redirect:/";
    }

    // // 루틴 완료 상태를 업데이트하는 메서드
    @PostMapping("/routines/{id}/complete")
    @ResponseBody
    public void updateCompleted( 
            @PathVariable Long id,
            @RequestParam boolean completed) {
        routineService.updateRoutineCompleted(id, completed); // 루틴 ID와 완료 상태를 전달하여 업데이트 처리
    }
}