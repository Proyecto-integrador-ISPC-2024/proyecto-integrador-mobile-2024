package com.example.tiendadecampeones.ui;

import android.app.AlertDialog;
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

public class ManageProfile extends AppCompatActivity {

    private static final String TAG = "ManageProfile"; // Log tag

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
        SharedPreferences sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE);

        String nombre = sharedPref.getString("nombre", "");
        String apellido = sharedPref.getString("apellido", "");
        String email = sharedPref.getString("email", "");
        String domicilio = sharedPref.getString("domicilio", "");
        String password = sharedPref.getString("contraseña", "");

        // Logging to debug the values fetched from SharedPreferences
        Log.d(TAG, "Nombre: " + nombre);
        Log.d(TAG, "Apellido: " + apellido);
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "Domicilio: " + domicilio);
        Log.d(TAG, "Contraseña: " + password);

        // Check if any values are null or empty and log accordingly
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || domicilio.isEmpty() || password.isEmpty()) {
            Log.e(TAG, "Some user fields are empty, possibly indicating an issue with SharedPreferences.");
        }

        // Populate the fields with user data
        etName.setText(nombre);
        etLastName.setText(apellido);
        etEmail.setText(email);
        etDomicilio.setText(domicilio);
        etPassword.setText(password);

        // Disable editing fields initially
        setFieldsEditable(false);

        // Handle Edit and Save button logic
        btnEditProfile.setOnClickListener(this::onEditProfileClicked);
        btnSaveChanges.setOnClickListener(this::onSaveChangesClicked);

        // Botón de navegación superior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void setFieldsEditable(boolean editable) {
        etName.setEnabled(editable);
        etLastName.setEnabled(editable);
        etEmail.setEnabled(editable);
        etDomicilio.setEnabled(editable);
        etPassword.setEnabled(editable);
    }

    public void onEditProfileClicked(View view) {
        // Enable fields for editing
        setFieldsEditable(true);

        // Show save button and hide edit button
        btnEditProfile.setVisibility(View.GONE);
        btnSaveChanges.setVisibility(View.VISIBLE);
    }

    public void onSaveChangesClicked(View view) {
        // Get updated data from the fields
        String updatedName = etName.getText().toString();
        String updatedLastName = etLastName.getText().toString();
        String updatedEmail = etEmail.getText().toString();
        String updatedDomicilio = etDomicilio.getText().toString();
        String updatedPassword = etPassword.getText().toString();

        Log.d(TAG, "Updated Name: " + updatedName);
        Log.d(TAG, "Updated Last Name: " + updatedLastName);
        Log.d(TAG, "Updated Email: " + updatedEmail);
        Log.d(TAG, "Updated Domicilio: " + updatedDomicilio);
        Log.d(TAG, "Updated Password: " + updatedPassword);

        // Save updated data in SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("nombre", updatedName);
        editor.putString("apellido", updatedLastName);
        editor.putString("email", updatedEmail);
        editor.putString("domicilio", updatedDomicilio);
        editor.putString("contraseña", updatedPassword);
        editor.apply();

        Log.d(TAG, "User information saved to SharedPreferences successfully.");

        // Show confirmation message
        Toast.makeText(this, "Información actualizada con éxito", Toast.LENGTH_SHORT).show();

        // Disable fields after saving
        setFieldsEditable(false);

        // Show edit button and hide save button
        btnEditProfile.setVisibility(View.VISIBLE);
        btnSaveChanges.setVisibility(View.GONE);
    }

    public void onDeleteAccountClicked(View view) {
        // Handle delete account logic here (e.g., show confirmation dialog)
        new AlertDialog.Builder(this)
                .setTitle("Desactivar cuenta")
                .setMessage("¿Estás seguro/a que querés desactivar tu cuenta?")
                .setPositiveButton("Desactivar", (dialog, which) -> {
                    // Perform delete account action here
                    Log.d(TAG, "Account deactivation confirmed.");
                })
                .setNegativeButton("Volver", null)
                .show();
    }
}
