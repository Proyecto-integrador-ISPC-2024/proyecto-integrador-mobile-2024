package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiendadecampeones.R;

import com.example.tiendadecampeones.models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartResume extends AppCompatActivity {

    private TextView listPurchaseTextView;
    private TextView totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_resume);

        listPurchaseTextView = findViewById(R.id.listPurchaseTextView);
        totalTextView = findViewById(R.id.totalTextView);

        // Obtener productos del carrito (simulado)
        Map<Product, Integer> cartItems = getCartItems();

        // Mostrar los productos en el TextView
        displayCartSummary(cartItems);
    }

    // Simulación de los productos en el carrito con cantidades
    private Map<Product, Integer> getCartItems() {
        Map<Product, Integer> cartItems = new HashMap<>();

        // Creamos algunos productos de ejemplo
        Product product1 = new Product("Remera 1", "Remera deportiva", 5000.0, R.mipmap.alemania_1954_primera);
        Product product2 = new Product("Medias", "Medias deportivas", 750.0, R.mipmap.alemania_1974_primera);

        // Agregamos productos con cantidades
        cartItems.put(product1, 1); // 1 unidad de Remera
        cartItems.put(product2, 2); // 2 unidades de Medias

        return cartItems;
    }

    private void displayCartSummary(Map<Product, Integer> cartItems) {
        StringBuilder cartSummary = new StringBuilder();
        double totalAmount = 0;

        cartSummary.append("Compra\n");

        // Iterar sobre los productos en el carrito
        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double itemTotal = product.getPrice() * quantity;

            // Truncar el nombre del producto a 7 caracteres y formatear
            String productName = product.getName();
            if (productName.length() > 7) {
                productName = productName.substring(0, 7);
            }

            // Ajustar el espaciado para alinear los nombres
            cartSummary.append(String.format("%d\t%-7s\t\t\t------------------------\t\t$%.2f\n", quantity, productName, itemTotal));

            totalAmount += itemTotal;
        }

        StringBuilder total = new StringBuilder();

        // Línea separadora y total
        total.append("----------------------------\n");
        total.append("Total: $").append(String.format("%.2f", totalAmount));

        // Establecer el resumen en el TextView
        listPurchaseTextView.setText(cartSummary.toString());
        totalTextView.setText(total.toString());
    }


    //Lanzar vista Productos
    public void vProducts(View v){
        Intent intento = new Intent(this, ProductsActivity.class);
        startActivity(intento);
    }

    //Cerramos la ventana actual
    public void _back(View v){
        finish();
    }

    //Lanazamos la ventana Home
    public void vHome(View v){
        Intent intento = new Intent(this, Home.class);
        startActivity(intento);
    }

    public void vProfile(View v){
        Intent intento = new Intent(this, Profile.class);
        startActivity(intento);
    }
}
