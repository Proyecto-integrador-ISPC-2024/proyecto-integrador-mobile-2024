package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiendadecampeones.AboutUs;
import com.example.tiendadecampeones.R;


public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });
    }
    // Métodos para manejar  los botones
    public void dashClick(View v) {
        Toast.makeText(this, "Redireccionando a tu dashboard", Toast.LENGTH_SHORT).show();

        // Intent para iniciar la actividad del dashboard
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    public void logoutClick(View v) {
        Toast.makeText(this, "Has cerrado la sesión", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void backButton(View v) {
        Toast.makeText(this, "Redireccionando a tu página anterior ", Toast.LENGTH_SHORT).show();
        finish();
    }
    //botones de la barra de navegacion inferior
    public void webtn(View v) {
        Toast.makeText(this, "¡ Conócenos !", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, AboutUs.class);
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

