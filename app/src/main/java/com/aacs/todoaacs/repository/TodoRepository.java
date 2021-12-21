package com.aacs.todoaacs.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.aacs.todoaacs.db.TodoDao;
import com.aacs.todoaacs.db.TodoDatabase;
import com.aacs.todoaacs.model.TodoModel;

import java.util.List;

public class TodoRepository {
    private final TodoDao todoDao;

    public TodoRepository(Application application) {
        TodoDatabase db = TodoDatabase.getDatabase(application);
        todoDao = db.todoDao();
    }

    public void addTodo(TodoModel todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> todoDao.insert(todo));
    }

    public void updateTodo(TodoModel todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> todoDao.update(todo));
    }

    public void deleteTodo(TodoModel todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> todoDao.delete(todo));
    }

    public LiveData<TodoModel> getTodoByUid(int uid) {
        return todoDao.getByUid(uid);
    }

    public LiveData<List<TodoModel>> getTodos() {
        return todoDao.getALL();
    }
}
