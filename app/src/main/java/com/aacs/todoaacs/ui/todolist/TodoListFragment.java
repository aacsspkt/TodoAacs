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
import androidx.lifecycle.LifecycleOwner;
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
        setViewModelObserver(getViewLifecycleOwner());

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
    private void setViewModelObserver(LifecycleOwner owner) {
        todoListFragmentViewModel.getTodosLiveData().observe(owner, todoModels -> {
                    Log.w(TAG, "Todos Arraylist Changed.");
                    adapter.setTodos((ArrayList<TodoModel>) todoModels);
                }
        );
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
            Log.w(TAG, "Todo with uid " + todo.getUid() + " Updated");
        };
    }

    private void setDeleteButtonClickListener() {
        deleteButtonClickListener = (view, todo) -> {
            todoListFragmentViewModel.deleteTodo(todo);
            Log.w(TAG, "Todo with uid " + todo.getUid() + " Deleted");
        };
    }

    private void setRecyclerViewItemClickListener() {
        recyclerViewItemClickListener = this::updateTodo;
    }

    private void updateTodo(View view, TodoModel todo) {
        Log.d(TAG, "Recycle View Item Clicked.");
        Bundle bundle = new Bundle();
        bundle.putInt(TodoFragment.UID, todo.getUid());
        Log.d(TAG,
                "Argument Sent: UID = " + todo.getUid());
        TodoFragment todoFragment = new TodoFragment();
        todoFragment.setArguments(bundle);
        FragmentChangeListener listener = (FragmentChangeListener) getActivity();
        if (listener != null) {
            listener.replaceFragment(todoFragment);
        } else {
            Log.d(TAG, "Fragment not associated with Activity");
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
        Log.d(TAG, "Floating Action Button Clicked");
        FragmentChangeListener listener = (FragmentChangeListener) getActivity();
        if (listener != null) {
            listener.replaceFragment(new TodoFragment());
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