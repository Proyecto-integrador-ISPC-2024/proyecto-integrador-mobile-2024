package com.example.tiendadecampeones.adapters;

import android.content.Context;
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
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Product;
import java.util.List;
import com.bumptech.glide.Glide;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final List<Product> products;
    private final Context context; // Context to use for Toast messages

    public ProductsAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);


        holder.productName.setText(product.getName());
        holder.productPrice.setText("$" + product.getPrice());

        Log.d("Product", "Nombre: " + product.getName());
        Log.d("Product", "Imagen URL: " + product.getImageUrl());


        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.productImage);

        holder.btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(context, product.getName() + " fue agregado al carrito.", Toast.LENGTH_SHORT).show();
        });
    }




    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productPrice;
        ImageView productImage;
        Button btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnAddToCart = itemView.findViewById(R.id.addToCartButton); // Initialize the button
        }
    }
}
