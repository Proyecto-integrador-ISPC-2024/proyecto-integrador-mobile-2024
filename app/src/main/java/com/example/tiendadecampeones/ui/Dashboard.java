package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.models.DashboardOrder;
import com.example.tiendadecampeones.adapters.OrderAdapter;
import com.example.tiendadecampeones.network.ApiService;

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
    private List<DashboardOrder> orders = new ArrayList<>();
    private ImageButton backButton;
    private SearchView searchInput;
    private int currentUserId = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        listView = findViewById(R.id.listView);
        searchInput = findViewById(R.id.searchV);
        backButton = findViewById(R.id.backButton);

        // Establecer el texto de referencia del SearchView
        searchInput.setQueryHint(getString(R.string.searchV));

        orderAdapter = new OrderAdapter(this, orders);
        listView.setAdapter(orderAdapter);

        // Cargar los pedidos desde la API
        fetchOrders();

        // Botón para volver a la pantalla anterior
        backButton.setOnClickListener(v -> finish());

        // Manejar clics en elementos de la lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DashboardOrder selectedOrder = orderAdapter.getItem(position);
                Toast.makeText(Dashboard.this, "Pedido: " + selectedOrder.getIdPedido() + "\n\nFecha: " + selectedOrder.getFecha(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Dashboard.this, OrderActivity.class);
                intent.putExtra("ORDER_ID", selectedOrder.getIdPedido());
                intent.putExtra("ORDER_DATE", selectedOrder.getFecha());
                startActivity(intent);
            }
        });

        // Manejar la búsqueda en el SearchView
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

    // Método para cargar pedidos desde la API
    private void fetchOrders() {
        ApiService apiService = ApiService.create();

        Call<List<Order>> call = apiService.getOrders();
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    orders.clear();

                    for (Order order : response.body()) {
                        if (order.getIdUsuario() == currentUserId) {
                            DashboardOrder dashboardOrder = new DashboardOrder(
                                    order.getIdPedido(),
                                    order.getFecha(),
                                    order.getEstado()
                            );
                            orders.add(dashboardOrder);
                        }
                    }

                    // Ordenar la lista de pedidos según el estado
                    Collections.sort(orders, new Comparator<DashboardOrder>() {
                        @Override
                        public int compare(DashboardOrder o1, DashboardOrder o2) {
                            return getOrderPriority(o1.getEstado()) - getOrderPriority(o2.getEstado());
                        }

                        // Método para asignar una prioridad a cada estado
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

                    Log.d("Dashboard", "Orders fetched: " + orders.size());

                    if (orders.isEmpty()) {
                        Toast.makeText(Dashboard.this, "No tienes pedidos.", Toast.LENGTH_SHORT).show();
                        listView.setVisibility(View.GONE);
                    } else {
                        listView.setVisibility(View.VISIBLE);
                    }

                    orderAdapter.notifyDataSetChanged();
                    orderAdapter.getFilter().filter("");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Método para filtrar pedidos por estado
    private void filterOrdersByStatus(String status) {
        Toast.makeText(this, "Filtrando por: " + status, Toast.LENGTH_SHORT).show();

        List<DashboardOrder> filteredList = new ArrayList<>();
        if (status.equals("TODOS LOS ESTADOS")) {
            filteredList.addAll(orders);
        } else {
            for (DashboardOrder order : orders) {
                if (order.getEstado() != null && order.getEstado().equalsIgnoreCase(status)) {
                    filteredList.add(order);
                }
            }
        }
        orderAdapter.updateOrders(filteredList); // Actualiza la lista mostrada
    }

    public void logoutClick(View v) {
        Toast.makeText(this, "Has cerrado tu sesión", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void backButton(View v) {
        Toast.makeText(this, "Redirigiendo a tu página anterior", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void webtn(View v) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AboutUs.class);
        startActivity(intent);
    }

    public void homeButton(View v) {
        Toast.makeText(this, "Redirigiendo a home", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void productsButton(View v) {
        Toast.makeText(this, "Redirigiendo a productos", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }
}
