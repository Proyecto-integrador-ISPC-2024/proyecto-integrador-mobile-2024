package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiendadecampeones.R;
import com.tiendadecampeones.ui.Login;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Boton de Inicio a Login
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            // Crea el Intent para iniciar la nueva actividad
            Intent intent = new Intent(com.example.tiendadecampeones.ui.MainActivity.this, LoginActivity.class);
            startActivity(intent);

        });
    }
}