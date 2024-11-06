package com.example.tiendadecampeones.models;

import com.google.gson.annotations.SerializedName;

public class UserLogInResponse {
    @SerializedName("usuario")
    private Usuario usuario;

    @SerializedName("token")
    private String accessToken;

    @SerializedName("refresh_token")
    private String refreshToken;

    // Constructor vacío (requerido por Gson)
    public UserLogInResponse() {}

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
