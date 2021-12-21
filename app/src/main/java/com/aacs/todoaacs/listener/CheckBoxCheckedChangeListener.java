package com.aacs.todoaacs.listener;

import android.view.View;
import android.widget.TextView;

import com.aacs.todoaacs.model.TodoModel;

public interface CheckBoxCheckedChangeListener {
    void onCheckedChanged(View view, boolean b, TodoModel todoModel, TextView textView);
}
