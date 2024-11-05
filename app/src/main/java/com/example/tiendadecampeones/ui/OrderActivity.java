package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import com.example.tiendadecampeones.adapters.OrderProductAdapter;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    private TextView orderNumber;
    private TextView orderDate;
    private TextView orderStatus;
    private TextView totalAmount;
    private TextView paymentMethod;
    private TextView cardsMethod;
    private Button volverBtn;
    private ImageButton backButton;
    private Button trackOrderButton;
    private Button cancelButton;
    private int id_pedido;
    private RecyclerView productsRecyclerView;
    private OrderProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initializeViews();
        Intent intent = getIntent();
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        id_pedido = intent.getIntExtra("ORDER_ID", -1);

        if (id_pedido != -1) {
            loadOrderDetails();
        } else {
            showToast("Error al cargar la información del pedido");
        }

        setupButtonListeners();
    }

    private void initializeViews() {
        orderNumber = findViewById(R.id.orderNumber);
        orderDate = findViewById(R.id.orderDate);
        orderStatus = findViewById(R.id.orderStatus);
        paymentMethod = findViewById(R.id.paymentMethod);
        cardsMethod = findViewById(R.id.cardsMethod);
        totalAmount = findViewById(R.id.totalAmount);
        volverBtn = findViewById(R.id.volverBtn);
        backButton = findViewById(R.id.backButton);
        trackOrderButton = findViewById(R.id.trackOrderButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    private void loadOrderDetails() {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String token = preferences.getString("accessToken", null);
        int id_usuario = preferences.getInt("id_usuario", -1);

        if (id_usuario != -1 && token != null) {
            ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

            Call<List<Order>> call = apiService.getOrders();
            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Order> allOrders = response.body();
                        for (Order order : allOrders) {
                            if (order.getIdUsuario() == id_usuario && order.getIdPedido() == id_pedido) {
                                displayOrderDetails(order);
                                return;
                            }
                        }
                        showToast("Pedido no encontrado");
                    } else {
                        showToast("Error al obtener detalles del pedido");
                    }
                }
                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    showToast("Error al cargar los detalles del pedido: " + t.getMessage());
                }
            });
        } else {
            showToast("Usuario no autenticado. Inicie sesión.");
        }
    }

    private void cancelOrder(int id_pedido) {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String token = preferences.getString("accessToken", null);
        int id_usuario = preferences.getInt("id_usuario", -1);

        if (id_usuario != -1 && token != null) {
            ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

            Call<Void> deleteOrderCall = apiService.deleteOrder(id_pedido);
            deleteOrderCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        showToast("Pedido cancelado exitosamente");
                        volverAlDashboard();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            if (response.code() == 404) {
                                showToast("El pedido no se encontró o ya estaba cancelado.");
                            } else if (response.code() == 403) {
                                showToast("No tienes permiso para cancelar este pedido.");
                            } else {
                                showToast("Error desconocido al cancelar el pedido.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Error al cancelar el pedido: " + t.getMessage());
                }
            });
        } else {
            showToast("Usuario no autenticado. Inicie sesión.");
        }
    }

    private void displayOrderDetails(Order order) {
        orderNumber.setText(getString(R.string.order_label) + " " + order.getIdPedido());

        orderDate.setText(getString(R.string.order_date_label) + " " + (order.getFecha()));

        orderStatus.setText(getString(R.string.order_status_label) + " " + order.getEstado());

        List<Order.OrderDetail> detalles = order.getDetalles();
        if (detalles != null) {
            adapter = new OrderProductAdapter(this, detalles);
            productsRecyclerView.setAdapter(adapter);
        }

        List<Order.FormaDePago> formasDePagoList = order.getFormaDePago();
        if (formasDePagoList != null && !formasDePagoList.isEmpty()) {
            StringBuilder formasDePagoStr = new StringBuilder();
            StringBuilder tarjetasStr = new StringBuilder();
            boolean ocultarTarjetas = false;
            for (Order.FormaDePago forma : formasDePagoList) {
                if (forma.getFormaDePagoDescripcion() != null) {
                    formasDePagoStr.append(forma.getFormaDePagoDescripcion()).append(", ");
                }
                if ("Transferencia".equalsIgnoreCase(forma.getFormaDePagoDescripcion()) ||
                        "Debito".equalsIgnoreCase(forma.getFormaDePagoDescripcion())) {
                    ocultarTarjetas = true;
                } else if ("Credito".equalsIgnoreCase(forma.getFormaDePagoDescripcion()) && forma.getTarjeta() != null) {
                    tarjetasStr.append(forma.getTarjeta()).append(", ");
                }
            }
            if (formasDePagoStr.length() > 0) {
                formasDePagoStr.setLength(formasDePagoStr.length() - 2);
            }
            if (tarjetasStr.length() > 0) {
                tarjetasStr.setLength(tarjetasStr.length() - 2);
            }
            paymentMethod.setText(getString(R.string.payment_method_label) + " " + formasDePagoStr.toString());
            if (ocultarTarjetas) {
                cardsMethod.setVisibility(View.GONE);
            } else {
                cardsMethod.setText(getString(R.string.cards_method) + " " + tarjetasStr.toString());
                cardsMethod.setVisibility(View.VISIBLE);
            }
        } else {
            cardsMethod.setVisibility(View.VISIBLE);
        }

        totalAmount.setText("Total Abonado: $" + order.getTotal());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupButtonListeners() {
        volverBtn.setOnClickListener(v -> volverAlDashboard());
        backButton.setOnClickListener(v -> volverAlDashboard());

        trackOrderButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.andreani.com/"));
            startActivity(browserIntent);
        });

        cancelButton.setOnClickListener(v -> cancelOrder(id_pedido));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void volverAlDashboard() {
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void homeButton(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void productsButton(View view) {
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }

    public void profileBtn(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
