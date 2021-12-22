package com.aacs.todoaacs.ui.todolist;

import android.annotation.SuppressLint;
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
    // key for putting result in bundle
    public static final String RESUlT = "Result";
    // key for sending result request
    public static final String REQUEST_KEY = "Request_Key";
    // Tag for logging
    private final String TAG = this.toString();

    //listeners for recycler view
    private RecyclerViewItemClickListener recyclerViewItemClickListener;
    private RecyclerViewItemClickListener deleteButtonClickListener;
    private CheckBoxCheckedChangeListener checkBoxClickListener;

    //viewModel for this fragment
    private TodoListFragmentViewModel todoListFragmentViewModel;
    //view binding for this fragment
    private TodoListFragmentBinding binding;
    //recycler view adapter
    private TodoListAdapter adapter;

    //return new new instance of this fragment
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

        // observe todos live data
        todoListFragmentViewModel.getTodosLiveData()
                .observe(getViewLifecycleOwner(), this::passDataToAdapter);

        // set click listener for floating action button
        binding.buttonCreateNewTodo.setOnClickListener(this::createNewTodo);

        //returning root view in binding
        return binding.getRoot();
    }

    /*
    * set result listener for this fragment
    * */
    private void checkFragmentResults() {
        getParentFragmentManager().setFragmentResultListener(
                REQUEST_KEY,
                this,
                (requestKey, result) -> Toast.makeText(getContext(),
                        result.getString(RESUlT),
                        Toast.LENGTH_SHORT).show()
        );
    }

    /*
    * pass todos arraylist to adapter
    * */
    @SuppressLint("NotifyDataSetChanged")
    private void passDataToAdapter(List<TodoModel> todos) {
        Log.w(TAG, "ArrayList passed to adapter.");
        adapter.setTodos((ArrayList<TodoModel>) todos);
        adapter.notifyDataSetChanged();
    }

    /*
    * set checkbox click listener
    * */
    private void setCheckBoxClickListener() {
        checkBoxClickListener = (view, b, todo) -> {
            if (!b) {
                Log.d(TAG, "Todo=\"" + todo.getTask() + "\" Unchecked");
                todo.setIsCompleted(TodoStatus.NO);
            } else {
                Log.d(TAG, "Todo=\"" + todo.getTask() + "\" Checked");
                todo.setIsCompleted(TodoStatus.YES);
            }
            todoListFragmentViewModel.updateTodo(todo);
        };
    }

    /*
    * set delete button listener
    * */
    private void setDeleteButtonClickListener() {
        deleteButtonClickListener = (view, todo) -> {
            todoListFragmentViewModel.deleteTodo(todo);
            Toast.makeText(getContext(),
                    "Deleted successfully",
                    Toast.LENGTH_SHORT).show();
        };
    }

    /*
    * set recycler view item click listener
    * */
    private void setRecyclerViewItemClickListener() {
        recyclerViewItemClickListener = this::editTodo;
    }

    /*
    * go to TodoFragment with arg for edit
    * */
    private void editTodo(View view, TodoModel todo) {
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


    /*
    * initialize recycler view
    * */
    private void initRecyclerView() {
        adapter = new TodoListAdapter(
                recyclerViewItemClickListener,
                deleteButtonClickListener,
                checkBoxClickListener
        );
        binding.recyclerViewTodoList.setAdapter(adapter);
        binding.recyclerViewTodoList.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    /*
    * go to TodoFragment to create new todo_
    * */
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


    /*
    * override method to return fragment name
    * */
    @NonNull
    @Override
    public String toString() {
        return "TodoListFragment";
    }
}