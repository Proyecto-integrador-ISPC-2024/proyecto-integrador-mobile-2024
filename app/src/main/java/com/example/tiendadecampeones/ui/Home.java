package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tiendadecampeones.R;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        // Recibe el nombre del usuario desde el Intent
        Intent intent = getIntent();
        String nombreUsuario = intent.getStringExtra("nombreUsuario");

        // Muestra el AlertDialog con el mensaje de bienvenida
        new AlertDialog.Builder(this)
                .setTitle("Bienvenido de vuelta")
                .setMessage("Hola, " + nombreUsuario + "!")
                .setPositiveButton("Continuar", (dialog, which) -> dialog.dismiss())
                .show();

        //  Barra de navegación lateral
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.navigationView);

        // Botón para abrir/cerrar el menú lateral
        ImageButton sideNavButton = findViewById(R.id.sideNavButton);
        sideNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        //Eventos del click en el menú de navegación
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_products) {
                    // Acción para el ítem "Productos"
                    Intent intent = new Intent(Home.this, ProductsActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_cart) {
                    // Acción para el ítem "Mi carrito"
                    Intent intent = new Intent(Home.this, Cart.class);
                    startActivity(intent);

                } else if (id == R.id.nav_conocenos) {
                    // Acción para el ítem "Conócenos"
                    Intent intent = new Intent(Home.this, AboutUs.class);
                    startActivity(intent);

                } else if (id == R.id.nav_contacto) {
                    // Acción para el ítem "Contacto"
                    Intent intent = new Intent(Home.this, Contact.class);
                    startActivity(intent);

                } else if (id == R.id.nav_perfil) {
                    // Acción para el ítem "Perfil"
                    Intent intent = new Intent(Home.this, Profile.class);
                    startActivity(intent);

                } else if (id == R.id.nav_dashboard) {
                    // Acción para el ítem "Dashboard"
                    Intent intent = new Intent(Home.this, Dashboard.class);
                    startActivity(intent);
                }


                return true;
            }
        });

        ImageButton cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Cart.class);
                startActivity(intent);
            }
        });

        Button buyButton = findViewById(R.id.buyButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent para abrir la pantalla de Productos
                Intent intent = new Intent(Home.this, ProductCategories.class);
                startActivity(intent);
            }
        });


    }

    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        // Intent para iniciar la actividad del dashboard
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
