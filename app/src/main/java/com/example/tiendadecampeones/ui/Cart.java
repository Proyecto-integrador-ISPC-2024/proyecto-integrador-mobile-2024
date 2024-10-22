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
import com.example.tiendadecampeones.models.Pedido;
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

        // Setup for navigation buttons
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Inferior nav buttons setup
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> {
            Intent intent = new Intent(Cart.this, Home.class);
            startActivity(intent);
        });

        // RecyclerView setup
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load cart products from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        String productsJson = sharedPreferences.getString("cart_products", "[]");

        // Deserialize JSON into a list of products
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {}.getType();
        productList = gson.fromJson(productsJson, productListType);

        // Set the adapter for the RecyclerView
        cartAdapter = new CartAdapter(productList, this);
        cartRecyclerView.setAdapter(cartAdapter);

        // Handle checkout button
        Button checkoutButton = findViewById(R.id.endShop);
        checkoutButton.setOnClickListener(v -> {
            if (productList.isEmpty()) {
                Toast.makeText(this, "¡El carro está vacío!", Toast.LENGTH_SHORT).show();
            } else {
                // Create Pedido object for the checkout
                Pedido pedido = new Pedido();
                pedido.setIdUsuario(getUserIdFromPreferences());
                pedido.setTotal(calculateTotal(productList));
                pedido.setDetalles(buildDetallesFromCart(productList));

                // Send the order data to the CartResume activity
                Intent intent = new Intent(Cart.this, CartResume.class);
                intent.putExtra("pedido_data", gson.toJson(pedido)); // Serialize Pedido
                startActivity(intent);
            }
        });

        // Update cart UI based on contents
        updateCartUI();
    }

    // Retrieve user ID from SharedPreferences
    private int getUserIdFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        return sharedPref.getInt("id_usuario", -1);
    }

    // Build the list of details from the cart products
    private List<Pedido.Detalle> buildDetallesFromCart(List<Product> productList) {
        List<Pedido.Detalle> detalles = new ArrayList<>();
        for (Product product : productList) {
            Product.Producto productoDetails = product.getProductos(); // Access nested Producto object
            for (Product.Talle talle : product.getTalles()) {
                Pedido.Detalle detalle = new Pedido.Detalle();
                int selectedQuantity = talle.getCantidad();
                if (selectedQuantity > talle.getStock()) {
                    Toast.makeText(this, "La cantidad seleccionada para " + talle.getTalle() + " supera el stock disponible.", Toast.LENGTH_SHORT).show();
                    continue;
                }
                detalle.setCantidad(selectedQuantity);
                detalle.setSubtotal(productoDetails.getPrecio() * selectedQuantity);  // Use productoDetails to access price
                detalles.add(detalle);
            }
        }
        return detalles;
    }

    // Calculate the total price of the cart
    private double calculateTotal(List<Product> productList) {
        double total = 0;
        for (Product product : productList) {
            Product.Producto productoDetails = product.getProductos();  // Access nested Producto object
            for (Product.Talle talle : product.getTalles()) {
                int selectedQuantity = talle.getCantidad();
                total += productoDetails.getPrecio() * selectedQuantity;  // Use productoDetails to access price
            }
        }
        return total;
    }

    // Update the UI to reflect cart state
    public void updateCartUI() {
        TextView emptyCartTextView = findViewById(R.id.emptyCartTextView);
        Button checkoutButton = findViewById(R.id.endShop);

        if (productList.isEmpty()) {
            emptyCartTextView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            emptyCartTextView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
        }
    }
}
