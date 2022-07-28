package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ebookfrenzy.pebuddy.CountdownService;
import com.ebookfrenzy.pebuddy.UserDataViewModel;
import com.ebookfrenzy.pebuddy.UserViewModel;
import com.ebookfrenzy.pebuddy.databinding.FragmentWorkoutBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ebookfrenzy.pebuddy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WorkoutFragment extends Fragment {

    private String weekday;
    private static final HashMap<String, ArrayList<String>> workouts = new HashMap<>();
    private ArrayList<String> exercises;
    private FragmentWorkoutBinding binding;
    private boolean isDone = false;
    private Date currentTime;
    private UserViewModel viewModel;
    private UserDataViewModel dataViewModel;

    //RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //Broadcast receiver
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                String time = intent.getExtras().getString("countdown");
                binding.txtTimer.setText(time);

                //Add user Data when service is complete (using intent flag to mark complete)
                isDone = intent.getExtras().getBoolean("isDone");
                if (isDone) {
                    currentTime = Calendar.getInstance().getTime();
                    addUserData(weekday, true, currentTime.toString());
                }
            }
        }
    };
    
    public WorkoutFragment() {
        // Required empty public constructor
    }

    public WorkoutFragment(String weekday) {

        this.weekday = weekday;
        //this.workout = workout;
    }

    @Override
    public void onResume() {
        super.onResume();

        //Register broadcast receiver
        requireActivity().registerReceiver(broadcastReceiver,
                new IntentFilter(CountdownService.COUNTDOWN_BR));
    }

    @Override
    public void onDestroy() {
        requireActivity().stopService(new Intent(this.getActivity(), CountdownService.class));
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        String level = viewModel.getLevel();

        //Create workout List using adapter
        switch (level) {
            case "Beginner":
                createWorkoutList("Beginner");
                break;
            case "Intermediate":
                createWorkoutList("Intermediate");
                break;
            case "Advanced":
                createWorkoutList("Advanced");
        }

        binding.txtWeekday.setText(weekday.toUpperCase());
        binding.txtLevel.setText(level.toUpperCase());

        //Display workout list
        layoutManager = new LinearLayoutManager(this.requireActivity());
        binding.recyclerExerciseView.setLayoutManager(layoutManager);
        adapter = new RecyclerWorkoutListAdapter(workouts.get(weekday));
        binding.recyclerExerciseView.setAdapter(adapter);

        //Begin countdown timer for service
        binding.btnStart.setOnClickListener(v -> {
            requireActivity().startService(new Intent(this.getActivity(), CountdownService.class));
            binding.btnStart.setClickable(false);
            //binding.btnStart.setBackgroundColor(Color.GRAY);
        });

        binding.btnStop.setOnClickListener(v -> {
            requireActivity().stopService(new Intent(this.getActivity(), CountdownService.class));
            Toast.makeText(getContext(), "Workout Incomplete, recorded", Toast.LENGTH_SHORT).show();
            currentTime = Calendar.getInstance().getTime();
            addUserData(weekday, false, currentTime.toString());
            binding.btnStart.setClickable(true);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWorkoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //Returns JSON workout data in String format
    private String getJsonData(String fileName) throws IOException {
        String json = null;

        try {
            InputStream inputStream = getContext().getAssets().open(fileName);
            int sizeOfFile = inputStream.available();
            byte[] bufferData = new byte[sizeOfFile];
            inputStream.read(bufferData);
            inputStream.close();
            json = new String(bufferData, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    //Creates workout list from JSON file
    private void createWorkoutList(String level) {
        try {
            JSONArray jsonArray = new JSONArray(getJsonData(String.format("%s.json", level)));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray exs = jsonObject.getJSONArray("exercise");
                exercises = new ArrayList<>();
                for (int j = 0; j < exs.length(); j++) {
                    exercises.add(exs.getString(j));
                }
                workouts.put(jsonObject.getString("weekday"), exercises);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    //Writes user workout results to JSON file
    private void writeUserWorkoutData(File file, String json) {

        BufferedWriter bufferedWriter;
        try {
            if (!file.exists()) file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addUserData(String weekday, boolean completed, String date) {
        File file = new File(requireActivity().getFilesDir(), "userData.json");

        try {
            if (!file.exists()) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                JSONObject userdata = new JSONObject();
                userdata.put("weekday", weekday);
                userdata.put("completed", completed);
                userdata.put("date", date);
                jsonArray.put(userdata);
                jsonObject.put("userData", jsonArray);
                writeUserWorkoutData(file, jsonObject.toString());

            } else {
                JSONObject orig_jsonObject = new JSONObject(getUserWorkoutData(file));
                JSONArray array = orig_jsonObject.getJSONArray("userData");
                JSONObject userData = new JSONObject();
                userData.put("weekday", weekday);
                userData.put("completed", completed);
                userData.put("date", date);
                array.put(userData);
                JSONObject newJsonObject = new JSONObject();
                newJsonObject.put("userData", array);
                writeUserWorkoutData(file, newJsonObject.toString());
            }


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
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