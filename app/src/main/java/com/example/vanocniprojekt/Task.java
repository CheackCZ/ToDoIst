package com.example.vanocniprojekt;

import java.io.Serializable;

import kotlin.jvm.internal.SerializedIr;

public class Task implements Serializable {

    private String title;
    private String description;

    private float priority;

    private String date;
    private String time;

    private boolean completed;


    public Task() {
    }

    public Task(String title, String description, float priority, String date, String time) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.date = date;
        this.time = time;
    }

    public Task(String title, String description, String date, String time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
    }


    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public void setPriority(float priority) {
        this.priority = priority;
    }
    public float getPriority() {
        return priority;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    public boolean isCompleted() {
        return completed;
    }


    @Override
    public String toString() {
        return "Task: Title = " + title + ", Description = " + description + ", Priority = " + priority + ", Date = " + date +", Time = " + time;
    }
}
