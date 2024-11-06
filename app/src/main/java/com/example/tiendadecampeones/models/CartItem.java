package com.example.tiendadecampeones.models;

import java.util.Objects;

public class CartItem {
    private int id_producto_talle;
    private Product.Producto producto;
    private Product.Talle talle;
    private int cantidadCompra;

    public CartItem(int id_producto_talle, Product.Producto producto, Product.Talle talle) {
        this.id_producto_talle = id_producto_talle;
        this.producto = producto;
        this.talle = talle;
        this.cantidadCompra = 1; // Inicializa la cantidad en 1
    }

    // Getters y Setters
    public int getIdProductoTalle() {
        return id_producto_talle;
    }

    public Product.Producto getProducto() {
        return producto;
    }

    public Product.Talle getTalle() {
        return talle;
    }

    public int getCantidadCompra() {
        return cantidadCompra;
    }

    public void setCantidadCompra(int cantidad) {
        this.cantidadCompra = cantidad;
    }

    public void incrementQuantity() {
        this.cantidadCompra++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem cartItem = (CartItem) o;
        return id_producto_talle == cartItem.id_producto_talle &&
                producto.getIdProducto() == cartItem.producto.getIdProducto();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_producto_talle, producto.getIdProducto());
    }
}