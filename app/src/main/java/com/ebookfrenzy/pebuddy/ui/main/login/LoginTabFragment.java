package com.ebookfrenzy.pebuddy.ui.main.login;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.ebookfrenzy.pebuddy.R;
import com.ebookfrenzy.pebuddy.UserViewModel;
import com.ebookfrenzy.pebuddy.databinding.FragmentLoginTabBinding;
import com.ebookfrenzy.pebuddy.ui.main.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LoginTabFragment extends Fragment {

    private FragmentLoginTabBinding binding;
    private FirebaseAuth mAuth;
    private String email, password;
    private UserViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("TAG", String.valueOf(isNetworkAvailable(getContext())));
        isInternetOn(getContext());

        //Add user data to JSON -- onCreate
        try {
            addJsonToViewModel();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Check if network available
        if (isNetworkAvailable(getContext())) {
            //Check if user is already logged in
            mAuth = FirebaseAuth.getInstance();
            if (mAuth.getCurrentUser() != null) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new DashboardFragment())
                        .commit();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.login.setOnClickListener( v -> {
            userLogin();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void userLogin() {
        email = binding.email.getText().toString();
        password = binding.password.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty())
                Toast.makeText(getContext(), "Please enter e-mail address", Toast.LENGTH_SHORT).show();
            if (password.isEmpty())
                Toast.makeText(getContext(), "Please enter password", Toast.LENGTH_SHORT).show();
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);

            if (binding.networkSwitch.isChecked() && isNetworkAvailable(getContext())) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    binding.progressBar.setVisibility(View.GONE);
                                    DashboardFragment dashboardFragment = new DashboardFragment();
                                    FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                            .beginTransaction();
                                    transaction.replace(R.id.container, dashboardFragment).commit();
                                } else {
                                    if (email.equals(viewModel.getEmail()) && password.equals(viewModel.getPassword())) {
                                        binding.progressBar.setVisibility(View.GONE);
                                        DashboardFragment dashboardFragment = new DashboardFragment();
                                        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                                                .beginTransaction();
                                        transaction.replace(R.id.container, dashboardFragment).commit();
                                    } else {
                                        Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                                        binding.progressBar.setVisibility(View.GONE);
                                    }
                                }
                            }
                        });
            } else {
                if (email.equals(viewModel.getEmail()) && password.equals(viewModel.getPassword())) {
                    Toast.makeText(getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                    DashboardFragment dashboardFragment = new DashboardFragment();
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                            .beginTransaction();
                    transaction.replace(R.id.container, dashboardFragment).commit();
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Incorrect Login, Try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }



    }

    //Gets user workout data from JSON file and returns String of current data
    private String getUserData(File file) throws IOException {
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

    private void addJsonToViewModel() throws IOException {
        File file = new File(requireActivity().getFilesDir(), "userPersonalData.json");
        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        try {
            JSONObject jsonObject = new JSONObject(getUserData(file));
            JSONArray jsonArray = jsonObject.getJSONArray("userPersonalData");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject data = jsonArray.getJSONObject(i);
                viewModel.setFirst_name(data.getString("first_name"));
                viewModel.setLast_name(data.getString("last_name"));
                viewModel.setUsername(data.getString("username"));
                viewModel.setPassword(data.getString("password"));
                viewModel.setLevel(data.getString("level"));
                viewModel.setEmail(data.getString("email"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            Log.i("TAG", "NEW: Internet is working");
            return true;
        } else {
            Log.i("TAG", "NEW: No internet connection");
            return false;
        }
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
}