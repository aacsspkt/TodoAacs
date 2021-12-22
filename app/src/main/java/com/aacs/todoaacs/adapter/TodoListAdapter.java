package com.aacs.todoaacs.adapter;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aacs.todoaacs.databinding.TodoListRowBinding;
import com.aacs.todoaacs.listener.CheckBoxCheckedChangeListener;
import com.aacs.todoaacs.listener.RecyclerViewItemClickListener;
import com.aacs.todoaacs.model.TodoModel;
import com.aacs.todoaacs.model.TodoStatus;
import com.aacs.todoaacs.util.DateUtility;

import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {
    private final RecyclerViewItemClickListener recyclerViewItemClickListener;
    private final RecyclerViewItemClickListener deleteButtonClickListener;
    private final CheckBoxCheckedChangeListener checkBoxCheckedChangeListener;
    private ArrayList<TodoModel> todos;

    public TodoListAdapter(
            RecyclerViewItemClickListener recyclerViewItemClickListener,
            RecyclerViewItemClickListener deleteButtonClickListener,
            CheckBoxCheckedChangeListener checkBoxClickListener
    ) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
        this.deleteButtonClickListener = deleteButtonClickListener;
        this.checkBoxCheckedChangeListener = checkBoxClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTodos(ArrayList<TodoModel> todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoViewHolder(TodoListRowBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoModel todo = todos.get(position);
        holder.setData(todo);
        if (todo.getIsCompleted().equals(TodoStatus.YES)) {
            holder.binding.textViewTask.setPaintFlags(
                    holder.binding.textViewTask.getPaintFlags() |
                            Paint.STRIKE_THRU_TEXT_FLAG
            );
        } else {
            holder.binding.textViewTask.setPaintFlags(0);
        }
    }

    @Override
    public int getItemCount() {
        if (todos == null) return 0;
        return todos.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        private final TodoListRowBinding binding;

        public TodoViewHolder(@NonNull TodoListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.buttonDeleteTask.setOnClickListener(this::onDeleteButtonClick);
            binding.getRoot().setOnClickListener(this::onViewClick);
            binding.checkBoxIsCompleted.setOnCheckedChangeListener(this::onCheckBoxClick);
        }

        private void onCheckBoxClick(CompoundButton compoundButton, boolean b) {
            checkBoxCheckedChangeListener.onCheckedChanged(compoundButton, b, todos.get(getAdapterPosition()));
        }

        private void onDeleteButtonClick(View v) {
            deleteButtonClickListener.onClick(v, todos.get(getAdapterPosition()));
        }

        public void setData(TodoModel todo) {
            binding.textViewTask.setText(todo.getTask());
            binding.textViewDate.setText(DateUtility.dateToString(DateUtility.DEFAULT_DATE_FORMAT, todo.getDate()));
            binding.checkBoxIsCompleted.setChecked(todo.getIsCompleted().equals(TodoStatus.YES));
        }

        public void onViewClick(View v) {
            recyclerViewItemClickListener.onClick(v, todos.get(getAdapterPosition()));
        }
    }
}

