package com.sanjay.udacity.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sanjay.udacity.todolist.databinding.ActivityNotesOverviewBinding;
import com.sanjay.udacity.todolist.db.NotesEntity;
import com.sanjay.udacity.todolist.viewmodel.ViewModel;

import java.util.List;
import java.util.Objects;

public class NotesOverview extends AppCompatActivity {

    private ActivityNotesOverviewBinding binding;
    final String TAG = NotesOverview.class.getSimpleName();
    private ViewModel notesViewModel;
    private FirebaseAnalytics firebaseAnalytics;
    private boolean isChanged = false;
    private int id;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotesOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (!(intent != null && intent.getStringExtra("action") != null &&
                intent.getIntExtra("id", -1) != -1)) {
            closeForError();
        }else{
            action = intent.getStringExtra("action");
            id = intent.getIntExtra("id", -1);
        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        if (savedInstanceState != null && savedInstanceState.containsKey("isChanged")) {
            isChanged = savedInstanceState.getBoolean("isChanged");
            binding.titleNotes.setText(savedInstanceState.getString("title_notes"));
            binding.bodyNotes.setText(savedInstanceState.getString("body_notes"));
        }
        setSupportActionBar(binding.toolbar);
        setUpListeners();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        notesViewModel = new ViewModelProvider(this).get(ViewModel.class);
        notesViewModel.getNotesById(id).observe(this, this::setUpTextFields);

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
                Log.d(TAG,"Adfailed to load: " + i);
            }
        });
    }
    private void firebaseAnalyticsLogEvent(String i, String adding_notes) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,i);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,adding_notes);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("isChanged",isChanged);
        outState.putString("title_notes",binding.titleNotes.getText().toString());
        outState.putString("body_notes",binding.bodyNotes.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void setUpListeners() {
        binding.titleNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged= true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.bodyNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpTextFields(List<NotesEntity> notesEntities) {

        if (notesEntities.size() < 1) {
            return;
        }

        if (!isChanged) {
            NotesEntity notesEntity = notesEntities.get(0);
            binding.titleNotes.setText(notesEntity.getTitle());
            binding.bodyNotes.setText(notesEntity.getNotes());
        }

    }

    private void closeForError() {
        finish();
        Toast.makeText(this, "Failed to open!!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.save:
                save(id, binding.titleNotes.getText().toString(), binding.bodyNotes.getText().toString(), action);
                break;

            case R.id.discard:

            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void save(int id, String titleNotes, String bodyNotes, String action) {
        if (action.equals("add")) {
            firebaseAnalyticsLogEvent("0", "Adding Notes");
            notesViewModel.insert(new NotesEntity(titleNotes, bodyNotes));
        }else{
            firebaseAnalyticsLogEvent("0", "Updating Notes");
            notesViewModel.update(new NotesEntity(id, titleNotes, bodyNotes));
        }
        Toast.makeText(this, "Saved...", Toast.LENGTH_SHORT).show();
        finish();
    }
}