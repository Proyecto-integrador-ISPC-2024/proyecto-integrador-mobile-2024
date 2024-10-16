package com.example.tiendadecampeones.network;

import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.models.PaymentMethods;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.models.UserLogInResponse;

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
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;


public interface ApiService {

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

    @GET("pedidos/")
    Call<List<Order>> getOrders(
            @Header("Authorization") String authToken
    );

    @DELETE("pedidos/{id}")
    Call<Void> cancelOrder(
            @Header("Authorization") String authToken,
            @Path("id") int id_pedido
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
