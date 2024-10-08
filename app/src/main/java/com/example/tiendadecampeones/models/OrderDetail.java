package com.example.tiendadecampeones.models;

public class OrderDetail {
    private int id_talle;
    private int cantidad;
    private double subtotal;
    private Product producto;

    public OrderDetail(int id_talle, int cantidad, double subtotal, Product producto) {
        this.id_talle = id_talle;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    // Getters
    public int getIdTalle() {
        return id_talle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public Product getProducto() {
        return producto;
    }

    @Override
    public String toString() {
        return "Talle: " + id_talle + ", Cantidad: " + cantidad + ", Subtotal: $" + subtotal + ", Producto: " + producto;
    }
}
