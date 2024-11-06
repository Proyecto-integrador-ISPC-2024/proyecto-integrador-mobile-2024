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
import com.example.tiendadecampeones.models.CartItem;
import com.example.tiendadecampeones.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<CartItem> cartItemList;
    private TextView emptyCartTextView, totalTextView;
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
        calculateTotal();
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
        totalTextView = findViewById(R.id.totalPrice);
        checkoutButton = findViewById(R.id.endShop);
    }

    private void loadCartProducts() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        String cartItemsJson = sharedPreferences.getString("cart_items", "[]");

        Gson gson = new Gson();
        Type cartItemListType = new TypeToken<List<CartItem>>() {}.getType();
        cartItemList = gson.fromJson(cartItemsJson, cartItemListType);
    }

    private void setupRecyclerView() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItemList, this);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void setupCheckoutButton() {
        checkoutButton.setOnClickListener(v -> {
            List<CartItem> filteredCartItemList = new ArrayList<>();
            for (CartItem cartItem : cartItemList) {
                if (cartItem.getCantidadCompra() > 0) {
                    filteredCartItemList.add(cartItem);
                }
            }
            if (filteredCartItemList.isEmpty()) {
                Toast.makeText(this, "¡El carro está vacío!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Cart.this, CartResume.class);
                String cartItemListJson = new Gson().toJson(filteredCartItemList);
                intent.putExtra("cart_item_list", cartItemListJson);
                startActivity(intent);
            }
        });
    }

    public void updateCartUI() {
        if (cartItemList.isEmpty()) {
            emptyCartTextView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            emptyCartTextView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
        }
    }

    public void calculateTotal() {
        double total = 0;
        for (CartItem cartItem : cartItemList) {
            total += cartItem.getProducto().getPrecio() * cartItem.getCantidadCompra(); // Calcular total con CartItem
        }
        totalTextView.setText("Total: $" + String.format("%.2f", total));
        if (total == 0 || cartItemList.isEmpty()) {
            checkoutButton.setEnabled(false);
        } else {
            checkoutButton.setEnabled(true);
        }
    }

    public void clearCartIfNotInCartActivities() {
        SharedPreferences cartPrefs = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        SharedPreferences.Editor cartEditor = cartPrefs.edit();

        String currentActivityName = this.getClass().getSimpleName();

        if (!currentActivityName.equals("Cart") &&
                !currentActivityName.equals("CartResume") &&
                !currentActivityName.equals("PaymentMethodsActivity")) {

            cartEditor.clear();
            cartEditor.apply();
        }
    }
}