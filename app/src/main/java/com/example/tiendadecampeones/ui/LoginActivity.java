package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.models.UserLogInResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public class LoginActivity extends AppCompatActivity {

    // métodos para validar que los campos no esten vacíos, mail y contrasña
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{};:,<.>])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    private boolean areFieldsNotEmpty(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        EditText emailField = findViewById(R.id.emailInput);
        EditText passwordField = findViewById(R.id.passwordInput);

        // Pantalla de Bienvenida a formulario de registro

        Button btn1 = findViewById(R.id.registerButton);
        btn1.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Register.class);
            startActivity(intent);
        });

        // Pantalla de Bienvenida a Conocenos

        // Button btn2 = findViewById(R.id.buttonIniciarSecion);
        // btn2.setOnClickListener(v -> {
        //     Intent intent = new Intent(LoginActivity.this, Home.class);
        //     startActivity(intent);
        // });

        // Botón de Inicio de Sesión
        Button btn2 = findViewById(R.id.loginButton);
        btn2.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (!areFieldsNotEmpty(email, password)) {
                // Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                showAlert("Error", "Todos los campos son obligatorios");
            } else if (!isValidEmail(email)) {
                // Toast.makeText(this, "Email no tiene formato válido", Toast.LENGTH_SHORT).show();
                showAlert("Error", "Email no tiene formato válido");
            } else if (!isValidPassword(password)) {
                // Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres, un número y un carácter especial", Toast.LENGTH_SHORT).show();
                showAlert("Error", "La contraseña debe tener al menos 8 caracteres, un número y un carácter especial");
            } else {
                loginUser(email, password);
            }
        });
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    // Método para hacer login
    private void loginUser(String email, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://recdev.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<UserLogInResponse> call = apiService.login(email, password);
        call.enqueue(new Callback<UserLogInResponse>() {
            @Override
            public void onResponse(Call<UserLogInResponse> call, Response<UserLogInResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String nombreUsuario = response.body().getUsuario().getNombre();
                    // Login exitoso, redirigir a la pantalla principal
                    Intent intent = new Intent(LoginActivity.this, Home.class);
                    intent.putExtra("nombreUsuario", nombreUsuario);
                    startActivity(intent);
                    finish();
                } else {
                    // Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    showAlert("Error", "Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<UserLogInResponse> call, Throwable t) {
                // Toast.makeText(LoginActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                showAlert("Error", "Error de conexión");
            }
        });
    }
}