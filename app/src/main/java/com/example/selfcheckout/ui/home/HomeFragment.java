package com.example.selfcheckout.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.selfcheckout.BottomBar;
import com.example.selfcheckout.MainActivity;
import com.example.selfcheckout.NavigationUtils;
import com.example.selfcheckout.Scanner;
import com.example.selfcheckout.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (isAdded()) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
            String userID = sharedPreferences.getString("userId", "12345678");
            Log.d("BackendCheck", "userId: " + userID);
        }


        final CardView scannerBtn = binding.scannerBtn;
        NavigationUtils.setFragmentToActivityNavigationClickListener(scannerBtn, this, Scanner.class);

        final ImageButton logout = binding.logoutButton;
        //NavigationUtils.setFragmentToActivityNavigationClickListener(logout, this, MainActivity.class);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an AlertDialog to confirm logout
                new AlertDialog.Builder(getContext())
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User pressed "Yes", handle the logout
                                handleLogout();
                            }
                        })
                        .setNegativeButton("No", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

       // final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void handleLogout() {
        // Clear the isLoggedIn flag from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // Navigate to the main activity
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);

        // Close the current activity
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}