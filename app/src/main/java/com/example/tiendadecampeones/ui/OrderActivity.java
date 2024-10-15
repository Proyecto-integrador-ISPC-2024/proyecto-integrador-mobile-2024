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

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.models.OrderDetail;
import com.example.tiendadecampeones.models.PaymentMethods;
import com.example.tiendadecampeones.models.Usuario;

import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;

import java.io.IOException;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class OrderActivity extends AppCompatActivity {
    private TextView orderNumber;
    private TextView orderDate;
    private TextView orderStatus;
    private TextView productDetails;
    private TextView totalAmount;
    private TextView paymentMethod;
    private TextView cardMethods;
    private Button volverBtn;
    private ImageButton backButton;
    private Button trackOrderButton;
    private Button cancelButton;
    private int id_pedido;
    private int id_usuario;
    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Inicialización de vistas
        initializeViews();

        // Obtener el ID del pedido de la intención
        Intent intent = getIntent();
        id_pedido = intent.getIntExtra("ORDER_ID", -1);

        // Recuperar ID de usuario y token desde SharedPreferences
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        authToken = preferences.getString("accessToken", null);

        id_usuario = preferences.getInt("id_usuario", -1);

        if (id_pedido != -1) {
            // Cargar detalles del pedido
            loadOrderDetails();
        } else {
            showToast("Error al cargar la información del pedido");
        }

        // Configuración de acciones de los botones
        setupButtonListeners();
    }
    private void initializeViews() {
        orderNumber = findViewById(R.id.orderNumber);
        orderDate = findViewById(R.id.orderDate);
        orderStatus = findViewById(R.id.orderStatus);
        productDetails = findViewById(R.id.productDetails);
        paymentMethod = findViewById(R.id.paymentMethod);
        cardMethods = findViewById(R.id.cardMethods);
        totalAmount = findViewById(R.id.totalAmount);
        volverBtn = findViewById(R.id.volverBtn);
        backButton = findViewById(R.id.backButton);
        trackOrderButton = findViewById(R.id.trackOrderButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    // Cargar detalles del pedido desde la API
    private void loadOrderDetails() {
        if (id_usuario != -1 && authToken != null) {
            ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

            // Preparar el token con el prefijo "Bearer "
            String fullAuthToken = "Bearer " + authToken;

            // Pasar el token y el id del usuario (ahora idUsuario es int)
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
                        Log.e("OrderActivity", "Error al obtener detalles del pedido, código de respuesta: " + response.code());
                        showToast("Error al obtener detalles del pedido");
                    }
                }

                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    Log.e("OrderActivity", "Fallo de conexión al cargar pedidos: " + t.getMessage());
                    showToast("Fallo de conexión");
                }
            });
        } else {
            showToast("Usuario no autenticado");
        }
    }

    //     Mostrar los detalles del pedido en la interfaz de usuario
    private void displayOrderDetails(Order order) {
        //Numero de pedido
        orderNumber.setText(getString(R.string.order_label) + " " + order.getIdPedido());
        //Fecha de pedido
        orderDate.setText(getString(R.string.order_date_label) + " " + (order.getFecha()));
        //Estado de pedido
        orderStatus.setText(getString(R.string.order_status_label) + " " + order.getEstado());

        // Detales del producto
        if (order.getDetalles() != null) {
            productDetails.setText(order.getDetalles().toString());
        } else {
            productDetails.setText("No hay detalles disponibles");
        }

        //Formas de pago
        List<PaymentMethods.FormaDePago> formas_de_pago = order.getFormasDePago();
        String descripcion = (formas_de_pago != null && !formas_de_pago.isEmpty())
                ? formas_de_pago.get(0).getDescripcion() : "No disponible";
        paymentMethod.setText(getString(R.string.payment_method_label) + " " + descripcion);

        //Tarjetas
        List<PaymentMethods.Tarjeta> tarjetas = order.getTarjetas();
        String nombre_tarjeta = (tarjetas != null && !tarjetas.isEmpty())
                ? tarjetas.get(0).getNombreTarjeta() : "No disponible";
        cardMethods.setText(getString(R.string.cardMethods) + " " + nombre_tarjeta);

        //Total abonado
        totalAmount.setText("Total Abonado: $" + order.getTotal());
    }

    private void cancelOrder(int id_pedido) {
        // Verificar si el usuario está autenticado
        if (id_usuario != -1 && authToken != null) {
            ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

            // Preparar el token con el prefijo "Bearer "
            String fullAuthToken = "Bearer " + authToken;

            // Llama al método cancelOrder pasando solo el id_pedido
            Call<Void> deleteCall = apiService.cancelOrder(fullAuthToken, id_pedido);

            deleteCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        showToast("Pedido cancelado exitosamente");
                        volverAlDashboard(null);
                    } else {
                        handleErrorResponse(response);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Fallo de conexión");
                    Log.e("OrderActivity", "Fallo de conexión: " + t.getMessage());
                }
            });
        } else {
            showToast("Usuario no autenticado");
        }
    }
    // Manejar errores en la respuesta
    private void handleErrorResponse(Response<Void> response) {
        try {
            String errorBody = response.errorBody().string();
            Log.e("OrderActivity", "Error al cancelar el pedido: " + errorBody);
            showToast("Error al cancelar el pedido");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupButtonListeners() {
        volverBtn.setOnClickListener(this::volverAlDashboard);
        backButton.setOnClickListener(v -> finish());

        trackOrderButton.setOnClickListener(v -> {
            // Abrir enlace de seguimiento del pedido
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.andreani.com/"));
            startActivity(browserIntent);
        });

        cancelButton.setOnClickListener(v -> {
            Log.d("OrderActivity", "Intentando cancelar el pedido con ID: " + id_pedido);
            cancelOrder(id_pedido);
        });
    }

    // Redirigir al Dashboard
    public void volverAlDashboard(View view) {
        showToast("Redireccionando al dashboard");
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

    // Mostrar un mensaje de Toast
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
