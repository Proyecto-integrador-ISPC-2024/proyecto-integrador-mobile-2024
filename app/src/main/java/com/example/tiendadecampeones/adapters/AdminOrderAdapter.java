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

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ProductViewHolder> {

    private Context context;
    private List<Order.OrderDetail> orderDetails;
    private List<Order> orders;

    public AdminOrderAdapter(Context context, List<Order.OrderDetail> orderDetails, List<Order> orders) {
        this.context = context;
        this.orderDetails = orderDetails;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_orders, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Order.OrderDetail orderDetail = orderDetails.get(position);
        Order.OrderDetail.Product product = orderDetail.getProducto();

        int orderIndex = getOrderIndex(orderDetail);
        if (orderIndex != -1) {
            int idUsuario = orders.get(orderIndex).getIdUsuario();
            holder.userId.setText("Cliente: " + idUsuario);
        }

        holder.productName.setText(product.getNombreProducto());
        holder.productSize.setText("Talle: " + orderDetail.getTalleString());
        holder.productQuantity.setText("Cantidad: " + orderDetail.getCantidad());
        holder.productSubtotal.setText("Subtotal: $" + orderDetail.getSubtotal());
        holder.productPrice.setText("Precio unitario: $" + product.getPrecio());

        Glide.with(context)
                .load(product.getImagen())
                .into(holder.productImage);
    }

    private int getOrderIndex(Order.OrderDetail orderDetail) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).getDetalles().contains(orderDetail)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView userId;
        ImageView productImage;
        TextView productName;
        TextView productSize;
        TextView productQuantity;
        TextView productSubtotal;
        TextView productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            userId = itemView.findViewById(R.id.userId);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productSize = itemView.findViewById(R.id.productSize);
            productQuantity = itemView.findViewById(R.id.productQuantity);
            productSubtotal = itemView.findViewById(R.id.productSubtotal);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }
}
