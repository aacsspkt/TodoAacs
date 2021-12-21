package com.aacs.todoaacs.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.aacs.todoaacs.model.TodoModel;

import java.util.List;

@Dao
public interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TodoModel todoModel);

    @Delete
    void delete(TodoModel todo);

    @Update
    void update(TodoModel todo);

    @Query("SELECT * FROM todo")
    LiveData<List<TodoModel>> getALL();

    @Query("SELECT * FROM todo WHERE uid = :uid")
    LiveData<TodoModel> getByUid(int uid);
}
