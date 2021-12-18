package com.aacs.todoaacs.ui.todolist;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.aacs.todoaacs.model.TodoModel;
import com.aacs.todoaacs.model.TodoStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TodoListViewModel extends AndroidViewModel {
    private final MutableLiveData<ArrayList<TodoModel>> todosLiveData;
    private final ArrayList<TodoModel> todos;

    public TodoListViewModel(@NonNull Application application) {
        super(application);
        todos = new ArrayList<>();
        todosLiveData = new MutableLiveData<>();
        loadTodos();
    }


    public MutableLiveData<ArrayList<TodoModel>> getTodosLiveData() {
        return todosLiveData;
    }

    private void loadTodos() {
        // todo add repository later
        populateTodos();
        todosLiveData.setValue(todos);
    }

    private void populateTodos() {
        Date date = null;
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
            date = spf.parse("2021-12-19");

        } catch (Exception e) {
            e.printStackTrace();
        }
        todos.add(new TodoModel(1, "Wakeup", date, TodoStatus.NO));
        todos.add(new TodoModel(1, "Running", date, TodoStatus.NO));
        todos.add(new TodoModel(1, "Coffee", date, TodoStatus.NO));
    }
}