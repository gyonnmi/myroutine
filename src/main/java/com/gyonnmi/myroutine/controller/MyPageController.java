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

    private final UserRepository userRepository;
    private final RoutineRepository routineRepository;
    private final UserService userService;
    private final RoutineService routineService;

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
        User user = getLoginUser(authentication);

        List<Routine> routines = routineRepository.findByUser_IdAndActiveTrueOrderByIdDesc(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("routines", routines);

        return "mypage";
    }

    @PostMapping("/mypage/nickname")
    public String updateNickname(
            @RequestParam String nickname,
            Authentication authentication) {
        User user = getLoginUser(authentication);

        userService.updateNickname(user.getId(), nickname);

        return "redirect:/mypage?toast=nickname";
    }

    @PostMapping("/mypage/password")
    public String updatePassword(
            @RequestParam String password,
            Authentication authentication) {
        User user = getLoginUser(authentication);

        userService.updatePassword(user.getId(), password);

        return "redirect:/mypage?toast=password";
    }

    @PostMapping("/mypage/delete")
    public String deleteAccount(
            Authentication authentication,
            HttpServletRequest request) throws ServletException {

        User user = getLoginUser(authentication);

        userService.deleteAccount(user.getId());

        request.logout();

        return "redirect:/login?deleted";
    }

    private User getLoginUser(Authentication authentication) {
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("ユーザーが見つかりません。"));
    }

    @PostMapping("/mypage/routines/{id}/delete")
    public String deleteRoutineFromMyPage(@PathVariable Long id) {
        routineService.deleteRoutine(id);

        return "redirect:/mypage";
    }
}