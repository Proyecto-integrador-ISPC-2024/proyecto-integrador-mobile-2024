package com.example.tiendadecampeones.network;

import java.util.List;
import retrofit2.Call;  // tipo de retorno
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import com.example.tiendadecampeones.models.Product; //importar modelos
import com.example.tiendadecampeones.models.UserLogInResponse;

public interface ApiService {

    @POST("login/")
    @FormUrlEncoded
    Call<UserLogInResponse> login(@Field("email") String email, @Field("password") String password);

    // Aqui definimos los endpoints
    @GET("productos/")
    Call<List<Product>> getProductos();
}
