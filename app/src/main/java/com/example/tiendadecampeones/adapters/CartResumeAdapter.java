package com.example.tiendadecampeones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.CartItem;
import com.example.tiendadecampeones.models.Product;

import java.util.List;
import java.util.Map;

public class CartResumeAdapter extends RecyclerView.Adapter<CartResumeAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItemList;
    private Map<CartItem, Integer> cartItems;

    // Constructor actualizado
    public CartResumeAdapter(Context context, List<CartItem> cartItemList, Map<CartItem, Integer> cartItems) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_resume_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        Product.Producto producto = cartItem.getProducto();

        holder.productName.setText(producto.getNombreProducto());
        holder.productPrice.setText(String.format("$%.2f", producto.getPrecio()));

        Integer quantity = cartItems.get(cartItem);
        if (quantity != null) {
            holder.productQuantity.setText(String.valueOf(quantity));
        }

        holder.productQuantity.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
        }
    }
}