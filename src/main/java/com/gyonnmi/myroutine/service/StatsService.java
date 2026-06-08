package com.gyonnmi.myroutine.service;

import com.gyonnmi.myroutine.entity.Routine;
import com.gyonnmi.myroutine.repository.RoutineLogRepository;
import com.gyonnmi.myroutine.repository.RoutineRepository;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;

@Service
public class StatsService {
    // 필드
    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;
    private final RoutineService routineService;

    // 생성자
    public StatsService(
            RoutineRepository routineRepository,
            RoutineLogRepository routineLogRepository,
            RoutineService routineService) {
        this.routineRepository = routineRepository;
        this.routineLogRepository = routineLogRepository;
        this.routineService = routineService;
    }

    public long getTotalCompletedCount(Long userId) {
        return routineLogRepository.countByRoutine_User_Id(userId);
    }

    public int getWeekAchievementRate(Long userId) {
        LocalDate today = routineService.getRoutineDate();

        LocalDate startOfWeek = today.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        return getAchievementRateBetween(userId, startOfWeek, today);
    }

    public int getMonthAchievementRate(Long userId) {
        LocalDate today = routineService.getRoutineDate();

        LocalDate startOfMonth = today.withDayOfMonth(1);

        return getAchievementRateBetween(userId, startOfMonth, today);
    }

    private int getAchievementRateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate) {
        long completedCount = routineLogRepository.countByRoutine_User_IdAndRoutineDateBetween(
                userId,
                startDate,
                endDate);

        long targetCount = countTargetRoutines(userId, startDate, endDate);

        if (targetCount == 0) {
            return 0;
        }

        return (int) Math.round((double) completedCount / targetCount * 100);
    }

    // 실행 대상 루틴 수 계산 메서드
    private long countTargetRoutines(
            Long userId,
            LocalDate startDate,
            LocalDate endDate) {
        List<Routine> routines = routineRepository.findByUser_IdAndActiveTrue(userId);

        long count = 0;

        LocalDate date = startDate;

        while (!date.isAfter(endDate)) {
            String day = convertDayOfWeek(date);

            for (Routine routine : routines) {
                if (routine.getRepeatDays() == null) {
                    continue;
                }

                if (Arrays.asList(routine.getRepeatDays().split(",")).contains(day)) {
                    count++;
                }
            }

            date = date.plusDays(1);
        }

        return count;
    }

    // 요일 변환 메서드
    private String convertDayOfWeek(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> "MON";
            case TUESDAY -> "TUE";
            case WEDNESDAY -> "WED";
            case THURSDAY -> "THU";
            case FRIDAY -> "FRI";
            case SATURDAY -> "SAT";
            case SUNDAY -> "SUN";
        };
    }
}