package com.aacs.todoaacs.ui.todolist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aacs.todoaacs.model.TodoModel;
import com.aacs.todoaacs.repository.TodoRepository;

import java.util.List;

public class TodoListFragmentViewModel extends AndroidViewModel {
    private final TodoRepository repository;

    public TodoListFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoRepository(application);
    }

    public LiveData<List<TodoModel>> getTodosLiveData() {
        return repository.getTodos();
    }

    public void updateTodo(TodoModel todo){
        repository.updateTodo(todo);
    }

    public void deleteTodo(TodoModel todo) {
        repository.deleteTodo(todo);
    }

}