package com.example.tiendadecampeones.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Product;
import com.example.tiendadecampeones.ui.Cart;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<Product> productList;
    private Context context;

    // Constructor
    public CartAdapter(ArrayList<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    // agregado de información para el recycler view
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = productList.get(position);
        Product.Producto productoDetails = product.getProductos();
        holder.productName.setText(productoDetails.getNombreProducto());
        holder.productPrice.setText("Precio: $" + productoDetails.getPrecio());
        // Cargar la imagen
        String imageUrl = productoDetails.getImagen();
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.error_image)
                .into(holder.productImage);

        // Buscar el talle seleccionado (cantidadCompra > 0)
        Product.Talle talleSeleccionado = null;
        for (Product.Talle talle : product.getTalles()) {
            if (talle.getCantidadCompra() > 0) {
                talleSeleccionado = talle;
                break;
            }
        }

        // Crear una copia final de talleSeleccionado
        final Product.Talle finalTalleSeleccionado = talleSeleccionado;

        // Mostrar solo el talle seleccionado
        if (finalTalleSeleccionado != null) {
            holder.productSize.setText(finalTalleSeleccionado.getTalle());
            holder.productQuantity.setText(Integer.toString(finalTalleSeleccionado.getCantidadCompra()));
        } else {
            holder.productSize.setText("Talle no seleccionado");
            holder.productQuantity.setText("0");
        }

        // Calcular el subtotal
        double subtotal = productoDetails.getPrecio() * (finalTalleSeleccionado != null ? finalTalleSeleccionado.getCantidadCompra() : 0);
        holder.productSubtotal.setText("Subtotal: $" + subtotal);

        // Configurar botones para manipular la cantidad usando la copia final
        holder.increaseQuantityButton.setOnClickListener(v -> {
            updateQuantity(holder, product, finalTalleSeleccionado, true); // Aumentar
        });
        holder.decreaseQuantityButton.setOnClickListener(v -> {
            updateQuantity(holder, product, finalTalleSeleccionado, false); // Disminuir
        });
        holder.removeButton.setOnClickListener(v -> {
            removeProduct(position); // Eliminar
        });
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Actualización de cantidades de productos
    private void updateQuantity(CartViewHolder holder, Product product, Product.Talle talleSeleccionado, boolean isIncrease) {
        if (talleSeleccionado == null) return;

        int currentQuantity = talleSeleccionado.getCantidadCompra();

        if (isIncrease) {
            if (currentQuantity < talleSeleccionado.getStock()) {
                talleSeleccionado.setCantidadCompra(currentQuantity + 1);
            } else {
                Toast.makeText(context, "Stock máximo alcanzado", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (currentQuantity > 1) {
                talleSeleccionado.setCantidadCompra(currentQuantity - 1);
            } else {
                Toast.makeText(context, "Cantidad mínima alcanzada", Toast.LENGTH_SHORT).show();
            }
        }

        double newSubtotal = product.getProductos().getPrecio() * talleSeleccionado.getCantidadCompra();
        holder.productSubtotal.setText("Subtotal: $" + newSubtotal);
        holder.productQuantity.setText(Integer.toString(talleSeleccionado.getCantidadCompra()));

        ((Cart) context).calculateTotal();
        updateSharedPreferences();
    }


    // Remoción de productos del carro
    private void removeProduct(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        ((Cart) context).calculateTotal();

        // Actualización de las sharedpreferences guardadas para el carro
        updateSharedPreferences();
    }

    // Actualización de las shared preferences usadas para gestionar los ítems del carro
    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart_shared_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String updatedProductsJson = gson.toJson(productList);
        editor.putString("cart_products", updatedProductsJson);
        editor.apply();
    }

    // Captura de los elementos de la UI para poder renderizar los datos de los productos y para poder manipular la información
    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productSize, productSubtotal, productQuantity;
        Button increaseQuantityButton, decreaseQuantityButton, removeButton;
        ImageView productImage;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productSize = itemView.findViewById(R.id.productTalle);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productSubtotal = itemView.findViewById(R.id.productSubtotal);
            increaseQuantityButton = itemView.findViewById(R.id.quantityIncrement);
            decreaseQuantityButton = itemView.findViewById(R.id.quantityDecrement);
            removeButton = itemView.findViewById(R.id.removeButton);
            productImage = itemView.findViewById(R.id.productImage);
        }
    }
}
