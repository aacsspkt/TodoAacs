package com.aacs.todoaacs.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.aacs.todoaacs.model.TodoModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {TodoModel.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TodoDatabase extends RoomDatabase {
    private static final String LOG_TAG = TodoDatabase.class.getSimpleName();
    private static volatile TodoDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TodoDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodoDatabase.class) {
                if (INSTANCE == null) {
                    Log.d(LOG_TAG, "Creating a new database instance");
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodoDatabase.class, "todo")
                            .build();
                }
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return INSTANCE;
    }

    public abstract TodoDao todoDao();
}
