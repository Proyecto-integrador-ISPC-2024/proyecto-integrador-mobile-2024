package com.example.tiendadecampeones.models;

import com.google.gson.annotations.SerializedName;

public class Size {

    @SerializedName("id")
    private int idTalle;

    @SerializedName("talle")
    private String talle;

    // Constructor
    public Size(int idTalle, String talle) {
        this.idTalle = idTalle;
        this.talle = talle;
    }

    // Getters y Setters
    public int getIdTalle() {
        return idTalle;
    }

    public void setIdTalle(int idTalle) {
        this.idTalle = idTalle;
    }

    public String getTalle() {
        return talle;
    }

    public void setTalle(String talle) {
        this.talle = talle;
    }
}