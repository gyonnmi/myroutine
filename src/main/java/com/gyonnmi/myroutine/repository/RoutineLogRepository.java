package com.gyonnmi.myroutine.repository;

import com.gyonnmi.myroutine.entity.RoutineLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RoutineLogRepository extends JpaRepository<RoutineLog, Long> {

    List<RoutineLog> findByRoutine_User_IdAndRoutineDate(Long userId, LocalDate routineDate);

    long countByRoutine_User_IdAndRoutineDate(Long userId, LocalDate routineDate);

    boolean existsByRoutine_IdAndRoutineDate(Long routineId, LocalDate routineDate);
}