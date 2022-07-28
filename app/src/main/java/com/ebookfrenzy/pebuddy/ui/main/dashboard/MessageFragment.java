package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.os.Bundle;
import com.ebookfrenzy.pebuddy.databinding.FragmentMessageBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ebookfrenzy.pebuddy.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class MessageFragment extends Fragment {

    private FragmentMessageBinding binding;
    private Date currentTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSend.setOnClickListener(v -> {
            currentTime = Calendar.getInstance().getTime();
            String to = binding.txtTo.getText().toString();
            String subject = binding.subject.getText().toString();
            String message = binding.txtMessage.getText().toString();

            addUserMessage(currentTime.toString(), to, subject, message);

            Snackbar.make(getActivity().findViewById(android.R.id.content), "Message sent",
                    Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //Writes user workout results to JSON file
    private void writeUserMessageData(File file, String json) {

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

    private void addUserMessage(String date, String to, String subject, String message) {
        File file = new File(requireActivity().getFilesDir(), "userMessages.json");

        try {
            if (!file.exists()) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                JSONObject userdata = new JSONObject();
                userdata.put("date", date);
                userdata.put("to", to);
                userdata.put("subject", subject);
                userdata.put("message", message);
                jsonArray.put(userdata);
                jsonObject.put("userMessages", jsonArray);
                writeUserMessageData(file, jsonObject.toString());

            } else {
                JSONObject orig_jsonObject = new JSONObject(getUserMessages(file));
                JSONArray array = orig_jsonObject.getJSONArray("userMessages");
                JSONObject userData = new JSONObject();
                userData.put("date", date);
                userData.put("to", to);
                userData.put("subject", subject);
                userData.put("message", message);
                array.put(userData);
                JSONObject newJsonObject = new JSONObject();
                newJsonObject.put("userMessages", array);
                writeUserMessageData(file, newJsonObject.toString());
            }


        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getUserMessages(File file) throws IOException {
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