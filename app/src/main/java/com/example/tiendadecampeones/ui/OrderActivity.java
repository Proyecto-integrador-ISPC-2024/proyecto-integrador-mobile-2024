package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.tiendadecampeones.network.ApiInterface;

public class OrderActivity extends AppCompatActivity {
//    private TextView customerName;
//    private TextView orderNumber;
//    private TextView orderDate;
//    private TextView orderStatus;
//    private TextView productDetails;
//    private TextView totalAmount;
//    private Button volverBtn;
//    private Button volverButton;
//    private Button trackOrderButton;
//    private Button cancelButton;
//    private int orderId;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order);
//
//        customerName = findViewById(R.id.customerName);
//        orderNumber = findViewById(R.id.orderNumber);
//        orderDate = findViewById(R.id.orderDate);
//        orderStatus = findViewById(R.id.orderStatus);
//        productDetails = findViewById(R.id.productDetails);
//        totalAmount = findViewById(R.id.totalAmount);
//        volverBtn = findViewById(R.id.volverBtn);
//        volverButton = findViewById(R.id.volverButton);
//        trackOrderButton = findViewById(R.id.trackOrderButton);
//        cancelButton = findViewById(R.id.cancelButton);
//
//        Intent intent = getIntent();
//        orderId = intent.getIntExtra("ORDER_ID", -1);
//
//        if (orderId != -1) {
//            loadAllOrders();
//        } else {
//            Toast.makeText(this, "Error al cargar la información del pedido", Toast.LENGTH_SHORT).show();
//        }
//
//        volverBtn.setOnClickListener(this::volverBtn);
//        volverButton.setOnClickListener(this::volverButton);
//
//        trackOrderButton.setOnClickListener(v -> {
//            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.andreani.com/"));
//            startActivity(browserIntent);
//        });
//
//        cancelButton.setOnClickListener(v -> {
//            Log.d("OrderActivity", "Intentando cancelar el pedido con ID: " + orderId);
//            cancelOrder(orderId);
//        });
//    }
//
//    private void loadAllOrders() {
//        ApiInterface apiService = ApiService.getApi();
//        Call<List<Order>> call = apiService.getOrders();
//
//        call.enqueue(new Callback<List<Order>>() {
//            @Override
//            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Order> allOrders = response.body();
//                    for (Order order : allOrders) {
//                        if (order.getIdPedido() == orderId) {
//                            displayOrderDetails(order);
//                            return;
//                        }
//                    }
//                    Toast.makeText(OrderActivity.this, "Pedido no encontrado", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.e("OrderActivity", "Error al obtener detalles del pedido, código de respuesta: " + response.code());
//                    Toast.makeText(OrderActivity.this, "Error al obtener detalles del pedido", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Order>> call, Throwable t) {
//                Log.e("OrderActivity", "Fallo de conexión al cargar pedidos: " + t.getMessage());
//                Toast.makeText(OrderActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void displayOrderDetails(Order order) {
////        customerName.setText(order.getIdUsuario() + "");
//        orderNumber.setText("Pedido " + order.getIdPedido());
//        orderDate.setText(order.getFecha());
//        orderStatus.setText("Estado: " + order.getEstado());
//        productDetails.setText(order.getDetalle().toString());
//        totalAmount.setText("Total Abonado: $" + order.getTotal());
//    }
//
//    private void cancelOrder(int orderId) {
//        ApiInterface apiService = ApiService.getApi();
//
//        Call<List<Order>> call = apiService.getOrders();
//        call.enqueue(new Callback<List<Order>>() {
//            @Override
//            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<Order> allOrders = response.body();
//                    for (Order order : allOrders) {
//                        if (order.getIdPedido() == orderId) {
//                            order.setEstado("CANCELADO");
//
//                            Call<Order> updateCall = apiService.updateOrderStatus(orderId, order);
//                            updateCall.enqueue(new Callback<Order>() {
//                                @Override
//                                public void onResponse(Call<Order> call, Response<Order> response) {
//                                    if (response.isSuccessful()) {
//                                        Toast.makeText(OrderActivity.this, "Pedido cancelado exitosamente", Toast.LENGTH_SHORT).show();
//                                        volverBtn(null);
//                                    } else {
//                                        try {
//                                            String errorBody = response.errorBody().string();
//                                            Log.e("OrderActivity", "Error al cancelar el pedido: " + errorBody);
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                        Toast.makeText(OrderActivity.this, "Error al cancelar el pedido", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<Order> call, Throwable t) {
//                                    Toast.makeText(OrderActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
//                                    Log.e("OrderActivity", "Fallo de conexión: " + t.getMessage());
//                                }
//                            });
//                            return;
//                        }
//                    }
//                    Toast.makeText(OrderActivity.this, "Pedido no encontrado", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(OrderActivity.this, "Error al obtener detalles del pedido", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Order>> call, Throwable t) {
//                Toast.makeText(OrderActivity.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
//                Log.e("OrderActivity", "Fallo de conexión: " + t.getMessage());
//            }
//        });
//    }
//
//
//    public void volverBtn(View view) {
//        Toast.makeText(this, "Redirigiendo al dashboard", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, Dashboard.class);
//        startActivity(intent);
//        finish();
//    }
//
//    public void volverButton(View view) {
//        Toast.makeText(this, "Redirigiendo al dashboard", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, Dashboard.class);
//        startActivity(intent);
//        finish();
//    }
//
//    public void homeButton(View view) {
//        Toast.makeText(this, "Redirigiendo a home", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, Home.class);
//        startActivity(intent);
//    }
//
//    public void productsButton(View view) {
//        Toast.makeText(this, "Redirigiendo a productos", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, ProductsActivity.class);
//        startActivity(intent);
//    }
//
//    public void profileBtn(View view) {
//        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, Profile.class);
//        startActivity(intent);
//    }
}
