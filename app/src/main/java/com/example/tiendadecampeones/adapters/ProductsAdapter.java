package com.example.tiendadecampeones.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Product;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.bumptech.glide.Glide;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final List<Product> products;
    private final Context context; // Context to use for Toast messages
    //private List<String> tallesStringList = Collections.emptyList();

    public ProductsAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
        //this.tallesStringList = tallesStringList; // Inicializa tallesStringList
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
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText("$" + product.getPrice());

        Log.d("Product", "Nombre: " + product.getName());
        Log.d("Product", "Imagen URL: " + product.getImageUrl());


        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.productImage);


        // Prepare the sizes for the spinner
        List<String> sizes = new ArrayList<>();
        for (Product.SizeDetails sizeDetail : product.getTalles()) {
            if (sizeDetail.getStock() > 0) { // Solo talles disponibles
                sizes.add(sizeDetail.getTalle());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, sizes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.sizeSpinner.setAdapter(adapter);


        // Handle "Add to Cart" button click
        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a toast message
                Toast.makeText(context, product.getName() + " was added to the cart.", Toast.LENGTH_SHORT).show();

                // Here you can also add logic to actually add the product to the cart.
                // For now, it's just a toast.
            }
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
        Spinner sizeSpinner;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnAddToCart = itemView.findViewById(R.id.addToCartButton); // Initialize the button
            sizeSpinner = itemView.findViewById(R.id.sizeSpinner);
        }
    }
}
