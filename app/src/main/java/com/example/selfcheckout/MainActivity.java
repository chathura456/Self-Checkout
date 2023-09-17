package com.example.selfcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for auto login
        checkBackendHealth();

        TextView regShortcut = findViewById(R.id.regShortcut);
        NavigationUtils.setNavigationClickListener(regShortcut, MainActivity.this, Register.class);

        Button loginBtn = findViewById(R.id.button);
        //NavigationUtils.setNavigationClickListener(loginBtn, MainActivity.this, BottomBar.class);

        TextInputEditText email = findViewById(R.id.loginEmail);
        TextInputEditText password = findViewById(R.id.loginPassword);

        loginBtn.setOnClickListener(v -> {
            // Retrieve email and password from the TextInputEditText views
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            // Check if email and password are not empty
            if(!userEmail.isEmpty() && !userPassword.isEmpty()) {
               loginUser(userEmail, userPassword);
              //  checkBackend();
            } else {
                Toast.makeText(getApplicationContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            }
        });


    }



    public void loginUser(String email1, String password1) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                String jsonInputString = "{\"email\": \"" + email1 + "\", \"password\": \"" + password1 + "\"}";
                Log.d("LoginRequest", "Sending JSON: " + jsonInputString);

                RequestBody body = RequestBody.create(JSON, jsonInputString);
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL+"/users/login")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("BackendCheck", "Response: Success");

                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);

                    if (jsonObject.has("user")) {
                        JSONObject userObject = jsonObject.getJSONObject("user");

                        if (userObject.has("_id")) {
                            String userId = userObject.getString("_id");
                            String username = userObject.getString("username");
                            String email = userObject.getString("email");
                            String userType = userObject.getString("userType");

                            Log.d("BackendCheck", "userId: " + userId);

                            // Store user details in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("userId", userId);
                            editor.putString("username", username);
                            editor.putString("email", email);
                            editor.putString("userType", userType);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            // Navigate to SuccessActivity
                            Intent intent = new Intent(getApplicationContext(), BottomBar.class);
                            startActivity(intent);

                            // Display a toast message on the main thread
                            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show());

                        } else {
                            Log.d("BackendCheck", "No _id found in the user object");
                        }
                    } else {
                        Log.d("BackendCheck", "No user object found in the response");
                    }
                } else {
                    Log.d("BackendCheck", "Failed to log in. Response code: " + response.code() + ", Response body: " + response.body().string());
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to log in. Please check email and password again", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("BackendCheck", "Exception: " + e.toString());
            }
        }).start();
    }

    public void checkBackendHealth() {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL + "/users/test")
                        .get()
                        .build();

                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);

                if (response.isSuccessful() && jsonObject.has("message") && jsonObject.getString("message").equals("success")) {
                    // Backend is running and responded with success, check if user is already logged in
                    checkAutoLogin();
                } else {
                    // Handle the error, maybe show a message that the server is down or didn't respond with success
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Server is currently down. Please try again later.", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle the exception, maybe show a message that there's a network error or the server is unreachable
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Unable to connect to the server. Please check your internet connection.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
    public void checkAutoLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            // Navigate to main activity or wherever you want to take the user post-login
            Intent intent = new Intent(getApplicationContext(), BottomBar.class);
            startActivity(intent);
            finish();
        }
    }
}

