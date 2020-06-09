package com.sanjay.udacity.todolist.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;


import com.sanjay.udacity.todolist.db.AllDatabases;
import com.sanjay.udacity.todolist.db.ListDao;
import com.sanjay.udacity.todolist.db.ListEntity;
import com.sanjay.udacity.todolist.db.NotesDao;
import com.sanjay.udacity.todolist.db.NotesEntity;
import com.sanjay.udacity.todolist.db.TaskDao;
import com.sanjay.udacity.todolist.db.TaskEntity;

import java.util.List;

public class Repository {

    private NotesDao notesDao;
    private TaskDao taskDao;
    private ListDao listDao;

    public Repository(Application application) {
        AllDatabases db = AllDatabases.getInstance(application);
        notesDao = db.notesDao();
        taskDao = db.taskDao();
        listDao = db.listDao();
    }

    // Fetch all notes
    public LiveData<List<NotesEntity>> getAllNotes() {
        return notesDao.getAllNotes();
    }

    // Fetch all tasks
    public LiveData<List<TaskEntity>> getAllTasks() {
        return taskDao.getAllTasks();
    }

    // Fetch all lists
    public LiveData<List<ListEntity>> getAllLists(){
        return listDao.getAllLists();
    }

    // Fetch all lists by task id
    public LiveData<List<ListEntity>> getAllListsByTaskId(int taskId) {
        return listDao.getAllListsByTaskId(taskId);
    }

    // Fetch a note by id
    public LiveData<List<NotesEntity>> getNotesById(int id) {
       return notesDao.getNotesById(id);
    }

    // Fetch a task by id
    public LiveData<List<TaskEntity>> getTasksById(int id) {
        return taskDao.getTaskById(id);
    }

    // Fetch a list by id and task id
    public LiveData<List<ListEntity>> getListsByIdAndTaskId(int id, int taskId) {
        return listDao.getListsByIdAndTaskId(id, taskId);
    }

    // Insert a note
    public void insert(NotesEntity notesEntity) {
        AllDatabases.databaseWriteExecutor.execute(() -> notesDao.insert(notesEntity));
    }

    // Insert a task
    public void insert(TaskEntity taskEntity) {
        AllDatabases.databaseWriteExecutor.execute(() -> taskDao.insert(taskEntity));
    }

    // Insert a list by task id
    public void insert(ListEntity listEntity) {
        AllDatabases.databaseWriteExecutor.execute(() -> listDao.insert(listEntity));
    }

    // Update a note
    public void update(NotesEntity notesEntity) {
        AllDatabases.databaseWriteExecutor.execute(() -> notesDao.update(notesEntity));
    }

    // Update a task
    public void update(TaskEntity taskEntity) {
        AllDatabases.databaseWriteExecutor.execute(() -> taskDao.update(taskEntity));
    }

    // Update a list by task id
    public void update(ListEntity listEntity) {
        AllDatabases.databaseWriteExecutor.execute(() -> listDao.update(listEntity));
    }

    // Delete all notes
    public void delete(NotesEntity notesEntity) {
        AllDatabases.databaseWriteExecutor.execute(() -> notesDao.delete(notesEntity));
    }

    // Delete all tasks
    public void delete(TaskEntity taskEntity) {
        AllDatabases.databaseWriteExecutor.execute(() -> taskDao.delete(taskEntity));
    }

    // Delete all lists by task id
    public void deleteAllListsByTaskId(int taskId) {
        AllDatabases.databaseWriteExecutor.execute(() -> listDao.deleteAllListsById(taskId));
    }

    // Delete a note by id
    public void deleteNotesById(int id) {
        AllDatabases.databaseWriteExecutor.execute(() -> notesDao.deleteNotesById(id));
    }

    // Delete a task by id and all its lists
    public void deleteTasksById(int id) {
        AllDatabases.databaseWriteExecutor.execute(() -> {
            taskDao.deleteTasksById(id);
            listDao.deleteAllListsById(id);
        });
    }

    // Delete a list by task id and id
    public void deleteListsByIdAndTaskId(int id, int taskId) {
        AllDatabases.databaseWriteExecutor.execute(() -> listDao.deleteListById(id, taskId));
    }
}
