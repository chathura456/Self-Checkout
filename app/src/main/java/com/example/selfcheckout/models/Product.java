package com.example.selfcheckout.models;

import androidx.annotation.NonNull;

public class Product {

    private String id; // MongoDB's unique ID
    private String productName;
    private double productPrice;
    private String barcode;
    private String imageUri;
    private String description;
    private int productStock;

    // Constructors
    public Product() {
    }

    public Product(String id, String productName, double productPrice, String barcode, String imageUri, String description, int productStock) {
        this.id = id;
        this.productName = productName;
        this.productPrice = productPrice;
        this.barcode = barcode;
        this.imageUri = imageUri;
        this.description = description;
        this.productStock = productStock;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    @NonNull
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", barcode='" + barcode + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", description='" + description + '\'' +
                ", productStock=" + productStock +
                '}';
    }
}
