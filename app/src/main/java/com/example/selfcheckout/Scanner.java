package com.example.selfcheckout;


import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class Scanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);

        ImageButton backBtn = findViewById(R.id.backButton);
        NavigationUtils.setBackNavigationClickListener(backBtn, this);

        Button detailsBtn = findViewById(R.id.detailsBtn);
        NavigationUtils.setNavigationClickListener(detailsBtn, Scanner.this, ItemDetails.class);

    }
}