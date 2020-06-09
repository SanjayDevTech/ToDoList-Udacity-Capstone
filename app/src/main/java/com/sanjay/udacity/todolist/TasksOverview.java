package com.sanjay.udacity.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sanjay.udacity.todolist.adapter.ListsAdapter;
import com.sanjay.udacity.todolist.databinding.ActivityTasksOverviewBinding;
import com.sanjay.udacity.todolist.db.ListEntity;
import com.sanjay.udacity.todolist.db.TaskEntity;
import com.sanjay.udacity.todolist.viewmodel.ViewModel;
import com.sanjay.udacity.todolist.widget.TasksListWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TasksOverview extends AppCompatActivity implements ListsAdapter.deleteList{
    public static StringBuilder LISTS = new StringBuilder("Your lists appear here");
    final String TAG = TasksOverview.class.getSimpleName();
    private ActivityTasksOverviewBinding binding;
    private int id;
    private String title;
    private ViewModel viewModel;
    private ListsAdapter listsAdapter;
    private List<ListEntity> listEntities = new ArrayList<>();
    private FirebaseAnalytics firebaseAnalytics;
    private AppWidgetProvider appWidgetProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTasksOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (!(intent != null && intent.getIntExtra("id", -1) != -1 &&
                intent.getStringExtra("title") != null)) {
            closeForError();
        }else{
            id = intent.getIntExtra("id", -1);
            title = intent.getStringExtra("title");

        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(title);

//        Log.d("Id of task",""+id);
        if (savedInstanceState != null && savedInstanceState.containsKey("listName")) {
            binding.listName.setText(savedInstanceState.getString("listName"));
        }
        listsAdapter = new ListsAdapter(this, this);
        binding.listsListView.setLayoutManager(new LinearLayoutManager(this));
        binding.listsListView.setAdapter(listsAdapter);
        listsAdapter.setListItems(new ArrayList<>());
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllListsByTaskId(id).observe(this, listEntities -> {
            this.listEntities = listEntities;
            listsAdapter.setListItems(listEntities);
        });
        viewModel.getTasksById(id).observe(this, this::setUpTitle);

        binding.addButton.setOnClickListener(v -> {
            String listName = binding.listName.getText().toString().trim();
            if (listName.equals("")) {

            }else {
                viewModel.insert(new ListEntity(listName,id));
                binding.listName.setText("");
            }
        });
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG,"AdLoaded");
            }

            @Override
            public void onAdFailedToLoad(int i) {
                Log.d(TAG,"Ad failed to load: " + i);
            }
        });

    }
    private void firebaseAnalyticsLogEvent(String i, String adding_notes) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,i);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,adding_notes);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    private void setUpTitle(List<TaskEntity> taskEntities) {
        TaskEntity taskEntity = taskEntities.get(0);
        title = taskEntity.getTitle();
        Objects.requireNonNull(getSupportActionBar()).setTitle(taskEntity.getTitle());
    }

    private void closeForError() {
        finish();
        Toast.makeText(this, "Failed to open!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListDeleted(int id, int taskId) {
        viewModel.deleteListsByIdAndTaskId(id, taskId);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("listName",binding.listName.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lists_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.edit_task:
                alertForEditTask();
                break;

            case R.id.delete_all_list:
                firebaseAnalyticsLogEvent("2","Delete all lists");
                viewModel.deleteAllListsByTaskId(id);
                break;

            case R.id.add_to_widget:
                addToWidget(listEntities);
                break;

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addToWidget(List<ListEntity> listEntities) {
        LISTS = new StringBuilder();
        for (int i = 0; i < listEntities.size(); i++) {

            LISTS.append(listEntities.get(i).getTitle());

            if (i < listEntities.size() - 1) {
                LISTS.append("\n\n");
            }
        }
        Intent intent = new Intent(this, TasksListWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = AppWidgetManager.getInstance(this)
                .getAppWidgetIds(new ComponentName(this, TasksListWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
        firebaseAnalyticsLogEvent("3","Added to widget");
        Toast.makeText(this, "Added to widget!!!", Toast.LENGTH_SHORT).show();
    }

    private void alertForEditTask() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Edit Task");
        final EditText inputTaskName = new EditText(TasksOverview.this);
        inputTaskName.setHint("Enter task name here");
        inputTaskName.setInputType(InputType.TYPE_CLASS_TEXT);
        inputTaskName.setMaxLines(1);
        inputTaskName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        inputTaskName.setLayoutParams(layoutParams);
        inputTaskName.setText(title);
        builder.setView(inputTaskName);
        builder.setPositiveButton("Update", (dialog, which) -> {
            String taskName = inputTaskName.getText().toString().trim();
            if (taskName.equals("")) {
                Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
            }else{
                viewModel.update(new TaskEntity(id, inputTaskName.getText().toString()));
            }


        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}