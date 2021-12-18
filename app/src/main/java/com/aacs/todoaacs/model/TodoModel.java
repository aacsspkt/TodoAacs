package com.aacs.todoaacs.model;

import java.util.Date;

public class TodoModel {
    private int uid;
    private String task;
    private Date date;
    private TodoStatus isCompleted;

    public TodoModel(int uid, String task, Date date, TodoStatus isCompleted) {
        this.uid = uid;
        this.task = task;
        this.date = date;
        this.isCompleted = isCompleted;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public TodoStatus getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(TodoStatus isCompleted) {
        this.isCompleted = isCompleted;
    }

}
