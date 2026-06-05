package com.gyonnmi.myroutine.entity;

import java.util.Arrays;
import java.util.List;

import jakarta.persistence.*;

@Entity // 이 클래스가 데이터베이스의 테이블과 연결되는 엔티티(Entity)임을 의미
@Table(name = "routines") // 연결될 테이블 이름을 지정
public class Routine {

    @Id // 기본키(Primary Key) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO_INCREMENT
    private Long id;

    private String title;

    private String description;

    @Column(name = "repeat_days") // DB의 repeat_days 컬럼과 연결
    private String repeatDays;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY) // 테이블 간의 1:N(일대다) 관계 설정
    @JoinColumn(name = "user_id") // user_id 컬럼을 외래키(FK)로 사용
    private User user;

    public List<String> getDayList() { // repeatDays 문자열을 요일 리스트로 변환
        return Arrays.stream(repeatDays.split(","))
                .map(day -> switch (day) {
                    case "MON" -> "月";
                    case "TUE" -> "火";
                    case "WED" -> "水";
                    case "THU" -> "木";
                    case "FRI" -> "金";
                    case "SAT" -> "土";
                    case "SUN" -> "日";
                    default -> day;
                })
                .toList();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public boolean isActive() {
        return active;
    }

    public User getUser() {
        return user;
    }

    public void setTitle(String title) {
    this.title = title;
}

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setUser(User user) {
        this.user = user;
    }
}