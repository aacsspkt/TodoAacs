package com.aacs.todoaacs.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.aacs.todoaacs.util.DateUtility;

import java.util.Date;

@Entity(tableName = "todo")
public class TodoModel {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    private String task;
    private Date date;
    @ColumnInfo(name = "status")
    private TodoStatus isCompleted;

    public TodoModel(){}

    @Ignore
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

    @NonNull
    @Override
    public String toString() {
        return "Todo: \n" +
                "Uid: " + this.getUid() + "\n" +
                "Task: " + this.getTask() + "\n" +
                "Date: " + DateUtility.dateToString(DateUtility.DEFAULT_DATE_FORMAT, this.getDate()) + "\n" +
                "Status: " + this.getIsCompleted().name() + "\n";
    }
}
