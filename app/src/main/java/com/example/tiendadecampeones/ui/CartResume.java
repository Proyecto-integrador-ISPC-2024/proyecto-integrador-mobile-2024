package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.CartResumeAdapter;
import com.example.tiendadecampeones.models.Pedido;
import com.example.tiendadecampeones.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
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

        initializeUI();

        // Receive product list from the Cart Activity
        Intent intent = getIntent();
        String productListJson = intent.getStringExtra("product_list");

        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {}.getType();
        productList = gson.fromJson(productListJson, productListType);

        setupRecyclerView();

        Pedido pedido = createPedidoFromCart();
        System.out.println(pedido.getDetalles().get(0));

        double totalAmount = pedido.getTotal();
        totalTextView.setText(String.format("Total: $%.2f", totalAmount));

        confirmPurchaseButton.setOnClickListener(v -> navigateToPaymentMethods());
    }

    private void initializeUI() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewCart);
        totalTextView = findViewById(R.id.totalPrice);
        confirmPurchaseButton = findViewById(R.id.confirmPurchaseButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupRecyclerView() {
        Map<Product, Integer> cartItems = new HashMap<>();
        for (Product product : productList) {
            for (Product.Talle talle : product.getTalles()) {
                if (talle.getCantidadCompra() > 0) {
                    cartItems.put(product, talle.getCantidadCompra());
                }
            }
        }

        cartResumeAdapter = new CartResumeAdapter(this, productList, cartItems);
        recyclerView.setAdapter(cartResumeAdapter);
    }

    // Create Pedido object with product details
    private Pedido createPedidoFromCart() {
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(getUserIdFromPreferences());
        pedido.setTotal(calculateTotal(productList));
        pedido.setDetalles(buildDetallesFromCart(productList));
        return pedido;
    }

    private int getUserIdFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        return sharedPref.getInt("id_usuario", -1);
    }

    private double calculateTotal(List<Product> productList) {
        double total = 0;
        for (Product product : productList) {
            for (Product.Talle talle : product.getTalles()) {
                total += product.getProductos().getPrecio() * talle.getCantidadCompra();
            }
        }
        return total;
    }

    private List<Pedido.Detalle> buildDetallesFromCart(List<Product> productList) {
        List<Pedido.Detalle> detalles = new ArrayList<>();

        for (Product product : productList) {
            for (Product.Talle talle : product.getTalles()) {
                Pedido.Detalle detalle = new Pedido.Detalle();
                detalle.setCantidad(talle.getCantidadCompra());
                detalle.setSubtotal(product.getProductos().getPrecio() * talle.getCantidadCompra());
                // detalle.setIdProductoTalle(product.getIdProductoTalle()); // Assuming we also want to set this for each item.
                detalles.add(detalle);
            }
        }
        System.out.println(detalles);
        return detalles;
    }

    private void navigateToPaymentMethods() {
        Intent intent = new Intent(CartResume.this, PaymentMethodsActivity.class);
        startActivity(intent);
    }
}
