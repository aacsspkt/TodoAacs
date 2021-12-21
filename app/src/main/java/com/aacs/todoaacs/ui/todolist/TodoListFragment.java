package com.aacs.todoaacs.ui.todolist;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aacs.todoaacs.adapter.TodoListAdapter;
import com.aacs.todoaacs.databinding.TodoListFragmentBinding;
import com.aacs.todoaacs.listener.CheckBoxCheckedChangeListener;
import com.aacs.todoaacs.listener.FragmentChangeListener;
import com.aacs.todoaacs.listener.RecyclerViewItemClickListener;
import com.aacs.todoaacs.model.TodoModel;
import com.aacs.todoaacs.model.TodoStatus;
import com.aacs.todoaacs.ui.todo.TodoFragment;

import java.util.ArrayList;
import java.util.List;

public class TodoListFragment extends Fragment {
    public static final String RESUlT = "Result";
    public static final String REQUEST_KEY = "Request_Key";
    private final String TAG = this.toString();
    private RecyclerViewItemClickListener recyclerViewItemClickListener;
    private RecyclerViewItemClickListener deleteButtonClickListener;
    private CheckBoxCheckedChangeListener checkBoxClickListener;
    private TodoListFragmentViewModel todoListFragmentViewModel;
    private TodoListFragmentBinding binding;
    private TodoListAdapter adapter;

    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.w(TAG, "Fragment View Created");

        binding = TodoListFragmentBinding.inflate(getLayoutInflater());
        todoListFragmentViewModel = new ViewModelProvider(this).get(TodoListFragmentViewModel.class);

        checkFragmentResults();
        setRecyclerViewItemClickListener();
        setDeleteButtonClickListener();
        setCheckBoxClickListener();
        initRecyclerView();

        todoListFragmentViewModel.getTodosLiveData()
                .observe(getViewLifecycleOwner(), this::passDataToAdapter);

        binding.buttonCreateNewTodo.setOnClickListener(this::createNewTodo);

        return binding.getRoot();
    }

    private void checkFragmentResults() {
        getParentFragmentManager().setFragmentResultListener(
                REQUEST_KEY,
                this,
                (requestKey, result) -> Toast.makeText(getContext(),
                        result.getString(RESUlT),
                        Toast.LENGTH_SHORT).show()
        );
    }

    @SuppressLint("NotifyDataSetChanged")
    private void passDataToAdapter(List<TodoModel> todos) {
        Log.w(TAG, "ArrayList passed to adapter.");
        adapter.setTodos((ArrayList<TodoModel>) todos);
        adapter.notifyDataSetChanged();
    }

    private void setCheckBoxClickListener() {
        checkBoxClickListener = (view, b, todo, textView) -> {
            if (b) {
                todo.setIsCompleted(TodoStatus.YES);
                textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                todo.setIsCompleted(TodoStatus.NO);
                textView.setPaintFlags(0);
            }
            todoListFragmentViewModel.updateTodo(todo);
        };
    }

    private void setDeleteButtonClickListener() {
        deleteButtonClickListener = (view, todo) -> {
            todoListFragmentViewModel.deleteTodo(todo);
            Toast.makeText(getContext(),
                    "Deleted successfully",
                    Toast.LENGTH_SHORT).show();
        };
    }

    private void setRecyclerViewItemClickListener() {
        recyclerViewItemClickListener = this::updateTodo;
    }

    private void updateTodo(View view, TodoModel todo) {
        FragmentChangeListener listener = (FragmentChangeListener) getActivity();
        if (listener != null) {
            listener.replaceFragment(TodoFragment.newInstance(todo.getUid()));
        } else {
            Log.w(TAG, "Fragment not associated with Activity");
            Toast.makeText(getContext(),
                    "Error occurred while going to new Fragment",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void initRecyclerView() {
        adapter = new TodoListAdapter(
                recyclerViewItemClickListener,
                deleteButtonClickListener,
                checkBoxClickListener
        );
        binding.recyclerViewTodoList.setAdapter(adapter);
        binding.recyclerViewTodoList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void createNewTodo(View view) {
        FragmentChangeListener listener = (FragmentChangeListener) getActivity();
        if (listener != null) {
            listener.replaceFragment(new TodoFragment());
        } else {
            Log.d(TAG, "Fragment not associated with Activity");
            Toast.makeText(getContext(),
                    "Error occurred while going to new Fragment",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "TodoListFragment";
    }
}