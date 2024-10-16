package com.example.tiendadecampeones.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.UserProfile;
import com.example.tiendadecampeones.network.ApiClient;
import com.example.tiendadecampeones.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageProfile extends AppCompatActivity {

    private EditText etName, etLastName, etEmail, etPassword, etAddress;
    private Button btnEditProfile, btnSaveChanges, btnDeactivateAccount;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize fields
        etName = findViewById(R.id.et_name);
        etLastName = findViewById(R.id.et_last_name);
        etAddress = findViewById(R.id.et_address);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);


        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnSaveChanges = findViewById(R.id.btn_save_changes);
        btnDeactivateAccount = findViewById(R.id.btnDeactivateAccount);


        // Set listener for the edit button
        btnEditProfile.setOnClickListener(v -> enableEditFields());

        // Set listener for the save button
        btnSaveChanges.setOnClickListener(v -> onSaveChangesClicked());

        //Set listener for the deactivate button
        btnDeactivateAccount.setOnClickListener(v -> onDeactivateAccountClicked());


    }

    private void enableEditFields() {
        etName.setEnabled(true);
        etLastName.setEnabled(true);
        etEmail.setEnabled(true);
        etPassword.setEnabled(true);
        etAddress.setEnabled(true);

        btnEditProfile.setVisibility(View.GONE);
        btnSaveChanges.setVisibility(View.VISIBLE);
    }

    public void onSaveChangesClicked() {
        // Get the updated user details
        String newName = etName.getText().toString().trim();
        String newLastname = etLastName.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();
        String newPassword = etPassword.getText().toString().trim();
        String newAddress = etAddress.getText().toString().trim();

        if (newName.isEmpty() || newLastname.isEmpty() || newEmail.isEmpty() || newPassword.isEmpty() || newAddress.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new UserProfile object with the updated details
        UserProfile updatedProfile = new UserProfile(newName, newLastname, newEmail, newPassword, newAddress);

        // Call API to update the user profile
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.updateUserProfile(updatedProfile.getEmail(), updatedProfile);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageProfile.this, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show();
                    disableEditFields();
                } else {
                    Toast.makeText(ManageProfile.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableEditFields() {
        etName.setEnabled(false);
        etLastName.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);
        etAddress.setEnabled(false);

        btnEditProfile.setVisibility(View.VISIBLE);
        btnSaveChanges.setVisibility(View.GONE);
    }

    // Functionality for deactivating the account
    public void onDeactivateAccountClicked() {
        new AlertDialog.Builder(this)
                .setTitle("Desactivar cuenta")
                .setMessage("¿Estás seguro de que deseas desactivar tu cuenta? Esta acción no puede deshacerse.")
                .setPositiveButton("Desactivar", (dialog, which) -> {
                    deactivateUserProfile(etEmail.getText().toString().trim());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // Functionality to deactivate the account
    private void deactivateUserProfile(String email) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.deleteUserProfile(email);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ManageProfile.this, "Cuenta desactivada con éxito", Toast.LENGTH_SHORT).show();
                    // Close or redirect the user after deactivating the account
                } else {
                    Toast.makeText(ManageProfile.this, "Error al desactivar la cuenta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ManageProfile.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}