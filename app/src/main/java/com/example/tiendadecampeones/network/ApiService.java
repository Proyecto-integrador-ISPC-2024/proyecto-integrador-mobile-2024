package com.example.tiendadecampeones.network;

import com.example.tiendadecampeones.models.AccessTokenResponse;
import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.models.PaymentMethods;
import com.example.tiendadecampeones.models.Pedido;
import com.example.tiendadecampeones.models.Product;
//import com.example.tiendadecampeones.models.Product.Talle;
import com.example.tiendadecampeones.models.RefreshTokenRequest;
import com.example.tiendadecampeones.models.SalesResponse;
import com.example.tiendadecampeones.models.UserLogInResponse;
import com.example.tiendadecampeones.models.UserProfile;
import com.example.tiendadecampeones.models.RegisterResponse;
import com.example.tiendadecampeones.models.Usuario;

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
    Call<AccessTokenResponse> refreshToken(@Body RefreshTokenRequest request);

    @GET("productos/")
    Call<List<Product>> getProductos();

    @GET("productos/")
    Call<List<Product>> getProductosPorPais(@Query("pais") String pais);

    /*@GET("talles")
    Call<List<Size>> getTalles();*/

    @POST("pedidos/")
    Call<Pedido> realizarPedido(@Body Pedido pedido);

    @GET("pedidos/listar_metodopago")
    Call<PaymentMethods> getPaymentMethods();

    @GET("pedidos/")
    Call<List<Order>> getOrders();

    @DELETE("pedidos/{id}/")
    Call<Void> deleteOrder(@Path("id") int id_pedido);

    @PATCH("usuarios/{id}/")
    Call<UserProfile> updateProfile(@Path("id") int id,
                                    @Body UserProfile profile
    );

    @GET("pedidos/{id}/enviar/")
    Call<Void> sendOrder(@Path("id") int orderId);

    @GET("administrador/")
    Call<List<Usuario>> getAllUsuarios(@Query("incluir_inactivos") boolean incluirInactivos);

    @DELETE("administrador/{id}/")
    Call<Void> deleteUserFromAdministrador(@Path("id") int idUsuario);

    @GET("pedidos/calcular_ventas/")
    Call<SalesResponse> calcularVentas(
            @Query("fecha_inicio") String fechaInicio,
            @Query("fecha_fin") String fechaFin
    );
    @POST("administrador/")
    Call<RegisterResponse> createAdmin(@Body Map<String, String> userData);
}
