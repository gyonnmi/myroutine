package com.gyonnmi.myroutine.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity // 이 클래스가 데이터베이스의 테이블과 연결되는 엔티티(Entity)임을 의미
@Table(name = "routine_logs") // 연결될 테이블 이름을 지정
public class RoutineLog {

    @Id // 기본키(Primary Key) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO_INCREMENT
    private Long id;

    @Column(name = "routine_date") // DB의 routine_date 컬럼과 연결
    private LocalDate routineDate;

    @ManyToOne(fetch = FetchType.LAZY) // 테이블 간의 1:N(일대다) 관계 설정
    @JoinColumn(name = "routine_id") // routine_id 컬럼을 외래키(FK)로 사용
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