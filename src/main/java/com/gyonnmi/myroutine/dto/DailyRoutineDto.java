package com.gyonnmi.myroutine.dto;

public class DailyRoutineDto {

    private String title;
    private String description;
    private boolean completed;

    public DailyRoutineDto(String title, String description, boolean completed) {
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
}