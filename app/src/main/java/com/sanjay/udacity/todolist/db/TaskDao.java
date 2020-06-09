package com.sanjay.udacity.todolist.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TaskEntity taskEntity);

    @Update
    void update(TaskEntity taskEntity);

    @Delete
    void delete(TaskEntity taskEntity);

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    LiveData<List<TaskEntity>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE id=:id LIMIT 1")
    LiveData<List<TaskEntity>> getTaskById(int id);

    @Query("DELETE FROM tasks")
    void deleteAll();

    @Query("DELETE FROM tasks WHERE id = :id")
    void deleteTasksById(int id);

}
