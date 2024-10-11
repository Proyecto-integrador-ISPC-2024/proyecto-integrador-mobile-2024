package com.example.tiendadecampeones.ui;

import static com.example.tiendadecampeones.R.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.ProductsAdapter;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.models.Size;
import com.example.tiendadecampeones.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private List<Product> carrito = new ArrayList<>();
    private List<String> tallesStringList = new ArrayList<>();
    private String selectedPais;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // Botones de navegación superior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, Cart.class);
                startActivity(intent);
            }
        });

        // Botones de navegación inferior
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, Home.class);
                startActivity(intent);
            }
        });

        Button productsButton = findViewById(R.id.productsButton);
        productsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // User is already on the ProductsActivity
            }
        });

        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, Profile.class);
                startActivity(intent);
            }
        });

        // Seteo del recycler view
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String selectedPais = getIntent().getStringExtra("pais");
        Log.d("ProductsActivity", "País seleccionado: " + selectedPais);
        getProductosPorPais(selectedPais);

        selectedPais = getIntent().getStringExtra("pais");
        Log.d("ProductsActivity", "País seleccionado: " + selectedPais);


        getTalles();
    }


    private void getProductosPorPais(String pais) {

        ApiService apiService = ApiService.create();

        Call<List<Product>> call = apiService.getProductosPorPais(pais);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body();
                    Log.d("ProductsActivity", "Productos recibidos: " + productList.size());
                    productsAdapter = new ProductsAdapter(productList, ProductsActivity.this);
                    productsRecyclerView.setAdapter(productsAdapter);
                } else {
                    Log.e("ProductsActivity", "Error en la respuesta: " + response.message());
                    Toast.makeText(ProductsActivity.this, "No hay producto " + pais, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProductsActivity.this, "Error producto: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getTalles() {
        ApiService apiService = ApiService.create();
        Call<List<Size>> call = apiService.getTalles();

        call.enqueue(new Callback<List<Size>>() {
            @Override
            public void onResponse(Call<List<Size>> call, Response<List<Size>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Size> tallesList = response.body();

                    // Convertir la lista de objetos Size a una lista de Strings
                    for (Size size : tallesList) {
                        tallesStringList.add(size.getTalle());
                    }
                    // Cargar productos después de obtener los talles
                    getProductosPorPais(selectedPais);
                } else {
                    Toast.makeText(ProductsActivity.this, "Error al obtener los talles", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Size>> call, Throwable t) {
                Toast.makeText(ProductsActivity.this, "Error en la solicitud: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}