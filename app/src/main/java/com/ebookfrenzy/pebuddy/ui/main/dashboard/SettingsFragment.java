package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.os.Bundle;

import com.ebookfrenzy.pebuddy.UserViewModel;
import com.ebookfrenzy.pebuddy.databinding.FragmentSettingsBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private UserViewModel viewModel;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        binding.changeEmail.setText(viewModel.getEmail());
        binding.changeLast.setText(viewModel.getLast_name());
        binding.changeFirst.setText(viewModel.getFirst_name());
        binding.changeUn.setText(viewModel.getUsername());
        binding.radSettingsBeginner.setChecked(true);

        binding.btnSave.setOnClickListener(v -> {
            String first_name = binding.changeFirst.getText().toString();
            String last_name = binding.changeLast.getText().toString();
            String username = binding.changeUn.getText().toString();
            String email = binding.changeEmail.getText().toString();

            radioGroup = (RadioGroup) getActivity().findViewById(R.id.rad_settings);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) getActivity().findViewById(selectedId);
            String level = String.valueOf(radioButton.getText());

            addUserData(first_name, last_name, username, viewModel.getPassword(), level, email);

            viewModel.setLevel(level);

            Toast.makeText(getActivity(), "Level changed!", Toast.LENGTH_SHORT).show();


        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

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

    private void addUserData(String fn, String ln, String un, String pwd, String level, String email) {
        File file = new File(requireActivity().getFilesDir(), "userPersonalData.json");

        try {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            JSONObject userdata = new JSONObject();
            userdata.put("first_name", fn);
            userdata.put("last_name", ln);
            userdata.put("username", un);
            userdata.put("password", pwd);
            userdata.put("level", level);
            userdata.put("email", email);
            jsonArray.put(userdata);
            jsonObject.put("userPersonalData", jsonArray);
            writeUserWorkoutData(file, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}