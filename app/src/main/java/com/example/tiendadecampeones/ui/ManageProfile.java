package com.example.tiendadecampeones.ui;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private EditText etName, etLastName, etEmail, etPassword, etDomicilio;
    private Button btnEditProfile, btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize fields
        etName = findViewById(R.id.et_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etDomicilio = findViewById(R.id.et_address);
        etPassword = findViewById(R.id.et_password);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        // Retrieve user data from SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String nombre = sharedPref.getString("nombre", "");
        String apellido = sharedPref.getString("apellido", "");
        String email = sharedPref.getString("email", "");
        String domicilio = sharedPref.getString("domicilio", "");
        String password = sharedPref.getString("contraseña", "");

        // Populate the fields with user data
        etName.setText(nombre);
        etLastName.setText(apellido);
        etEmail.setText(email);
        etDomicilio.setText(domicilio);
        etPassword.setText(password);

        // Disable editing fields initially
        etName.setEnabled(false);
        etLastName.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);

        // Handle Edit and Save button logic
        btnEditProfile.setOnClickListener(this::onEditProfileClicked);
        btnSaveChanges.setOnClickListener(this::onSaveChangesClicked);

        // Botones de navegación superior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    public void onEditProfileClicked(View view) {
        // Enable fields for editing
        etName.setEnabled(true);
        etLastName.setEnabled(true);
        etEmail.setEnabled(true);
        etPassword.setEnabled(true);

        // Show save button and hide edit button
        btnEditProfile.setVisibility(View.GONE);
        btnSaveChanges.setVisibility(View.VISIBLE);
    }

    public void onSaveChangesClicked(View view) {
        // Create the updated user profile object
        UserProfile updatedProfile = new UserProfile(
                1, // Replace with actual user ID
                etName.getText().toString(),
                etLastName.getText().toString(),
                etEmail.getText().toString(),
                etDomicilio.getText().toString(),
                etPassword.getText().toString()
        );

        // Get the refresh token
        String token = getRefreshToken();

        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<UserProfile> call = apiService.updateProfile(updatedProfile.getId(), "Bearer " + token, updatedProfile);

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageProfile.this, "Cambios guardados exitosamente", Toast.LENGTH_SHORT).show();
                    updateSharedPreferences(updatedProfile);
                } else {
                    Toast.makeText(ManageProfile.this, "Error al guardar cambios: " + response.message(), Toast.LENGTH_SHORT).show();
                }
                disableEditingFields();
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(ManageProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                disableEditingFields();
            }
        });
    }

    public void onDeleteAccountClicked(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Desactivar cuenta")
                .setMessage("¿Estás seguro/a que querés desactivar tu cuenta?")
                .setPositiveButton("Desactivar", (dialog, which) -> {
                    // Handle delete account action here
                })
                .setNegativeButton("Volver", null)
                .show();
    }

    private String getRefreshToken() {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPref.getString("refresh_token", "");
    }

    private void updateSharedPreferences(UserProfile profile) {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
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
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);

        btnEditProfile.setVisibility(View.VISIBLE);
        btnSaveChanges.setVisibility(View.GONE);
    }
}
