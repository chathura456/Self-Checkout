package com.example.selfcheckout.ui.notifications;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.selfcheckout.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView name, price, qty;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.cartImage);
        name = itemView.findViewById(R.id.lblName1);
        price = itemView.findViewById(R.id.lblPrice1);
        qty = itemView.findViewById(R.id.lblQty);
    }

    public void bindData(Context context, Items item) {
        name.setText(item.getName());
        price.setText("$ "+String.valueOf(item.getPrice())+"0"); // Assuming price is a double or float
        qty.setText("Qty : "+String.valueOf(item.getQty())); // Assuming qty is an integer

        Glide.with(context)
                .load(item.getImageUrl()) // Assuming getImageUrl() returns a URL string
                .placeholder(R.drawable.placeholder) // Optional: a placeholder image while the main image loads
                .error(R.drawable.error) // Optional: an image to display if there's an error loading the main image
                .into(imageView);
    }
}
