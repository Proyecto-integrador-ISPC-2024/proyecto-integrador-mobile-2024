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

        // Obtener los productos del carrito desde el Intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null) {
            productList = (ArrayList<Product>) bundle.getSerializable("cart_items");
            ArrayList<Integer> quantityList = bundle.getIntegerArrayList("cart_item_quantities");

            // Verificar si las listas están vacías
            if (productList != null && quantityList != null && !productList.isEmpty() && !quantityList.isEmpty()) {

                // Crear el mapa de productos con cantidades
                Map<Product, Integer> cartItemsMap = new HashMap<>();
                for (int i = 0; i < productList.size(); i++) {
                    cartItemsMap.put(productList.get(i), quantityList.get(i));  // Mapear producto con su cantidad
                }

                // Configurar el adaptador con productos y el mapa de cantidades
                cartResumeAdapter = new CartResumeAdapter(this, productList, cartItemsMap);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(cartResumeAdapter);

                // Calcular el total
                double totalAmount = calculateTotal(productList, quantityList);
                totalTextView.setText(String.format("Total: $%.2f", totalAmount));
            } else {
                // Si el carrito está vacío, mostrar mensaje y cambiar funcionalidad del botón
                totalTextView.setText("No tienes productos seleccionados.");
                confirmPurchaseButton.setText("Ver productos");
                confirmPurchaseButton.setOnClickListener(v -> vProducts(v));
            }
        } else {
            // Manejar el caso donde el Bundle es nulo
            totalTextView.setText("Error retrieving cart items.");
            confirmPurchaseButton.setText("Ver productos");
            confirmPurchaseButton.setOnClickListener(v -> vProducts(v));
        }
    }



    // Calcular el total basado en los productos
    private double calculateTotal(List<Product> products, List<Integer> quantities){
        double totalAmount = 0;
        for (int i = 0; i < products.size(); i++) {
            totalAmount += products.get(i).getPrice() * quantities.get(i);
        }
        return totalAmount;
    }

    // Navegar a la actividad de métodos de pago
    private void navigateToPaymentMethods() {
        Intent intent = new Intent(CartResume.this, PaymentMethodsActivity.class);

        // Obtener el total desde el TextView
        String total = totalTextView.getText().toString();
        intent.putExtra("TOTAL_PRICE", total);
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


