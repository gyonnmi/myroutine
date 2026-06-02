package com.gyonnmi.myroutine.repository;

import com.gyonnmi.myroutine.entity.RoutineLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

// RoutineLog 엔티티에 대한 데이터 액세스 레이어를 정의하는 인터페이스
public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {
    // 특정 사용자와 날짜에 해당하는 루틴 로그를 조회하는 메서드
    List<RoutineLog> findByRoutine_User_IdAndRoutineDate(Long userId, LocalDate routineDate);
    // 특정 사용자와 날짜에 해당하는 루틴 로그의 개수를 조회하는 메서드
    long countByRoutine_User_IdAndRoutineDate(Long userId, LocalDate routineDate);
    // 특정 루틴과 날짜에 해당하는 루틴 로그가 존재하는지 여부를 조회하는 메서드
    boolean existsByRoutine_IdAndRoutineDate(Long routineId, LocalDate routineDate);
}