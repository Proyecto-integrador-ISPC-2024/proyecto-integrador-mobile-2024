// ProductsActivity.java
package com.example.tiendadecampeones.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.ProductsAdapter;
import com.example.tiendadecampeones.models.Product;
import android.content.Intent;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private List<Product> productList;

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

        // Simulando datos obtenidos desde la BD
        productList = new ArrayList<>();
        productList.add(new Product("Camiseta Argentina 2021", "Description 1", 19.99, R.mipmap.argentina_2021_primera, 1,10));
        productList.add(new Product("Camiseta Brasil 2002", "Description 2", 29.99, R.mipmap.brasil_2002_primera, 2,10));
        productList.add(new Product("Camiseta Alemania 2014", "Description 3", 39.99, R.mipmap.alemania_2014_segunda, 2,10));

        productsAdapter = new ProductsAdapter(productList, this);
        productsRecyclerView.setAdapter(productsAdapter);
    }
}
