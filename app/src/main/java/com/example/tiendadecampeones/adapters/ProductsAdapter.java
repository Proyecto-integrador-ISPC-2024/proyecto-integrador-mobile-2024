/*package com.example.tiendadecampeones.adapters;

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
import com.example.tiendadecampeones.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.bumptech.glide.Glide;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private final List<Product> products;
    private final Context context; // Context to use for Toast messages
    private List<String> tallesStringList = Collections.emptyList();

    public ProductsAdapter(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
        this.tallesStringList = tallesStringList; // Inicializa tallesStringList
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
       // holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText("$" + product.getPrice());

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.productImage);

        // Configurar el Spinner con los talles
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, tallesStringList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.sizeSpinner.setAdapter(adapter);

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un mensaje de confirmación
                Toast.makeText(context, product.getName() + " fue añadido al carrito.", Toast.LENGTH_SHORT).show();

                // Aquí añadimos el producto al carrito
                addToCart(product);
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, //productDescription,
        productPrice;
        ImageView productImage;
        Button btnAddToCart;
        Spinner sizeSpinner;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            //productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            btnAddToCart = itemView.findViewById(R.id.addToCartButton); // Initialize the button
            sizeSpinner = itemView.findViewById(R.id.sizeSpinner);
        }
    }

    private void addToCart(Product product) {

        ApiService apiService = ApiService.create();


        apiService.addToCart(product).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("ProductsAdapter", "Producto añadido al carrito: " + product.getName());
                } else {
                    Log.e("ProductsAdapter", "Error al añadir al carrito: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ProductsAdapter", "Error al conectar con el servidor: " + t.getMessage());
            }
        });
    }
}
*/

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

        holder.btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(context, productoDetails.getNombreProducto() + " fue añadido al carrito.", Toast.LENGTH_SHORT).show();

            addToCart(product);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    // ViewHolder para los productos
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


    private void addToCart(Product product) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(context);

        List<Product> currentCart = sharedPrefManager.getCartProducts();

        currentCart.add(product);

        sharedPrefManager.saveCartProducts(currentCart);

        Log.d("ProductsAdapter", "Productos en el carrito:");
        for (Product p : currentCart) {
            Log.d("ProductsAdapter", p.getProductos().getNombreProducto() + " - Precio: " + p.getProductos().getPrecio());
        }

        Toast.makeText(context, product.getProductos().getNombreProducto() + " fue añadido al carrito.", Toast.LENGTH_SHORT).show();
    }
}
