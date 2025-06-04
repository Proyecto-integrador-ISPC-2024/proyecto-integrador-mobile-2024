package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tiendadecampeones.R;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Recibimos el nombre de usuario
        Intent intent = getIntent();
        String nombreUsuario = intent.getStringExtra("nombreUsuario");

        // Diálogo inicial
        boolean mostrarBienvenida = intent.getBooleanExtra("mostrarBienvenida", false);
        if (mostrarBienvenida) {
            new AlertDialog.Builder(this)
                    .setTitle("Bienvenido de vuelta")
                    .setMessage("Hola, " + nombreUsuario + "!")
                    .setPositiveButton("Continuar", (dialog, which) -> dialog.dismiss())
                    .show();

            intent.removeExtra("mostrarBienvenida");
        }

        // Initialize the DrawerLayout
        drawerLayout = findViewById(R.id.main);

        // Navegación lateral
        ImageButton sideNavButton = findViewById(R.id.sideNavButton);
        sideNavButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageButton cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Cart.class);
                startActivity(intent);
            }
        });


        // Navegación lateral
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_products) {
                startActivity(new Intent(Home.this, ProductCategories.class));
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(Home.this, Cart.class));
            } else if (id == R.id.nav_about) {
                startActivity(new Intent(Home.this, AboutUs.class));
            } else if (id == R.id.nav_contact) {
                startActivity(new Intent(Home.this, Contact.class));
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(Home.this, Profile.class));
            } else if (id == R.id.nav_dashboard) {
                SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
                String userRole = preferences.getString("userRole", "");
                boolean isStaff = preferences.getBoolean("isStaff", false);
                boolean isSuperuser = preferences.getBoolean("isSuperuser", false);
                // Verificar si el usuario es admin o super admin
                boolean isAdmin = "ADMIN".equals(userRole) && isStaff;
                boolean isSuperAdmin = isAdmin && isSuperuser;

                if (isAdmin || isSuperAdmin) {
                    Toast.makeText(this, "Redireccionando al panel de administración", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Home.this, AdminListUsersActivity.class));
                } else {
                    Toast.makeText(this, "Redireccionando a tu dashboard", Toast.LENGTH_SHORT).show();
                    Intent dashboardIntent = new Intent(Home.this, Dashboard.class);
                    dashboardIntent.putExtra("ORIGIN", "HOME");
                    startActivity(dashboardIntent);
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Navegación central
        Button buyButton = findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ProductCategories.class);
                startActivity(intent);
            }
        });

        // Botones de navegación inferior
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Actualmente en home");
            }
        });
        Button productsButton = findViewById(R.id.productsButton);
        productsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ProductCategories.class);
                startActivity(intent);
            }
        });
        Button profileButton = findViewById(R.id.profileBtn);
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Profile.class);
                startActivity(intent);
            }
        });
    }

    // Navegación inferior
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
