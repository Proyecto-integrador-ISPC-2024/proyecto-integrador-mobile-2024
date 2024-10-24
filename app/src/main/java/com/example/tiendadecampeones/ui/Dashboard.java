package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.adapters.OrderAdapter;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Dashboard extends AppCompatActivity {
    private ListView listView;
    private OrderAdapter orderAdapter;
    private List<Order> orders = new ArrayList<>();
    private ImageButton backButton;
    private SearchView searchInput;
    private String authToken;
    private ApiService apiService;
    private TextView noOrdersTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        listView = findViewById(R.id.listView);
        searchInput = findViewById(R.id.searchV);
        backButton = findViewById(R.id.backButton);
        noOrdersTextView = findViewById(R.id.no_orders_message);

        apiService = RetrofitClient.getClient(this).create(ApiService.class);

        searchInput.setQueryHint(getString(R.string.searchV));

        orderAdapter = new OrderAdapter(this, orders);
        listView.setAdapter(orderAdapter);

        fetchOrders();

        backButton.setOnClickListener(v -> finish());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order selectedOrder = orderAdapter.getItem(position);
                Toast.makeText(Dashboard.this, "Pedido: " + selectedOrder.getIdPedido() + "\n\nFecha: " + selectedOrder.getFecha(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Dashboard.this, OrderActivity.class);
                intent.putExtra("ORDER_ID", selectedOrder.getIdPedido());
                intent.putExtra("ORDER_DATE", selectedOrder.getFecha());
                startActivity(intent);
            }
        });

        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                orderAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void fetchOrders() {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        authToken = preferences.getString("accessToken", null);
        int id_usuario = preferences.getInt("id_usuario", -1);

        if (authToken == null) {
            Toast.makeText(this, "No autenticado. Inicie sesión.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        String fullAuthToken = "Bearer " + authToken;

        if (id_usuario == -1) {
            Toast.makeText(Dashboard.this, "ID de usuario no válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<List<Order>> call = apiService.getOrders(fullAuthToken);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orders.clear();
                    for (Order order : response.body()) {
                        if (order.getIdUsuario() == id_usuario) {
                            orders.add(order);
                        }
                    }
                    updateOrderDisplay();
                } else {
                    Toast.makeText(Dashboard.this, "Error en la respuesta: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(Dashboard.this, "Error al cargar pedidos: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterOrdersByStatus(String status) {
        Toast.makeText(this, "Filtrando por: " + status, Toast.LENGTH_SHORT).show();
        List<Order> filteredList = new ArrayList<>();

        if (status.equals("TODOS LOS ESTADOS")) {
            filteredList.addAll(orders);
        } else {
            for (Order order : orders) {
                if (order.getEstado() != null && order.getEstado().equalsIgnoreCase(status)) {
                    filteredList.add(order);
                }
            }
        }
        orderAdapter.updateOrders(filteredList);
    }

    public void showFilterDialog(View view) {
        String[] statuses = {"TODOS LOS ESTADOS", "ACEPTADO", "PENDIENTE", "CANCELADO"};

        new AlertDialog.Builder(this)
                .setTitle("Filtrar por estado")
                .setItems(statuses, (dialog, which) -> {
                    String selectedStatus = statuses[which];
                    filterOrdersByStatus(selectedStatus);
                })
                .show();
    }

    private void updateOrderDisplay() {
        Collections.sort(orders, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                return getOrderPriority(o1.getEstado()) - getOrderPriority(o2.getEstado());
            }

            private int getOrderPriority(String estado) {
                switch (estado) {
                    case "ACEPTADO":
                        return 1;
                    case "PENDIENTE":
                        return 2;
                    case "CANCELADO":
                        return 3;
                    default:
                        return 4;
                }
            }
        });

        if (orders.isEmpty()) {
            Toast.makeText(Dashboard.this, "No tienes pedidos", Toast.LENGTH_SHORT).show();
            listView.setVisibility(View.GONE);
            noOrdersTextView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            noOrdersTextView.setVisibility(View.GONE);
        }
        orderAdapter.updateOrders(orders);
    }

    public void profileBtn(View view) {
        Toast.makeText(this, "Redireccionando a tu perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void homeButton(View v) {
        Toast.makeText(this, "Redireccionando a home", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void productsButton(View v) {
        Toast.makeText(this, "Redireccionando a productos", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProductCategories.class);
        startActivity(intent);
    }
}
