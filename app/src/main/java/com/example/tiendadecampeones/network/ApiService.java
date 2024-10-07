package com.example.tiendadecampeones.network;

import java.util.List;
import retrofit2.Call;  // tipo de retorno
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import com.example.tiendadecampeones.models.Product; //importar modelos

public interface ApiService {

    // Aqui definimos los endpoints
    @GET("productos/")
    Call<List<Product>> getProductos();
}
