package com.example.tiendadecampeones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.util.Log;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> implements Filterable {
    private List<Order> orders;
    private List<Order> filteredOrders;
    private Filter orderFilter;
    private String currentStatusFilter = "TODOS LOS ESTADOS";

    public OrderAdapter(Context context, List<Order> orders) {
        super(context, 0, orders);
        this.orders = orders;
        this.filteredOrders = new ArrayList<>(orders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Order order = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_order, parent, false);
        }

        TextView orderIdTextView = convertView.findViewById(R.id.order_id);
        TextView orderDateTextView = convertView.findViewById(R.id.order_date);

        orderIdTextView.setText("Pedido " + order.getIdPedido());
        orderDateTextView.setText(order.getFecha());

        Log.d("OrderAdapter", "Mostrando pedido con ID: " + order.getIdPedido() );
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (orderFilter == null) {
            orderFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    List<Order> filteredResults = new ArrayList<>();
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    boolean isNumber = filterPattern.matches("\\d+");

                    for (Order order : orders) {
                        boolean matchesStatus = currentStatusFilter.equals("TODOS LOS ESTADOS") ||
                                order.getEstado().equalsIgnoreCase(currentStatusFilter);

                        boolean matchesQuery = isNumber
                                ? String.valueOf(order.getIdPedido()).contains(filterPattern)
                                : order.getFecha().toLowerCase().contains(filterPattern) ||
                                order.getEstado().toLowerCase().contains(filterPattern);

                        if (matchesStatus && matchesQuery) {
                            filteredResults.add(order);
                        }
                    }

                    FilterResults results = new FilterResults();
                    results.values = filteredResults;
                    results.count = filteredResults.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    filteredOrders.clear();
                    filteredOrders.addAll((List<Order>) results.values);
                    notifyDataSetChanged();
                }
            };
        }
        return orderFilter;
    }

    @Override
    public int getCount() {
        return filteredOrders.size();
    }

    @Override
    public Order getItem(int position) {
        return filteredOrders.get(position);
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        this.filteredOrders.clear();
        this.filteredOrders.addAll(newOrders);
        notifyDataSetChanged();
    }
}
