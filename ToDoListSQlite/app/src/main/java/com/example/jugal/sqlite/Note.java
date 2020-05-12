package com.example.jugal.sqlite;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    private long _id;
    private String task,priority,date;
    private int position,status;

    public Note(String task, int status, String priority, String date,int position) {
        this.task = task;
        this.status = status;
        this.priority = priority;
        this.date=date;
        this.position=position;
    }
    public Note(){

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Note{" +
                "_id=" + _id +
                ", task='" + task + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", date='" + date + '\'' +
                ", position=" + position +
                '}';
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


}