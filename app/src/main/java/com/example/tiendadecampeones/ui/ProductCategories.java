package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.CartItem;
import com.example.tiendadecampeones.viewmodel.CartViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class ProductCategories extends AppCompatActivity {

    private CartViewModel cartVM;
    private TextView cartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_categories);

        /* ───── ViewModel + badge ───── */
        cartVM    = new ViewModelProvider(this).get(CartViewModel.class);
        cartBadge = findViewById(R.id.cartBadge);
        ImageButton cartButton = findViewById(R.id.cartButton);

        cartVM.getCount().observe(this, n -> {
            if (n != null && n > 0) {
                cartBadge.setText(String.valueOf(n));
                cartBadge.setVisibility(View.VISIBLE);
            } else { cartBadge.setVisibility(View.GONE); }
        });
        refreshBadge();                                      // valor inicial
        cartButton.setOnClickListener(v ->
                startActivity(new Intent(this, Cart.class)));

        /* ───── Navegación superior ───── */
        findViewById(R.id.backButton).setOnClickListener(v -> finish());

        /* ───── Navegación inferior ───── */
        findViewById(R.id.homeButton).setOnClickListener(
                v -> startActivity(new Intent(this, Home.class)));

        findViewById(R.id.productsButton).setOnClickListener(
                v -> startActivity(new Intent(this, ProductCategories.class)));

        findViewById(R.id.profileButton).setOnClickListener(
                v -> startActivity(new Intent(this, Profile.class)));

        /* ───── Botones de país ───── */
        setupCountryButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshBadge();
    }

    /* ---------- helpers ---------- */

    private void refreshBadge() {
        SharedPreferences sp = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        String json = sp.getString("cart_items", "[]");

        List<CartItem> list = new Gson().fromJson(json,
                new TypeToken<List<CartItem>>(){}.getType());
        int qty = 0;
        if (list != null) for (CartItem ci : list) qty += ci.getCantidadCompra();

        cartVM.setCount(qty);
    }

    private void setupCountryButtons() {
        /* Mapea ID ↔ País y registra listener */
        int[][] mapping = {
                {R.id.buttonGermany,  R.string.products_germany },
                {R.id.buttonArgentina,R.string.products_argentina},
                {R.id.buttonFrance,   R.string.products_france },
                {R.id.buttonUruguay,  R.string.products_uruguay},
                {R.id.buttonEngland,  R.string.products_england},
                {R.id.buttonItaly,    R.string.products_italy  },
                {R.id.buttonSpain,    R.string.products_spain  },
                {R.id.buttonBrazil,   R.string.products_brazil }
        };

        for (int[] map : mapping) {
            ImageButton btn = findViewById(map[0]);
            String pais   = getString(map[1]);
            btn.setOnClickListener(v -> openProductList(pais));
        }
    }

    private void openProductList(String pais) {
        Intent i = new Intent(this, ProductsActivity.class);
        i.putExtra("pais", pais);
        startActivity(i);
    }
}
