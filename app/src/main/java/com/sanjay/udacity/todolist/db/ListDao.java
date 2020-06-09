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
public interface ListDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ListEntity listEntity);

    @Update
    void update(ListEntity listEntity);

    @Delete
    void delete(ListEntity listEntity);

    @Query("SELECT * FROM lists")
    LiveData<List<ListEntity>> getAllLists();

    @Query("SELECT * FROM lists WHERE task_id=:taskId")
    LiveData<List<ListEntity>> getAllListsByTaskId(int taskId);

    @Query("SELECT * FROM lists WHERE id=:id AND task_id=:taskId")
    LiveData<List<ListEntity>> getListsByIdAndTaskId(int id, int taskId);

    @Query("DELETE FROM lists WHERE task_id=:taskId")
    void deleteAllListsById(int taskId);

    @Query("DELETE FROM lists WHERE id=:id AND task_id=:taskId")
    void deleteListById(int id, int taskId);


}
