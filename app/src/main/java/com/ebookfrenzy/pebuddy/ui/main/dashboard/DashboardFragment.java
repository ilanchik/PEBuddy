package com.ebookfrenzy.pebuddy.ui.main.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.ULocale;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.ebookfrenzy.pebuddy.UserViewModel;
import com.ebookfrenzy.pebuddy.databinding.FragmentDashboardBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebookfrenzy.pebuddy.R;
import com.ebookfrenzy.pebuddy.ui.main.login.SelectProfilePhotoFragment;
import com.ebookfrenzy.pebuddy.ui.main.login.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private FirebaseAuth mAuth;
    private UserViewModel viewModel;
    private static final String BACK_STACK_ROOT_TAG = "root_fragment";

    public DashboardFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        binding.bottomNavBar.setSelectedItemId(R.id.home);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new HomeFragment()).commit();

        viewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        binding.bottomNavBar.setOnItemSelectedListener( item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.workoutWeek:
                    replaceFragment(new WorkoutWeekFragment());
                    break;
                case R.id.results:
                    replaceFragment(new ResultsFragment());
                    break;
            }

            return true;
        });

        return binding.getRoot();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /** USE IF INTERNET AVAILABLE -- WILL IMPLEMENT LATER
        mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("users").child(mAuth.getCurrentUser().getUid())
                .child("username");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String un = snapshot.getValue().toString();
                binding.collapsingToolbar.setTitle(viewModel.getFirst_name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        }); **/

        binding.imageUser.setOnClickListener(v -> {
            SelectProfilePhotoFragment selectProfilePhotoFragment = new SelectProfilePhotoFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, selectProfilePhotoFragment).commit();
        });

        //Display name in toolbar
        binding.collapsingToolbar.setTitle(viewModel.getFirst_name());

        //Display user photo in toolbar
        File file = new File(requireActivity().getFilesDir(), "/profile/"+"ilan.jpg");
        //Log.i("TAG", file.getAbsolutePath());
        if (file.exists()) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            } catch (Exception e) {}
            binding.imageUser.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
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