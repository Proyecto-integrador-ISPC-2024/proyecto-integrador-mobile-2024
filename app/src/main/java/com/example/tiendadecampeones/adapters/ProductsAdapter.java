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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.models.Product.Talle;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Glide.with(context)
                .load(productoDetails.getImagen())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
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

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnAddToCart = itemView.findViewById(R.id.addToCartButton);
            sizeSpinner = itemView.findViewById(R.id.sizeSpinner);
        }
    }

    private void addToCart(Product product, int selectedTallePosition) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);

        Talle talleSeleccionado = product.getTalles().get(selectedTallePosition);

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

        Toast.makeText(context, product.getProductos().getNombreProducto() + " fue añadido al carrito.", Toast.LENGTH_SHORT).show();
    }


}

