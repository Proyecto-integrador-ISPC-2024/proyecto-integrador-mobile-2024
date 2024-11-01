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

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        Product.Producto productoDetails = product.getProductos();

        holder.productName.setText(productoDetails.getNombreProducto());
        holder.productPrice.setText(String.format("$%.2f", productoDetails.getPrecio()));

        // Mostrar la animación de carga y ocultar la animación de error antes de cargar la imagen
        holder.lottiePlaceholder.setVisibility(View.VISIBLE);
        holder.lottieError.setVisibility(View.GONE);
        holder.productImage.setVisibility(View.INVISIBLE); // Oculta la imagen hasta que esté cargada

        Glide.with(context)
                .load(productoDetails.getImagen())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // Oculta la animación de carga y muestra la animación de error en caso de fallo
                        holder.lottiePlaceholder.setVisibility(View.GONE);
                        holder.lottieError.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // Oculta las animaciones y muestra la imagen una vez que se carga
                        holder.lottiePlaceholder.setVisibility(View.GONE);
                        holder.lottieError.setVisibility(View.GONE);
                        holder.productImage.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.productImage);

        List<Talle> tallesList = product.getTalles();
        tallesStringList.clear();
        for (Talle talle : tallesList) {
            tallesStringList.add(talle.getTalle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, tallesStringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.sizeSpinner.setAdapter(adapter);

        final int[] selectedTallePosition = {0};

        holder.sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedTallePosition[0] = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        holder.btnAddToCart.setOnClickListener(v -> {
            Talle talleSeleccionado = tallesList.get(selectedTallePosition[0]);
            talleSeleccionado.setCantidadCompra(1);

            Toast.makeText(context, productoDetails.getNombreProducto() + " (Talle: " + talleSeleccionado.getTalle() + ") fue añadido al carrito.", Toast.LENGTH_SHORT).show();
            addToCart(product, selectedTallePosition[0]);
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
        LottieAnimationView lottiePlaceholder, lottieError; // Añadir las referencias a las animaciones Lottie

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnAddToCart = itemView.findViewById(R.id.addToCartButton);
            sizeSpinner = itemView.findViewById(R.id.sizeSpinner);
            lottiePlaceholder = itemView.findViewById(R.id.lottie_placeholder); // Asociar lottiePlaceholder
            lottieError = itemView.findViewById(R.id.lottie_error); // Asociar lottieError
        }
    }

    private void addToCart(Product product, int selectedTallePosition) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
        Talle talleSeleccionado = product.getTalles().get(selectedTallePosition);
        Log.d("addToCart", "ID del talle seleccionado: " + talleSeleccionado.getIdTalle());
        product.setIdProductoTalle(talleSeleccionado.getIdTalle());

        List<Product> currentCart = sharedPrefManager.getCartProducts();
        boolean productExists = false;
        for (Product cartProduct : currentCart) {
            if (cartProduct.getProductos().getIdProducto() == product.getProductos().getIdProducto()) {
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

        Log.d("ProductsAdapter", "Productos en el carrito:");
        for (Product p : currentCart) {
            Log.d("ProductsAdapter", p.getProductos().getNombreProducto() + " - Talle seleccionado: " + p.getIdProductoTalle());
        }
    }
}
