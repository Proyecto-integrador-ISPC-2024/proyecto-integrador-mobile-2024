package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.CartAdapter;
import com.example.tiendadecampeones.models.Product;

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

        // Initialize product list (You can replace this with data from your backend)
        productList = new ArrayList<>();
        productList.add(new Product("Camiseta Argentina 2021", "Description 1", 19.99, R.mipmap.argentina_2021_primera, 1));
        productList.add(new Product("Camiseta Brasil 2002", "Description 2", 29.99, R.mipmap.brasil_2002_primera, 2));
        productList.add(new Product("Camiseta Alemania 2014", "Description 3", 39.99, R.mipmap.alemania_2014_segunda, 2));

        // Set adapter
        cartAdapter = new CartAdapter(productList, this);
        cartRecyclerView.setAdapter(cartAdapter);

        // Botón de finalización de compra
        Button endShopButton = findViewById(R.id.endShop);
        endShopButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Cart.this, CartResume.class);
                startActivity(intent);
            }
        });

    }
}