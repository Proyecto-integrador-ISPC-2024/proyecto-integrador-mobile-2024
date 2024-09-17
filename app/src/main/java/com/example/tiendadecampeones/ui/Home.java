package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

    }

    public void productClick(View v) {
        // Intent para abrir la pantalla de Productos
        Intent intent = new Intent(this, CountryProducts.class);
        startActivity(intent);
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
