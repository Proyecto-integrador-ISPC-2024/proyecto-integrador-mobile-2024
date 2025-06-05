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
import com.example.tiendadecampeones.models.CartItem;
import com.example.tiendadecampeones.ui.Cart;
import com.example.tiendadecampeones.viewmodel.CartViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final ArrayList<CartItem> cartItemList;
    private final Context context;
    private final CartViewModel cartVM;

    public CartAdapter(ArrayList<CartItem> cartItemList,
                       Context context,
                       CartViewModel cartVM) {
        this.cartItemList = cartItemList;
        this.context      = context;
        this.cartVM       = cartVM;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder h, int pos) {
        CartItem ci = cartItemList.get(pos);

        h.productName.setText(ci.getProducto().getNombreProducto());
        h.productPrice.setText("Precio: $" + ci.getProducto().getPrecio());
        h.productSize.setText(ci.getTalle().getTalle());

        double subtotal = ci.getProducto().getPrecio() * ci.getCantidadCompra();
        h.productSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));
        h.productQuantity.setText(String.valueOf(ci.getCantidadCompra()));

        Glide.with(context)
                .load(ci.getProducto().getImagen())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(h.productImage);

        h.increaseQuantityButton.setOnClickListener(v -> updateQuantity(h, ci, true));
        h.decreaseQuantityButton.setOnClickListener(v -> updateQuantity(h, ci, false));
        h.removeButton.setOnClickListener(v -> removeProduct(pos));
    }

    @Override public int getItemCount() { return cartItemList.size(); }

    /* ----- helpers ----- */

    private void updateQuantity(CartViewHolder h, CartItem ci, boolean inc) {
        int q = ci.getCantidadCompra();
        int stock = ci.getTalle().getStock();

        if (inc) {
            if (q < stock) ci.incrementQuantity();
            else { Toast.makeText(context,"Stock máximo",Toast.LENGTH_SHORT).show(); return; }
        } else {
            if (q > 1) ci.setCantidadCompra(q - 1);
            else { Toast.makeText(context,"Cantidad mínima",Toast.LENGTH_SHORT).show(); return; }
        }

        double subtotal = ci.getProducto().getPrecio() * ci.getCantidadCompra();
        h.productSubtotal.setText("Subtotal: $" + String.format("%.2f", subtotal));
        h.productQuantity.setText(String.valueOf(ci.getCantidadCompra()));

        ((Cart) context).calculateTotal();
        persist();
        syncCount();
    }

    private void removeProduct(int pos) {
        cartItemList.remove(pos);
        notifyItemRemoved(pos);

        ((Cart) context).calculateTotal();
        ((Cart) context).updateCartUI();
        Toast.makeText(context, "¡Producto eliminado!", Toast.LENGTH_SHORT).show();

        persist();
        syncCount();
    }

    private void persist() {
        SharedPreferences sp = context.getSharedPreferences("cart_shared_prefs", Context.MODE_PRIVATE);
        sp.edit().putString("cart_items", new Gson().toJson(cartItemList)).apply();
    }

    private void syncCount() {
        int qty = 0;
        for (CartItem ci : cartItemList) qty += ci.getCantidadCompra();
        cartVM.setCount(qty);
    }

    /* ----- ViewHolder ----- */
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productSize,
                productSubtotal, productQuantity;
        Button   increaseQuantityButton, decreaseQuantityButton, removeButton;
        ImageView productImage;

        CartViewHolder(@NonNull View v) {
            super(v);
            productName  = v.findViewById(R.id.productName);
            productPrice = v.findViewById(R.id.productPrice);
            productSize  = v.findViewById(R.id.productTalle);
            productQuantity = v.findViewById(R.id.productQuantity);
            productSubtotal = v.findViewById(R.id.productSubtotal);
            increaseQuantityButton = v.findViewById(R.id.quantityIncrement);
            decreaseQuantityButton = v.findViewById(R.id.quantityDecrement);
            removeButton           = v.findViewById(R.id.removeButton);
            productImage           = v.findViewById(R.id.productImage);
        }
    }
}
