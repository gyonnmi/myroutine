package com.gyonnmi.myroutine.dto;

import java.time.LocalDate;

public class CalendarDayDto {

    private LocalDate date;
    private int achievementRate;

    public CalendarDayDto(
            LocalDate date,
            int achievementRate
    ) {
        this.date = date;
        this.achievementRate = achievementRate;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getAchievementRate() {
        return achievementRate;
    }
}