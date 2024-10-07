package com.example.tiendadecampeones.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.DashboardOrder;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends ArrayAdapter<DashboardOrder> implements Filterable {
    private List<DashboardOrder> orders;
    private List<DashboardOrder> filteredOrders;
    private Filter orderFilter;
    private String currentStatusFilter = "TODOS LOS ESTADOS";

    public OrderAdapter(Context context, List<DashboardOrder> orders) {
        super(context, 0, orders);
        this.orders = orders;
        this.filteredOrders = new ArrayList<>(orders);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DashboardOrder order = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_order, parent, false);
        }

        TextView orderIdTextView = convertView.findViewById(R.id.order_id);
        TextView orderDateTextView = convertView.findViewById(R.id.order_date);

        orderIdTextView.setText("Pedido " + order.getIdPedido());
        orderDateTextView.setText(order.getFecha());

        return convertView;
    }

    @Override
    public int getCount() {
        return filteredOrders.size();
    }

    @Override
    public DashboardOrder getItem(int position) {
        return filteredOrders.get(position);
    }

    @Override
    public Filter getFilter() {
        if (orderFilter == null) {
            orderFilter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    List<DashboardOrder> filteredResults = new ArrayList<>();
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    boolean isNumber = filterPattern.matches("\\d+");

                    for (DashboardOrder order : orders) {
                        boolean matchesStatus = currentStatusFilter.equals("TODOS LOS ESTADOS") ||
                                order.getEstado().equalsIgnoreCase(currentStatusFilter);

                        boolean matchesQuery = isNumber
                                ? String.valueOf(order.getIdPedido()).contains(filterPattern)
                                : order.getFecha().toLowerCase().contains(filterPattern) || order.getEstado().toLowerCase().contains(filterPattern);

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
                    filteredOrders.addAll((List<DashboardOrder>) results.values);
                    notifyDataSetChanged();
                }
            };
        }
        return orderFilter;
    }

    public void filterByStatus(String status) {
        currentStatusFilter = status;
        updateOrders(orders);
        getFilter().filter("");
    }

    public void updateOrders(List<DashboardOrder> newOrders) {
        this.orders = newOrders;
        this.filteredOrders.clear();
        this.filteredOrders.addAll(newOrders);
        notifyDataSetChanged();
    }
}
