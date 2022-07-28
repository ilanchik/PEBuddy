package com.ebookfrenzy.pebuddy.ui.main.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import com.ebookfrenzy.pebuddy.databinding.FragmentSelectProfilePhotoBinding;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebookfrenzy.pebuddy.R;
import com.ebookfrenzy.pebuddy.ui.main.dashboard.DashboardFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SelectProfilePhotoFragment extends Fragment {

    private FragmentSelectProfilePhotoBinding binding;
    private String username = "ilan";

    //For selecting Image
    ActivityResultLauncher<Intent> startOpenForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                                saveImage(bitmap);
                                binding.profilePhoto.setImageURI(imageUri);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //binding.imageView2.setImageURI(imageUri);
                        }
                    }
                }
            }
    );

    //Save image to internal storage
    public void saveImage(Bitmap bitmap) {
        OutputStream outputStream;
        String recentImageInCache;
        File filepath = requireActivity().getFilesDir();

        //Create new folder
        File dir = new File(filepath.getAbsolutePath()+"/profile");
        dir.mkdir();

        //Create name for saved image
        File file = new File(dir, username+".jpg");
        try {
            outputStream = new FileOutputStream(file);

            //Compress
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {e.printStackTrace();}

    }

    //Open image file from storage
    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startOpenForResult.launch(intent);
    }

    public SelectProfilePhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSelectProfilePhotoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSelectPhoto.setOnClickListener( v -> {
            openFile(v);
        });

        binding.btnContinue.setOnClickListener( v -> {
            DashboardFragment dashboardFragment = new DashboardFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, dashboardFragment).commit();
        });

    }
}