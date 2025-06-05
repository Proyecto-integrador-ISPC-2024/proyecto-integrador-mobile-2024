package com.example.tiendadecampeones.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.CartItem;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.models.Product.Talle;
import com.example.tiendadecampeones.utils.SharedPrefManager;
import com.example.tiendadecampeones.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final List<Product> products;
    private final Context context;

    public ProductsAdapter(List<Product> products, CartViewModel cartVM, Context context) {
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
        Product.Producto productoDetails = product.getProductos();

        holder.productName.setText(productoDetails.getNombreProducto());
        holder.productPrice.setText(String.format("$%.2f", productoDetails.getPrecio()));

        holder.lottiePlaceholder.setVisibility(View.VISIBLE);
        holder.lottieError.setVisibility(View.GONE);
        holder.productImage.setVisibility(View.INVISIBLE);

        // Cargar la imagen con Glide
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

        List<Talle> tallesList = product.getTalles();
        List<String> tallesStringList = new ArrayList<>();

        for (Talle talle : tallesList) {
            if (talle.getTalle() != null && !talle.getTalle().isEmpty()) {
                tallesStringList.add(talle.getTalle());
            }
        }

        if (tallesStringList.isEmpty()) {
            holder.sizeSpinner.setVisibility(View.GONE);
            holder.btnAddToCart.setEnabled(false);
        } else {
            holder.sizeSpinner.setVisibility(View.VISIBLE);
            holder.btnAddToCart.setEnabled(true);
        }

        // Crear un ArrayAdapter para el Spinner con los talles disponibles
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, tallesStringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.sizeSpinner.setAdapter(adapter);

        final int[] selectedTallePosition = {0};

        holder.sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position < tallesStringList.size()) {
                    selectedTallePosition[0] = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        holder.btnAddToCart.setOnClickListener(v -> {
            Talle talleSeleccionado = tallesList.get(selectedTallePosition[0]);

            // Verificar si el talle seleccionado está disponible
            if (!tallesStringList.contains(talleSeleccionado.getTalle())) {
                Toast.makeText(context, "Talle no disponible", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, productoDetails.getNombreProducto() + " (Talle: " + talleSeleccionado.getTalle() + ") fue añadido al carrito.", Toast.LENGTH_SHORT).show();
                addToCart(product, selectedTallePosition[0]);
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
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
        List<Talle> tallesList = product.getTalles();
        if (selectedTallePosition < tallesList.size()) {
            Talle talleSeleccionado = tallesList.get(selectedTallePosition);
            CartItem cartItem = new CartItem(talleSeleccionado.getIdTalle(), product.getProductos(), talleSeleccionado);

            List<CartItem> currentCart = sharedPrefManager.getCartItems();
            boolean itemExists = false;
            for (CartItem existingItem : currentCart) {
                if (existingItem.equals(cartItem)) {
                    existingItem.incrementQuantity();
                    itemExists = true;
                    break;
                }
            }
            if (!itemExists) {
                currentCart.add(cartItem);
            }
            sharedPrefManager.saveCartItems(currentCart);

            Toast.makeText(context, product.getProductos().getNombreProducto() +
                            " (Talle: " + talleSeleccionado.getTalle() + ") fue añadido al carrito.",
                    Toast.LENGTH_SHORT).show();
            Log.d("ProductsAdapter", "Productos en el carrito:");
            for (CartItem item : currentCart) {
                Log.d("ProductsAdapter", item.getProducto().getNombreProducto() +
                        " - Talle seleccionado: " + item.getTalle().getTalle() +
                        " - Cantidad: " + item.getCantidadCompra());
            }
        } else {
            Log.e("ProductsAdapter", "Índice seleccionado fuera de rango");
        }
    }
}

