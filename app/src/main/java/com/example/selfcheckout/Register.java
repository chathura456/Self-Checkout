package com.example.selfcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView logShortcut = findViewById(R.id.logShortcut);
        NavigationUtils.setNavigationClickListener(logShortcut, Register.this, MainActivity.class);
    }
}