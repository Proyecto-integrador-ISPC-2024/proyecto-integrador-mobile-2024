package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.CartResumeAdapter;
import com.example.tiendadecampeones.models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartResume extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartResumeAdapter cartResumeAdapter;
    private List<Product> productList;
    private TextView totalTextView;
    private Button confirmPurchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_resume);

        recyclerView = findViewById(R.id.recyclerViewCart);
        totalTextView = findViewById(R.id.totalPrice);
        confirmPurchaseButton = findViewById(R.id.confirmPurchaseButton);

        // Set RecyclerView layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get cart items (dummy data for now)
        Map<Product, Integer> cartItems = getCartItems();
        productList = new ArrayList<>(cartItems.keySet());

        // Initialize adapter and set it to RecyclerView
        cartResumeAdapter = new CartResumeAdapter(this, productList, cartItems);
        recyclerView.setAdapter(cartResumeAdapter);

        // Calculate total and set it
        double totalAmount = calculateTotal(cartItems);
        totalTextView.setText(String.format("Total: $%.2f", totalAmount));

        // Set up button to navigate to Payment Methods Activity
        confirmPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPaymentMethods();
            }
        });
    }

    // Simulated cart items with quantities
    private Map<Product, Integer> getCartItems() {
        Map<Product, Integer> cartItems = new HashMap<>();

        // Example products

        // Adding products with their quantities
       // cartItems.put(product1, 1);  // 1 unit of product 1
        //cartItems.put(product2, 2);  // 2 units of product 2

        return cartItems;
    }

    // Calculate total amount
    private double calculateTotal(Map<Product, Integer> cartItems) {
        double totalAmount = 0;

        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalAmount += product.getPrice() * quantity;
        }

        return totalAmount;
    }

    // Navigate to Payment Methods Activity
    private void navigateToPaymentMethods() {
        Intent intent = new Intent(CartResume.this, PaymentMethodsActivity.class);
        startActivity(intent);
    }

    // Navigation methods for bottom navbar
    public void vProducts(View v) {
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }

    public void vHome(View v) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void vProfile(View v) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
