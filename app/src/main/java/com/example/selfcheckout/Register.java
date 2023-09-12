package com.example.selfcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView logShortcut = findViewById(R.id.logShortcut);
        NavigationUtils.setNavigationClickListener(logShortcut, Register.this, MainActivity.class);

        TextInputEditText name = findViewById(R.id.regName);
        TextInputEditText email = findViewById(R.id.regEmail);
        TextInputEditText password = findViewById(R.id.regPass);
        TextInputEditText rePassword = findViewById(R.id.regConfirmPass);
        Button regBtn = findViewById(R.id.regButton);

        regBtn.setOnClickListener(v -> {
            // Retrieve data from the TextInputEditText views
            String userName = name.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            String confirmPassword = rePassword.getText().toString().trim();

            // Check if email and password are not empty
            if(!userEmail.isEmpty() && !userPassword.isEmpty() && !userName.isEmpty() && !confirmPassword.isEmpty()) {
                registerUser(userName,userPassword,userEmail, "customer");
                //  checkBackend();
            } else {
                Toast.makeText(getApplicationContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerUser(String username, String userPassword, String email, String userType) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                String jsonInputString = "{\"username\": \"" + username + "\", \"userPassword\": \"" + userPassword + "\", \"email\": \"" + email + "\", \"userType\": \"" + userType + "\"}";
                Log.d("RegisterRequest", "Sending JSON: " + jsonInputString);

                RequestBody body = RequestBody.create(JSON, jsonInputString);
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL + "/users/register")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("BackendCheck", "Response: Registration Success");
                    Intent intent = new Intent(getApplicationContext(), BottomBar.class);
                    startActivity(intent);

                    // Display a toast message on the main thread
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show());
                } else {
                    Log.d("BackendCheck", "Failed to register. Response code: " + response.code() + ", Response body: " + response.body().string());
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Registration Failed. Please check the details and try again", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("BackendCheck", "Exception: " + e.toString());
            }
        }).start();
    }

}