package com.gyonnmi.myroutine.repository;

import com.gyonnmi.myroutine.entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    List<Routine> findByUser_IdAndActiveTrue(Long userId);
}