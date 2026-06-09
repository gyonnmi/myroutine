package com.gyonnmi.myroutine.controller;

import com.gyonnmi.myroutine.dto.DailyRoutineDto;
import com.gyonnmi.myroutine.entity.User;
import com.gyonnmi.myroutine.service.StatsService;
import com.gyonnmi.myroutine.service.UserService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatsController {

	// 필드
	private final StatsService statsService;
	private final UserService userService;

	// 생성자
	public StatsController(
			StatsService statsService,
			UserService userService) {
		this.statsService = statsService;
		this.userService = userService;
	}

	@GetMapping("/stats")
	public String stats(
			Model model,
			Authentication authentication) {

		User user = userService.getLoginUser(authentication);

		Long userId = user.getId();

		// 이번주 달성률
		int weekAchievementRate = statsService.getWeekAchievementRate(userId);

		// 이번달 달성률
		int monthAchievementRate = statsService.getMonthAchievementRate(userId);

		// 총 완료 횟수
		long totalCompletedCount = statsService.getTotalCompletedCount(userId);

		model.addAttribute("weekAchievementRate", weekAchievementRate);
		model.addAttribute("monthAchievementRate", monthAchievementRate);
		model.addAttribute("totalCompletedCount", totalCompletedCount);
		model.addAttribute(
				"calendarDays",
				statsService.getCalendarDays(userId));

		return "stats";
	}

	@GetMapping("/stats/day")
	@ResponseBody
	public List<DailyRoutineDto> getDailyRoutines(
			@RequestParam LocalDate date,
			Authentication authentication) {
		User user = userService.getLoginUser(authentication);

		return statsService.getDailyRoutines(user.getId(), date);
	}
}