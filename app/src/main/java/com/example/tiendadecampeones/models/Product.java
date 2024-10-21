package com.example.tiendadecampeones.models;

import java.util.List;

public class Product {

    private int id_producto_talle;
    private Producto productos;
    private List<Talle> talles;
    private int stock;

    // Constructor
    public Product(int id_producto_talle, Producto productos, List<Talle> talles, int stock) {
        this.id_producto_talle = id_producto_talle;
        this.productos = productos;
        this.talles = talles;
        this.stock = stock;  // Inicializar el stock
    }

    // Getters y Setters
    public int getIdProductoTalle() {
        return id_producto_talle;
    }

    public void setIdProductoTalle(int id_producto_talle) {
        this.id_producto_talle = id_producto_talle;
    }

    public Producto getProductos() {
        return productos;
    }

    public void setProductos(Producto productos) {
        this.productos = productos;
    }

    public List<Talle> getTalles() {
        return talles;
    }

    public void setTalles(List<Talle> talles) {
        this.talles = talles;
    }

    public int getStock() {
        return stock;
    }

    // Clase Producto
    public static class Producto {
        private int id_producto;
        private String nombre_producto;
        private double precio;
        private String imagen;

        // Constructor
        public Producto(int id_producto, String nombre_producto, double precio, String imagen) {
            this.id_producto = id_producto;
            this.nombre_producto = nombre_producto;
            this.precio = precio;
            this.imagen = imagen;
        }

        // Getters y Setters
        public int getIdProducto() {
            return id_producto;
        }

        public void setIdProducto(int id_producto) {
            this.id_producto = id_producto;
        }

        public String getNombreProducto() {
            return nombre_producto;
        }

        public void setNombreProducto(String nombre_producto) {
            this.nombre_producto = nombre_producto;
        }

        public double getPrecio() {
            return precio;
        }

        public void setPrecio(double precio) {
            this.precio = precio;
        }

        public String getImagen() {
            return imagen;
        }

        public void setImagen(String imagen) {
            this.imagen = imagen;
        }
    }

    // Clase Talle
    public static class Talle {
        private int id_talle;
        private String talle;
        private int stock;

        // Constructor
        public Talle(int id_talle, String talle, int stock) {
            this.id_talle = id_talle;
            this.talle = talle;
            this.stock = stock;
        }

        // Getters y Setters
        public int getIdTalle() {
            return id_talle;
        }

        public void setIdTalle(int id_talle) {
            this.id_talle = id_talle;
        }

        public String getTalle() {
            return talle;
        }

        public void setTalle(String talle) {
            this.talle = talle;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }
}
