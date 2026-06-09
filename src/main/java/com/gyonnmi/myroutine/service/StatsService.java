package com.gyonnmi.myroutine.service;

import com.gyonnmi.myroutine.dto.CalendarDayDto;
import com.gyonnmi.myroutine.dto.DailyRoutineDto;
import com.gyonnmi.myroutine.entity.Routine;
import com.gyonnmi.myroutine.repository.RoutineLogRepository;
import com.gyonnmi.myroutine.repository.RoutineRepository;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
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

    // 사용자가 지금까지 완료한 루틴의 전체 횟수 조회
    public long getTotalCompletedCount(Long userId) {
        return routineLogRepository.countByRoutine_User_Id(userId);
    }

    // 이번 주 월요일부터 일요일까지의 루틴 달성률을 계산
    public int getWeekAchievementRate(Long userId) {
        LocalDate today = routineService.getRoutineDate(); // 오늘 날짜 가져오기

        // 이번 주의 월요일 구하기
        LocalDate startOfWeek = today.with(
                // 오늘 기준으로 가장 가까운 이전 월요일을 찾아줘.
                // 단, 오늘이 월요일이면 오늘 그대로 사용해줘.
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        // 이번 주의 일요일 구하기
        LocalDate endOfWeek = today.with(
                TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        // 월요일부터 일요일의 달성률 계산
        return getAchievementRateBetween(userId, startOfWeek, endOfWeek);
    }

    // 1일부터 말일까지의 루틴 달성률을 계산
    public int getMonthAchievementRate(Long userId) {
        LocalDate today = routineService.getRoutineDate();

        // 이번달 1일
        LocalDate startOfMonth = today.withDayOfMonth(1);

        // 이번달 말일
        LocalDate endOfMonth = today.withDayOfMonth(
                today.lengthOfMonth());

        // 이번 달 전체 달성률 계산
        return getAchievementRateBetween(userId, startOfMonth, endOfMonth);
    }

    // 특정 사용자의 특정 기간 동안의 달성률을 계산하는 메서드
    private int getAchievementRateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate) {
        // 해당 기간 동안 실제로 완료한 루틴 개수 조회
        long completedCount = routineLogRepository.countByRoutine_User_IdAndRoutineDateBetween(
                userId,
                startDate,
                endDate);

        // 해당 기간 동안 원래 수행해야 했던 루틴 개수 계산
        long targetCount = countTargetRoutines(userId, startDate, endDate);

        // 예외처리(할 일이 없으면 달성률도 0%)
        if (targetCount == 0) {
            return 0;
        }

        // 완료 개수 / 목표 개수 * 100 으로 달성률 계산
        // Math.round()로 반올림
        return (int) Math.round((double) completedCount / targetCount * 100);
    }

    // 특정 기간 동안 사용자가 수행해야 하는 루틴 개수 계산
    private long countTargetRoutines(
            Long userId,
            LocalDate startDate,
            LocalDate endDate) {
        // 사용자의 활성화된 루틴 목록 조회
        List<Routine> routines = routineRepository.findByUser_IdAndActiveTrue(userId);

        // 수행해야 하는 루틴 개수를 저장할 변수
        long count = 0;

        // 시작 날짜부터 하루씩 확인하기 위한 변수
        LocalDate date = startDate;

        // startDate부터 endDate까지 반복
        while (!date.isAfter(endDate)) {
            // 현재 날짜의 요일을 MON, TUE 같은 형식으로 변환
            String day = convertDayOfWeek(date);

            // 사용자의 모든 루틴을 하나씩 확인
            for (Routine routine : routines) {
                // 반복 요일이 설정되지 않은 루틴은 계산에서 제외
                if (routine.getRepeatDays() == null) {
                    continue;
                }

                // 루틴의 반복 요일에 현재 요일이 포함되어 있으면
                // 그날 수행해야 하는 루틴으로 카운트
                if (Arrays.asList(routine.getRepeatDays().split(",")).contains(day)) {
                    count++;
                }
            }
            // 다음 날짜로 이동
            date = date.plusDays(1);
        }

        return count;
    }

    // LocalDate의 요일을 DB에 저장된 요일 형식으로 변환
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

    // 통계 달력에 표시할 이번 달 날짜별 달성률 목록 생성
    public List<CalendarDayDto> getCalendarDays(
            Long userId) {

        // 앱 기준 오늘 날짜
        LocalDate today = routineService.getRoutineDate();

        // 이번 달 1일
        LocalDate firstDay = today.withDayOfMonth(1);

        // 이번 달 말일
        LocalDate lastDay = today.withDayOfMonth(
                today.lengthOfMonth());

        // 달력에 표시할 날짜별 데이터 목록
        List<CalendarDayDto> result = new ArrayList<>();

        // 1일부터 마지막 날까지 하루씩 확인
        LocalDate current = firstDay;

        while (!current.isAfter(lastDay)) {
            // 현재 날짜의 달성률 계산
            int achievementRate = getDailyAchievementRate(userId, current);

            // 날짜와 달성률을 DTO에 담아서 리스트에 추가
            result.add(
                    new CalendarDayDto(
                            current,
                            achievementRate));

            // 다음 날짜로 이동
            current = current.plusDays(1);
        }
        // 이번 달 날짜별 달성률 목록 반환
        return result;
    }

    // 특정 하루의 달성률 계산
    private int getDailyAchievementRate(Long userId, LocalDate date) {
        // 해당 날짜에 실제로 완료한 루틴 개수 조회
        long completedCount = routineLogRepository.countByRoutine_User_IdAndRoutineDate(
                userId,
                date);

        // 해당 날짜에 원래 수행해야 했던 루틴 개수 계산
        long targetCount = countTargetRoutines(userId, date, date);

        // 수행해야 할 루틴이 없으면 0% 반환
        if (targetCount == 0) {
            return 0;
        }
        // 완료 개수 / 목표 개수 * 100 으로 하루 달성률 계산
        return (int) Math.round((double) completedCount / targetCount * 100);
    }

    public List<DailyRoutineDto> getDailyRoutines(Long userId, LocalDate date) {
        String day = convertDayOfWeek(date);

        List<Routine> routines = routineRepository.findByUser_IdAndActiveTrue(userId)
                .stream()
                .filter(routine -> routine.getRepeatDays() != null)
                .filter(routine -> Arrays.asList(routine.getRepeatDays().split(",")).contains(day))
                .toList();

        return routines.stream()
                .map(routine -> {
                    boolean completed = routineLogRepository.existsByRoutine_IdAndRoutineDate(
                            routine.getId(),
                            date);

                    return new DailyRoutineDto(
                            routine.getTitle(),
                            routine.getDescription(),
                            completed);
                })
                .toList();
    }
}