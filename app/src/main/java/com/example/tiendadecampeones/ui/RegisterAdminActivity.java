package com.example.tiendadecampeones.ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.RegisterResponse;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterAdminActivity extends AppCompatActivity {
    private EditText nombreInput;
    private EditText apellidoInput;
    private EditText emailInput;
    private EditText domicilioInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;
    private Button registerButton;
    private ImageButton backButton;
    private RadioGroup roleRadioGroup;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

        apiService = RetrofitClient.getClient(this).create(ApiService.class);

        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        nombreInput = findViewById(R.id.nombreInput);
        apellidoInput = findViewById(R.id.apellidoInput);
        emailInput = findViewById(R.id.emailInput);
        domicilioInput = findViewById(R.id.domicilioInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);
    }

    private void setupListeners() {
        backButton.setOnClickListener(v -> finish());

        registerButton.setOnClickListener(v -> {
            String nombre = nombreInput.getText().toString().trim();
            String apellido = apellidoInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String domicilio = domicilioInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (validateInputs(nombre, apellido, email, domicilio, password, confirmPassword)) {
                int selectedRoleId = roleRadioGroup.getCheckedRadioButtonId();
                RadioButton selectedRoleButton = findViewById(selectedRoleId);
                String role = selectedRoleButton.getText().toString();

                if (role.equals("Administrador")) {
                    registerAdmin(nombre, apellido, email, domicilio, password);
                } else {
                    registerClient(nombre, apellido, email, domicilio, password);
                }
            }
        });
    }

    private boolean validateInputs(String nombre, String apellido, String email,
                                   String domicilio, String password, String confirmPassword) {
        if (nombre.isEmpty()) {
            nombreInput.setError("El nombre es requerido");
            return false;
        }
        if (apellido.isEmpty()) {
            apellidoInput.setError("El apellido es requerido");
            return false;
        }
        if (email.isEmpty()) {
            emailInput.setError("El email es requerido");
            return false;
        }
        if (domicilio.isEmpty()) {
            domicilioInput.setError("El domicilio es requerido");
            return false;
        }
        if (password.isEmpty()) {
            passwordInput.setError("La contraseña es requerida");
            return false;
        }
        if (confirmPassword.isEmpty()) {
            confirmPasswordInput.setError("Debe confirmar la contraseña");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordInput.setError("Las contraseñas no coinciden");
            return false;
        }
        return true;
    }

    private void registerAdmin(String nombre, String apellido, String email,
                               String domicilio, String password) {
        Map<String, String> userData = new HashMap<>();
        userData.put("nombre", nombre);
        userData.put("apellido", apellido);
        userData.put("email", email);
        userData.put("domicilio", domicilio);
        userData.put("password", password);
        userData.put("rol", "ADMIN");

        apiService.createAdmin(userData).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterAdminActivity.this,
                            "Administrador registrado exitosamente", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(RegisterAdminActivity.this,
                            "Error al registrar administrador: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterAdminActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerClient(String nombre, String apellido, String email,
                                String domicilio, String password) {
        Map<String, String> userData = new HashMap<>();
        userData.put("nombre", nombre);
        userData.put("apellido", apellido);
        userData.put("email", email);
        userData.put("domicilio", domicilio);
        userData.put("password", password);
        userData.put("rol", "CLIENTE");

        apiService.register(userData).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterAdminActivity.this,
                            "Cliente registrado exitosamente", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(RegisterAdminActivity.this,
                            "Error al registrar cliente: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterAdminActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void homeButton(View v) {
        Toast.makeText(this, "¡ Home !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void productsButton(View v) {
        Toast.makeText(this, "¡ Nuestros Productos !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProductCategories.class);
        startActivity(intent);
    }
}