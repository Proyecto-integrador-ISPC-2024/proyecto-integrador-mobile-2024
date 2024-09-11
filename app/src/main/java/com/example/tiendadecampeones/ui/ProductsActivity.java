package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.ProductsAdapter;
import com.example.tiendadecampeones.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // Inicializo DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);

        // Inicializo botón de menú de navegación lateral
        Button sideNavButton = findViewById(R.id.sideNavButton);
        sideNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(findViewById(R.id.sideNav))) {
                    drawerLayout.closeDrawer(findViewById(R.id.sideNav));
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.sideNav));
                }
            }
        });

        // Inicializo RecyclerView para renderizar datos dinámicos
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Simulación de obtención de datos desde DB
        productList = new ArrayList<>();
        productList.add(new Product("Product 1", "Description 1", 19.99, R.mipmap.argentina_2021_primera));
        productList.add(new Product("Product 2", "Description 2", 29.99, R.mipmap.brasil_2002_primera));
        productList.add(new Product("Product 3", "Description 3", 39.99, R.mipmap.alemania_2014_segunda));

        productsAdapter = new ProductsAdapter(productList);
        productsRecyclerView.setAdapter(productsAdapter);

        // Floating Action Button to navigate to the cart
        FloatingActionButton fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to CartActivity
                Intent intent = new Intent(ProductsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });


    }
}
