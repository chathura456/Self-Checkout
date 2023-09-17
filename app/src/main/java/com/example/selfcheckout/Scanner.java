package com.example.selfcheckout;


import static android.app.PendingIntent.getActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.selfcheckout.models.Product;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Scanner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);

        ImageButton backBtn = findViewById(R.id.backButton);
        NavigationUtils.setBackNavigationClickListener(backBtn, this);

        Button detailsBtn = findViewById(R.id.detailsBtn);
        //NavigationUtils.setNavigationClickListener(detailsBtn, Scanner.this, ItemDetails.class);

        detailsBtn.setOnClickListener(v -> {
            scanCode();
        });

    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to Flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(Scanner.this);
            builder.setTitle("Result");
            builder.setMessage("You scanned the barcode code successfully");
            builder.setPositiveButton("Show Results", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    checkBarcode(result.getContents());

                  /* if(product==null){
                       Log.d("BackendCheck", "Product: "+ null);

                   }else{
                       // Start the NextActivity
                       Intent intent = new Intent(Scanner.this, ItemDetails.class);
                       startActivity(intent);
                       Log.d("product", "data: " + product);
                   }*/


                }
            }).show();}
    });

    public void checkBarcode(String barcodeValue) {

        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();

                // Construct the URL with the barcode value as a query parameter
                String url = Constants.BASE_URL + "/products/validate/" + barcodeValue;
                Log.d("BarcodeCheckRequest", "Sending request to URL: " + url);

                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();

                Response response = client.newCall(request).execute();

                if (response.isSuccessful()) {
                    Log.d("BarcodeCheck", "Response: Success");

                    // Parse the response to get product details
                    String responseBody = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseBody);

                    String productName = jsonObject.getString("productName");
                    double productPrice = jsonObject.getDouble("productPrice");
                    String barcode = jsonObject.getString("barcode");
                    String imageUri = jsonObject.getString("imageUri");
                    String description = jsonObject.getString("description");
                    int productStock = jsonObject.getInt("productStock");
                    String productId = jsonObject.getString("_id");
                    Product product = new Product(productId, productName, productPrice, barcode, imageUri, description, productStock);
                    Log.d("BarcodeCheck", "results: " + product);
                    Intent intent = new Intent(Scanner.this, ItemDetails.class);
                    intent.putExtra("name",productName);
                    intent.putExtra("price",productPrice);
                    intent.putExtra("uri",imageUri);
                    intent.putExtra("desc",description);
                    intent.putExtra("id",productId);
                    startActivity(intent);

                } else {
                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "You Scanned Wrong Barcode. Data is not Valid.", Toast.LENGTH_SHORT).show());
                    Log.d("BarcodeCheck", "Failed to find product. Response code: " + response.code() + ", Response body: " + response.body().string());
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.d("BarcodeCheck", "Exception: " + e.toString());
            }
        }).start();

    }




}