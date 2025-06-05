package com.example.tiendadecampeones.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.example.tiendadecampeones.utils.UIUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.utils.UIUtils;
import com.example.tiendadecampeones.R;



public class LoginActivity extends AppCompatActivity {
    private View mainView; // Declaramos la variable a nivel de clase
    private TextView errorMessage;

    // métodos para validar que los campos no esten vacíos, mail y contrasña
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+{};:,<.>]).{8,18}$";
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

        mainView = findViewById(android.R.id.content);

        EditText emailField = findViewById(R.id.emailInput);
        EditText passwordField = findViewById(R.id.passwordInput);
        errorMessage = findViewById(R.id.errorMessage);

        // Pantalla de Bienvenida a formulario de registro
        Button btn1 = findViewById(R.id.registerButton);
        btn1.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, Register.class);
            startActivity(intent);
        });

        // Botón de Inicio de Sesión
        Button btn2 = findViewById(R.id.loginButton);
        btn2.setOnClickListener(v -> {
            hideKeyboard(); // Cierra el teclado al hacer clic en el botón
            btn2.setEnabled(false); // Desactiva el botón temporalmente

            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            if (!areFieldsNotEmpty(email, password)) {
                showErrorMessage( "Todos los campos son obligatorios");
                btn2.setEnabled(true);// Reactiva el botón
            } else if (!isValidEmail(email)) {
                showErrorMessage("Email no tiene formato válido");
                btn2.setEnabled(true); // Reactiva el botón
            } else if (!isValidPassword(password)) {
                showErrorMessage("La contraseña debe tener un minimo de 8 caracteres y un maximo de 18, un número y un carácter especial");
                btn2.setEnabled(true); // Reactiva el botón
            } else {
                loginUser(email, password, btn2);
                errorMessage.setVisibility(View.GONE);
            }
            btn2.setEnabled(true);

        });
    }

    private void showErrorMessage(String message) {
        errorMessage.setText(message);
        errorMessage.setVisibility(View.VISIBLE);
        errorMessage.setTextSize(22f); // Aumenta el tamaño del texto
        errorMessage.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        errorMessage.postDelayed(() -> errorMessage.setVisibility(View.GONE), 3000);
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
        editor.putString("userRole", rol);

        try {
            String[] parts = token.split("\\.");
            String payload = parts[1];
            byte[] decodedBytes = android.util.Base64.decode(payload, android.util.Base64.URL_SAFE);
            String decoded = new String(decodedBytes, "UTF-8");
            org.json.JSONObject json = new org.json.JSONObject(decoded);
            boolean isStaff = json.optBoolean("is_staff", false);
            boolean isSuperuser = json.optBoolean("is_superuser", false);

            editor.putBoolean("isStaff", isStaff);
            editor.putBoolean("isSuperuser", isSuperuser);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
    private void loginUser(String email, String password, Button loginButton) {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<UserLogInResponse> call = apiService.login(email, password);
        call.enqueue(new Callback<UserLogInResponse>() {
            @Override
            public void onResponse(Call<UserLogInResponse> call, Response<UserLogInResponse> response) {
                loginButton.setEnabled(true); // Reactiva el botón

                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getAccessToken();
                    String refreshToken = response.body().getRefreshToken();
                    String nombre = response.body().getUsuario().getNombre();
                    String apellido = response.body().getUsuario().getApellido();
                    String email = response.body().getUsuario().getEmail();
                    String domicilio = response.body().getUsuario().getDomicilio();
                    String rol = response.body().getUsuario().getRol();
                    int id_usuario = response.body().getUsuario().getIdUsuario();

                    // Guardar tokens en SharedPreferences
                    saveTokens(token, refreshToken, id_usuario, nombre, apellido, email, domicilio, rol);

                    // Redirigir según el rol
                    handleLoginSuccess(response.body());
                } else {
                    showAlert("Error", "Credenciales incorrectas");
                }
            }

            @Override
            public void onFailure(Call<UserLogInResponse> call, Throwable t) {
                loginButton.setEnabled(true);
                showAlert("Error", "Error de conexión");
            }
        });
    }

    private void handleLoginSuccess(UserLogInResponse response) {
        Intent intent = new Intent(LoginActivity.this, Home.class);
        startActivity(intent);
        finish();
    }
}