package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        // Ajustar los insets de la ventana
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Métodos para manejar clics en los botones
    public void dashClick(View v) {
        Toast.makeText(this, "Redireccionando a tu dashboard", Toast.LENGTH_SHORT).show();

        // Intent para iniciar la actividad del dashboard
        Intent intent = new Intent(this, Dashboard.class);  // Usa el nombre correcto de la clase
        startActivity(intent);
    }

    public void logoutClick(View v) {
        Toast.makeText(this, "Has cerrado la sesión", Toast.LENGTH_SHORT).show();


    }
}
