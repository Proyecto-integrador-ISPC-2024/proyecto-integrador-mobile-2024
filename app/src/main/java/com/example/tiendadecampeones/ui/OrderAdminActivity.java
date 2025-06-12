package com.example.tiendadecampeones.ui;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.app.AlertDialog;
import android.widget.ProgressBar;

public class OrderAdminActivity extends AppCompatActivity {
    private TextView orderNumber;
    private TextView orderDate;
    private TextView orderStatus;
    private TextView totalAmount;
    private TextView paymentMethod;
    private TextView cardsMethod;
    private ImageButton backButton;
    private Button sendOrderBtn;
    private Button trackOrderButton;
    private Button cancelButton;
    private TextView trackingTitle;
    private int id_pedido;
    private RecyclerView productsRecyclerView;
    private OrderProductAdapter adapter;
    private LinearLayout orderDetailsLayout;
    private boolean isFirstLoad = true;
    private ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_admin);
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
        backButton = findViewById(R.id.backButton);
        sendOrderBtn = findViewById(R.id.sendOrderBtn);
        trackOrderButton = findViewById(R.id.trackOrderButton);
        cancelButton = findViewById(R.id.cancelButton);
        trackingTitle = findViewById(R.id.trackingTitle);
        orderDetailsLayout = findViewById(R.id.orderDetailsLayout);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        orderDetailsLayout.setVisibility(View.GONE);
        sendOrderBtn.setVisibility(View.GONE);
        trackOrderButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);
        trackingTitle.setVisibility(View.GONE);
    }

    private void loadOrderDetails() {
        loadingSpinner.setVisibility(View.VISIBLE);
        orderDetailsLayout.setVisibility(View.GONE);
        productsRecyclerView.setVisibility(View.GONE);

        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String token = preferences.getString("accessToken", null);

        if (token != null) {
            ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

            Call<List<Order>> call = apiService.getOrders();
            call.enqueue(new Callback<List<Order>>() {
                @Override
                public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                    loadingSpinner.setVisibility(View.GONE);
                    orderDetailsLayout.setVisibility(View.VISIBLE);
                    productsRecyclerView.setVisibility(View.VISIBLE);

                    if (response.isSuccessful() && response.body() != null) {
                        List<Order> allOrders = response.body();
                        android.util.Log.d("OrderAdminActivity", "Pedidos recibidos: " + allOrders.size());

                        boolean orderFound = false;
                        for (Order order : allOrders) {
                            if (order.getIdPedido() == id_pedido) {
                                displayOrderDetails(order);
                                orderFound = true;
                                break;
                            }
                        }

                        if (!orderFound) {
                            showToast("Pedido no encontrado");
                        }
                    } else {
                        showToast("Error al obtener detalles del pedido");
                    }
                }
                @Override
                public void onFailure(Call<List<Order>> call, Throwable t) {
                    loadingSpinner.setVisibility(View.GONE);
                    orderDetailsLayout.setVisibility(View.VISIBLE);
                    productsRecyclerView.setVisibility(View.VISIBLE);
                    showToast("Error al cargar los detalles del pedido: " + t.getMessage());
                }
            });
        } else {
            loadingSpinner.setVisibility(View.GONE);
            orderDetailsLayout.setVisibility(View.VISIBLE);
            productsRecyclerView.setVisibility(View.VISIBLE);
            showToast("Usuario no autenticado. Inicie sesión.");
        }
    }

    private void sendOrder(int id_pedido) {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String token = preferences.getString("accessToken", null);

        if (token != null) {
            ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

            Call<Void> sendOrderCall = apiService.sendOrder(id_pedido);
            sendOrderCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        showToast("Pedido marcado como enviado exitosamente");
                        loadOrderDetails();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            if (response.code() == 400) {
                                showToast("Este pedido ya ha sido enviado o está cancelado.");
                            } else if (response.code() == 403) {
                                showToast("No tienes permisos para enviar pedidos.");
                            } else {
                                showToast("Error desconocido al enviar el pedido.");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    showToast("Error al enviar el pedido: " + t.getMessage());
                }
            });
        } else {
            showToast("Usuario no autenticado. Inicie sesión.");
        }
    }

    private void cancelOrder(int id_pedido) {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String token = preferences.getString("accessToken", null);

        if (token != null) {
            ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);

            Call<Void> deleteOrderCall = apiService.deleteOrder(id_pedido);
            deleteOrderCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        showToast("Pedido cancelado exitosamente");
                        finish();
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
        if (order == null) {
            showToast("Error al cargar los detalles del pedido");
            return;
        }

        orderNumber.setText(getString(R.string.order_label) + " " + order.getIdPedido());
        orderDate.setText(getString(R.string.order_date_label) + " " + order.getFecha());
        orderStatus.setText(getString(R.string.order_status_label) + " " + order.getEstado());

        if (isFirstLoad) {
            showToast("Detalles del pedido " + order.getIdPedido());
            isFirstLoad = false;
        }

        // Visibilidad de botones según el estado
        if ("ENVIADO".equalsIgnoreCase(order.getEstado())) {
            sendOrderBtn.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            trackOrderButton.setVisibility(View.VISIBLE);
            trackingTitle.setVisibility(View.VISIBLE);
        } else if ("CANCELADO".equalsIgnoreCase(order.getEstado())) {
            sendOrderBtn.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            trackOrderButton.setVisibility(View.GONE);
            trackingTitle.setVisibility(View.GONE);
        } else if ("ACEPTADO".equalsIgnoreCase(order.getEstado())) {
            sendOrderBtn.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            trackOrderButton.setVisibility(View.GONE);
            trackingTitle.setVisibility(View.GONE);
        } else {
            sendOrderBtn.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            trackOrderButton.setVisibility(View.GONE);
            trackingTitle.setVisibility(View.GONE);
        }

        // Mostrar detalles de productos
        List<Order.OrderDetail> detalles = order.getDetalles();
        if (detalles != null && !detalles.isEmpty()) {
            adapter = new OrderProductAdapter(this, detalles);
            productsRecyclerView.setAdapter(adapter);
        }

        // Mostrar formas de pago
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
            paymentMethod.setText(getString(R.string.payment_method_label) + " No especificado");
            cardsMethod.setVisibility(View.GONE);
        }

        totalAmount.setText("Total Abonado: $" + order.getTotal());
        orderDetailsLayout.setVisibility(View.VISIBLE);
    }

    private void setupButtonListeners() {
        backButton.setOnClickListener(v -> finish());
        sendOrderBtn.setOnClickListener(v -> sendOrder(id_pedido));
        trackOrderButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.andreani.com/"));
            startActivity(browserIntent);
        });
        cancelButton.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Cancelar Pedido")
                    .setMessage("¿Está seguro que desea cancelar este pedido?")
                    .setPositiveButton("Sí", (dialog, which) -> cancelOrder(id_pedido))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void homeButton(View v) {
        Toast.makeText(this, "¡ Home !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void productsButton(View v) {
        Toast.makeText(this, "¡ Nuestros Productos !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProductCategories.class);
        startActivity(intent);
    }
}