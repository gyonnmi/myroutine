package com.gyonnmi.myroutine.entity;

import jakarta.persistence.*;

@Entity //이 클래스가 데이터베이스의 테이블과 연결되는 엔티티(Entity)임을 의미
@Table(name = "users") // 연결될 테이블 이름을 지정
public class User {

    @Id //기본키(Primary Key) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO_INCREMENT
    private Long id;

    private String username;

    private String password;

    private String nickname;

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}