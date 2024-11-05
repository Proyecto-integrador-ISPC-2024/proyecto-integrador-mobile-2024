package com.example.tiendadecampeones.models;
import com.google.gson.annotations.SerializedName;

public class AccessTokenResponse {
        @SerializedName("access")
        private String accessToken;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }
}

