package com.example.tiendadecampeones.network;

import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.models.PaymentMethods;
import com.example.tiendadecampeones.models.Pedido;
import com.example.tiendadecampeones.models.Product;
//import com.example.tiendadecampeones.models.Product.Talle;
import com.example.tiendadecampeones.models.UserLogInResponse;
import com.example.tiendadecampeones.models.UserProfile;
import com.example.tiendadecampeones.models.RegisterResponse;


import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;


public interface ApiService {

    @POST("usuarios/")
    Call<RegisterResponse> register(@Body Map<String, String> userData);


    @POST("login/")
    @FormUrlEncoded
    Call<UserLogInResponse> login(@Field("email") String email, @Field("password") String password);

//    @POST("api/login/")
//    Call<UserLogInResponse> token(@Body Map<String, String> loginData);


    @POST("api/token/refresh/")
    Call<UserLogInResponse> refreshToken(@Header("Authorization") String refresh);

    @GET("productos/")
    Call<List<Product>> getProductos();

    @GET("productos/")
    Call<List<Product>> getProductosPorPais(@Query("pais") String pais);

    /*@GET("talles")
    Call<List<Size>> getTalles();*/

    @POST("pedidos")
    Call<Pedido> realizarPedido(@Header("Authorization") String authToken, @Body Pedido pedido);

    @GET("pedidos/listar_metodopago")
    Call<PaymentMethods> getPaymentMethods(@Header("Authorization") String token);

    @GET("pedidos/")
    Call<List<Order>> getOrders(
            @Header("Authorization") String authToken
    );

    @DELETE("pedidos/{id}/")
    Call<Void> deleteOrder(
            @Header("Authorization") String authToken,
            @Path("id") int id_pedido
    );
    @PATCH("usuarios/{id}/")
    Call<UserProfile> updateProfile(@Path("id") int id,
            @Header("Authorization") String token,
            @Body UserProfile profile
    );

    static ApiService create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://recdev.pythonanywhere.com/") // Base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }

    Call<PaymentMethods> getPaymentMethods();
}
