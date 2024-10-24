package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.util.Log;
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

    // Eliminación de ítems del carro si el usuario no está en esta actividad
    @Override
    protected void onPause() {
        super.onPause();
        clearCartIfNotInCartActivities();
    }

    // Inicialización y captura de elementos de UI
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

    // Obtención de productos del carro mediante SharedPreferences
    private void loadCartProducts() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        String productsJson = sharedPreferences.getString("cart_products", "[]");
        Log.d("Cart", "JSON de productos del carrito: " + productsJson);

        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {}.getType();
        productList = gson.fromJson(productsJson, productListType);
    }

    // Seteo de información para el recycler view
    private void setupRecyclerView() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(productList, this);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    // Verificación de estado del carro antes de avanzar hacia otra actividad
    private void setupCheckoutButton() {
        checkoutButton.setOnClickListener(v -> {
            List<Product> filteredProductList = new ArrayList<>();
            for (Product product : productList) {
                boolean hasPositiveQuantity = false;
                for (Product.Talle talle : product.getTalles()) {
                    if (talle.getCantidadCompra() > 0) {
                        hasPositiveQuantity = true;
                        break;
                    }
                }
                if (hasPositiveQuantity) {
                    filteredProductList.add(product);
                }
            }
            if (filteredProductList.isEmpty()) {
                Toast.makeText(this, "¡El carro está vacío!", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Cart.this, CartResume.class);
                intent.putExtra("product_list", new Gson().toJson(filteredProductList));
                startActivity(intent);
            }
        });
    }

    // Dinamismo en la UI del carro dependiendo de la cantidad de productos existentes en el mismo
    public void updateCartUI() {
        if (productList.isEmpty()) {
            emptyCartTextView.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            emptyCartTextView.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
        }
    }

    // Cálculo de monto total
    public void calculateTotal() {
        double total = 0;
        for (Product product : productList) {
            double subtotal = 0;
            for (Product.Talle talle : product.getTalles()) {
                subtotal += product.getProductos().getPrecio() * talle.getCantidadCompra();
            }
            total += subtotal;
        }
        totalTextView.setText("Total: $" + String.format("%.2f", total));
        if (total == 0 || productList.isEmpty()) {
            checkoutButton.setEnabled(false);
        } else {
            checkoutButton.setEnabled(true);
        }
    }

    // Definición de limpieza de carro si no se está en esta actividad o en las siguientes en la cadena del proceso de compra
    public void clearCartIfNotInCartActivities() {
        SharedPreferences cartPrefs = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        SharedPreferences.Editor cartEditor = cartPrefs.edit();

        String currentActivityName = this.getClass().getSimpleName();
        Log.d("Cart", "Nombre de la actividad actual: " + currentActivityName);

        if (!currentActivityName.equals("Cart") &&
                !currentActivityName.equals("CartResume") &&
                !currentActivityName.equals("PaymentMethodsActivity")) {

            Log.d("Cart", "Limpiando el carrito...");
            cartEditor.clear();
            cartEditor.apply();
            Log.d("Cart", "El carrito ha sido limpiado"); // Confirmación adicional
        }
    }
}
