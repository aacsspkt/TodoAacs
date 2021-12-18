package com.aacs.todoaacs.ui.todolist;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aacs.todoaacs.R;
import com.aacs.todoaacs.listener.FragmentChangeListener;
import com.aacs.todoaacs.ui.todo.TodoFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TodoListFragment extends Fragment {

    private static final String TAG = newInstance().toString();
    private TodoListViewModel todoListViewModel;

    public static TodoListFragment newInstance() {
        return new TodoListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_list_fragment, container, false);

        FloatingActionButton buttonCreateNewTodo = view.findViewById(R.id.buttonCreateNewTodo);

        buttonCreateNewTodo.setOnClickListener(this::showTodoFragment);

        return view;
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