package com.aacs.todoaacs.ui.todo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aacs.todoaacs.model.TodoModel;
import com.aacs.todoaacs.model.TodoStatus;
import com.aacs.todoaacs.repository.TodoRepository;
import com.aacs.todoaacs.util.DateUtility;

import java.util.Date;

public class TodoFragmentViewModel extends AndroidViewModel {
    private final TodoRepository repository;
    private MutableLiveData<TodoModel> todo;


    public TodoFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
        todo = new MutableLiveData<>();
    }

    public MutableLiveData<TodoModel> getTodoLiveData() {
        if (todo == null) {
            todo = new MutableLiveData<>();
        }
        return todo;
    }

    public void addTodo(TodoModel todo) {
        repository.addTodo(todo);
    }

    public void updateTodo(TodoModel todo){
        repository.updateTodo(todo);
    }

    public LiveData<TodoModel> getTodoById(int uid) {
        return repository.getTodoByUid(uid);
    }
}


