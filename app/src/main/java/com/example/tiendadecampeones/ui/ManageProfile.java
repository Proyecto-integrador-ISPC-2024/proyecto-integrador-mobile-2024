package com.example.tiendadecampeones.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.UserProfile;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageProfile extends AppCompatActivity {

    private static final String TAG = "ManageProfile"; // Log tag

    private EditText etName, etLastName, etEmail, etPassword, etAddress;
    private Button btnEditProfile, btnSaveChanges;
    private String fullAuthToken;
    private int id_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etName = findViewById(R.id.et_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etAddress = findViewById(R.id.et_address);
        etPassword = findViewById(R.id.et_password);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        SharedPreferences sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String authToken = sharedPref.getString("accessToken", null);
        id_usuario = sharedPref.getInt("id_usuario", -1);

        if (authToken == null) {
            Log.e(TAG, "Token de autenticación no encontrado");
            Toast.makeText(this, "Sesión expirada. Inicia sesión de nuevo.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        loadUserData(sharedPref);
        setFieldsEditable(false);
        btnEditProfile.setOnClickListener(this::onEditProfileClicked);
        btnSaveChanges.setOnClickListener(this::onSaveChangesClicked);

        // Botón de navegación superior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    // Obtención de información de usuario mediante shared preferences
        @Override
        protected void onResume() {
            super.onResume();
            SharedPreferences sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
            loadUserData(sharedPref);
        }

        private void loadUserData(SharedPreferences sharedPref) {
            String nombre = sharedPref.getString("nombre", "");
            String apellido = sharedPref.getString("apellido", "");
            String email = sharedPref.getString("email", "");
            String domicilio = sharedPref.getString("domicilio", "");
            String password = sharedPref.getString("contraseña", "");

            etName.setText(nombre);
            etLastName.setText(apellido);
            etEmail.setText(email);
            etAddress.setText(domicilio);
            etPassword.setText(password);
        }


        private void setFieldsEditable(boolean editable) {
        etName.setEnabled(editable);
        etLastName.setEnabled(editable);
        etEmail.setEnabled(editable);
        etAddress.setEnabled(editable);
        etPassword.setEnabled(editable);
    }

    public void onEditProfileClicked(View view) {
        setFieldsEditable(true);
        btnEditProfile.setVisibility(View.GONE);
        btnSaveChanges.setVisibility(View.VISIBLE);
    }

    public void onSaveChangesClicked(View view) {

        // Validación de longitud con ventana de alerta
        if (etName.getText().toString().length() > 40) {
            showAlertDialog("Error de validación", "El nombre no puede tener más de 40 caracteres");
            return;
        }
        if (etLastName.getText().toString().length() > 40) {
            showAlertDialog("Error de validación", "El apellido no puede tener más de 40 caracteres");
            return;
        }
        if (etAddress.getText().toString().length() > 40) {
            showAlertDialog("Error de validación", "El domicilio no puede tener más de 40 caracteres");
            return;
        }
        if (etEmail.getText().toString().length() > 40) {
            showAlertDialog("Error de validación", "El email no puede tener más de 40 caracteres");
            return;
        }
        if (etPassword.getText().toString().length() > 18) {
            showAlertDialog("Error de validación", "La contraseña no puede tener más de 18 caracteres");
            return;
        }
        UserProfile updatedProfile = new UserProfile(
                id_usuario,
                etName.getText().toString(),
                etLastName.getText().toString(),
                etEmail.getText().toString(),
                etAddress.getText().toString(),
                etPassword.getText().toString()
        );

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<UserProfile> call = apiService.updateProfile(
                updatedProfile.getId(),
                updatedProfile
        );

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageProfile.this, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();
                    updateSharedPreferences(updatedProfile);
                } else {
                    int errorCode = response.code();
                    String errorMessage = "Error al guardar cambios: " + response.message() + " (Código de error: " + errorCode + ")";
                    Toast.makeText(ManageProfile.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
                disableEditingFields();
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                showAlertDialog("Error", "Error de conexión: " + t.getMessage());
                disableEditingFields();
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Aceptar", null)
                .show();

    }

    public void onDeleteAccountClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Desactivar cuenta")
                .setMessage("¿Estás seguro/a que querés desactivar tu cuenta?")
                .setPositiveButton("Desactivar", (dialog, which) -> {
                    Log.d(TAG, "Account deactivation confirmed.");
                })
                .setNegativeButton("Volver", null)
                .show();
    }

    private void updateSharedPreferences(UserProfile profile) {
        SharedPreferences sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("nombre", profile.getNombre());
        editor.putString("apellido", profile.getApellido());
        editor.putString("email", profile.getEmail());
        editor.putString("domicilio", profile.getDomicilio());
        editor.putString("contraseña", profile.getContraseña());
        editor.apply();
    }

    private void disableEditingFields() {
        etName.setEnabled(false);
        etLastName.setEnabled(false);
        etAddress.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);

        btnEditProfile.setVisibility(View.VISIBLE);
        btnSaveChanges.setVisibility(View.GONE);
    }

}