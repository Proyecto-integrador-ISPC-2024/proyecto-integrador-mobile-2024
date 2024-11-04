package com.example.tiendadecampeones.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.drawable.Drawable;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.models.Product.Talle;
import com.example.tiendadecampeones.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final List<Product> products;
    private final Context context;
    private List<String> tallesStringList = new ArrayList<>();

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

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        Product.Producto productoDetails = product.getProductos();

        holder.productName.setText(productoDetails.getNombreProducto());
        holder.productPrice.setText(String.format("$%.2f", productoDetails.getPrecio()));

        // Show loading animation and hide error animation
        holder.lottiePlaceholder.setVisibility(View.VISIBLE);
        holder.lottieError.setVisibility(View.GONE);
        holder.productImage.setVisibility(View.INVISIBLE); // Hide image until loaded

        Glide.with(context)
                .load(productoDetails.getImagen())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.lottiePlaceholder.setVisibility(View.GONE);
                        holder.lottieError.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.lottiePlaceholder.setVisibility(View.GONE);
                        holder.lottieError.setVisibility(View.GONE);
                        holder.productImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.productImage);

        // Populate the Spinner with the available sizes for the product
        List<String> tallesStringList = new ArrayList<>();
        for (Talle talle : product.getTalles()) {
            tallesStringList.add(talle.getTalle());
        }

        // Set up the ArrayAdapter for the Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, tallesStringList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.sizeSpinner.setAdapter(spinnerAdapter);

        // Set the default selection position to 0
        final int[] selectedTallePosition = {0};

        holder.sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id) {
                selectedTallePosition[0] = pos; // Update selected position
                Log.d("ProductsAdapter", "Selected size position: " + pos + ", Size: " + tallesStringList.get(pos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                Log.d("ProductsAdapter", "Nothing selected in Spinner");
            }
        });

        holder.btnAddToCart.setOnClickListener(v -> {
            if (product.getTalles() != null && !product.getTalles().isEmpty() &&
                    selectedTallePosition[0] >= 0 && selectedTallePosition[0] < product.getTalles().size()) {

                Talle talleSeleccionado = product.getTalles().get(selectedTallePosition[0]);
                talleSeleccionado.setCantidadCompra(1);

                // Set only the selected talle in the product's talle list
                List<Talle> selectedTalleList = new ArrayList<>();
                selectedTalleList.add(talleSeleccionado);
                product.setTalles(selectedTalleList);

                Toast.makeText(context, productoDetails.getNombreProducto() + " (Talle: " + talleSeleccionado.getTalle() + ") fue añadido al carrito.", Toast.LENGTH_SHORT).show();

                addToCart(product, selectedTallePosition[0]);
            } else {
                Log.e("ProductsAdapter", "Error: Índice de talle fuera de los límites o lista de talles vacía.");
                Toast.makeText(context, "Error: No hay talles disponibles o índice inválido.", Toast.LENGTH_SHORT).show();
            }
        });


}

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice;
        ImageView productImage;
        Button btnAddToCart;
        Spinner sizeSpinner;
        LottieAnimationView lottiePlaceholder, lottieError;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnAddToCart = itemView.findViewById(R.id.addToCartButton);
            sizeSpinner = itemView.findViewById(R.id.sizeSpinner);
            lottiePlaceholder = itemView.findViewById(R.id.lottie_placeholder);
            lottieError = itemView.findViewById(R.id.lottie_error);
        }
    }

    private void addToCart(Product product, int selectedTallePosition) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        List<Talle> tallesList = product.getTalles();

        if (selectedTallePosition >= 0 && selectedTallePosition < tallesList.size()) {
            Talle talleSeleccionado = tallesList.get(selectedTallePosition);
            product.setIdProductoTalle(talleSeleccionado.getIdTalle());
            product.setTalleSeleccionado(talleSeleccionado);

            List<Product> currentCart = sharedPrefManager.getCartProducts();
            boolean productExists = false;

            for (Product cartProduct : currentCart) {
                if (cartProduct.getProductos().getIdProducto() == product.getProductos().getIdProducto() &&
                        cartProduct.getIdProductoTalle() == product.getIdProductoTalle()) {
                    currentCart.remove(cartProduct);
                    currentCart.add(product);
                    productExists = true;
                    break;
                }
            }

            if (!productExists) {
                currentCart.add(product);
            }

            sharedPrefManager.saveCartProducts(currentCart);
            Log.d("ProductsAdapter", "Producto añadido al carrito con éxito.");
        } else {
            Log.e("ProductsAdapter", "Error: Índice de talle fuera de los límites.");
        }
    }
}
