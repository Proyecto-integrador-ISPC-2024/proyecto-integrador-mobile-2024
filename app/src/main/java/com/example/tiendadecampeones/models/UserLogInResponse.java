package com.example.tiendadecampeones.models;

public class UserLogInResponse {
    private Usuario usuario;
    private String token;

    public void UserLoginResponse(Usuario usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
