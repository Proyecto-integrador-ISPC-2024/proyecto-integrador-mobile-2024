package com.example.tiendadecampeones.models;

public class FormaDePago {
    private String descripcion;

    public FormaDePago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
