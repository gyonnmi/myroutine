package com.gyonnmi.myroutine.repository;

import com.gyonnmi.myroutine.entity.RoutineLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// RoutineLog 엔티티에 대한 데이터 액세스 레이어를 정의하는 인터페이스
public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {
    // 특정 사용자가 특정 날짜에 완료한 루틴 개수를 조회하는 메서드
    long countByRoutine_User_IdAndRoutineDate(Long userId, LocalDate routineDate);

    // 특정 루틴이 특정 날짜에 이미 완료되었는지 확인하는 메서드
    boolean existsByRoutine_IdAndRoutineDate(Long routineId, LocalDate routineDate);

    // 특정 루틴의 특정 날짜 수행 기록을 조회하는 메서드
    Optional<RoutineLog> findByRoutine_IdAndRoutineDate(Long routineId, LocalDate routineDate);

    // 특정 루틴의 특정 날짜 수행 기록 삭제하는 메서드
    void deleteByRoutine_IdAndRoutineDate(Long routineId, LocalDate routineDate);

    // 특정 사용자가 특정 날짜에 완료한 루틴의 ID 목록을 조회하는 메서드
    List<RoutineLog> findByRoutine_User_IdAndRoutineDate(Long userId, LocalDate routineDate);

    // 특정 사용자의 모든 루틴 수행 기록을 삭제하는 메서드
    void deleteByRoutine_User_Id(Long userId);

    long countByRoutine_User_Id(Long userId);

    long countByRoutine_User_IdAndRoutineDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate);
}