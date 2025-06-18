package com.example.tiendadecampeones.ui;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
    private TextView errorMessage;

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
        errorMessage = findViewById(R.id.errorMessageRegister);
        errorMessage.setVisibility(View.GONE);

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


        if (isFieldEmpty(nombre, etNombre, "El nombre no puede estar vacío") ||
                !isValidText(nombre, etNombre, "El nombre solo debe contener letras y tener un máximo de 40 caracteres",false) ||
                isFieldEmpty(apellido, etApellido, "El apellido no puede estar vacío") ||
                !isValidText(apellido, etApellido, "El apellido solo debe contener letras y tener un máximo de 40 caracteres",false) ||
                isFieldEmpty(domicilio, etDomicilio, "El domicilio no puede estar vacío") ||
                !isValidText(domicilio, etDomicilio, "El domicilio solo debe contener letras y números y tener un máximo de 40 caracteres", true) ||
                !isValidEmail(email) ||
                !isValidPassword(password) ||
                !arePasswordsMatching(password, confirmPassword)) {
            return false; // Si alguna validación falla, retorna false
        }

        return true;
    }

    // Método para verificar si un campo está vacío
    private boolean isFieldEmpty(String field, EditText editText, String errorMessage) {
        if (field.isEmpty()) {
            editText.setError(errorMessage);
            return true;
        }
        return false;
    }

    // Método para validar que los campos solo contengan letras y espacios, y tengan un máximo de 40 caracteres
    private boolean isValidText(String text, EditText editText, String errorMessage, boolean allowNumbers) {
        String textPattern = allowNumbers ? "^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9\\s]{1,40}$" : "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]{1,40}$";
        if (!text.matches(textPattern)) {
            editText.setError(errorMessage);
            return false;
        }
        return true;
    }

    // Método para validar el correo electrónico
    private boolean isValidEmail(String email) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("El formato del correo electrónico no es válido");
            return false;
        }
        return true;
    }

    // Método para validar la contraseña
    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 18) {
            etPassword.setError("La contraseña debe tener entre 8 y 18 caracteres");
            return false;
        }

        String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+{};:,<.>])(?=\\S+$).*$";
        if (!password.matches(passwordPattern)) {
            etPassword.setError("La contraseña debe tener un número y un símbolo especial");
            return false;
        }
        return true;
    }

    // Método para verificar si las contraseñas coinciden
    private boolean arePasswordsMatching(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            return false;
        }
        return true;
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
                    Toast.makeText(Register.this, "Registro exitoso", Toast.LENGTH_LONG).show();

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






