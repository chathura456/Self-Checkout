package com.example.selfcheckout.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selfcheckout.Constants;
import com.example.selfcheckout.NavigationUtils;
import com.example.selfcheckout.Payment;
import com.example.selfcheckout.R;
import com.example.selfcheckout.Scanner;
import com.example.selfcheckout.databinding.FragementCartBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CartFragment extends Fragment {

    private FragementCartBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragementCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        SharedPreferences sharedPreferences =  getActivity().getSharedPreferences("user_details", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "");// Replace with the actual user ID
        fetchCartData(userId);

        final Button processButton = binding.detailsBtn4;
        //NavigationUtils.setFragmentToActivityNavigationClickListener(scannerBtn, this, Payment.class);
        processButton.setOnClickListener(v -> {
            createOrder(userId);
        });




        return root;
    }

    private void fetchCartData(String userID) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL + "/cart/" + userID)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);
                    JSONArray productsArray = jsonObject.getJSONObject("cart").getJSONArray("products");

                    List<Items> items = new ArrayList<>();


                    for (int i = 0; i < productsArray.length(); i++) {
                        JSONObject productObject = productsArray.getJSONObject(i).getJSONObject("productID");
                        String productName = productObject.getString("productName");
                        double productPrice = productObject.getDouble("productPrice");
                        int quantity = productsArray.getJSONObject(i).getInt("quantity");
                        String imageUri = productObject.getString("imageUri");

                        items.add(new Items(productName, productPrice, quantity, imageUri));
                    }

                    double totalPrice = jsonObject.getJSONObject("cart").getDouble("totalPrice");

                    // Update UI on the main thread
                    requireActivity().runOnUiThread(() -> {
                        final RecyclerView recyclerView = binding.myRecycleView;
                        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                        recyclerView.setAdapter(new MyAdapter(getContext(), items));

                        final TextView total = binding.lblTotal;
                        final TextView subTotal = binding.lblSubTotal;
                        total.setText(String.format("$ %.2f", totalPrice));
                        subTotal.setText(String.format("$ %.2f", totalPrice));
                    });
                } else {
                    String responseBody = response.body().string();
                    Log.d("BackendCheck", "Failed to fetch cart. Response code: " + response.code() + ", Response body: " + responseBody);

                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("BackendCheck", "Exception: " + e.toString());
            }
        }).start();
    }

    private void createOrder(String id) {
        String userID = id; // Replace with the actual user ID

        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonInputString = "{\"userID\": \"" + userID + "\"}";

        RequestBody body = RequestBody.create(JSON, jsonInputString);
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/order/create")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    try {
                        String orderID = "";
                        double orderTotal = 0.0;
                        String responseBody = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseBody);

                        if (jsonObject.has("order")) {
                            JSONObject orderObject = jsonObject.getJSONObject("order");

                            // Extract order ID
                            if (orderObject.has("_id")) {
                                orderID = orderObject.getString("_id");
                                // Use the orderID as needed
                                Log.d("BackendCheck", "Order ID: " + orderID);
                            } else {
                                Log.d("BackendCheck", "_id (Order ID) not found in order object");
                            }

                            // Extract total amount
                            if (orderObject.has("totalAmount")) {
                                orderTotal = orderObject.getDouble("totalAmount");

                                // Use the orderTotal as needed
                                Log.d("BackendCheck", "Order Total: " + orderTotal);
                            } else {
                                Log.d("BackendCheck", "totalAmount not found in order object");
                            }
                        } else {
                            Log.d("BackendCheck", "order object not found in response");
                        }

                        double finalOrderTotal = orderTotal;
                        String finalOrderID = orderID;
                        Log.d("BackendCheck", "Order ID sent: " + finalOrderID);
                        requireActivity().runOnUiThread(() -> {

                            // Navigate to another activity
                            Intent intent = new Intent(requireContext(), Payment.class);
                            intent.putExtra("total", finalOrderTotal);
                            intent.putExtra("orderId", finalOrderID);

                            startActivity(intent);

                            Toast.makeText(getContext(), "Order created. Waiting for Payment...", Toast.LENGTH_SHORT).show();

                        });
                    } catch (JSONException e) {
                        Log.e("BackendCheck", "JSON parsing error: " + e.getMessage());
                    }


                } else {
                    // Handle the error scenario
                    Log.d("BackendCheck", "Failed to create order. Response code: " + response.code() + ", Response body: " + response.body().string());
                }

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d("BackendCheck", "Request failed: " + e.getMessage());
            }
        });
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }




}