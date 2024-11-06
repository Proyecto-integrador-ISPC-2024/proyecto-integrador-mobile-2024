package com.example.tiendadecampeones.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.example.tiendadecampeones.models.CartItem;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.ui.Cart;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<CartItem> cartItemList;
    private Context context;

    // Constructor
    public CartAdapter(ArrayList<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
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
        CartItem cartItem = cartItemList.get(position);
        Product.Producto productoDetails = cartItem.getProducto();
        holder.productName.setText(productoDetails.getNombreProducto());
        holder.productPrice.setText("Precio: $" + productoDetails.getPrecio());

        // Cargar la imagen
        String imageUrl = productoDetails.getImagen();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.productImage);

        // Obtener detalles del talle seleccionado
        String talleInfo = cartItem.getTalle().getTalle();
        double subtotal = productoDetails.getPrecio() * cartItem.getCantidadCompra();

        // Setear los datos en la UI
        holder.productSize.setText(talleInfo);
        holder.productSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));

        // Manejar la manipulación de cantidades
        holder.increaseQuantityButton.setOnClickListener(v -> {
            updateQuantity(holder, cartItem, true);
        });
        holder.decreaseQuantityButton.setOnClickListener(v -> {
            updateQuantity(holder, cartItem, false);
        });
        holder.removeButton.setOnClickListener(v -> {
            removeProduct(position);
        });

        holder.productQuantity.setText(Integer.toString(cartItem.getCantidadCompra()));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    private void updateQuantity(CartViewHolder holder, CartItem cartItem, boolean isIncrease) {
        Product.Talle talle = cartItem.getTalle();
        int currentQuantity = cartItem.getCantidadCompra();
        int stock = talle.getStock();
        Log.d("CartItem", "Stock disponible: " + stock);

        // Verifica si la cantidad debe aumentar o disminuir
        if (isIncrease) {
            if (currentQuantity < stock) {
                // Aumenta la cantidad si no se supera el stock disponible
                cartItem.incrementQuantity();
                Log.d("CartItem", "Cantidad aumentada a: " + cartItem.getCantidadCompra());
            } else {
                Toast.makeText(context, "Stock máximo alcanzado", Toast.LENGTH_SHORT).show();
                Log.d("CartItem", "Stock máximo alcanzado: " + stock);
            }
        } else {
            if (currentQuantity > 1) {
                // Decrementa la cantidad solo si es mayor a 1 (mínimo permitido)
                cartItem.setCantidadCompra(currentQuantity - 1);
                Log.d("CartItem", "Cantidad disminuida a: " + cartItem.getCantidadCompra());
            } else {
                Toast.makeText(context, "Cantidad mínima alcanzada", Toast.LENGTH_SHORT).show();
                Log.d("CartItem", "Cantidad mínima alcanzada, no se puede reducir más.");
            }
        }

        // Cálculo del subtotal
        double price = cartItem.getProducto().getPrecio();
        int quantity = cartItem.getCantidadCompra();
        double newSubtotal = price * quantity;

        // Actualizar la interfaz con el nuevo subtotal y cantidad
        holder.productSubtotal.setText("Subtotal: $" + String.format("%.2f", newSubtotal));
        holder.productQuantity.setText(Integer.toString(quantity));

        // Recalcular el total en el carrito
        ((Cart) context).calculateTotal();

        // Guardar los cambios en SharedPreferences
        updateSharedPreferences();
    }

    private void removeProduct(int position) {
        cartItemList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        ((Cart) context).calculateTotal();
        Toast.makeText(context, "¡Producto eliminado!", Toast.LENGTH_SHORT).show();
        updateSharedPreferences();
    }

    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart_shared_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String updatedProductsJson = gson.toJson(cartItemList);
        editor.putString("cart_items", updatedProductsJson);
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
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}