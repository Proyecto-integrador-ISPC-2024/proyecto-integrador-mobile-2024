package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.ProductsAdapter;
import com.example.tiendadecampeones.models.CartItem;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import com.example.tiendadecampeones.viewmodel.CartViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;

    private CartViewModel cartVM;
    private TextView cartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        /* ───── ViewModel + badge ───── */
        cartVM   = new ViewModelProvider(this).get(CartViewModel.class);
        cartBadge = findViewById(R.id.cartBadge);
        ImageButton cartButton = findViewById(R.id.cartButton);

        cartVM.getCount().observe(this, n -> {
            if (n != null && n > 0) {
                cartBadge.setText(String.valueOf(n));
                cartBadge.setVisibility(View.VISIBLE);
            } else { cartBadge.setVisibility(View.GONE); }
        });
        refreshBadge();                                     // valor inicial
        cartButton.setOnClickListener(v ->
                startActivity(new Intent(this, Cart.class)));

        /* ---- Navegación superior ---- */
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        /* ---- Navegación inferior ---- */
        findViewById(R.id.homeButton).setOnClickListener(
                v -> startActivity(new Intent(this, Home.class)));

        findViewById(R.id.productsButton).setOnClickListener(
                v -> startActivity(new Intent(this, ProductCategories.class)));

        findViewById(R.id.profileBtn).setOnClickListener(
                v -> startActivity(new Intent(this, Profile.class)));

        /* ---- RecyclerView ---- */
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        String pais = getIntent().getStringExtra("pais");
        Log.d("ProductsActivity", "País seleccionado: " + pais);
        getProductosPorPais(pais);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshBadge();
    }

    /* ---------- helpers ---------- */

    private void refreshBadge() {
        SharedPreferences sp = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        String json = sp.getString("cart_items", "[]");

        List<CartItem> list = new Gson().fromJson(json,
                new TypeToken<List<CartItem>>(){}.getType());
        int qty = 0;
        if (list != null) for (CartItem ci : list) qty += ci.getCantidadCompra();

        cartVM.setCount(qty);
    }

    private void getProductosPorPais(String pais) {
        ApiService api = RetrofitClient.getClient(this).create(ApiService.class);

        api.getProductosPorPais(pais).enqueue(new Callback<List<Product>>() {
            @Override public void onResponse(Call<List<Product>> c, Response<List<Product>> r) {
                if (r.isSuccessful() && r.body() != null) {
                    List<Product> list = r.body();
                    productsAdapter = new ProductsAdapter(list, cartVM, ProductsActivity.this);
                    productsRecyclerView.setAdapter(productsAdapter);
                } else {
                    Toast.makeText(ProductsActivity.this,
                            "No hay producto " + pais, Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<List<Product>> c, Throwable t) {
                Toast.makeText(ProductsActivity.this,
                        "Error producto: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
