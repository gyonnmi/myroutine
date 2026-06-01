package com.gyonnmi.myroutine.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "routines")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @Column(name = "repeat_days")
    private String repeatDays;

    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

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