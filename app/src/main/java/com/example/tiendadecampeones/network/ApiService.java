package com.example.tiendadecampeones.network;

import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.models.UserLogInResponse;
import com.example.tiendadecampeones.models.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("login/")
    @FormUrlEncoded
    Call<UserLogInResponse> login(@Field("email") String email, @Field("password") String password);

    @GET("productos/")
    Call<List<Product>> getProductos();

    @GET("pedidos")
    Call<List<Order>> getOrders();

    @DELETE("pedidos/{id}")
    Call<Void> cancelOrder(@Path("id") int id);

    @GET("user/profile/{email}")
    Call<UserProfile> getUserProfile(@Path("email") String email);

    @PUT("user/profile/{email}")
    Call<Void> updateUserProfile(@Path("email") String email, @Body UserProfile userProfile);

    @DELETE("user/profile/{email}")
    Call<Void> deleteUserProfile(@Path("email") String email);


    static ApiService create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://6656d1989f970b3b36c6a331.mockapi.io/") // Base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiService.class);
    }
}
