package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.CartItem;
import com.example.tiendadecampeones.viewmodel.CartViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class Home extends AppCompatActivity {

    private CartViewModel cartVM;
    private TextView cartBadge;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* ───── ViewModel + badge ───── */
        cartVM    = new ViewModelProvider(this).get(CartViewModel.class);
        cartBadge = findViewById(R.id.cartBadge);
        ImageButton cartButton = findViewById(R.id.cartButton);

        cartVM.getCount().observe(this, n -> {
            if (n != null && n > 0) {
                cartBadge.setText(String.valueOf(n));
                cartBadge.setVisibility(View.VISIBLE);
            } else {
                cartBadge.setVisibility(View.GONE);
            }
        });

        refreshBadgeFromPrefs();                         // valor inicial
        cartButton.setOnClickListener(v ->
                startActivity(new Intent(this, Cart.class)));

        /* ───── Resto de UI / navegación ───── */
        setupMenusAndButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshBadgeFromPrefs();                         // al volver de otras pantallas
    }

    /* ╔══════════════════════════════════════════════╗
       ║  Sincroniza ViewModel con SharedPreferences  ║
       ╚══════════════════════════════════════════════╝ */
    private void refreshBadgeFromPrefs() {
        SharedPreferences sp = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        String json = sp.getString("cart_items", "[]");

        List<CartItem> list = new Gson().fromJson(json,
                new TypeToken<List<CartItem>>(){}.getType());

        int qty = 0;
        if (list != null)
            for (CartItem ci : list) qty += ci.getCantidadCompra();

        cartVM.setCount(qty);
    }

    /* ╔══════════════════════════════════════════════╗
       ║   Código de navegación que ya tenías antes   ║
       ╚══════════════════════════════════════════════╝ */
    private void setupMenusAndButtons() {

        // Mensaje de bienvenida (idéntico al original)
        Intent intent = getIntent();
        String nombreUsuario = intent.getStringExtra("nombreUsuario");
        if (intent.getBooleanExtra("mostrarBienvenida", false)) {
            new AlertDialog.Builder(this)
                    .setTitle("Bienvenido de vuelta")
                    .setMessage("Hola, " + nombreUsuario + "!")
                    .setPositiveButton("Continuar", (d, w) -> d.dismiss())
                    .show();
            intent.removeExtra("mostrarBienvenida");
        }

        drawerLayout = findViewById(R.id.main);

        /* ---- Top bar ---- */
        ImageButton sideNavButton = findViewById(R.id.sideNavButton);
        sideNavButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START);
            else
                drawerLayout.openDrawer(GravityCompat.START);
        });

        /* ---- Navigation Drawer ---- */
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_products)           startActivity(new Intent(this, ProductCategories.class));
            else if (id == R.id.nav_cart)          startActivity(new Intent(this, Cart.class));
            else if (id == R.id.nav_about)         startActivity(new Intent(this, AboutUs.class));
            else if (id == R.id.nav_contact)       startActivity(new Intent(this, Contact.class));
            else if (id == R.id.nav_profile)       startActivity(new Intent(this, Profile.class));
            else if (id == R.id.nav_dashboard)     openDashboardOrAdmin();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        /* ---- Botón central ---- */
        findViewById(R.id.buyButton).setOnClickListener(
                v -> startActivity(new Intent(this, ProductCategories.class)));

        /* ---- Bottom Navigation ---- */
        findViewById(R.id.homeButton).setOnClickListener(
                v -> Toast.makeText(this, "Actualmente en home", Toast.LENGTH_SHORT).show());

        findViewById(R.id.productsButton).setOnClickListener(
                v -> startActivity(new Intent(this, ProductCategories.class)));

        findViewById(R.id.profileBtn).setOnClickListener(
                v -> startActivity(new Intent(this, Profile.class)));
    }

    /* Helper admin/dashboard */
    private void openDashboardOrAdmin() {
        SharedPreferences prefs = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String role = prefs.getString("userRole", "");
        boolean isStaff = prefs.getBoolean("isStaff", false);
        boolean isSuperuser = prefs.getBoolean("isSuperuser", false);

        boolean isAdmin      = "ADMIN".equals(role) && isStaff;
        boolean isSuperAdmin = isAdmin && isSuperuser;

        if (isAdmin || isSuperAdmin) {
            Toast.makeText(this, "Redireccionando al panel de administración", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AdminListUsersActivity.class));
        } else {
            Toast.makeText(this, "Redireccionando a tu dashboard", Toast.LENGTH_SHORT).show();
            Intent dash = new Intent(this, Dashboard.class);
            dash.putExtra("ORIGIN", "HOME");
            startActivity(dash);
        }
    }
}
