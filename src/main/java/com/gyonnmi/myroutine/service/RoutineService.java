package com.gyonnmi.myroutine.service;

import com.gyonnmi.myroutine.entity.Routine;
import com.gyonnmi.myroutine.entity.RoutineLog;
import com.gyonnmi.myroutine.repository.RoutineLogRepository;
import com.gyonnmi.myroutine.repository.RoutineRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineLogRepository routineLogRepository;

    public RoutineService(
            RoutineRepository routineRepository,
            RoutineLogRepository routineLogRepository
    ) {
        this.routineRepository = routineRepository;
        this.routineLogRepository = routineLogRepository;
    }

    public List<Routine> getTodayRoutines(Long userId) {
        return routineRepository.findByUser_IdAndActiveTrue(userId);
    }

    public List<RoutineLog> getTodayLogs(Long userId) {
        return routineLogRepository.findByRoutine_User_IdAndRoutineDate(userId, LocalDate.now());
    }

    public int getCompletedCount(Long userId) {
        return (int) routineLogRepository.countByRoutine_User_IdAndRoutineDate(userId, LocalDate.now());
    }

    public int getAchievementRate(Long userId) {
        int totalCount = getTodayRoutines(userId).size();
        int completedCount = getCompletedCount(userId);

        if (totalCount == 0) {
            return 0;
        }

        return (int) Math.round((double) completedCount / totalCount * 100);
    }
}