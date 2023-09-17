package com.example.selfcheckout;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.selfcheckout.ui.home.HomeFragment;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ItemDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        ImageButton backBtn = findViewById(R.id.backButton);
        NavigationUtils.setBackNavigationClickListener(backBtn, this);


        TextView name = findViewById(R.id.productName);
        TextView desc = findViewById(R.id.desc);
        TextView price = findViewById(R.id.price);
        ImageView imageView = findViewById(R.id.img);

        Intent intent = getIntent();
        String productName = intent.getStringExtra("name");
        name.setText(productName);

        desc.setText(intent.getStringExtra("desc"));
        double productPrice = intent.getDoubleExtra("price",0);

        price.setText("$ "+String.valueOf(productPrice)+"0");
        Glide.with(this)
                .load(intent.getStringExtra("uri"))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);

        String productID = intent.getStringExtra("id");


        SharedPreferences sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");


        Button detailsBtn = findViewById(R.id.detailsBtn2);
        //NavigationUtils.setNavigationClickListener(detailsBtn, ItemDetails.this, BottomBar.class);
        detailsBtn.setOnClickListener(v -> {
            addToCart(userId,productID,1, this);
        });


    }

    public void addToCart(String userID, String productID, int quantity, Context context) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                String jsonInputString = "{\"userID\": \"" + userID + "\", \"productID\": \"" + productID + "\", \"quantity\": " + quantity + "}";
                Log.d("AddToCartRequest", "Sending JSON: " + jsonInputString);

                RequestBody body = RequestBody.create(JSON, jsonInputString);
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL + "/cart/add")
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("BackendCheck", "Product added to cart successfully");

                    // You can parse the response if needed
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    // Use the jsonObject to extract any data if needed

                    Intent intent = new Intent(getApplicationContext(), BottomBar.class);
                    startActivity(intent);
                   runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show());


                } else {
                    Log.d("BackendCheck", "Failed to add to cart. Response code: " + response.code() + ", Response body: " + response.body().string());
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to add to cart. Please try again", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("BackendCheck", "Exception: " + e.toString());
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error occurred. Please try again", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }





}