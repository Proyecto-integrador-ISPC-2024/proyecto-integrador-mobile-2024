package com.example.tiendadecampeones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import com.bumptech.glide.Glide;

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

        // Get the current product
        Product product = productList.get(position);
        Product.Producto producto = product.getProductos();  // Access the Producto details

        // Set product name and price
        holder.productName.setText(producto.getNombreProducto());
        holder.productPrice.setText(String.format("Precio: $%.2f", producto.getPrecio()));

        // Set product quantity based on the size of the 'talles' list
        int quantity = product.getTalles().size();
        holder.productQuantity.setText(String.valueOf(quantity));

        // Load product image using Glide
        Glide.with(context)
                .load(producto.getImagen())
                .placeholder(R.drawable.placeholder_image)  // Placeholder while loading
                .error(R.drawable.error_image)  // Error image in case loading fails
                .into(holder.productImage);

        // Increment button click listener
        holder.incrementButton.setOnClickListener(v -> {
            if (quantity < product.getStock()) {  // Check if the quantity is less than the available stock
                // Add one more to the quantity
                product.getTalles().add(new Product.Talle(0, "Nuevo Talle", 1));  // Example of adding a Talle
                holder.productQuantity.setText(String.valueOf(product.getTalles().size()));  // Update quantity display
                notifyItemChanged(position);  // Notify adapter of data change
            }
        });

        // Decrement button click listener
        holder.decrementButton.setOnClickListener(v -> {
            if (quantity > 1) {  // Ensure quantity does not go below 1
                // Remove one talle from the list
                product.getTalles().remove(product.getTalles().size() - 1);
                holder.productQuantity.setText(String.valueOf(product.getTalles().size()));  // Update quantity display
                notifyItemChanged(position);  // Notify adapter of data change
            }
        });

        // Remove product from the cart
        holder.removeButton.setOnClickListener(v -> {
            productList.remove(position);  // Remove the product from the list
            notifyItemRemoved(position);  // Notify adapter of item removal
            notifyItemRangeChanged(position, productList.size());  // Notify adapter of range change
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
            // Initialize UI components
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
