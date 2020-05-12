package com.example.jugal.firbasesetup;

import java.util.Date;

public class TodoItem {
    String id;
    String task;
    String priority;
    int position;
    Date date;
    int completed;

    public TodoItem(String task, String priority, int position,String id, int completed) {
        this.task = task;
        this.priority = priority;
        this.position = position;
        this.date = new Date();
        this.id= id;
        this.completed =completed;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "id='" + id + '\'' +
                ", task='" + task + '\'' +
                ", priority='" + priority + '\'' +
                ", position=" + position +
                ", date=" + date +
                ", completed=" + completed +
                '}';
    }

    public int getPosition() {
        return position;
    }

    public String getTask() {
        return task;
    }

    public String getPriority() {
        return priority;
    }

    public Date getDate() {
        return date;
    }

    public TodoItem() {
    }
}
