package com.example.tiendadecampeones.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.TextUtils;
import android.util.Patterns;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.utils.MailSender;

public class Contact extends AppCompatActivity {
    private static final String TAG = "ContactActivity";
    private static final String EMAIL_DESTINO = "tiendadecampeonescba@gmail.com";
    private static final String GMAIL_USERNAME = "tiendadecampeonescba@gmail.com";
    private static final String GMAIL_APP_PASSWORD = "vujq ibgs zicw myxt";

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

        initViews();
    }

    private void initViews() {
        EditText nameEditText = findViewById(R.id.nameContactInput);
        EditText lastnameEditText = findViewById(R.id.lastnameContactInput);
        EditText emailEditText = findViewById(R.id.emailContactInput);
        EditText messageEditText = findViewById(R.id.messageContactInput);
        Button sendButton = findViewById(R.id.sendFormContactButton);
        Button termButton = findViewById(R.id.termsBttn);
        Button productsButton = findViewById(R.id.comprarButton);

        sendButton.setOnClickListener(v -> {
            String nombre = nameEditText.getText().toString().trim();
            String apellido = lastnameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String mensaje = messageEditText.getText().toString().trim();

            if (validateForm(nombre, apellido, email, mensaje)) {
                sendEmailDirectly(nombre, apellido, email, mensaje);
            }
        });

        termButton.setOnClickListener(v -> {
            Intent intent = new Intent(Contact.this, TermsActivity.class);
            startActivity(intent);
        });

        productsButton.setOnClickListener(v -> {
            Intent intent = new Intent(Contact.this, ProductCategories.class);
            startActivity(intent);
        });

        setupNavigationButtons();
    }

    private boolean validateForm(String nombre, String apellido, String email, String mensaje) {
        if (TextUtils.isEmpty(nombre)) {
            showError("Por favor, ingresá tu nombre");
            return false;
        }

        if (TextUtils.isEmpty(apellido)) {
            showError("Por favor, ingresá tu apellido");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            showError("Por favor, ingresá tu correo electrónico");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError("El correo electrónico ingresado no es válido");
            return false;
        }

        if (TextUtils.isEmpty(mensaje)) {
            showError("Por favor, escribí tu mensaje");
            return false;
        }

        if (mensaje.length() < 10) {
            showError("El mensaje debe tener al menos 10 caracteres");
            return false;
        }

        if (mensaje.length() > 500) {
            showError("El mensaje no debe superar los 500 caracteres");
            return false;
        }

        if (!isValidName(nombre) || !isValidName(apellido)) {
            showError("Nombre o apellido contienen caracteres no permitidos");
            return false;
        }

        if (!isValidMessage(mensaje)) {
            showError("El mensaje contiene caracteres no permitidos");
            return false;
        }

        return true;
    }

    private void sendEmailDirectly(String nombre, String apellido, String email, String mensaje) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Enviando correo...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String subject = "Contacto desde la app: " + nombre + " " + apellido;
        String body = "Nombre: " + nombre + "\n" +
                "Apellido: " + apellido + "\n" +
                "Email: " + email + "\n\n" +
                "Mensaje:\n" + mensaje +
                "\n\nEnviado desde Android Studio";

        new Thread(() -> {
            try {
                MailSender mailSender = new MailSender(GMAIL_USERNAME, GMAIL_APP_PASSWORD);
                mailSender.sendMail(subject, body, GMAIL_USERNAME, EMAIL_DESTINO);

                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    showSuccess("Correo enviado exitosamente");
                    clearForm();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    showError("Error al enviar el correo: " + e.getMessage());
                    Log.e(TAG, "Error al enviar correo", e);
                });
            }
        }).start();
    }

    private void clearForm() {
        ((EditText)findViewById(R.id.nameContactInput)).setText("");
        ((EditText)findViewById(R.id.lastnameContactInput)).setText("");
        ((EditText)findViewById(R.id.emailContactInput)).setText("");
        ((EditText)findViewById(R.id.messageContactInput)).setText("");
    }

    private void setupNavigationButtons() {
        findViewById(R.id.backButton).setOnClickListener(v -> finish());
        findViewById(R.id.homeButton).setOnClickListener(v -> startActivity(new Intent(this, Home.class)));
        findViewById(R.id.productsButton).setOnClickListener(v -> startActivity(new Intent(this, ProductCategories.class)));
        findViewById(R.id.profileButton).setOnClickListener(v -> startActivity(new Intent(this, Profile.class)));
        findViewById(R.id.comprarButton).setOnClickListener(v -> startActivity(new Intent(this, ProductCategories.class)));
    }

    private boolean isValidName(String input) {
        String pattern = "^[\\p{L} \\-'áéíóúÁÉÍÓÚñÑüÜ]+$";
        return input.matches(pattern);
    }

    private boolean isValidMessage(String input) {
        String lower = input.toLowerCase();

        String[] blacklist = {
                "<script", "</script", "<img", "<iframe", "onerror", "onload",
                "javascript:", "select *", "drop table", "insert into", "delete from"
        };

        for (String bad : blacklist) {
            if (lower.contains(bad)) {
                return false;
            }
        }

        String pattern = "^[\\p{L}\\p{N}\\s.,!?¡¿'\"\\-:;()áéíóúÁÉÍÓÚñÑüÜ]*$";
        return input.matches(pattern);
    }


    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
