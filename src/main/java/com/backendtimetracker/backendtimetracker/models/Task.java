package com.backendtimetracker.backendtimetracker.models;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Tasks")
public class Task {
    @Id
    private String id;
    private String taskName;
    private boolean active;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public Task(String id, String taskName, boolean active, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.taskName = taskName;
        this.active = active;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Task(){
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}