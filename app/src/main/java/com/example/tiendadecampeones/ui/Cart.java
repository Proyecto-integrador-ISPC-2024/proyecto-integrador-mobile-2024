package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
    private ArrayList<Product> productList;
    private TextView emptyCartTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeUI();
        loadCartProducts();
        setupRecyclerView();
        setupCheckoutButton();
        updateCartUI();
    }

    private void initializeUI() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, Home.class);
            startActivity(intent);
        });

        emptyCartTextView = findViewById(R.id.emptyCartTextView);
        checkoutButton = findViewById(R.id.endShop);
    }

    private void loadCartProducts() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        String productsJson = sharedPreferences.getString("cart_products", "[]");

        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {}.getType();
        productList = gson.fromJson(productsJson, productListType);
    }

    private void setupRecyclerView() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(productList, this);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void setupCheckoutButton() {
        checkoutButton.setOnClickListener(v -> {
            if (productList.isEmpty()) {
                Toast.makeText(this, "¡El carro está vacío!", Toast.LENGTH_SHORT).show();
            } else {
                // Pass the product list to CartResume via intent
                Intent intent = new Intent(Cart.this, CartResume.class);
                intent.putExtra("product_list", new Gson().toJson(productList));
                startActivity(intent);
            }
        });
    }

    public void updateCartUI() {
        if (productList.isEmpty()) {
            emptyCartTextView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            emptyCartTextView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
        }
    }
}
