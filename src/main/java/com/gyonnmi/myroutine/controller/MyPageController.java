package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.entity.Routine;
import com.gyonnmi.myroutine.entity.User;
import com.gyonnmi.myroutine.repository.RoutineRepository;
import com.gyonnmi.myroutine.repository.UserRepository;
import com.gyonnmi.myroutine.service.RoutineService;
import com.gyonnmi.myroutine.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MyPageController {

    // 필드
    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;
    private final UserService userService;
    private final RoutineService routineService;

    // 생성자
    public MyPageController(
            UserRepository userRepository,
            RoutineRepository routineRepository,
            UserService userService,
            RoutineService routineService) {
        this.userRepository = userRepository;
        this.routineRepository = routineRepository;
        this.userService = userService;
        this.routineService = routineService;
    }

    @GetMapping("/mypage")
    public String mypage(Model model, Authentication authentication) {
        User user = getLoginUser(authentication); // 현재 로그인한 사용자 정보 가져오기

        // 로그인한 사용자의 ID로 활성화된 루틴 목록을 가져옴
        List<Routine> routines = routineRepository.findByUser_IdAndActiveTrueOrderByIdDesc(user.getId());

        // 모델에 사용자 정보와 루틴 목록을 추가하여 뷰로 전달
        model.addAttribute("user", user);
        model.addAttribute("routines", routines);

        return "mypage";
    }

    @PostMapping("/mypage/nickname")
    public String updateNickname(
            @RequestParam String nickname,
            Authentication authentication) {
        User user = getLoginUser(authentication); // 현재 로그인한 사용자 정보 가져오기

        userService.updateNickname(user.getId(), nickname); // 사용자 ID와 새로운 닉네임을 전달하여 닉네임 업데이트

        return "redirect:/mypage?toast=nickname";
    }

    @PostMapping("/mypage/password")
    public String updatePassword(
            @RequestParam String password,
            Authentication authentication) {
        User user = getLoginUser(authentication); // 현재 로그인한 사용자 정보 가져오기

        userService.updatePassword(user.getId(), password); // 사용자 ID와 새로운 비밀번호를 전달하여 비밀번호 업데이트

        return "redirect:/mypage?toast=password";
    }

    @PostMapping("/mypage/delete")
    public String deleteAccount(
            Authentication authentication,
            HttpServletRequest request) throws ServletException {

        User user = getLoginUser(authentication); // 현재 로그인한 사용자 정보 가져오기

        userService.deleteAccount(user.getId()); // 사용자 ID를 전달하여 계정 삭제

        request.logout(); // 로그아웃 처리하여 세션 종료

        return "redirect:/login?deleted";
    }

    private User getLoginUser(Authentication authentication) {
        String username = authentication.getName(); // 현재 로그인한 사용자의 username(ID) 가져오기

        // username으로 User 엔티티 조회, 없으면 예외 발생
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
    }

    @PostMapping("/mypage/routines/{id}/delete")
    public String deleteRoutineFromMyPage(@PathVariable Long id) { // 루틴 ID를 경로 변수로 받아서 삭제 처리
        routineService.deleteRoutine(id); // 루틴 ID를 전달하여 루틴 삭제

        return "redirect:/mypage";
    }
}