package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tiendadecampeones.R;

public class Contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contacto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Campos del form
        EditText nameEditText = findViewById(R.id.nameContactInput);
        EditText lastnameEditText = findViewById(R.id.lastnameContactInput);
        EditText emailEditText = findViewById(R.id.emailContactInput);
        EditText messageEditText = findViewById(R.id.messageContactInput);
        Button sendButton = findViewById(R.id.sendFormContactButton);

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
                Intent intent = new Intent(Contact.this, Home.class);
                startActivity(intent);
            }
        });

        // Validar al tocar el botón de enviar
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nameEditText.getText().toString().trim();
                String apellido = lastnameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String mensaje = messageEditText.getText().toString().trim();

                // Validar de campos vacíos
                if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(apellido) ||
                        TextUtils.isEmpty(email) || TextUtils.isEmpty(mensaje)) {
                    Toast.makeText(Contact.this, "Por favor, completá todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar de email
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(Contact.this, "Ingresá un correo electrónico válido.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar minimo de letras
                if (mensaje.length() < 10) {
                    Toast.makeText(Contact.this, "El mensaje debe tener al menos 10 caracteres.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validar de caracteres permitidos (nombre, apellido, mensaje)
                if (!secureInput(nombre) || !secureInput(apellido) || !secureInput(mensaje)) {
                    Toast.makeText(Contact.this, "Los campos contienen caracteres no permitidos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // mensaje de éxito
                Toast.makeText(Contact.this, "¡Mensaje enviado correctamente!", Toast.LENGTH_LONG).show();

                // Reiniciamos los campos del form
                nameEditText.setText("");
                lastnameEditText.setText("");
                emailEditText.setText("");
                messageEditText.setText("");
            }
        });

        Button productsButton = findViewById(R.id.productsButton);
        productsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Contact.this, ProductCategories.class);
                startActivity(intent);
            }
        });

        Button profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Contact.this, Profile.class);
                startActivity(intent);
            }
        });

        Button comprarButton = findViewById(R.id.comprarButton);
        comprarButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Contact.this, ProductCategories.class);
                startActivity(intent);
            }
        });

    }

    private boolean secureInput(String input) {
        // Permite letras, acentos, números básicos, signos de puntuación y espacios
        String pattern = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9.,!?¡¿\\s'-]{1,500}$";
        return input.matches(pattern);
    }

}