package com.example.tiendadecampeones.models;

public class Product {
    final private String name;
    final private String description;
    final private double price;
    final private int imageResId;
    private int quantity;
    private int stock;

    public Product(String name, String description, double price, int imageResId, int quantity,int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = quantity;
        this.stock = stock;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStock() {
        return stock;
    }
}
