package com.example.tiendadecampeones.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiendadecampeones.R;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacto);
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
        Intent intent = new Intent(this, activity_dashboard.class);
        startActivity(intent);
    }

    public void logoutClick(View v) {
        Toast.makeText(this, "Has cerrado la sesión", Toast.LENGTH_SHORT).show();

        // Aquí puedes agregar lógica para cerrar sesión
    }
}
