package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginActivity);

        // Pantalla de Bienvenida a formulario de registro

        Button btn1 = findViewById(R.id.buttonRegister);
        btn1.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Register.class);
            startActivity(intent);
        });

        // Pantalla de Bienvenida a Conosenos

        Button btn2 = findViewById(R.id.buttonIniciarSecion);
        btn2.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Home.class);
            startActivity(intent);
        });
    }
}