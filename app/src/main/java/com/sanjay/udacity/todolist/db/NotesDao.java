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
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(NotesEntity notesEntity);

    @Update
    void update(NotesEntity notesEntity);

    @Delete
    void delete(NotesEntity notesEntity);

    @Query("SELECT * FROM notes ORDER BY id DESC")
    LiveData<List<NotesEntity>> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :id LIMIT 1")
    LiveData<List<NotesEntity>> getNotesById(int id);

    @Query("DELETE FROM notes")
    void deleteAll();

    @Query("DELETE FROM notes WHERE id = :id")
    void deleteNotesById(int id);
}
