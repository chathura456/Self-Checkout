package com.example.selfcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class ItemDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        ImageButton backBtn = findViewById(R.id.backButton);
        NavigationUtils.setBackNavigationClickListener(backBtn, this);

        Button detailsBtn = findViewById(R.id.detailsBtn2);
        NavigationUtils.setNavigationClickListener(detailsBtn, ItemDetails.this, BottomBar.class);
    }
}