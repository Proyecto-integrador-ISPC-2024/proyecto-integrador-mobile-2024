package com.example.tiendadecampeones.network;

import com.example.tiendadecampeones.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.PUT;
import retrofit2.http.Body;

public interface ApiInterface {

    @GET("pedidos")
    Call<List<Order>> getOrders();

    @PUT("pedidos/{id}")
    Call<Order> updateOrderStatus(@Path("id") int id, @Body Order order);
}