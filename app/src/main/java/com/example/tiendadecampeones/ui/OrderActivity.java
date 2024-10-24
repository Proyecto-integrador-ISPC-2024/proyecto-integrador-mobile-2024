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
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import com.example.tiendadecampeones.adapters.OrderProductAdapter;
import androidx.recyclerview.widget.RecyclerView;

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
    private int id_usuario;
    private String authToken;
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
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        authToken = preferences.getString("accessToken", null);
        id_usuario = preferences.getInt("id_usuario", -1);

        if (id_pedido != -1) {
            loadOrderDetails();
        } else {
            showToast("Error al cargar la información del pedido");
        }

        setupButtonListeners();

    }

    private void clearCart() {
        SharedPreferences cartPrefs = getSharedPreferences("cart_shared_prefs", MODE_PRIVATE);
        SharedPreferences.Editor cartEditor = cartPrefs.edit();
        cartEditor.clear(); // Limpia todas las preferencias del carrito
        cartEditor.apply(); // Aplica los cambios
        Log.d("Cart", "El carrito ha sido limpiado");
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
        if (id_usuario != -1 && authToken != null) {
            ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
            String fullAuthToken = "Bearer " + authToken;

            Call<List<Order>> call = apiService.getOrders(fullAuthToken);
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
                    showToast("Fallo de conexión");
                }
            });
        } else {
            showToast("Usuario no autenticado");
        }
    }

    private void cancelOrder(int id_pedido) {
        if (id_usuario != -1 && authToken != null) {
            ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
            String fullAuthToken = "Bearer " + authToken;

            Call<Void> deleteOrderCall = apiService.deleteOrder(fullAuthToken, id_pedido);
            deleteOrderCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        showToast("Pedido cancelado exitosamente");
                        clearCart();  // Asegúrate de limpiar el carrito aquí
                        volverAlDashboard(null);
                    } else {
                        handleErrorResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Fallo de conexión al cancelar el pedido");
                }
            });
        } else {
            showToast("Usuario no autenticado");
        }
    }

    private void handleErrorResponse(Response<Void> response) {
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

    private void displayOrderDetails(Order order) {
        //Mostrar número de pedido
        orderNumber.setText(getString(R.string.order_label) + " " + order.getIdPedido());
        //Mostrar fecha
        orderDate.setText(getString(R.string.order_date_label) + " " + (order.getFecha()));
        //Mostrar estado
        orderStatus.setText(getString(R.string.order_status_label) + " " + order.getEstado());

        // Mostrar detalles de productos
        List<Order.OrderDetail> detalles = order.getDetalles();
        if (detalles != null) {
            adapter = new OrderProductAdapter(this, detalles);
            productsRecyclerView.setAdapter(adapter);
        }

        // Mostrar métodos de pago
        List<Order.FormaDePago> formasDePagoList = order.getFormaDePago();
        if (formasDePagoList != null && !formasDePagoList.isEmpty()) {
            StringBuilder formasDePagoStr = new StringBuilder();
            StringBuilder tarjetasStr = new StringBuilder();
            boolean ocultarTarjetas = false;
            for (Order.FormaDePago forma : formasDePagoList) {
                if (forma.getFormaDePagoDescripcion() != null) {
                    formasDePagoStr.append(forma.getFormaDePagoDescripcion()).append(", ");
                }
                if ("Transferencia".equalsIgnoreCase(forma.getFormaDePagoDescripcion())) {
                    ocultarTarjetas = true;
                } else if (forma.getTarjeta() != null) {
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
            paymentMethod.setText("No hay métodos de pago disponibles");
            cardsMethod.setText("No hay tarjetas disponibles");
            cardsMethod.setVisibility(View.VISIBLE);
        }

        //Mostrar total
        totalAmount.setText("Total Abonado: $" + order.getTotal());
    }

    private void setupButtonListeners() {
        volverBtn.setOnClickListener(this::volverAlDashboard);
        backButton.setOnClickListener(v -> finish());

        trackOrderButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.andreani.com/"));
            startActivity(browserIntent);
        });

        cancelButton.setOnClickListener(v -> cancelOrder(id_pedido));
    }

    public void volverAlDashboard(View view) {
        Log.d("Cart", "Llamando a clearCart antes de volver al dashboard");
        clearCart();  // Asegúrate de limpiar el carrito al volver
        showToast("Redireccionando al dashboard");
        Intent intent = new Intent(this, Dashboard.class);
        startActivity(intent);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void homeButton(View view) {
        showToast("Redireccionando a home");
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void productsButton(View view) {
        showToast("Redireccionando a productos");
        Intent intent = new Intent(this, ProductsActivity.class);
        startActivity(intent);
    }

    public void profileBtn(View view) {
        showToast("Redireccionando a tu perfil");
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}
