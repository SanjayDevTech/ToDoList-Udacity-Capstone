package com.sanjay.udacity.todolist.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {NotesEntity.class, TaskEntity.class, ListEntity.class}, version = 1, exportSchema = false)
public abstract class AllDatabases extends RoomDatabase {
    public abstract NotesDao notesDao();
    public abstract TaskDao taskDao();
    public abstract ListDao listDao();
    private static volatile AllDatabases INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AllDatabases getInstance(final Context context){
        if (INSTANCE == null) {
            synchronized (AllDatabases.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AllDatabases.class, "db_list")
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                NotesDao notesDao = INSTANCE.notesDao();
                TaskDao taskDao = INSTANCE.taskDao();
                ListDao listDao = INSTANCE.listDao();
                notesDao.deleteAll();
                taskDao.deleteAll();
                listDao.deleteAllListsById(1);

                notesDao.insert(new NotesEntity("Intro to Notes", "Here you can edit or add your notes"));
                taskDao.insert(new TaskEntity("Today Task"));
                listDao.insert(new ListEntity("Wake", 1));
                listDao.insert(new ListEntity("Eat", 1));
                listDao.insert(new ListEntity("Code", 1));
                listDao.insert(new ListEntity("Sleep", 1));

            });
        }
    };
}
