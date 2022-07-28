package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.pebuddy.R;

public class RecyclerWeekAdapter extends RecyclerView.Adapter<RecyclerWeekAdapter.ViewHolder> {

    private final String WEEKDAYS[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
                                        "Saturday", "Sunday"};
    private AppCompatActivity activity;
    private Fragment fragment;

    public RecyclerWeekAdapter() {}

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView weekday;

        ViewHolder(View view) {
            super(view);
            weekday = view.findViewById(R.id.txt_weekday);
            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                activity = (AppCompatActivity) view.getContext();
                switch(position) {
                    case 0:
                        fragment = new WorkoutFragment("monday");
                        break;
                    case 1:
                        fragment = new WorkoutFragment("tuesday");
                        break;
                    case 2:
                        fragment = new WorkoutFragment("wednesday");
                        break;
                    case 3:
                        fragment = new WorkoutFragment("thursday");
                        break;
                    case 4:
                        fragment = new WorkoutFragment("friday");
                        break;
                    case 5:
                        fragment = new WorkoutFragment("saturday");
                        break;
                    case 6:
                        fragment = new WorkoutFragment("sunday");
                        break;
                    default:
                        break;
                }

                activity.getSupportFragmentManager().popBackStack();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_week_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.weekday.setText(WEEKDAYS[position]);
    }

    @Override
    public int getItemCount() {
        return WEEKDAYS.length;
    }
}
