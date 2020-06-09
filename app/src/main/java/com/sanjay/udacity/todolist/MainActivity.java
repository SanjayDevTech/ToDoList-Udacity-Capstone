package com.sanjay.udacity.todolist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.sanjay.udacity.todolist.adapter.ViewPagerAdapter;
import com.sanjay.udacity.todolist.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private TasksListFragment tasksListFragment;
    private NotesListFragment notesListFragment;
    private FirebaseAnalytics firebaseAnalytics;
    private int tabState;
    int[] colorIntArray = {R.color.notesTaking, R.color.tasksTaking};
    int[] iconIntArray = {R.drawable.ic_baseline_create_24, R.drawable.ic_baseline_add_24};

    public interface CreateListenerTask {
        void onTaskCreate(String title);
    }
    private CreateListenerTask createListenerTask;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        tabState = 0;
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        notesListFragment = new NotesListFragment();
        tasksListFragment = new TasksListFragment(getApplication());
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        fragments.add(notesListFragment);
        fragments.add(tasksListFragment);
        strings.add("Notes");
        strings.add("Tasks");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                fragments, 2, strings);
        binding.viewPager.setAdapter(viewPagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabState = tab.getPosition();
                binding.viewPager.setCurrentItem(tab.getPosition());
                animateFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.fab.setOnClickListener(v -> {
            switch (tabState) {
                case 0:
                    Intent intent = new Intent();
                    intent.putExtra("action", "add");
                    intent.putExtra("id", 0);
                    intent.setClass(this, NotesOverview.class);
                    startActivity(intent);
                    break;

                case 1:
                    alertForNewTask();
                    break;
            }
        });

    }

    private void firebaseAnalyticsLogEvent(String i, String adding_notes) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID,i);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME,adding_notes);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

    private void alertForNewTask() {
        createListenerTask = tasksListFragment;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Add new Task");
        final EditText inputTaskName = new EditText(MainActivity.this);
        inputTaskName.setHint("Enter task name here");
        inputTaskName.setInputType(InputType.TYPE_CLASS_TEXT);
        inputTaskName.setMaxLines(1);
        inputTaskName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        inputTaskName.setLayoutParams(layoutParams);
        builder.setView(inputTaskName);
        builder.setPositiveButton("Add New", (dialog, which) -> {
            firebaseAnalyticsLogEvent("1","adding tasks");
            createListenerTask.onTaskCreate(inputTaskName.getText().toString().trim());
            dialog.cancel();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    protected void animateFab(final int position) {
        binding.fab.clearAnimation();

        ScaleAnimation shrink = new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.fab.setBackgroundTintList(getResources().getColorStateList(colorIntArray[position]));
                binding.fab.setImageDrawable(getResources().getDrawable(iconIntArray[position]));

                ScaleAnimation expand = new ScaleAnimation(0.2f, 1f, 0.2f, 1f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);
                expand.setInterpolator(new AccelerateInterpolator());
                binding.fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        binding.fab.startAnimation(shrink);
    }

}