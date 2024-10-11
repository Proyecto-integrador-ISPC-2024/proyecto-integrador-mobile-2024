package com.example.tiendadecampeones.models;

import com.google.gson.annotations.SerializedName;

public class UserLogInResponse {
    private Usuario usuario;
    private String token;


    @SerializedName("refresh_token")
    private String refresh;

    public void UserLoginResponse(Usuario usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refresh; }
    public void setRefreshToken(String refresh) { this.refresh = refresh; }
}
