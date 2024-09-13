package com.example.tiendadecampeones;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Encontrar el botón de contacto
        Button contactButton = findViewById(R.id.contactButton);

        // Configuro el listener para el botón de contacto
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamo a la actividad de contacto
                openContactActivity();
            }
        });
    }

    private void openContactActivity() {
        Intent intent = new Intent(this, Contacto.class);
        startActivity(intent);
    }
}
