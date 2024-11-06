package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.CartResumeAdapter;
import com.example.tiendadecampeones.models.Pedido;
import com.example.tiendadecampeones.models.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartResume extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartResumeAdapter cartResumeAdapter;
    private List<Product> productList;
    private TextView totalTextView;
    private Button confirmPurchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_resume);

        initializeUI();

        // Recibiendo lista de productos mediante intent desde Cart.java
        Intent intent = getIntent();
        String productListJson = intent.getStringExtra("product_list");

        // Formateo a JSON
        Gson gson = new Gson();
        Type productListType = new TypeToken<List<Product>>() {}.getType();
        productList = gson.fromJson(productListJson, productListType);

        // Seteo de recycler view
        setupRecyclerView();

        // Creación de instancia de Pedido
        Pedido pedido = createPedidoFromCart();

        // Cálculo y renderizado del monto total
        double totalAmount = pedido.getTotal();
        totalTextView.setText(String.format("Total: $%.2f", totalAmount));

        // Confirmación de lista de productos
        confirmPurchaseButton.setOnClickListener(v -> navigateToPaymentMethods());
    }

    // Inicialización y captura de elementos de UI
    private void initializeUI() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewCart);
        totalTextView = findViewById(R.id.totalPrice);
        confirmPurchaseButton = findViewById(R.id.confirmPurchaseButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Seteo de información en el recycler view
    private void setupRecyclerView() {
        Map<Product, Integer> cartItems = new HashMap<>();
        for (Product product : productList) {
            for (Product.Talle talle : product.getTalles()) {
                if (talle.getCantidadCompra() > 0) {
                    cartItems.put(product, talle.getCantidadCompra());
                }
            }
        }

        cartResumeAdapter = new CartResumeAdapter(this, productList, cartItems);
        recyclerView.setAdapter(cartResumeAdapter);
    }

    // Declaración de creación de instancia de Pedido - se ejecuta en línea 56
    private Pedido createPedidoFromCart() {
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(getUserIdFromPreferences());
        pedido.setTotal(calculateTotal(productList));
        pedido.setDetalles(buildDetallesFromCart(productList));
        return pedido;
    }

    // Obtención del id de usuario desde SharedPreferences
    private int getUserIdFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        return sharedPref.getInt("id_usuario", -1);
    }

    // Cálculo del precio total de la lista de productos
    private double calculateTotal(List<Product> productList) {
        double total = 0;
        for (Product product : productList) {
            for (Product.Talle talle : product.getTalles()) {
                total += product.getProductos().getPrecio() * talle.getCantidadCompra();
            }
        }
        return total;
    }

    // Creación de la instancia de la clase Detalle
    private List<Pedido.Detalle> buildDetallesFromCart(List<Product> productList) {
        List<Pedido.Detalle> detalles = new ArrayList<>();

        for (Product product : productList) {
            for (Product.Talle talle : product.getTalles()) {
                if (talle.getCantidadCompra() > 0) {
                    Pedido.Detalle detalle = new Pedido.Detalle();
                    detalle.setCantidad(talle.getCantidadCompra());
                    detalle.setSubtotal(product.getProductos().getPrecio() * talle.getCantidadCompra());
                    detalle.setIdProducto(product.getProductos().getIdProducto());
                    detalle.setIdTalle(talle.getIdTalle());
                    detalles.add(detalle);
                }
            }
        }
        return detalles;
    }

    // Navegación a la selección de métodos de pago
    private void navigateToPaymentMethods() {
        Pedido pedido = createPedidoFromCart();
        Log.d("CartResume", "Pedido antes de enviar: " + pedido.toString());
        Gson gson = new Gson();
        String pedidoJson = gson.toJson(pedido);
        Intent intent = new Intent(CartResume.this, PaymentMethodsActivity.class);
        intent.putExtra("pedido", pedidoJson);

        startActivity(intent);
    }
}
