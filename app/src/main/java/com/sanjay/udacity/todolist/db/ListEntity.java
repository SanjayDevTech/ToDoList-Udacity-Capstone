package com.sanjay.udacity.todolist.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "lists")
public class ListEntity {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "task_id")
    private int taskId;

    @Ignore
    public ListEntity(int id, String title, int taskId) {
        this.id = id;
        this.title = title;
        this.taskId = taskId;
    }

    public ListEntity(String title, int taskId) {
        this.title = title;
        this.taskId = taskId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getTaskId() {
        return taskId;
    }
}
