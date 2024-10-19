package com.example.tiendadecampeones.models;

import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("id")
    private int id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("apellido")
    private String apellido;

    @SerializedName("email")
    private String email;

    @SerializedName("domicilio")
    private String domicilio;

    @SerializedName("contraseña")
    private String contraseña;

    // Constructor
    public UserProfile(int id, String nombre, String apellido, String email, String domicilio, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.domicilio = domicilio;
        this.contraseña = contraseña;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getContraseña() {
        return contraseña;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
