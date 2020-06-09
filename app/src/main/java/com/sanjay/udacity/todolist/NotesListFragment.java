package com.sanjay.udacity.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sanjay.udacity.todolist.adapter.NotesAdapter;
import com.sanjay.udacity.todolist.databinding.FragmentNotesListBinding;
import com.sanjay.udacity.todolist.db.NotesEntity;
import com.sanjay.udacity.todolist.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotesListFragment extends Fragment implements NotesAdapter.deleteNote{
    private FragmentNotesListBinding binding;
    private NotesAdapter notesAdapter;
    private List<NotesEntity> notesItems = new ArrayList<>();
    private ViewModel notesViewModel;

    public NotesListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesViewModel = new ViewModelProvider(this).get(ViewModel.class);
        notesViewModel.getAllNotes().observe(this, notesEntities -> notesAdapter.setNotesItems(notesEntities));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotesListBinding.inflate(inflater,container,false);
        notesAdapter = new NotesAdapter(getContext(),NotesListFragment.this);
        binding.notesListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.notesListView.setAdapter(notesAdapter);
        notesAdapter.setNotesItems(notesItems);
        return binding.getRoot();
    }


    @Override
    public void onDeleteClicked(int id) {
        notesViewModel.deleteNotesById(id);
    }
}
