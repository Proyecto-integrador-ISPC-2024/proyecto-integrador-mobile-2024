package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
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
import com.example.tiendadecampeones.network.RetrofitClient;

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
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
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

        // Botón de Inicio de Sesión
        Button btn2 = findViewById(R.id.loginButton);
        btn2.setOnClickListener(v -> {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (!areFieldsNotEmpty(email, password)) {
                showAlert("Error", "Todos los campos son obligatorios");
            } else if (!isValidEmail(email)) {
                showAlert("Error", "Email no tiene formato válido");
            } else if (!isValidPassword(password)) {
                showAlert("Error", "La contraseña debe tener al menos 8 caracteres, un número y un carácter especial");
            } else {
                loginUser(email, password);
            }
        });
    }

    private void saveTokens(String token, String refresh, int id_usuario, String nombre, String apellido, String email, String domicilio, String rol) {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("accessToken", token);
        editor.putString("refreshToken", refresh);
        editor.putInt("id_usuario", id_usuario);
        editor.putString("nombre", nombre);
        editor.putString("apellido", apellido);
        editor.putString("domicilio", domicilio);
        editor.putString("email", email);
        editor.putString("rol", rol);

        editor.apply();

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
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<UserLogInResponse> call = apiService.login(email, password);
        call.enqueue(new Callback<UserLogInResponse>() {
            @Override
            public void onResponse(Call<UserLogInResponse> call, Response<UserLogInResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    String refreshToken = response.body().getRefreshToken();
                    String nombre = response.body().getUsuario().getNombre();
                    String apellido = response.body().getUsuario().getApellido();
                    String email = response.body().getUsuario().getEmail();
                    String domicilio = response.body().getUsuario().getDomicilio();

                    String rol = response.body().getUsuario().getRol();
                    int id_usuario = response.body().getUsuario().getIdUsuario();

                    // Guardar tokens en SharedPreferences
                    saveTokens(token, refreshToken, id_usuario, nombre, apellido, email, domicilio, rol);

                    // Imprime el id_usuario en Logcat
                    Log.d("UserIdDebug", "ID de usuario recuperado: " + id_usuario);
                    // Redirigir según el rol
                    Intent intent;
                    if ("ADMIN".equals(rol)) {
                        intent = new Intent(LoginActivity.this, AdminOrdersActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, Home.class);
                    }
                    intent.putExtra("nombreUsuario", nombre); // Agrega el nombre de usuario al Intent
                    intent.putExtra("mostrarBienvenida", true);
                    startActivity(intent);
                    finish();
                } else {
                    showAlert("Error", "Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<UserLogInResponse> call, Throwable t) {
                showAlert("Error", "Error de conexión");
            }
        });
    }
}