package com.example.tiendadecampeones.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.ui.OrderActivity;
import com.example.tiendadecampeones.ui.OrderAdminActivity;
import java.util.List;

public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.OrderViewHolder> {
    private List<Order> ordersList;
    private Context context;
    private boolean isAdmin;

    public OrderRecyclerAdapter(List<Order> ordersList, Context context, boolean isAdmin) {
        this.ordersList = ordersList;
        this.context = context;
        this.isAdmin = isAdmin;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = ordersList.get(position);
        holder.orderId.setText("Pedido " + order.getIdPedido());
        holder.orderDate.setText(order.getFecha());
        holder.orderStatus.setText(order.getEstado());

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            if (isAdmin) {
                intent = new Intent(context, OrderAdminActivity.class);
            } else {
                intent = new Intent(context, OrderActivity.class);
            }
            intent.putExtra("ORDER_ID", order.getIdPedido());
            intent.putExtra("IS_ADMIN", isAdmin);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId;
        TextView orderDate;
        TextView orderStatus;

        OrderViewHolder(View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.order_id);
            orderDate = itemView.findViewById(R.id.order_date);
            orderStatus = itemView.findViewById(R.id.order_status);
        }
    }
}