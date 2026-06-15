package com.gyonnmi.myroutine.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import com.gyonnmi.myroutine.repository.RoutineLogRepository;
import com.gyonnmi.myroutine.repository.RoutineRepository;

public class StatsServiceTest {
    @Test
    void getTotalCompletedCount_완료한_루틴_전체_횟수를_반환한다() {
        // given
        Long userId = 1L;

        RoutineRepository routineRepository = mock(RoutineRepository.class);
        RoutineLogRepository routineLogRepository = mock(RoutineLogRepository.class);
        RoutineService routineService = mock(RoutineService.class);

        StatsService statsService = new StatsService(
                routineRepository,
                routineLogRepository,
                routineService);

        when(routineLogRepository.countByRoutine_User_Id(userId))
                .thenReturn(5L);

        // when
        long result = statsService.getTotalCompletedCount(userId);

        // then
        assertEquals(5L, result);
    }

    @Test
    void 완료횟수가_0이면_0을_반환한다() {

        Long userId = 1L;

        RoutineRepository routineRepository = mock(RoutineRepository.class);
        RoutineLogRepository routineLogRepository = mock(RoutineLogRepository.class);
        RoutineService routineService = mock(RoutineService.class);

        StatsService statsService = new StatsService(
                routineRepository,
                routineLogRepository,
                routineService);

        when(routineLogRepository.countByRoutine_User_Id(userId))
                .thenReturn(0L);

        long result = statsService.getTotalCompletedCount(userId);

        assertEquals(0L, result);
    }
}
