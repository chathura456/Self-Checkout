package com.example.selfcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView regShortcut = findViewById(R.id.regShortcut);
        NavigationUtils.setNavigationClickListener(regShortcut, MainActivity.this, Register.class);

        Button loginBtn = findViewById(R.id.button);
        NavigationUtils.setNavigationClickListener(loginBtn, MainActivity.this, BottomBar.class);
    }
}