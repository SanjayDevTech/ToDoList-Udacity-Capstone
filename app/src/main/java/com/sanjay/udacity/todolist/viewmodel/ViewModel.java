package com.sanjay.udacity.todolist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sanjay.udacity.todolist.db.ListEntity;
import com.sanjay.udacity.todolist.db.NotesEntity;
import com.sanjay.udacity.todolist.db.TaskEntity;
import com.sanjay.udacity.todolist.repository.Repository;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private Repository repository;

    public ViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }
    // Fetch all notes
    public LiveData<List<NotesEntity>> getAllNotes() {
        return repository.getAllNotes();
    }

    // Fetch all tasks
    public LiveData<List<TaskEntity>> getAllTasks() {
        return repository.getAllTasks();
    }

    // Fetch all lists
    public LiveData<List<ListEntity>> getAllLists(){
        return repository.getAllLists();
    }

    // Fetch all lists by task id
    public LiveData<List<ListEntity>> getAllListsByTaskId(int taskId) {
        return repository.getAllListsByTaskId(taskId);
    }

    // Fetch a note by id
    public LiveData<List<NotesEntity>> getNotesById(int id) {
        return repository.getNotesById(id);
    }

    // Fetch a task by id
    public LiveData<List<TaskEntity>> getTasksById(int id) {
        return repository.getTasksById(id);
    }

    // Fetch a list by id and task id
    public LiveData<List<ListEntity>> getListsByIdAndTaskId(int id, int taskId) {
        return repository.getListsByIdAndTaskId(id, taskId);
    }

    // Insert a note
    public void insert(NotesEntity notesEntity) {
        repository.insert(notesEntity);
    }

    // Insert a task
    public void insert(TaskEntity taskEntity) {
        repository.insert(taskEntity);
    }

    // Insert a list
    public void insert(ListEntity listEntity) {
        repository.insert(listEntity);
    }

    // Update a note
    public void update(NotesEntity notesEntity) {
        repository.update(notesEntity);
    }

    // Update a task
    public void update(TaskEntity taskEntity) {
        repository.update(taskEntity);
    }

    // Update a list
    public void update(ListEntity listEntity) {
        repository.update(listEntity);
    }

    // Delete a note by id
    public void deleteNotesById(int id) {
        repository.deleteNotesById(id);
    }

    // Delete a whole task by id
    public void deleteTasksById(int id){
        repository.deleteTasksById(id);
    }

    // Delete a list by task id and id
    public void deleteListsByIdAndTaskId(int id, int taskId) {
        repository.deleteListsByIdAndTaskId(id, taskId);
    }

    // Delete all lists by task id
    public void deleteAllListsByTaskId(int taskId) {
        repository.deleteAllListsByTaskId(taskId);
    }


}
