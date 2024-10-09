package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.network.ApiService;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity {
    private TextView customerName;
    private TextView orderNumber;
    private TextView orderDate;
    private TextView orderStatus;
    private TextView productDetails;
    private TextView totalAmount;
    private Button volverBtn;
    private ImageButton backButton;
    private Button trackOrderButton;
    private Button cancelButton;
    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Inicialización de vistas
        customerName = findViewById(R.id.customerName);
        orderNumber = findViewById(R.id.orderNumber);
        orderDate = findViewById(R.id.orderDate);
        orderStatus = findViewById(R.id.orderStatus);
        productDetails = findViewById(R.id.productDetails);
        totalAmount = findViewById(R.id.totalAmount);
        volverBtn = findViewById(R.id.volverBtn);
        backButton = findViewById(R.id.backButton);
        trackOrderButton = findViewById(R.id.trackOrderButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Obtener el ID del pedido de la intención
        Intent intent = getIntent();
        orderId = intent.getIntExtra("ORDER_ID", -1);

        if (orderId != -1) {
            // Cargar detalles del pedido
            loadOrderDetails();
        } else {
            Toast.makeText(this, "Error al cargar la información del pedido", Toast.LENGTH_SHORT).show();
        }

        // Acciones de botones
        volverBtn.setOnClickListener(this::volverAlDashboard);
        backButton.setOnClickListener(v -> finish());

        trackOrderButton.setOnClickListener(v -> {
            // Abrir enlace de seguimiento del pedido
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.andreani.com/"));
            startActivity(browserIntent);
        });

        // Acción para cancelar el pedido
        cancelButton.setOnClickListener(v -> {
            Log.d("OrderActivity", "Intentando cancelar el pedido con ID: " + orderId);
            cancelOrder(orderId);
        });
    }

    // Cargar detalles del pedido desde la API
    private void loadOrderDetails() {
        ApiService apiService = ApiService.create();
        Call<List<Order>> call = apiService.getOrders();

        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> allOrders = response.body();
                    for (Order order : allOrders) {
                        if (order.getIdPedido() == orderId) {
                            displayOrderDetails(order);
                            return;
                        }
                    }
                    Toast.makeText(OrderActivity.this, "Pedido no encontrado", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("OrderActivity", "Error al obtener detalles del pedido, código de respuesta: " + response.code());
                    Toast.makeText(OrderActivity.this, "Error al obtener detalles del pedido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.e("OrderActivity", "Fallo de conexión al cargar pedidos: " + t.getMessage());
                Toast.makeText(OrderActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Mostrar los detalles del pedido en la interfaz de usuario
    private void displayOrderDetails(Order order) {
        orderNumber.setText("Pedido " + order.getIdPedido());
        orderDate.setText(order.getFecha());
        orderStatus.setText("Estado: " + order.getEstado());
        productDetails.setText(order.getDetalle().toString());
        totalAmount.setText("Total Abonado: $" + order.getTotal());
    }

    // Cancelar pedido
    private void cancelOrder(int orderId) {
        ApiService apiService = ApiService.create();

        // Realizar la solicitud de eliminación (cancelación)
        Call<Void> deleteCall = apiService.cancelOrder(orderId);
        deleteCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(OrderActivity.this, "Pedido cancelado exitosamente", Toast.LENGTH_SHORT).show();
                    volverAlDashboard(null);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("OrderActivity", "Error al cancelar el pedido: " + errorBody);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(OrderActivity.this, "Error al cancelar el pedido", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(OrderActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
                Log.e("OrderActivity", "Fallo de conexión: " + t.getMessage());
            }
        });
    }

    // Redirigir al Dashboard
    public void volverAlDashboard(View view) {
        Toast.makeText(this, "Redirigiendo al dashboard", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    // Manejar la acción del botón de retroceso en la barra de acción
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void homeButton(View view) {
        Toast.makeText(this, "Redirigiendo a home", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void productsButton(View view) {
        Toast.makeText(this, "Redirigiendo a productos", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }

    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
