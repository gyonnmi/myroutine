package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    // 필드
    private final UserService userService;

    // 생성자
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "signup";
    }

    //// 회원가입 처리 메서드
    @PostMapping("/signup")
    public String signup( 
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nickname,
            Model model) {
        try { 

            userService.signup(
                    username,
                    password,
                    nickname);

            return "redirect:/login";

        } catch (IllegalArgumentException e) { // 회원가입 실패 시 예외 처리

            model.addAttribute("errorMessage", e.getMessage());

            return "signup";
        }
    }

    @GetMapping("/login")
    public String loginForm() { 
        return "login";
    }
}