package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.graphics.Color;
import android.util.Log;
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
import java.util.Map;

public class RecyclerNotificationsAdapter extends RecyclerView.Adapter<RecyclerNotificationsAdapter.
        ViewHolder> {

    private ArrayList<String> incomplete_date;
    private ArrayList<Boolean> incomplete_status;
    private ArrayList<String> incomplete_weekday;

    private AppCompatActivity activity;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemStatus;
        TextView itemDate;
        TextView itemDay;

        ViewHolder(View itemView) {
            super(itemView);
            itemStatus = itemView.findViewById(R.id.txt_status);
            itemDate = itemView.findViewById(R.id.txt_date);
            itemDay = itemView.findViewById(R.id.txt_wday);

            itemView.setOnClickListener(v -> {
                activity = (AppCompatActivity) v.getContext();
                WorkoutFragment workoutFragment = new WorkoutFragment(itemDay.getText().toString());
                activity.getSupportFragmentManager().popBackStack();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, workoutFragment).commit();
                Log.i("TAG", itemDay.getText().toString());
            });
        }
    }

    public RecyclerNotificationsAdapter(ArrayList<String> incomplete_date,
                                        ArrayList<Boolean> incomplete_status,
                                        ArrayList<String> incomplete_weekday) {
        this.incomplete_date = incomplete_date;
        this.incomplete_status = incomplete_status;
        this.incomplete_weekday = incomplete_weekday;
    }

    @NonNull
    @Override
    public RecyclerNotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_notifications_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerNotificationsAdapter.ViewHolder holder, int position) {
        holder.itemStatus.setText("INCOMPLETE");
        holder.itemDate.setText(incomplete_date.get(position));
        holder.itemDay.setText(incomplete_weekday.get(position));
    }

    @Override
    public int getItemCount() {
        return incomplete_date.size();
    }
}
