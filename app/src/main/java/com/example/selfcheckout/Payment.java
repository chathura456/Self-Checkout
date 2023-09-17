package com.example.selfcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Payment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        ImageButton backBtn = findViewById(R.id.backButton2);
        NavigationUtils.setBackNavigationClickListener(backBtn, this);



        TextView total = findViewById(R.id.total);
        Intent intent = getIntent();
        double total1 =intent.getDoubleExtra("total",0);
        String orderID= intent.getStringExtra("orderId");
                total.setText("$ "+String.valueOf(total1)+"0");

        Button detailsBtn = findViewById(R.id.detailsBtn3);
        detailsBtn.setOnClickListener(v -> {
            payOrder(orderID);
        });
    }

    public void payOrder(String id) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                String jsonInputString = "{\"orderID\": \"" + id + "\"}";
                Log.d("PayOrderRequest", "Sending JSON: " + jsonInputString);

                RequestBody body = RequestBody.create(JSON, jsonInputString);
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL + "/order/paid")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("BackendCheck", "Order payment successful");

                    // Update UI on the main thread, if needed
                    runOnUiThread(() -> {
                        new AlertDialog.Builder(this)
                                .setTitle("Payment Successful")
                                .setMessage("Your order payment was successful!")
                                .setPositiveButton("OK", (dialog, which) -> {
                                    // Handle button click, if needed
                                    dialog.dismiss();
                                    Intent intent = new Intent(Payment.this,BottomBar.class);
                                    startActivity(intent);

                                })
                                .show();
                    });
                } else {
                    Log.d("BackendCheck", "Failed to pay for the order. Response code: " + response.code() + ", Response body: " + response.body().string());

                    // Extract the error message from the response body
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    String errorMessage = jsonObject.getString("message");

                    // Display the error message as a toast on the main thread
                    runOnUiThread(() -> {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("BackendCheck", "Exception: " + e.toString());
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error occurred. Please try again", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }


}