package com.example.tiendadecampeones.ui;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;


public class Cart extends AppCompatActivity implements UpdateTotalListener {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private List<Product> productList;
    private TextView totalTextView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Inicializar el botón de retroceso
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Inicializar el TextView para el total
        totalTextView = findViewById(R.id.totalPrice);





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
        productList = new ArrayList<>();
        productList.add(new Product("Camiseta Argentina 2021", "Description 1", 19.99, R.mipmap.argentina_2021_primera, 1,10));
        productList.add(new Product("Camiseta Brasil 2002", "Description 2", 29.99, R.mipmap.brasil_2002_primera, 2,10));
        productList.add(new Product("Camiseta Alemania 2014", "Description 3", 39.99, R.mipmap.alemania_2014_segunda, 2,10));

        // Set adapter
        cartAdapter = new CartAdapter(productList, this,this::onUpdateTotal);
        cartRecyclerView.setAdapter(cartAdapter);

        // Calcular el total al iniciar la actividad
        calculateTotalAmount();

        // Botón de finalización de compra
        Button endShopButton = findViewById(R.id.endShop);
        endShopButton.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, CartResume.class);
            Bundle bundle = new Bundle();

            // Crear la lista de cantidades
            ArrayList<Integer> quantityList = new ArrayList<>();
            for (Product product : productList) {
                quantityList.add(product.getQuantity());  // O usa la cantidad seleccionada
            }

            bundle.putSerializable("cart_items", (ArrayList<Product>) productList);
            bundle.putIntegerArrayList("cart_item_quantities", quantityList);  // Lista de cantidades

            intent.putExtras(bundle);
            startActivity(intent);
        });
    }
    // Método para calcular el total del carrito
        private void calculateTotalAmount() {
            double total = 0;
            for (Product product : productList) {
                total += product.getPrice() * product.getQuantity();  // Sumar el precio por la cantidad de cada producto
            }
            // Actualizar el TextView con el total calculado
            totalTextView.setText(String.format("Total: $%.2f", total));
        }
    // Implementación de UpdateTotalListener
    @Override
    public void onUpdateTotal() {
        calculateTotalAmount(); // Recalcular el total cuando se actualizan los productos
    }

    // Configurar botones de navegación
    private void setupNavigationButtons() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, Home.class);
            startActivity(intent);
        });

        Button productsButton = findViewById(R.id.productsButton);
        productsButton.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, ProductsActivity.class);
            startActivity(intent);
        });

        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, Profile.class);
            startActivity(intent);
        });

    }
}