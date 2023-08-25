package com.example.selfcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ImageButton backBtn = findViewById(R.id.backButton2);
        NavigationUtils.setBackNavigationClickListener(backBtn, this);

        Button detailsBtn = findViewById(R.id.detailsBtn3);
        NavigationUtils.setNavigationClickListener(detailsBtn, Payment.this, BottomBar.class);
    }
}