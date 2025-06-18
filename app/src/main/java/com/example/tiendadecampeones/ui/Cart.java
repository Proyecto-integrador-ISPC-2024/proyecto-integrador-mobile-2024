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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.CartAdapter;
import com.example.tiendadecampeones.models.CartItem;
import com.example.tiendadecampeones.viewmodel.CartViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class Cart extends AppCompatActivity {

    private CartViewModel cartVM;
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private ArrayList<CartItem> cartItemList;
    private TextView emptyCartTextView, totalTextView;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartVM = new ViewModelProvider(this).get(CartViewModel.class);

        initUI();
        loadCartProducts();
        cartVM.setCount(getTotalQty());          // sincroniza badge
        setupRecyclerView();
        setupCheckoutButton();
        updateCartUI();
        calculateTotal();
    }

    /* ---------- utilidades ---------- */

    private void initUI() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        emptyCartTextView = findViewById(R.id.emptyCartTextView);
        totalTextView     = findViewById(R.id.totalPrice);
        checkoutButton    = findViewById(R.id.endShop);
    }

    private void loadCartProducts() {
        SharedPreferences sp = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        String json = sp.getString("cart_items", "[]");

        cartItemList = new Gson().fromJson(json,
                new TypeToken<List<CartItem>>(){}.getType());
        if (cartItemList == null) cartItemList = new ArrayList<>();
    }

    private int getTotalQty() {
        int q = 0;
        for (CartItem ci : cartItemList) q += ci.getCantidadCompra();
        return q;
    }

    private void setupRecyclerView() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartAdapter = new CartAdapter(cartItemList, this, cartVM);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void setupCheckoutButton() {
        checkoutButton.setOnClickListener(v -> {
            List<CartItem> filtered = new ArrayList<>();
            for (CartItem ci : cartItemList)
                if (ci.getCantidadCompra() > 0) filtered.add(ci);

            if (filtered.isEmpty()) {
                Toast.makeText(this, "¡El carro está vacío!", Toast.LENGTH_SHORT).show();
            } else {
                Intent i = new Intent(this, CartResume.class);
                i.putExtra("cart_item_list", new Gson().toJson(filtered));
                startActivity(i);
            }
        });
    }

    /* UI helpers */

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
        for (CartItem ci : cartItemList)
            total += ci.getProducto().getPrecio() * ci.getCantidadCompra();

        totalTextView.setText("Total: $" + String.format("%.2f", total));
        checkoutButton.setEnabled(total > 0);
    }

    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void homeButton(View v) {
        Toast.makeText(this, "¡ Home !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void productsButton(View v) {
        Toast.makeText(this, "¡ Nuestros Productos !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProductCategories.class);
        startActivity(intent);
    }
}