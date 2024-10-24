package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.RegisterResponse;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    private EditText etEmail, etPassword, etConfirmPassword, etNombre, etApellido, etDomicilio;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Ajuste de los insets para manejar barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botones de navegación superior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Inicialización de los campos de la interfaz
        etNombre = findViewById(R.id.user_label);
        etApellido = findViewById(R.id.apellido_label);
        etEmail = findViewById(R.id.email_label);
        etDomicilio = findViewById(R.id.editTextDomicilio);
        etPassword = findViewById(R.id.editTextPassword);
        etConfirmPassword = findViewById(R.id.editTextRepeatPassword);
        btnRegister = findViewById(R.id.btn_register);

        // Escucha del botón de registro
        btnRegister.setOnClickListener(v -> {
            if (validateFields()) {
                // Si las validaciones son correctas, hacer POST
                makePostRequest();
            }
        });
    }

    // Método para validar los campos
    private boolean validateFields() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String domicilio = etDomicilio.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validar que el nombre no esté vacío
        if (nombre.isEmpty()) {
            etNombre.setError("El nombre no puede estar vacío");
            return false;
        }

        // Validar que el apellido no esté vacío
        if (apellido.isEmpty()) {
            etApellido.setError("El apellido no puede estar vacío");
            return false;
        }

        // Validar que el domicilio no esté vacío
        if (domicilio.isEmpty()) {
            etDomicilio.setError("El domicilio no puede estar vacío");
            return false;
        }

        // Validar que el correo no esté vacío
        if ( !isValidEmail(email)) {
            etEmail.setError("El formato del correo electronico no es valido");
            return false;
        }


        // Validar que la contraseña no esté vacía
        if (password.isEmpty()) {
            etPassword.setError("La contraseña no puede estar vacía");
            return false;
        }
        // Validar formato de la contraseña
        if (!isValidPassword(password)) {
            etPassword.setError("La contraseña debe tener al menos 8 caracteres, un número y un símbolo especial");
            return false;
        }


        // Validar que la confirmación de contraseña no esté vacía
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Debes confirmar tu contraseña");
            return false;
        }

        // Validar que las contraseñas coincidan
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            return false;
        }

        // Si todas las validaciones son correctas, retorna true
        return true;
    }

    // métodos para validar que los campos no esten vacíos, mail y contrasña
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{};:,<.>])(?=\\S+$).{8,}$";
        return password.matches(passwordPattern);
    }

    // Método para realizar el POST
    private void makePostRequest() {
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

        Map<String, String> userData = new HashMap<>();
        userData.put("nombre", etNombre.getText().toString().trim());
        userData.put("apellido", etApellido.getText().toString().trim());
        userData.put("email", etEmail.getText().toString().trim());
        userData.put("domicilio", etDomicilio.getText().toString().trim());
        userData.put("password", etPassword.getText().toString().trim());

        // Realizar la llamada a la API

        Call<com.example.tiendadecampeones.models.RegisterResponse> call = apiService.register(userData);


        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    RegisterResponse registerResponse = response.body();
                    Toast.makeText(Register.this, "Registro exitoso" , Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Register.this, LoginActivity.class);
                    startActivity(intent);


                } else {
                    Toast.makeText(Register.this, "Error en el registro", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(Register.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}






