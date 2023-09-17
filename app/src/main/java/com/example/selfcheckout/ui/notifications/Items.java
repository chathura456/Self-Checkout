package com.example.selfcheckout.ui.notifications;

public class Items {
    String Name;
    double Price;
    int Qty;
    String ImageUrl;

    public Items(String name, double price, int qty, String imageUrl) {
        Name = name;
        Price = price;
        Qty = qty;
        ImageUrl = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
