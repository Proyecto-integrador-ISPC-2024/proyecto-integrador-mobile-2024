package com.example.tiendadecampeones.ui;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.net.Uri;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;

public class AboutUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Botones de navegación superior
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // Botones de navegación inferior
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AboutUs.this, Home.class);
                startActivity(intent);
            }
        });

        Button productsButton = findViewById(R.id.productsButton);
        productsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AboutUs.this, ProductCategories.class);
                startActivity(intent);
            }
        });

        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AboutUs.this, Profile.class);
                startActivity(intent);
            }
        });

//      Botón de navegación a contacto
        Button contactButton = findViewById(R.id.contactButton);
        contactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AboutUs.this, Contact.class);
                startActivity(intent);
            }
        });
        // Botón para visitar la web
        Button btnVisitarWeb = findViewById(R.id.btnVisitarWeb);
        btnVisitarWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/Proyecto-integrador-ISPC-2024";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}
