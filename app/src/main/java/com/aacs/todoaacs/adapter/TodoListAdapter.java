package com.aacs.todoaacs.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aacs.todoaacs.databinding.TodoListRowBinding;
import com.aacs.todoaacs.listener.DeleteButtonClickListener;
import com.aacs.todoaacs.listener.RecyclerViewItemClickListener;
import com.aacs.todoaacs.model.TodoModel;
import com.aacs.todoaacs.model.TodoStatus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {
    private final RecyclerViewItemClickListener recyclerViewItemClickListener;
    private final DeleteButtonClickListener deleteButtonClickListener;
    private ArrayList<TodoModel> todos;

    public TodoListAdapter(
            RecyclerViewItemClickListener recyclerViewItemClickListener,
            DeleteButtonClickListener deleteButtonClickListener
    ) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
        this.deleteButtonClickListener = deleteButtonClickListener;
    }

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
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    protected class TodoViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "TodoViewHolder";
        private final TodoListRowBinding binding;

        public TodoViewHolder(@NonNull TodoListRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.buttonDeleteTask.setOnClickListener(this::onDeleteButtonClick);
            binding.getRoot().setOnClickListener(this::onViewClick);
        }

        private void onDeleteButtonClick(View v) {
            deleteButtonClickListener.onButtonClick(v, getAdapterPosition());
        }

        public void setData(TodoModel todo) {
            binding.textViewTask.setText(todo.getTask());
            binding.textViewDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(todo.getDate()));
            binding.checkBoxIsCompleted.setChecked(todo.getIsCompleted().equals(TodoStatus.YES));
        }

        public void onViewClick(View v) {
            recyclerViewItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}

