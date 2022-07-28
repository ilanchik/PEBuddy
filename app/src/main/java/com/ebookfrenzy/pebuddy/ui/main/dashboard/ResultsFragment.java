package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.os.Bundle;
import com.ebookfrenzy.pebuddy.databinding.FragmentResultsBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebookfrenzy.pebuddy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ResultsFragment extends Fragment {

    private FragmentResultsBinding binding;
    private ArrayList<String> weekdays;
    private ArrayList<Boolean> completed;
    private ArrayList<String> dates;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        weekdays = new ArrayList<>();
        completed = new ArrayList<>();
        dates = new ArrayList<>();

        File file = new File(requireActivity().getFilesDir(), "userData.json");
        try {
            JSONObject jsonObject = new JSONObject(getUserWorkoutData(file));
            JSONArray jsonArray = jsonObject.getJSONArray("userData");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                weekdays.add(data.getString("weekday"));
                completed.add(data.getBoolean("completed"));
                dates.add(data.getString("date"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerResultsView.setLayoutManager(layoutManager);
        adapter = new RecyclerResultsAdapter(weekdays, completed, dates);
        binding.recyclerResultsView.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Gets user workout data from JSON file and returns String of current data
    private String getUserWorkoutData(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return stringBuilder.toString();
    }
}