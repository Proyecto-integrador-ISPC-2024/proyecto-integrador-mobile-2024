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
import com.example.tiendadecampeones.utils.SharedPrefManager;

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SharedPrefManager sharedPrefManager = new SharedPrefManager(this);
        List<Product> cartProducts = sharedPrefManager.getCartProducts();

        Map<Product, Integer> cartItems = new HashMap<>();
        for (Product product : cartProducts) {
            cartItems.put(product, 10);
        }

        productList = new ArrayList<>(cartItems.keySet());

        cartResumeAdapter = new CartResumeAdapter(this, productList, cartItems);
        recyclerView.setAdapter(cartResumeAdapter);

        double totalAmount = calculateTotal(cartItems);
        totalTextView.setText(String.format("Total: $%.2f", totalAmount));

        confirmPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPaymentMethods();
            }
        });
    }

    private double calculateTotal(Map<Product, Integer> cartItems) {
        double totalAmount = 0;

        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            totalAmount += product.getProductos().getPrecio() * quantity;
        }

        return totalAmount;
    }

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

    private void navigateToPaymentMethods() {
        Intent intent = new Intent(CartResume.this, PaymentMethodsActivity.class);
        startActivity(intent);
    }
}
