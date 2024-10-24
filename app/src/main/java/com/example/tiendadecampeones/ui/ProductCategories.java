package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;

public class ProductCategories extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_categories);

        // Botones de navegación superior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        ImageButton cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProductCategories.this, Cart.class);
                startActivity(intent);
            }
        });

        // Botones de navegación inferior
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProductCategories.this, Home.class);
                startActivity(intent);
            }
        });
        Button productsButton = findViewById(R.id.productsButton);
        productsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProductCategories.this, ProductCategories.class);
                startActivity(intent);
            }
        });
        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProductCategories.this, Profile.class);
                startActivity(intent);
            }
        });

        // Captura de botones de categorías de países
        ImageButton buttonGermany = findViewById(R.id.buttonGermany);
        ImageButton buttonArgentina = findViewById(R.id.buttonArgentina);
        ImageButton buttonFrance = findViewById(R.id.buttonFrance);
        ImageButton buttonUruguay = findViewById(R.id.buttonUruguay);

        // Agrego listeneres para cada botón
        buttonGermany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("Alemania");
            }
        });

        buttonArgentina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("Argentina");
            }
        });

        buttonFrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("Francia");
            }
        });

        buttonUruguay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductList("Uruguay");
            }
        });
    }

    // Renderizado de lista de productos según país elegido
    private void openProductList(String pais) {
        Intent intent = new Intent(ProductCategories.this, ProductsActivity.class);
        intent.putExtra("pais", pais);
        startActivity(intent);
    }
}
