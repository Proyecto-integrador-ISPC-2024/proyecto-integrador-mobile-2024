package com.example.tiendadecampeones.models;

public class RefreshTokenRequest {

    private String refresh;

    public RefreshTokenRequest(String refresh) {
        this.refresh = refresh;
    }

    public String getRefresh() {
        return refresh;
    }

    public void setRefresh(String refresh) {
        this.refresh = refresh;
    }
}

