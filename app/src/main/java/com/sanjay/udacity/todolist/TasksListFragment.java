package com.sanjay.udacity.todolist;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sanjay.udacity.todolist.adapter.TasksAdapter;
import com.sanjay.udacity.todolist.databinding.FragmentTasksListBinding;

import com.sanjay.udacity.todolist.db.TaskEntity;
import com.sanjay.udacity.todolist.repository.Repository;
import com.sanjay.udacity.todolist.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TasksListFragment extends Fragment implements TasksAdapter.deleteTask, MainActivity.CreateListenerTask {

    private FragmentTasksListBinding binding;
    private TasksAdapter tasksAdapter;
    private List<TaskEntity> taskItems = new ArrayList<>();
    private ViewModel viewModel;
    private Context context;

    public TasksListFragment(Application context) {
        this.context = context;
    }

    public TasksListFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("onCreate", "Hrllo");
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllTasks().observe(this, taskEntities -> tasksAdapter.setTaskItems(taskEntities));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        viewModel.getAllLists().observe(this, listEntities -> Log.d("Whole List","List size: "+listEntities.size()));
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentTasksListBinding.inflate(inflater, container, false);
        tasksAdapter = new TasksAdapter(getContext(), TasksListFragment.this);
        binding.tasksListView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.tasksListView.setAdapter(tasksAdapter);
        tasksAdapter.setTaskItems(taskItems);
        return binding.getRoot();
    }

    @Override
    public void onDeleteTaskClicked(int id) {
        viewModel.deleteTasksById(id);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("activtycreated", "Yes");
    }

    @Override
    public void onTaskCreate(String title) {
        new Repository((Application) context).insert(new TaskEntity(title));
    }
}
