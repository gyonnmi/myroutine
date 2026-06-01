package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.service.RoutineService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping("/routines")
    public String addRoutine(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam String repeatDays
    ) {
        Long userId = 1L;

        routineService.addRoutine(userId, title, description, repeatDays);

        return "redirect:/";
    }
}