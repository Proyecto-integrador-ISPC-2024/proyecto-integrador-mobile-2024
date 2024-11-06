package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.AdminOrderAdapter;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import com.example.tiendadecampeones.models.Order;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private AdminOrderAdapter orderAdminAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getOrders();

        ImageButton deleteAccountButton = findViewById(R.id.btn_delete_account);
        deleteAccountButton.setOnClickListener(v -> logout());
    }

    private void getOrders() {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String token = preferences.getString("accessToken", null);
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

        if (token != null) {
            Call<List<Order>> call = apiService.getOrders();
            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Order> orders = response.body();
                        List<Order.OrderDetail> orderDetails = new ArrayList<>();

                        for (Order order : orders) {
                            orderDetails.addAll(order.getDetalles());
                        }

                        orderAdminAdapter = new AdminOrderAdapter(AdminOrdersActivity.this, orderDetails, orders);
                        ordersRecyclerView.setAdapter(orderAdminAdapter);
                    } else {
                        showToast("No se pudieron cargar los pedidos. Intenta nuevamente.");
                    }
                }
                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    showToast("Error al cargar los pedidos: " + t.getMessage());
                }
            });
        } else {
            showToast("Usuario no autenticado. Inicie sesión.");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent loginIntent = new Intent(AdminOrdersActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
