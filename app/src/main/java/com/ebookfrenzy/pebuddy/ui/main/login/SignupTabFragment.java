package com.ebookfrenzy.pebuddy.ui.main.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.ebookfrenzy.pebuddy.MainActivity;
import com.ebookfrenzy.pebuddy.UserViewModel;
import com.ebookfrenzy.pebuddy.databinding.FragmentSignupTabBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ebookfrenzy.pebuddy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignupTabFragment extends Fragment {

    private FragmentSignupTabBinding binding;
    private FirebaseAuth mAuth;
    private Map<String, Object> addUserData = new HashMap<>();
    private UserViewModel viewModel;

    //Radio button for selecting level
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users");

        binding.radBeginner.setChecked(true);

        binding.signup.setOnClickListener( v -> {
            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            String first_name = binding.firstName.getText().toString();
            String last_name = binding.lastName.getText().toString();
            String email = binding.email.getText().toString();

            //Get level from radio button
            radioGroup = (RadioGroup) getActivity().findViewById(R.id.rad_level);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = (RadioButton) getActivity().findViewById(selectedId);
            String level = String.valueOf(radioButton.getText());

            //put user data into Map for database
            addUserData.put("username", username);
            addUserData.put("first_name", first_name);
            addUserData.put("last_name", last_name);

            //Add user personal data to local JSON database
            addUserData(first_name, last_name, username, password, level, email);
            viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
            viewModel.setFirst_name(first_name);
            viewModel.setLast_name(last_name);
            viewModel.setUsername(username);
            viewModel.setEmail(email);
            viewModel.setLevel(level);

            if (username.isEmpty() || password.isEmpty() || first_name.isEmpty() ||
                    last_name.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireActivity(), "Please fill in all fields",
                        Toast.LENGTH_LONG).show();
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.linearLayout1.setVisibility(View.GONE);
                if (isNetworkAvailable(getContext())) {
                    //Create user in Firebase
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), task -> {
                                if (task.isSuccessful()) {
                                    Log.i("TAG", "createUser: Success");

                                    //Add user to database
                                    String uid = mAuth.getCurrentUser().getUid();
                                    userRef.child(uid).setValue(new User(username, first_name, last_name));

                                    binding.progressBar.setVisibility(View.GONE);

                                    SelectProfilePhotoFragment selectProfilePhotoFragment = new SelectProfilePhotoFragment();
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.container, selectProfilePhotoFragment).commit();
                                } else {
                                    Log.i("TAG", "createUser: Failed");
                                    Toast.makeText(getContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                                    binding.progressBar.setVisibility(View.GONE);
                                    binding.linearLayout1.setVisibility(View.VISIBLE);
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "No internet, data saved locally",
                            Toast.LENGTH_LONG).show();
                    SelectProfilePhotoFragment selectProfilePhotoFragment = new SelectProfilePhotoFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container, selectProfilePhotoFragment).commit();

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignupTabBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
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