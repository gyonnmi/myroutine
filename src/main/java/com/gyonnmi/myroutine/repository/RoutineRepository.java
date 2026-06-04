package com.gyonnmi.myroutine.repository;

import com.gyonnmi.myroutine.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Routine 엔티티에 대한 데이터 액세스 레이어를 정의하는 인터페이스
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    // 사용자 ID와 활성화된 루틴을 가져오는 메서드
    List<Routine> findByUser_IdAndActiveTrue(Long userId);

    // 사용자 ID에 해당하는 모든 루틴을 ID 내림차순으로 가져오는 메서드
    List<Routine> findByUser_IdOrderByIdDesc(Long userId);

    // 사용자 ID와 활성화된 루틴을 ID 내림차순으로 가져오는 메서드
    List<Routine> findByUser_IdAndActiveTrueOrderByIdDesc(Long userId);

    void deleteByUser_Id(Long userId);
}