package com.aacs.todoaacs.listener;

import android.view.View;

import com.aacs.todoaacs.model.TodoModel;

public interface RecyclerViewItemClickListener {
    void onClick(View view, TodoModel todo);
}
