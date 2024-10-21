package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.CartAdapter;
import com.example.tiendadecampeones.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Cart extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Botón de navegación superior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // Botones de navegación inferior
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Cart.this, Home.class);
                startActivity(intent);
            }
        });
        Button productsButton = findViewById(R.id.productsButton);
        productsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Cart.this, ProductsActivity.class);
                startActivity(intent);
            }
        });
        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Cart.this, Profile.class);
                startActivity(intent);
            }
        });

        // Initialize RecyclerView
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inicializar la lista de productos
        productList = new ArrayList<Product>();

        // Initialize RecyclerView
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Retrieve products from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("cart_prefs", MODE_PRIVATE);
        String productsJson = sharedPreferences.getString("cart_products", "[]");

        // Convert JSON to List<Product>
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {}.getType();
        productList = gson.fromJson(productsJson, productListType);

        // Set adapter
        cartAdapter = new CartAdapter(productList, this);
        cartRecyclerView.setAdapter(cartAdapter);


    }
}