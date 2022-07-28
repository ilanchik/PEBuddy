package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import com.ebookfrenzy.pebuddy.databinding.FragmentHomeBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebookfrenzy.pebuddy.R;
import com.ebookfrenzy.pebuddy.ui.main.login.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.txtDate.setText(getCurrentDate());

        //Logout
        binding.cardLogout.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(requireActivity())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure you want to Log Out?")
                    .setPositiveButton("YES", (dialogInterface, i) -> {
                        FirebaseAuth.getInstance().signOut();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new LoginFragment())
                                .commitNow();
                    })
                    .setNegativeButton("NO", (dialogInterface, i) -> {}).show();
        });

        binding.cardSettings.setOnClickListener(v -> {
            SettingsFragment settingsFragment = new SettingsFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, settingsFragment)
                    .addToBackStack(null).commit();
        });

        binding.cardMessages.setOnClickListener(v -> {
            MessageFragment messageFragment = new MessageFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, messageFragment)
                    .addToBackStack(null).commit();
        });

        binding.cardNotifications.setOnClickListener(v -> {
            NotificationsFragment notificationsFragment = new NotificationsFragment();
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, notificationsFragment)
                    .addToBackStack(null).commit();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        String date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy",
                Locale.getDefault());

        date = simpleDateFormat.format(calendar.getTime());
        return date;
    }
}