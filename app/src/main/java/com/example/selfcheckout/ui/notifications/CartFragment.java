package com.example.selfcheckout.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.selfcheckout.NavigationUtils;
import com.example.selfcheckout.Payment;
import com.example.selfcheckout.Scanner;
import com.example.selfcheckout.databinding.FragementCartBinding;

public class CartFragment extends Fragment {

    private FragementCartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragementCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final Button scannerBtn = binding.detailsBtn4;
        NavigationUtils.setFragmentToActivityNavigationClickListener(scannerBtn, this, Payment.class);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}