package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.OrderAdapter;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import com.example.tiendadecampeones.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersActivity extends AppCompatActivity {

    private ListView ordersListView;
    private OrderAdapter orderAdapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_orders);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.adminActivity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String nombreUsuario = intent.getStringExtra("nombreUsuario");

        if (nombreUsuario != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Bienvenido de vuelta")
                    .setMessage("Hola, " + nombreUsuario + "!")
                    .setPositiveButton("Continuar", (dialog, which) -> dialog.dismiss())
                    .show();
        }

        ordersListView = findViewById(R.id.ordersListView);

        orderAdapter = new OrderAdapter(this, null);
        ordersListView.setAdapter(orderAdapter);


        apiService = RetrofitClient.getClient(this).create(ApiService.class);
        getOrders();

        ImageButton deleteAccountButton = findViewById(R.id.btn_delete_account);
        deleteAccountButton.setOnClickListener(v -> logout());
    }

    private void getOrders() {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String token = preferences.getString("accessToken", null);

        if (token != null) {
            Call<List<Order>> call = apiService.getOrders("Bearer " + token);
            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Order> orders = response.body();
                        orderAdapter.updateOrders(orders);
                    } else {
                        Log.e("OrdersError", "Error al obtener los pedidos: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Log.e("OrdersError", "Error en la petición: " + t.getMessage());
                }
            });
        }
    }

    private void logout() {
        // Borrar tokens y datos de usuario de SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent loginIntent = new Intent(AdminOrdersActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
