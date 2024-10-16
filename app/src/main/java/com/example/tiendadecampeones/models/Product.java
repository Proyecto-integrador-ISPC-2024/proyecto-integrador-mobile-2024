package com.example.tiendadecampeones.models;

import java.util.List;

public class Product {

    private ProductDetails productos;
    //private List<SizeDetails> talles;
    private int quantity;
    private int stock;

    public Product(String name, String description, double price, String imageUrl, int quantity, int stock) {
        this.productos = new ProductDetails(name, price, imageUrl);
        this.quantity = quantity;
        this.stock = stock;
    }

    // detalles del producto
    public ProductDetails getProductDetails() {
        return productos;
    }

    // nombre del producto
    public String getName() {
        return productos.getNombreProducto();
    }

    // precio del producto
    public double getPrice() {
        return productos.getPrecio();
    }

    // imagen del producto
    public String getImageUrl() {
        return productos.getImagen();
    }

    // cantidad
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // stock
    public int getStock() {
        return stock;
    }

    // detalles del producto
    public static class ProductDetails {
        private String nombre_producto;
        private double precio;
        private String imagen;

        public ProductDetails(String name, double price, String imageUrl) {
            this.nombre_producto = name;
            this.precio = price;
            this.imagen = imageUrl;
        }

        public String getNombreProducto() {
            return nombre_producto;
        }

        public double getPrecio() {
            return precio;
        }

        public String getImagen() {
            return imagen;
        }
    }
}
