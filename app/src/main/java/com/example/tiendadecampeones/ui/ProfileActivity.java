package com.example.tiendadecampeones.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tiendadecampeones.R;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Referencias a los elementos de la UI
        drawerLayout = findViewById(R.id.drawerLayout);

        // Inicializo botón de menú de navegación lateral
        Button sideNavButton = findViewById(R.id.sideNavButton);

        // Configurar el botón de menú lateral para abrir el drawer
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

        // Configurar el botón de atrás para finalizar la actividad
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
;