package com.aacs.todoaacs.ui.todo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aacs.todoaacs.model.TodoModel;
import com.aacs.todoaacs.model.TodoStatus;
import com.aacs.todoaacs.repository.TodoRepository;

import java.util.Date;

public class TodoFragmentViewModel extends AndroidViewModel {
    private final TodoRepository repository;
    private int uid;
    private MutableLiveData<String> task;
    private MutableLiveData<Date> date;
    private TodoStatus todoStatus;

    public TodoFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public TodoStatus getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(TodoStatus status) {
        this.todoStatus = status;
    }

    public MutableLiveData<String> getTask() {
        if (task == null) task = new MutableLiveData<>();
        return task;
    }

    public MutableLiveData<Date> getDate() {
        if (date == null) date = new MutableLiveData<>();
        return date;
    }

    public void addTodo(TodoModel todo) {
        repository.addTodo(todo);
    }

    public void updateTodo(TodoModel todo) {
        repository.updateTodo(todo);
    }

    public LiveData<TodoModel> getTodoById(int uid) {
        return repository.getTodoByUid(uid);
    }
}


