package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.service.UserService;
import org.springframework.stereotype.Controller;
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
    public String signupForm() { // 회원가입 페이지로 이동하는 메서드
        return "signup";
    }

    @PostMapping("/signup")
    public String signup( // 회원가입 처리 메서드
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nickname) {
        userService.signup(username, password, nickname); // UserService의 회원가입 메서드 호출하여 사용자 등록 처리

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm() { // 로그인 페이지로 이동하는 메서드
        return "login";
    }
}