package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.entity.User;
import com.gyonnmi.myroutine.repository.UserRepository;
import com.gyonnmi.myroutine.service.RoutineService;

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
    private final UserRepository userRepository;

    // 생성자
    public RoutineController(
            RoutineService routineService,
            UserRepository userRepository) {

        this.routineService = routineService;
        this.userRepository = userRepository;
    }

    // 새로운 루틴을 추가하는 메서드
    @PostMapping("/routines")
    public String addRoutine( 
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String repeatDays,
            Authentication authentication) {
        String username = authentication.getName(); // 현재 로그인한 사용자의 username(ID) 가져오기

        User user = userRepository.findByUsername(username)
                .orElseThrow(); // username으로 User 엔티티 조회, 없으면 예외 발생

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