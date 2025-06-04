package com.example.tiendadecampeones.ui;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.adapters.UserAdapter;
import com.example.tiendadecampeones.models.Usuario;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import android.widget.Button;
import retrofit2.Response;

public class AdminListUsersActivity extends AppCompatActivity implements UserAdapter.OnUserActionListener {
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<Usuario> usersList;
    private SearchView searchView;
    private ImageButton backButton;
    private Button btnCalculator;
    private ApiService apiService;
    private List<Usuario> originalUsersList = new ArrayList<>();
    private String selectedRole = "Todos";
    private String selectedStatus = "Todos";
    private TextView filterLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list_users);

        apiService = RetrofitClient.getClient(this).create(ApiService.class);
        btnCalculator = findViewById(R.id.btnCalculator);
        recyclerView = findViewById(R.id.usersRecyclerView);
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.setClickable(true);
        searchView.setFocusable(true);
        backButton = findViewById(R.id.backButton);
        ImageButton filterButton = findViewById(R.id.filterButton);
        filterLabel = findViewById(R.id.filterLabel);
        updateFilterLabel();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersList = new ArrayList<>();
        adapter = new UserAdapter(usersList, this, this);
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterUsers(newText);
                return true;
            }
        });

        backButton.setOnClickListener(v -> finish());
        filterButton.setOnClickListener(v -> showFilterDialog());

        btnCalculator.setOnClickListener(v -> setBtnCalculator(v));
        loadUsers();
    }

    private void loadUsers() {
        boolean incluirInactivos = selectedStatus.equals("Inactivos") || selectedStatus.equals("Todos");
        apiService.getAllUsuarios(incluirInactivos).enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    originalUsersList.clear();
                    originalUsersList.addAll(response.body());
                    sortUsersByPriority(originalUsersList);
                    usersList.clear();
                    if (selectedStatus.equals("Activos")) {
                        for (Usuario user : originalUsersList) {
                            if (user.isActive()) usersList.add(user);
                        }
                    } else if (selectedStatus.equals("Inactivos")) {
                        for (Usuario user : originalUsersList) {
                            if (!user.isActive()) usersList.add(user);
                        }
                    } else {
                        usersList.addAll(originalUsersList);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(AdminListUsersActivity.this,
                            "Error al cargar los usuarios: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(AdminListUsersActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeactivateUser(Usuario user, int position) {
        apiService.deleteUserFromAdministrador(user.getIdUsuario()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AdminListUsersActivity.this,
                            "Usuario desactivado correctamente",
                            Toast.LENGTH_SHORT).show();
                    loadUsers();
                } else {
                    Toast.makeText(AdminListUsersActivity.this,
                            "Error al desactivar usuario: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AdminListUsersActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFilterDialog() {
        final String[] roles = {"Todos", "Super Admin", "Admin", "Cliente"};
        int checkedRole = 0;
        for (int i = 0; i < roles.length; i++) {
            if (roles[i].equals(selectedRole)) checkedRole = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filtrar por rol");
        builder.setSingleChoiceItems(roles, checkedRole, (dialog, which) -> {
            selectedRole = roles[which];
        });
        builder.setPositiveButton("Siguiente", (dialog, which) -> {
            showStatusDialog();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void showStatusDialog() {
        final String[] estados = {"Todos", "Activos", "Inactivos"};
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
            filterUsers(searchView.getQuery().toString());
            updateFilterLabel();
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void filterUsers(String query) {
        List<Usuario> filteredList = new ArrayList<>();
        String lowerQuery = query == null ? "" : query.toLowerCase();
        for (Usuario user : originalUsersList) {
            boolean matchesQuery =
                    user.getNombre().toLowerCase().contains(lowerQuery) ||
                            user.getApellido().toLowerCase().contains(lowerQuery) ||
                            user.getEmail().toLowerCase().contains(lowerQuery);
            boolean matchesRole = selectedRole.equals("Todos") ||
                    (selectedRole.equals("Super Admin") && user.isSuperuser()) ||
                    (selectedRole.equals("Admin") && user.isStaff() && !user.isSuperuser()) ||
                    (selectedRole.equals("Cliente") && !user.isStaff() && !user.isSuperuser());
            boolean matchesStatus = selectedStatus.equals("Todos") ||
                    (selectedStatus.equals("Activos") && user.isActive()) ||
                    (selectedStatus.equals("Inactivos") && !user.isActive());
            if (matchesQuery && matchesRole && matchesStatus) {
                filteredList.add(user);
            }
        }
        sortUsersByPriority(filteredList);
        usersList.clear();
        usersList.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    private void sortUsersByPriority(List<Usuario> list) {
        Collections.sort(list, new Comparator<Usuario>() {
            @Override
            public int compare(Usuario u1, Usuario u2) {
                int p1 = getPriority(u1);
                int p2 = getPriority(u2);
                return Integer.compare(p1, p2);
            }
            private int getPriority(Usuario user) {
                if (user.isSuperuser()) return 0;
                if (user.isStaff()) return 1;
                return 2;
            }
        });
    }

    private void updateFilterLabel() {
        if (filterLabel != null) {
            filterLabel.setText("Filtrando por: " + selectedRole + " | " + selectedStatus);
        }
    }

    public void setBtnCalculator(View view) {
        Toast.makeText(this, "Calculadora de ventas", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SalesCalculatorActivity.class);
        startActivity(intent);
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