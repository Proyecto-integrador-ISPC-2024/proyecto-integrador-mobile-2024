package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.CartResumeAdapter;
import com.example.tiendadecampeones.models.CartItem;
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
    private List<CartItem> cartItemList;  // Cambiar de Product a CartItem
    private TextView totalTextView;
    private Button confirmPurchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_resume);

        // Verificar si el usuario es admin o super admin
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String userRole = preferences.getString("userRole", "");
        boolean isStaff = preferences.getBoolean("isStaff", false);
        boolean isSuperuser = preferences.getBoolean("isSuperuser", false);

        boolean isAdmin = "ADMIN".equals(userRole) && isStaff;
        boolean isSuperAdmin = isAdmin && isSuperuser;

        if (isAdmin || isSuperAdmin) {
            Toast.makeText(this, "Los administradores no pueden realizar pedidos", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        initializeUI();


        Intent intent = getIntent();
        String cartItemListJson = intent.getStringExtra("cart_item_list");


        Gson gson = new Gson();
        Type cartItemListType = new TypeToken<List<CartItem>>() {}.getType();
        cartItemList = gson.fromJson(cartItemListJson, cartItemListType);

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


    private void initializeUI() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewCart);
        totalTextView = findViewById(R.id.totalPrice);
        confirmPurchaseButton = findViewById(R.id.confirmPurchaseButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void setupRecyclerView() {
        Map<CartItem, Integer> cartItems = new HashMap<>();
        for (CartItem cartItem : cartItemList) {
            cartItems.put(cartItem, cartItem.getCantidadCompra());
        }

        // Pasar la lista de CartItem al adaptador
        cartResumeAdapter = new CartResumeAdapter(this, cartItemList, cartItems);
        recyclerView.setAdapter(cartResumeAdapter);
    }

    // Declaración de creación de instancia de Pedido
    private Pedido createPedidoFromCart() {
        Pedido pedido = new Pedido();
        pedido.setIdUsuario(getUserIdFromPreferences());
        pedido.setTotal(calculateTotal(cartItemList));
        pedido.setDetalles(buildDetallesFromCart(cartItemList));
        return pedido;
    }


    private int getUserIdFromPreferences() {
        SharedPreferences sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        return sharedPref.getInt("id_usuario", -1);
    }

    // Cálculo del precio total de la lista de CartItem
    private double calculateTotal(List<CartItem> cartItemList) {
        double total = 0;
        for (CartItem cartItem : cartItemList) {
            total += cartItem.getProducto().getPrecio() * cartItem.getCantidadCompra();  // Usar getProducto() y getCantidadCompra()
        }
        return total;
    }

    // Creación de la instancia de la clase Detalle
    private List<Pedido.Detalle> buildDetallesFromCart(List<CartItem> cartItemList) {
        List<Pedido.Detalle> detalles = new ArrayList<>();

        for (CartItem cartItem : cartItemList) {
            Product.Producto producto = cartItem.getProducto();
            Pedido.Detalle detalle = new Pedido.Detalle();
            detalle.setCantidad(cartItem.getCantidadCompra());
            detalle.setSubtotal(producto.getPrecio() * cartItem.getCantidadCompra());
            detalle.setIdProducto(producto.getIdProducto());
            detalle.setIdTalle(cartItem.getTalle().getIdTalle());
            detalles.add(detalle);
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