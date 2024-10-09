package com.example.tiendadecampeones.models;

public class Detalle {
    private int idTalle;
    private int cantidad;
    private double subtotal;
    private Producto producto;

    public Detalle(int idTalle, int cantidad, double subtotal, Producto producto) {
        this.idTalle = idTalle;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
        this.producto = producto;
    }

    public int getIdTalle() { return idTalle; }
    public void setIdTalle(int idTalle) { this.idTalle = idTalle; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
