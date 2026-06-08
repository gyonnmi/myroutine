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

    // 사용자가 지금까지 완료한 루틴의 총 횟수
    public long getTotalCompletedCount(Long userId) {
        return routineLogRepository.countByRoutine_User_Id(userId);
    }

    // 이번 주 월요일부터 일요일까지의 루틴 달성률을 계산
    public int getWeekAchievementRate(Long userId) {
        LocalDate today = routineService.getRoutineDate(); // 오늘 날짜 가져오기

        // 이번주의 시작일(=월요일) 구하기
        LocalDate startOfWeek = today.with(
                // 오늘 기준으로 가장 가까운 이전 월요일을 찾아줘.
                // 단, 오늘이 월요일이면 오늘 그대로 사용해줘.
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate endOfWeek = today.with(
                TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return getAchievementRateBetween(userId, startOfWeek, endOfWeek);
    }

    // 1일부터 말일까지의 루틴 달성률을 계산
    public int getMonthAchievementRate(Long userId) {
        LocalDate today = routineService.getRoutineDate();

        LocalDate startOfMonth = today.withDayOfMonth(1);

        LocalDate endOfMonth = today.withDayOfMonth(
                today.lengthOfMonth());

        return getAchievementRateBetween(userId, startOfMonth, endOfMonth);
    }

    // 특정 사용자의 특정 기간 동안의 달성률을 계산하는 메서드
    private int getAchievementRateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate) {
        // 실제로 완료한 루틴 개수 조회
        long completedCount = routineLogRepository.countByRoutine_User_IdAndRoutineDateBetween(
                userId,
                startDate,
                endDate);

        // 사용자가 원래 해야했던 루틴 개수
        long targetCount = countTargetRoutines(userId, startDate, endDate);

        // 예외처리(할 일이 없으면 달성률도 0%)
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