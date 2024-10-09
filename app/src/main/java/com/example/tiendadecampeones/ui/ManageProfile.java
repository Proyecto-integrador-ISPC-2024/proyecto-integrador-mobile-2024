package com.example.tiendadecampeones.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;

public class ManageProfile extends AppCompatActivity {

    private EditText etName, etLastName, etEmail, etPassword;
    private Button btnEditProfile, btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize fields
        etName = findViewById(R.id.et_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnSaveChanges = findViewById(R.id.btn_save_changes);

        // Botones de navegación superior
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
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
        // Save changes logic here

        // After saving, disable fields again
        etName.setEnabled(false);
        etLastName.setEnabled(false);
        etEmail.setEnabled(false);
        etPassword.setEnabled(false);

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
                })
                .setNegativeButton("Volver", null)
                .show();
    }


}
