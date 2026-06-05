package com.gyonnmi.myroutine.service;

import com.gyonnmi.myroutine.entity.Routine;
import com.gyonnmi.myroutine.entity.RoutineLog;
import com.gyonnmi.myroutine.repository.RoutineLogRepository;
import com.gyonnmi.myroutine.repository.RoutineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.gyonnmi.myroutine.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class RoutineService {
    // 필드
    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;

    @PersistenceContext // EntityManager를 주입받기 위한 어노테이션
    private EntityManager entityManager;

    // 생성자
    public RoutineService(
            RoutineRepository routineRepository,
            RoutineLogRepository routineLogRepository) {
        this.routineRepository = routineRepository;
        this.routineLogRepository = routineLogRepository;
    }

    // 오늘의 루틴 목록을 가져오는 메서드
    public List<Routine> getTodayRoutines(Long userId) {

        // 오늘의 요일을 문자열로 변환 (예: "MON", "TUE", ...)
        String today = switch (getRoutineDate().getDayOfWeek()) {
            case MONDAY -> "MON";
            case TUESDAY -> "TUE";
            case WEDNESDAY -> "WED";
            case THURSDAY -> "THU";
            case FRIDAY -> "FRI";
            case SATURDAY -> "SAT";
            case SUNDAY -> "SUN";
        };

        // 사용자 ID와 활성화된 루틴을 가져온 후, 오늘의 요일이 포함된 루틴만 필터링하여 반환
        return routineRepository.findByUser_IdAndActiveTrue(userId)
                .stream()
                .filter(routine -> routine.getRepeatDays() != null)
                .filter(routine -> Arrays.asList(routine.getRepeatDays().split(",")).contains(today))
                .toList();
    }

    // 오늘 완료된 루틴 목록을 가져오는 메서드
    public List<RoutineLog> getTodayLogs(Long userId) {
        return routineLogRepository.findByRoutine_User_IdAndRoutineDate(userId, getRoutineDate());
    }

    // 오늘 완료된 루틴의 개수를 가져오는 메서드
    public int getCompletedCount(Long userId) {
        return (int) routineLogRepository.countByRoutine_User_IdAndRoutineDate(userId, getRoutineDate());
    }

    // 오늘의 루틴 달성률을 계산하는 메서드
    public int getAchievementRate(Long userId) {
        int totalCount = getTodayRoutines(userId).size();
        int completedCount = getCompletedCount(userId);

        if (totalCount == 0) {
            return 0;
        }

        return (int) Math.round((double) completedCount / totalCount * 100);
    }

    // 루틴 추가 메서드
    public void addRoutine(
            Long userId,
            String title,
            String description,
            String repeatDays) {
        User user = entityManager.getReference(User.class, userId);

        Routine routine = new Routine();
        routine.setUser(user);
        routine.setTitle(title);
        routine.setDescription(description);
        routine.setRepeatDays(repeatDays);
        routine.setActive(true);

        routineRepository.save(routine);
    }

    // 루틴 업데이트 메서드
    public void updateRoutine(
            Long routineId,
            String title,
            String description,
            String repeatDays) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("ルーティンが見つかりません。"));

        routine.setTitle(title);
        routine.setDescription(description);
        routine.setRepeatDays(repeatDays);

        routineRepository.save(routine);
    }

    // 루틴 삭제 메서드
    public void deleteRoutine(Long routineId) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("ルーティンが見つかりません。"));

        routine.setActive(false);

        routineRepository.save(routine);
    }

    public LocalDate getRoutineDate() {
        return LocalDateTime.now()
                .minusHours(4)
                .toLocalDate();
    }

    // 루틴이 오늘 완료되었는지 여부를 확인하는 메서드
    public boolean isCompletedToday(Long routineId) {
        return routineLogRepository.existsByRoutine_IdAndRoutineDate(
                routineId,
                getRoutineDate());
    }

    // 루틴의 완료 상태를 업데이트하는 메서드
    @Transactional
    public void updateRoutineCompleted(Long routineId, boolean completed) {
        LocalDate routineDate = getRoutineDate();

        /*
         * 해당 루틴이 오늘 이미 완료 처리되어 있는지 확인
         *
         * routine_logs 테이블에
         * routine_id와 routine_date가 같은 데이터가 있으면 true,
         * 없으면 false를 반환
         */
        boolean alreadyCompleted = routineLogRepository.existsByRoutine_IdAndRoutineDate(routineId, routineDate);

        /*
         * 체크박스를 체크한 경우
         *
         * completed == true
         * alreadyCompleted == false
         *
         * 사용자가 루틴을 완료했지만
         * 아직 DB에 완료 이력이 없는 상태라면
         * routine_logs 테이블에 새 완료 이력을 저장
         */
        if (completed && !alreadyCompleted) {
            /*
             * routines 테이블에서 해당 루틴을 조회.
             *
             * 존재하지 않는 루틴 ID가 들어오면 예외를 발생시킴.
             */
            Routine routine = routineRepository.findById(routineId)
                    .orElseThrow(() -> new IllegalArgumentException("ルーティンが見つかりません。"));

            /*
             * 새 완료 이력 객체를 생성.
             *
             * routine에는 완료한 루틴 정보,
             * routineDate에는 완료 날짜를 저장
             */
            RoutineLog routineLog = new RoutineLog();
            routineLog.setRoutine(routine);
            routineLog.setRoutineDate(routineDate);

            /*
             * routine_logs 테이블에 INSERT
             *
             * 이 시점부터 새로고침해도
             * 해당 루틴은 완료 상태로 다시 표시될 수 있음.
             */
            routineLogRepository.save(routineLog);
        }

        /*
         * 체크박스를 해제한 경우
         *
         * completed == false
         * alreadyCompleted == true
         *
         * 사용자가 완료 상태를 취소했고 DB에 완료 이력이 존재한다면
         * routine_logs 테이블에서 해당 이력을 삭제합니다.
         */
        if (!completed && alreadyCompleted) {
            /*
             * 해당 루틴의 오늘 완료 이력을 삭제.
             */
            routineLogRepository.deleteByRoutine_IdAndRoutineDate(routineId, routineDate);
        }
    }

    // 오늘 완료된 루틴의 ID 목록을 가져오는 메서드
    public List<Long> getCompletedRoutineIds(Long userId) {
        return routineLogRepository
                .findByRoutine_User_IdAndRoutineDate(userId, getRoutineDate())
                .stream()
                .map(log -> log.getRoutine().getId())
                .toList();
    }
}