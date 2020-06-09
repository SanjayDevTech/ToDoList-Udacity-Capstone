package com.sanjay.udacity.todolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.sanjay.udacity.todolist.TasksOverview;
import com.sanjay.udacity.todolist.databinding.ListNotesBinding;
import com.sanjay.udacity.todolist.db.TaskEntity;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder>{
    private Context context;
    private List<TaskEntity> taskItems;
    public interface deleteTask{
        void onDeleteTaskClicked(int Id);
    }
    private deleteTask deleteTask;

    public TasksAdapter(Context context, deleteTask deleteTask) {
        this.context = context;
        this.deleteTask= deleteTask;
    }

    public void setTaskItems(List<TaskEntity> taskItems) {
        this.taskItems = taskItems;
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ListNotesBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TaskEntity taskEntity = taskItems.get(position);
        holder.setTitle(taskEntity.getTitle());
        holder.binding.base.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("id", taskEntity.getId());
            intent.putExtra("title", taskEntity.getTitle());
            intent.setClass(context, TasksOverview.class);
            context.startActivity(intent);
        });
        holder.binding.base.setOnLongClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Do you delete this task?");
            builder.setPositiveButton("Delete now", (dialog, which) -> {
                deleteTask.onDeleteTaskClicked(taskEntity.getId());
            });
            builder.setNegativeButton("Nope", (dialog, which) -> {
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return true;
        });


    }

    @Override
    public int getItemCount() {
        return taskItems.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        private ListNotesBinding binding;

        public MyViewHolder(@NonNull ListNotesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setTitle(String s) {
            binding.titleNotes.setText(s);
        }

    }
}
