package com.example.tiendadecampeones.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
//import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.ProductsAdapter;
import com.example.tiendadecampeones.models.Product;
import android.content.Intent;
import android.widget.ImageButton;

import com.example.tiendadecampeones.ui.Home;
import com.example.tiendadecampeones.ui.Profile;

//import com.example.tiendadecampeones.ui.CartActivity;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

//    private DrawerLayout drawerLayout;
    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        // Inicializo DrawerLayout - Sidenav no va a ser necesario en esta actividad
//        drawerLayout = findViewById(R.id.drawerLayout);

        // Side navigation button functionality
//        Button sideNavButton = findViewById(R.id.sideNavButton);
//        sideNavButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (drawerLayout.isDrawerOpen(findViewById(R.id.sideNav))) {
//                    drawerLayout.closeDrawer(findViewById(R.id.sideNav));
//                } else {
//                    drawerLayout.openDrawer(findViewById(R.id.sideNav));
//                }
//            }
//        });

        // Volver a la actividad anterior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Navegación al carro - CartActivity no definida todavía
//        ImageButton cartButton = findViewById(R.id.cartButton);
//        Button cartButton = findViewById(R.id.cartButton);
//        cartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ProductsActivity.this, CartActivity.class);
//                startActivity(intent);
//            }
//        });

        // Botones de navegación inferior
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, Home.class);
                startActivity(intent);
            }
        });

        Button productsButton = findViewById(R.id.productsButton);
        productsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Navegación a Products - No funcional ya que el usuario estaría en Products
            }
        });

        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductsActivity.this, Profile.class);
                startActivity(intent);
            }
        });

        // Inicializo RecyclerView para renderizar datos dinámicos
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Simulación de obtención de datos desde DB
        productList = new ArrayList<>();
        productList.add(new Product("Camiseta Argentina 2021", "Description 1", 19.99, R.mipmap.argentina_2021_primera));
        productList.add(new Product("Camiseta Brasil 2002", "Description 2", 29.99, R.mipmap.brasil_2002_primera));
        productList.add(new Product("Camiseta Alemania 2014", "Description 3", 39.99, R.mipmap.alemania_2014_segunda));

        productsAdapter = new ProductsAdapter(productList, this);
        productsRecyclerView.setAdapter(productsAdapter);

    }

}
