package com.example.tiendadecampeones.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.ui.Cart;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<Product> productList;
    private Context context;

    // Constructor for CartAdapter
    public CartAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);
        Product.Producto productoDetails = product.getProductos();

        // Set product name and price
        holder.productName.setText(productoDetails.getNombreProducto());
        holder.productPrice.setText("Precio: $" + productoDetails.getPrecio());

        // Set product image dynamically using the image URL
        String imageUrl = productoDetails.getImagen(); // URL of the product image

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image) // Optional placeholder
                .error(R.drawable.error_image) // Optional error image
                .into(holder.productImage);

        // Show the selected quantity and size (talle)
        StringBuilder talleInfo = new StringBuilder();
        for (Product.Talle talle : product.getTalles()) {
            talleInfo.append(talle.getTalle());
        }
        holder.productSize.setText(talleInfo.toString());

        // Calculate and display subtotal for the current product (price * quantity for each talle)
        double subtotal = 0;
        for (Product.Talle talle : product.getTalles()) {
            subtotal += productoDetails.getPrecio() * talle.getCantidadCompra();
        }
        holder.productSubtotal.setText("Subtotal: $" + subtotal);

        // Set up quantity change buttons
        holder.increaseQuantityButton.setOnClickListener(v -> {
            updateQuantity(holder, product, true); // Increase quantity
        });

        holder.decreaseQuantityButton.setOnClickListener(v -> {
            updateQuantity(holder, product, false); // Decrease quantity
        });

        // Set up remove button (trash icon)
        holder.removeButton.setOnClickListener(v -> {
            removeProduct(position);
        });

        // Display product quantity
        holder.productQuantity.setText(Integer.toString(product.getTalles().get(0).getCantidadCompra()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Function to update the quantity
    private void updateQuantity(CartViewHolder holder, Product product, boolean isIncrease) {
        Product.Talle talle = product.getTalles().get(0);
        int currentQuantity = talle.getCantidadCompra();

        if (isIncrease) {
            if (currentQuantity < talle.getStock()) {
                talle.setCantidadCompra(currentQuantity + 1);
            } else {
                Toast.makeText(context, "Stock máximo alcanzado", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (currentQuantity > 1) {
                talle.setCantidadCompra(currentQuantity - 1);
            } else {
                Toast.makeText(context, "Cantidad mínima alcanzada", Toast.LENGTH_SHORT).show();
            }
        }

        double newSubtotal = product.getProductos().getPrecio() * talle.getCantidadCompra();
        holder.productSubtotal.setText("Subtotal: $" + newSubtotal);
        holder.productQuantity.setText(Integer.toString(talle.getCantidadCompra()));

        ((Cart) context).calculateTotal();
        updateSharedPreferences();
    }

    // Function to remove a product from the cart
    private void removeProduct(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        ((Cart) context).calculateTotal();
        // Update SharedPreferences
        updateSharedPreferences();
    }

    // Function to update SharedPreferences with the modified product list
    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart_shared_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String updatedProductsJson = gson.toJson(productList);
        editor.putString("cart_products", updatedProductsJson);
        editor.apply();
    }
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productSize, productSubtotal, productQuantity;
        Button increaseQuantityButton, decreaseQuantityButton, removeButton;
        ImageView productImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productSize = itemView.findViewById(R.id.productTalle);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productSubtotal = itemView.findViewById(R.id.productSubtotal);
            increaseQuantityButton = itemView.findViewById(R.id.quantityIncrement);
            decreaseQuantityButton = itemView.findViewById(R.id.quantityDecrement);
            removeButton = itemView.findViewById(R.id.removeButton);
            productImage = itemView.findViewById(R.id.productImage); // Ensure this matches your layout
        }
    }
}
