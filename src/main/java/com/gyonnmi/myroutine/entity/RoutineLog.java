package com.gyonnmi.myroutine.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "routine_logs")
public class RoutineLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "routine_date")
    private LocalDate routineDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    public Long getId() {
        return id;
    }

    public LocalDate getRoutineDate() {
        return routineDate;
    }

    public Routine getRoutine() {
        return routine;
    }
}