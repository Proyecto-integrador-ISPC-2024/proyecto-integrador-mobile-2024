package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;

public class Order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
    }

    public void volverBtn(View view) {
        Toast.makeText(this, "Redirigiendo al dashboard", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
    }


    public void homeButton(View view) {
        Toast.makeText(this, "Redirigiendo a home", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    public void productsButton(View view) {
        Toast.makeText(this, "Redirigiendo a productos", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }
    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
