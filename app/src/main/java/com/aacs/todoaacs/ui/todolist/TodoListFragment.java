package com.aacs.todoaacs.ui.todolist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aacs.todoaacs.adapter.TodoListAdapter;
import com.aacs.todoaacs.databinding.TodoListFragmentBinding;
import com.aacs.todoaacs.listener.DeleteButtonClickListener;
import com.aacs.todoaacs.listener.FragmentChangeListener;
import com.aacs.todoaacs.listener.RecyclerViewItemClickListener;
import com.aacs.todoaacs.model.TodoModel;
import com.aacs.todoaacs.ui.SharedViewModel;
import com.aacs.todoaacs.ui.todo.TodoFragment;

import java.util.ArrayList;

public class TodoListFragment extends Fragment {
    private final String TAG = getTag();
    private RecyclerViewItemClickListener recyclerViewItemClickListener;
    private DeleteButtonClickListener deleteButtonClickListener;
    private TodoListViewModel todoListViewModel;
    private SharedViewModel sharedViewModel;
    private TodoListAdapter adapter;

    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.w(TAG, "Fragment View Created");

        TodoListFragmentBinding binding = TodoListFragmentBinding.inflate(getLayoutInflater());
        todoListViewModel = new ViewModelProvider(this).get(TodoListViewModel.class);


        setViewModelObserver(getViewLifecycleOwner(), binding);
        binding.buttonCreateNewTodo.setOnClickListener(this::showTodoFragment);

        return binding.getRoot();
    }

    private void setAdapter(ArrayList<TodoModel> todos) {
        adapter = new TodoListAdapter(
                recyclerViewItemClickListener,
                deleteButtonClickListener
        );
        adapter.setTodos(todos);
    }

    private void setViewModelObserver(LifecycleOwner owner, TodoListFragmentBinding binding) {
        final Observer<ArrayList<TodoModel>> todoListObserver = todoModels -> {
            if (todoModels.size() > 0) {
                setRecyclerViewItemClickListener(todoModels);
                setDeleteButtonClickListener();
                setAdapter(todoModels);
                initRecyclerView(binding);
            }
        };

        todoListViewModel.getTodosLiveData().observe(getViewLifecycleOwner(), todoListObserver);
    }

    private void setDeleteButtonClickListener() {
        deleteButtonClickListener = (view, position) -> {
            Log.d(TAG, "Delete Button Clicked.");
            // todo do something to remove todo at this position.
        };
    }

    private void setRecyclerViewItemClickListener(ArrayList<TodoModel> todos) {
        recyclerViewItemClickListener = (view, position) -> {
            Log.d(TAG, "Recycle View Item Clicked.");
            sharedViewModel.select(todos.get(position));
        };
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
    }

    private void initRecyclerView(TodoListFragmentBinding binding) {
        binding.recyclerViewTodoList.setAdapter(adapter);
        binding.recyclerViewTodoList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showTodoFragment(View view) {
        Log.d(TAG, "Floating Action Button Clicked");
        TodoFragment todoFragment = new TodoFragment();
        FragmentChangeListener listener = (FragmentChangeListener) getActivity();
        if (listener != null) {
            listener.replaceFragment(todoFragment);
        } else {
            Log.d(TAG, "Fragment not associated with Activity");
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "TodoListFragment";
    }
}