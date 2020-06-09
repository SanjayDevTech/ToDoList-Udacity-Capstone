package com.sanjay.udacity.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.sanjay.udacity.todolist.databinding.ListNotesBinding;
import com.sanjay.udacity.todolist.db.ListEntity;

import java.util.List;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.MyViewHolder>{

    private Context context;
    private List<ListEntity> listItems;

    public interface deleteList{
        void onListDeleted(int id, int taskId);
    }
    private deleteList deleteLists;

    public ListsAdapter(Context context, deleteList deleteLists) {
        this.context = context;
        this.deleteLists = deleteLists;
    }

    public void setListItems(List<ListEntity> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ListNotesBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ListEntity listEntity = listItems.get(position);
        holder.setTitle(listEntity.getTitle());
        holder.binding.base.setOnLongClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Do you delete this list?");
            builder.setPositiveButton("Delete now", (dialog, which) -> {
                deleteLists.onListDeleted(listEntity.getId(), listEntity.getTaskId());
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
        return listItems.size();
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
