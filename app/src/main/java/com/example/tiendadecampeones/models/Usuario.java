package com.example.tiendadecampeones.models;

public class Usuario {
    private int id_usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String domicilio;
    private String rol;
    private boolean is_active;
    private boolean is_staff;
    private boolean is_superuser;

    public Usuario(int id_usuario, String nombre, String apellido, String email, String domicilio, String rol) {
        this.id_usuario = id_usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.domicilio = domicilio;
        this.rol = rol;
    }

    public int getIdUsuario() { return id_usuario; }
    public void setIdUsuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public boolean isActive() { return is_active; }
    public void setActive(boolean active) { is_active = active; }
    public boolean isStaff() { return is_staff; }
    public void setStaff(boolean staff) { is_staff = staff; }
    public boolean isSuperuser() { return is_superuser; }
    public void setSuperuser(boolean superuser) { is_superuser = superuser; }
}
