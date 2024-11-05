package com.example.tiendadecampeones.network;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.tiendadecampeones.models.AccessTokenResponse;
import com.example.tiendadecampeones.models.RefreshTokenRequest;
import com.example.tiendadecampeones.models.UserLogInResponse;
import com.example.tiendadecampeones.ui.LoginActivity;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;

public class AuthInterceptor implements Interceptor{
    private Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", null);
        String refreshToken = sharedPreferences.getString("refreshToken", null);

        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();

        if (token != null) {
            builder.header("Authorization", "Bearer " + token);
        }

        Response response = chain.proceed(builder.build());

        // Si el token está expirado, uso el refresh token para obtener uno nuevo
        if (response.code() == 401 && refreshToken != null) {
            // Llama a la API para refrescar el token
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://recdev.pythonanywhere.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService authService = retrofit.create(ApiService.class);

            // Creo el cuerpo de la solicitud para el refresh token
            RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest(refreshToken);
            Call<AccessTokenResponse> refreshCall = authService.refreshToken(refreshTokenRequest);

            retrofit2.Response<AccessTokenResponse> refreshResponse = refreshCall.execute();
            if (refreshResponse.isSuccessful()) {
                // Guardo el nuevo token y repito la solicitud original
                String newToken = refreshResponse.body().getAccessToken();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("accessToken", newToken);
                editor.apply();

                // Repite la solicitud original con el nuevo token
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + newToken)
                        .build();

                response.close(); // Cierro la respuesta anterior antes de hacer otra llamada
                return chain.proceed(newRequest);
            } else {
                redirectToLogin();
            }
        }

        return response;
    }

    private void redirectToLogin() {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }



}


