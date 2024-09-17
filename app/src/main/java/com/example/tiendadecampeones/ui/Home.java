package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        }
    public void productClick(View v) {
        // Intent para abrir la pantalla de Productos
        Intent intent = new Intent(this, CountryProducts.class);
        startActivity(intent); }
    }
