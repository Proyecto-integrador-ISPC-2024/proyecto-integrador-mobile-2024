package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiendadecampeones.R;


public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Botones de navegación superior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
    // Métodos para manejar  los botones
    public void dashClick(View v) {
        Toast.makeText(this, "Redireccionando a tu dashboard", Toast.LENGTH_SHORT).show();

        // Intent para iniciar la actividad del dashboard
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }

    public void settingsClick(View v) {
        // Intent para iniciar la actividad de edición del perfil
        Intent intent = new Intent(this, ManageProfile.class);
        startActivity(intent);
    }

    public void logoutClick(View v) {
        // Limpieza de shared preferences de usuario
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        // Limpieza del carro
        SharedPreferences cartPrefs = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        SharedPreferences.Editor cartEditor = cartPrefs.edit();
        cartEditor.clear();
        cartEditor.apply();

        // Alerta de cierre
        Toast.makeText(this, "Has cerrado tu sesión", Toast.LENGTH_SHORT).show();

        // Navegación al login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
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

        Intent intent = new Intent(this, ProductCategories.class);
        startActivity(intent);
    }

}

