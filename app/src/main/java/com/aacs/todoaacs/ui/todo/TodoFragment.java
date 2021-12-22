package com.aacs.todoaacs.ui.todo;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.aacs.todoaacs.databinding.TodoFragmentBinding;
import com.aacs.todoaacs.listener.FragmentChangeListener;
import com.aacs.todoaacs.model.TodoModel;
import com.aacs.todoaacs.model.TodoStatus;
import com.aacs.todoaacs.ui.todolist.TodoListFragment;
import com.aacs.todoaacs.util.DateUtility;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class TodoFragment extends Fragment {
    //key for sending arg
    public static final String ARG_UID = "uid";
    //tag for logging
    private final String TAG = this.toString();
    //view binding of this fragment
    private TodoFragmentBinding binding;
    //viewModel of this fragment
    private TodoFragmentViewModel todoFragmentViewModel;

    //return new fragment instance with uid as args
    public static TodoFragment newInstance(int uid) {
        TodoFragment fragment = new TodoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = TodoFragmentBinding.inflate(getLayoutInflater());
        todoFragmentViewModel = new ViewModelProvider(this).get(TodoFragmentViewModel.class);

        //uid in ViewModel is 0 means no args has been passed yet
        if (todoFragmentViewModel.getUid() == 0) checkArguments();

        //if there is any savedInstanceState retrieve it
        if (savedInstanceState != null) retrieveSavedInstanceState(savedInstanceState);

        /*
        * observe values that update ui
        * */
        todoFragmentViewModel.getTask().observe(getViewLifecycleOwner(), this::setEditTextTask);
        todoFragmentViewModel.getDate().observe(getViewLifecycleOwner(), this::setTextViewDate);

        /*
        * setting listeners in clickable views
        * */
        binding.textViewDatePicker.setOnClickListener(this::showDatePickerDialog);
        binding.buttonAddEditTask.setOnClickListener(this::saveTodo);

        //returning outermost view in the binding
        return binding.getRoot();
    }

    /*
    * do as method name applies
    * */
    private void checkArguments() {
        // if argument is not null then retrieve int arg and
        // get todo_object for editing and updating
        if (getArguments() != null) {
            int arg_uid = getArguments().getInt(ARG_UID);
            getSetTodoFromArgUid(arg_uid);
        }
        // else set initial value in ViewModel
        else {
            setDefaultDataInViewModel();
        }
    }

    /*
    * set Initial value for creating new _todo
    * */
    private void setDefaultDataInViewModel() {
        todoFragmentViewModel.getTask().setValue("");
        todoFragmentViewModel.getDate().setValue(DateUtility.today(DateUtility.DEFAULT_DATE_FORMAT));
        todoFragmentViewModel.setTodoStatus(TodoStatus.NO);
    }

    /*
    * retrieve todo_object from uid passed and set data in viewModel
    * */
    private void getSetTodoFromArgUid(int arg_uid) {
        todoFragmentViewModel.getTodoById(arg_uid).observe(getViewLifecycleOwner(), todoModel -> {
            todoFragmentViewModel.setUid(todoModel.getUid());
            todoFragmentViewModel.getTask().setValue(todoModel.getTask());
            todoFragmentViewModel.getDate().setValue(todoModel.getDate());
            todoFragmentViewModel.setTodoStatus(todoModel.getIsCompleted());
        });
    }

    /*
    * retrieve data from savedInstanceState and set them in viewModel
    * */
    private void retrieveSavedInstanceState(@NonNull Bundle savedInstanceState) {
        Log.w(TAG, "Retrieving SavedInstanceState.");
        todoFragmentViewModel.getTask().setValue(savedInstanceState.getString("Task"));
        try {
            todoFragmentViewModel.getDate().setValue(
                    DateUtility.stringToDate(
                            DateUtility.DEFAULT_DATE_FORMAT,
                            savedInstanceState.getString("Date"))
            );
        } catch (ParseException e) {
            e.printStackTrace();

        }
    }

    /*
    * show date picker dialog to set date
    * */
    private void showDatePickerDialog(View view) {
        Calendar calender = Calendar.getInstance();
        int year = calender.get((Calendar.YEAR));
        int month = calender.get(Calendar.MONTH);
        int day = calender.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog.OnDateSetListener dateSetListener = (view1, year1, month1, dayOfMonth) -> {
            month1 = month1 + 1;
            String date = year1 + "-" + month1 + "-" + dayOfMonth;
            binding.textViewDatePicker.setText(date);
        };
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateSetListener,
                year,
                month,
                day
        );
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    /*
    * set text with passed value in editTextTodoTask
    * */
    private void setEditTextTask(String task){
        binding.editTextTodoTask.setText(task);
    }

    /*
    * set text with passed value in textViewDatePicker
    * */
    private void setTextViewDate(Date date) {
        binding.textViewDatePicker.setText(DateUtility.dateToString(DateUtility.DEFAULT_DATE_FORMAT, date));
    }

    /*
    * save new todo_ of update existing todo_
    * */
    private void saveTodo(View view) {
        String task = binding.editTextTodoTask.getText().toString();
        if (task.isEmpty()) {
            binding.editTextTodoTask.setError("Please enter any todo task!");
            binding.editTextTodoTask.requestFocus();
            return;
        }
        Date date = new Date();
        try {
            date = DateUtility.stringToDate(
                    DateUtility.DEFAULT_DATE_FORMAT,
                    binding.textViewDatePicker.getText().toString()
            );
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error in Parsing Date", Toast.LENGTH_SHORT).show();
        }
        int uid = todoFragmentViewModel.getUid();
        TodoStatus todoStatus = todoFragmentViewModel.getTodoStatus();
        // uid > 0 means uid is obtained from existing todo_object and is set
        if (uid > 0) {
            TodoModel todo = new TodoModel(uid, task, date, todoStatus);
            todoFragmentViewModel.updateTodo(todo);
            Log.w(TAG, "Todo with Uid:" + todo.getUid() + " Updated");
            showTodoListFragment("Updated Successfully");
        }
        // uid == 0 means no value is passed from other fragments hence, create new todo_object
        else {
            TodoModel newTodo = new TodoModel();
            newTodo.setTask(task);
            newTodo.setDate(date);
            newTodo.setIsCompleted(TodoStatus.NO);
            todoFragmentViewModel.addTodo(newTodo);
            Log.w(TAG, "Todo Saved");
            showTodoListFragment("Saved Successfully");
        }
    }

    /*
    * go back to TodoListFragment with result request
    * */
    private void showTodoListFragment(String requestValue) {
        Bundle bundle = new Bundle();
        bundle.putString(TodoListFragment.RESUlT, requestValue);
        getParentFragmentManager().setFragmentResult(TodoListFragment.REQUEST_KEY, bundle);
        FragmentChangeListener listener = (FragmentChangeListener) getActivity();
        if (listener != null) {
            listener.replaceFragment(TodoListFragment.newInstance());
        } else {
            Log.d(TAG, "Fragment not associated with Activity");
            Toast.makeText(getContext(),
                    "Error occurred while going to new Fragment",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * save state for retrieving later
    * */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Task", binding.editTextTodoTask.getText().toString());
        outState.putString("Date", binding.textViewDatePicker.getText().toString());
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