package com.example.tiendadecampeones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Order;

import java.util.List;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Order.OrderDetail> orderDetails;

    public OrderProductAdapter(Context context, List<Order.OrderDetail> orderDetails) {
        this.context = context;
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product_order, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Order.OrderDetail orderDetail = orderDetails.get(position);
        Order.OrderDetail.Product product = orderDetail.getProducto();

        holder.productName.setText(product.getNombreProducto());

        holder.productSize.setText("Talle: " + orderDetail.getTalleString());

        holder.productQuantity.setText("Cantidad: " + orderDetail.getCantidad());

        holder.productSubtotal.setText("Subtotal: $" + orderDetail.getSubtotal());

        holder.productPrice.setText("Precio unitario: $" + product.getPrecio());

        Glide.with(context)
                .load(product.getImagen())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productSize;
        TextView productQuantity;
        TextView productSubtotal;
        TextView productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productSize = itemView.findViewById(R.id.productSize);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productSubtotal = itemView.findViewById(R.id.productSubtotal);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}
