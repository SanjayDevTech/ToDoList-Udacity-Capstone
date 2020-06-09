package com.sanjay.udacity.todolist.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.sanjay.udacity.todolist.NotesOverview;
import com.sanjay.udacity.todolist.databinding.ListNotesBinding;
import com.sanjay.udacity.todolist.db.NotesEntity;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder>{

    private Context context;
    private List<NotesEntity> notesItems;
    public interface deleteNote{
        void onDeleteClicked(int Id);
    }
    private deleteNote deleteNotes;

    public NotesAdapter(Context context, deleteNote deleteNotes) {
        this.context = context;
        this.deleteNotes = deleteNotes;
    }
    public void setNotesItems(List<NotesEntity> notesItems) {
        this.notesItems = notesItems;
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
        NotesEntity notesItem = notesItems.get(position);
        holder.setTitle(notesItem.getTitle());
        holder.binding.base.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("action", "edit");
            intent.putExtra("id", notesItem.getId());
            intent.setClass(context, NotesOverview.class);
            context.startActivity(intent);
        });
        holder.binding.base.setOnLongClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Do you delete this note?");
            builder.setPositiveButton("Delete now", (dialog, which) -> {
                deleteNotes.onDeleteClicked(notesItem.getId());
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
        return notesItems.size();
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
