package com.example.selfcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

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

        TextView regShortcut = findViewById(R.id.regShortcut);
        NavigationUtils.setNavigationClickListener(regShortcut, MainActivity.this, Register.class);

        Button loginBtn = findViewById(R.id.button);
        //NavigationUtils.setNavigationClickListener(loginBtn, MainActivity.this, BottomBar.class);

        TextInputEditText email = findViewById(R.id.loginEmail);
        TextInputEditText password = findViewById(R.id.loginPassword);
        TextView textView = findViewById(R.id.textView16);

        loginBtn.setOnClickListener(v -> {
            // Retrieve email and password from the TextInputEditText views
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();
            textView.setText(userPassword);

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
                        .url("https://8cfc-2402-d000-8100-706a-9c6f-540-4299-3949.ngrok-free.app/users/login")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("BackendCheck", "Response: Success");
                    // Navigate to SuccessActivity
                    Intent intent = new Intent(getApplicationContext(), BottomBar.class);
                    startActivity(intent);

                    // Display a toast message on the main thread
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show());
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




    public void checkBackend() {
        new Thread(() -> {
            try {
                URL url = new URL("https://8cfc-2402-d000-8100-706a-9c6f-540-4299-3949.ngrok-free.app/users/test");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Accept", "application/json");

                int responseCode = httpURLConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Log the result
                   // Toast.makeText(getApplicationContext(), "test Success", Toast.LENGTH_SHORT).show();
                    Log.d("BackendCheck", "Response: Success" );
                } else {
                    //Toast.makeText(getApplicationContext(), "Test Success", Toast.LENGTH_SHORT).show();
                    Log.d("BackendCheck", "Response: error" );
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("BackendCheck", "Response: "+e.toString() );
            }
        }).start();
    }


}

