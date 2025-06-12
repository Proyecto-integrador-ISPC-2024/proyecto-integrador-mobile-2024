package com.example.tiendadecampeones.ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.DividerItemDecoration;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.OrderRecyclerAdapter;
import com.example.tiendadecampeones.models.Order;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.ProgressBar;

public class AdminOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderRecyclerAdapter adapter;
    private List<Order> ordersList;
    private ImageButton backButton;
    private TextView titleTextView;
    private ApiService apiService;
    private int userId;
    private String userName;
    private String selectedStatus = "Todos";
    private List<Order> originalOrdersList = new ArrayList<>();
    private TextView filterLabel;
    private SearchView searchView;
    private ProgressBar loadingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        userId = getIntent().getIntExtra("USER_ID", -1);
        userName = getIntent().getStringExtra("USER_NAME");

        if (userId == -1) {
            Toast.makeText(this, "Error: No se pudo cargar la información del usuario", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        titleTextView = findViewById(R.id.pageTitle);
        titleTextView.setText("Pedidos de " + userName);

        apiService = RetrofitClient.getClient(this).create(ApiService.class);

        initializeViews();
        setupSearchView();
        setupRecyclerView();

        loadUserOrders();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.ordersRecyclerView);
        backButton = findViewById(R.id.backButton);
        searchView = findViewById(R.id.searchView);
        filterLabel = findViewById(R.id.filterLabel);
        loadingSpinner = findViewById(R.id.loadingSpinner);
        ImageButton filterButton = findViewById(R.id.filterButton);

        backButton.setOnClickListener(v -> finish());
        filterButton.setOnClickListener(v -> showFilterDialog());
        updateFilterLabel();
    }

    private void loadUserOrders() {
        loadingSpinner.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        
        apiService.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                loadingSpinner.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                
                if (response.isSuccessful() && response.body() != null) {
                    ordersList.clear();
                    originalOrdersList.clear();

                    for (Order order : response.body()) {
                        if (order.getIdUsuario() == userId) {
                            ordersList.add(order);
                            originalOrdersList.add(order);
                        }
                    }

                    sortOrdersByStatus(ordersList);
                    adapter.notifyDataSetChanged();

                    if (ordersList.isEmpty()) {
                        Toast.makeText(AdminOrdersActivity.this,
                                "El usuario no tiene pedidos", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AdminOrdersActivity.this,
                                "Pedidos de " + userName, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminOrdersActivity.this,
                            "Error al cargar los pedidos: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                loadingSpinner.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(AdminOrdersActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchView() {
        searchView.setQueryHint("Buscar por número de pedido");
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setClickable(true);
        searchView.setFocusable(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterOrders();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterOrders();
                return true;
            }
        });
    }

    private void filterOrders() {
        String query = searchView.getQuery().toString();
        ordersList.clear();

        for (Order order : originalOrdersList) {
            String orderId = String.valueOf(order.getIdPedido());
            boolean matchesSearch = query.isEmpty() || orderId.contains(query);
            boolean matchesStatus = selectedStatus.equals("Todos") ||
                    (selectedStatus.equalsIgnoreCase("Enviado") && "ENVIADO".equalsIgnoreCase(order.getEstado())) ||
                    (selectedStatus.equalsIgnoreCase("Aceptado") && "ACEPTADO".equalsIgnoreCase(order.getEstado())) ||
                    (selectedStatus.equalsIgnoreCase("Cancelado") && "CANCELADO".equalsIgnoreCase(order.getEstado()));

            if (matchesSearch && matchesStatus) {
                ordersList.add(order);
            }
        }

        sortOrdersByStatus(ordersList);
        adapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        ordersList = new ArrayList<>();
        adapter = new OrderRecyclerAdapter(ordersList, this, true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void showFilterDialog() {
        final String[] estados = {"Todos", "Enviado", "Aceptado", "Cancelado"};
        int checkedStatus = 0;
        for (int i = 0; i < estados.length; i++) {
            if (estados[i].equals(selectedStatus)) checkedStatus = i;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrar por estado");
        builder.setSingleChoiceItems(estados, checkedStatus, (dialog, which) -> {
            selectedStatus = estados[which];
        });
        builder.setPositiveButton("Aplicar", (dialog, which) -> {
            filterOrders();
            updateFilterLabel();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void sortOrdersByStatus(List<Order> list) {
        Collections.sort(list, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                int p1 = getStatusPriority(o1.getEstado());
                int p2 = getStatusPriority(o2.getEstado());
                return Integer.compare(p1, p2);
            }
            private int getStatusPriority(String estado) {
                if ("ENVIADO".equalsIgnoreCase(estado)) return 0;
                if ("ACEPTADO".equalsIgnoreCase(estado)) return 1;
                if ("CANCELADO".equalsIgnoreCase(estado)) return 2;
                return 3;
            }
        });
    }

    private void updateFilterLabel() {
        if (filterLabel != null) {
            filterLabel.setText("Filtrando por: " + selectedStatus);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiService.getOrders().enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ordersList.clear();
                    originalOrdersList.clear();

                    for (Order order : response.body()) {
                        if (order.getIdUsuario() == userId) {
                            ordersList.add(order);
                            originalOrdersList.add(order);
                        }
                    }

                    sortOrdersByStatus(ordersList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
            }
        });
    }

    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void homeButton(View v) {
        Toast.makeText(this, "¡ Home !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    public void productsButton(View v) {
        Toast.makeText(this, "¡ Nuestros Productos !", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ProductCategories.class);
        startActivity(intent);
    }
}


