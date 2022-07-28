package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.pebuddy.R;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerWorkoutListAdapter extends RecyclerView.Adapter<
        RecyclerWorkoutListAdapter.ViewHolder> {

    private ArrayList<String> workouts = new ArrayList<>();

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView exercise;

        ViewHolder(View itemView) {
            super(itemView);
            exercise = itemView.findViewById(R.id.txt_exercise);

            itemView.setOnClickListener(v -> {
                v.setBackgroundColor(Color.GRAY);
            });
        }
    }

    public RecyclerWorkoutListAdapter(ArrayList<String> workouts) {
        this.workouts = workouts;
    }

    @NonNull
    @Override
    public RecyclerWorkoutListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_exercise_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerWorkoutListAdapter.ViewHolder holder, int position) {
        holder.exercise.setText(workouts.get(position));
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }
}
