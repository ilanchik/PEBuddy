package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.pebuddy.R;

import java.util.ArrayList;

public class RecyclerResultsAdapter extends RecyclerView.Adapter<RecyclerResultsAdapter.
        ViewHolder> {

    private ArrayList<String> weekdays = new ArrayList<>();
    private ArrayList<Boolean> completed = new ArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemWeekday;
        TextView itemCompleted;
        TextView itemDate;

        ViewHolder(View itemView) {
            super(itemView);
            itemWeekday = itemView.findViewById(R.id.txt_weekd);
            itemCompleted = itemView.findViewById(R.id.txt_completed);
            itemDate = itemView.findViewById(R.id.txt_date);
        }
    }

    public RecyclerResultsAdapter(ArrayList<String> weekdays,
                                  ArrayList<Boolean> completed, ArrayList<String> dates) {
        this.weekdays = weekdays;
        this.completed = completed;
        this.dates = dates;
    }

    @NonNull
    @Override
    public RecyclerResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_results_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerResultsAdapter.ViewHolder holder, int position) {
        holder.itemWeekday.setText(weekdays.get(position));
        holder.itemCompleted.setText(String.valueOf(completed.get(position)));
        holder.itemDate.setText(dates.get(position));

        if (completed.get(position) == false) {
            holder.itemView.setBackgroundColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return weekdays.size();
    }
}
