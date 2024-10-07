package com.example.tiendadecampeones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Product> productList;
    private Context context;

    public CartAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.format("Precio: $%.2f", product.getPrice()));
        holder.productQuantity.setText(String.valueOf(product.getQuantity()));
        holder.productImage.setImageResource(product.getImageResId());

        // Incrementar cantidad
        holder.incrementButton.setOnClickListener(v -> {
            if (product.getQuantity() < product.getStock()) {
                product.setQuantity(product.getQuantity() + 1);
                holder.productQuantity.setText(String.valueOf(product.getQuantity()));
                notifyItemChanged(position);
            }
        });

        // Decrementar cantidad
        holder.decrementButton.setOnClickListener(v -> {
            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
                holder.productQuantity.setText(String.valueOf(product.getQuantity()));
                notifyItemChanged(position);
            }
        });

        // Botón para eliminar ítems del carrito
        holder.removeButton.setOnClickListener(v -> {
            productList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, productList.size());
        });

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView productName, productPrice, productQuantity;
        ImageView productImage;
        Button incrementButton, decrementButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productImage = itemView.findViewById(R.id.productImage);
            incrementButton = itemView.findViewById(R.id.quantityIncrement);
            decrementButton = itemView.findViewById(R.id.quantityDecrement);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}
