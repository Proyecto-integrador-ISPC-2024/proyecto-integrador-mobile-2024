package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tiendadecampeones.R;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;

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
                .setMessage("Bienvenido de vuelta, " + nombreUsuario + "!")
                .setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss())
                .show();

        //  Barra de navegación lateral
        drawerLayout = findViewById(R.id.main);
        Button sideNavButton = findViewById(R.id.sideNavButton);
        sideNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(findViewById(R.id.sideNav))) {
                    drawerLayout.closeDrawer(findViewById(R.id.sideNav));
                } else {
                    drawerLayout.openDrawer(findViewById(R.id.sideNav));
                }
            }
        });

        Button cartButton = findViewById(R.id.cartButton);
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

        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }
}
