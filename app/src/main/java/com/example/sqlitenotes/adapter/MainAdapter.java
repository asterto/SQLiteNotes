package com.example.sqlitenotes.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlitenotes.EditActivity;
import com.example.sqlitenotes.R;
import com.example.sqlitenotes.db.MyConstants;
import com.example.sqlitenotes.db.MyDbManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MyViewHolder> {
    private final Context context;
    private final List<ListItem> mainArray;


    public MainAdapter(Context context) {
        this.context = context;
        mainArray = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false);
        return new MyViewHolder(view, context, mainArray);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setData(mainArray.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return mainArray.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvTitle;
        private final Context context;
        private final List<ListItem> mainArray;

        public MyViewHolder(@NonNull View itemView, Context context, List<ListItem> mainArray) {
            super(itemView);
            this.context = context;
            this.mainArray = mainArray;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            itemView.setOnClickListener(this);

        }

        public void setData(String title) {
            tvTitle.setText(title);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, EditActivity.class);
            i.putExtra(MyConstants.LIST_ITEM_INTENT, mainArray.get(getAdapterPosition()));
            i.putExtra(MyConstants.EDIT_STATE, false);
            context.startActivity(i);

        }

    }

    public void updateAdapter(List<ListItem> newList) {
        mainArray.clear();
        mainArray.addAll(newList);
        notifyDataSetChanged();
    }

    public void removeItem(int pos, MyDbManager dbManager, RecyclerView rcView) {
        // Show SnackBar
        Snackbar snackbar = Snackbar.make(rcView, "Really?", Snackbar.LENGTH_LONG);
        snackbar.setAction("Yes", v -> {
            Toast toast = Toast.makeText(context, "Removed", Toast.LENGTH_LONG);
            dbManager.delete(mainArray.get(pos).getId());
            mainArray.remove(pos);
            notifyItemRangeChanged(0, mainArray.size());
            notifyItemRemoved(pos);
            toast.show();
        });
        snackbar.show();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mainArray, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mainArray, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }


}