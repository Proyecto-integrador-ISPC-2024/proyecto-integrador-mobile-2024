package com.example.tiendadecampeones.adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.util.Base64;
import org.json.JSONObject;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.Usuario;
import com.example.tiendadecampeones.ui.AdminOrdersActivity;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<Usuario> usersList;
    private final Context context;
    private boolean isSuperAdmin = false;
    private boolean isAdmin = false;
    private OnUserActionListener listener;
    private int loggedUserId;

    public interface OnUserActionListener {
        void onDeactivateUser(Usuario user, int position);
    }

    public UserAdapter(List<Usuario> usersList, Context context, OnUserActionListener listener) {
        this.usersList = usersList;
        this.context = context;
        this.listener = listener;
        decodeLoggedUserRole();
    }

    private void decodeLoggedUserRole() {
        SharedPreferences prefs = context.getSharedPreferences("AuthPrefs", Context.MODE_PRIVATE);
        String token = prefs.getString("accessToken", "");
        loggedUserId = prefs.getInt("id_usuario", -1);
        try {
            String[] parts = token.split("\\.");
            String payload = parts[1];
            byte[] decodedBytes = Base64.decode(payload, Base64.URL_SAFE);
            String decoded = new String(decodedBytes, "UTF-8");
            JSONObject json = new JSONObject(decoded);
            boolean isStaff = json.optBoolean("is_staff", false);
            boolean isSuperuser = json.optBoolean("is_superuser", false);
            String rol = json.optString("rol", "");
            isSuperAdmin = isStaff && isSuperuser && "ADMIN".equals(rol);
            isAdmin = isStaff && !isSuperuser && "ADMIN".equals(rol);
        } catch (Exception e) {
            isSuperAdmin = false;
            isAdmin = false;
        }
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Usuario user = usersList.get(position);
        String fullName = context.getString(R.string.full_name_format, user.getNombre(), user.getApellido());
        holder.userName.setText(fullName);
        holder.userEmail.setText(user.getEmail());
        holder.userRole.setText(user.getRol());

        // Mostrar 'Inactivo' si el usuario está inactivo
        if (!user.isActive()) {
            holder.userStatus.setVisibility(View.VISIBLE);
            holder.userStatus.setText("Inactivo");
        } else {
            holder.userStatus.setVisibility(View.GONE);
        }

        // Mostrar 'Cuenta actual' si es el usuario logueado
        if (user.getIdUsuario() == loggedUserId) {
            holder.currentAccountLabel.setVisibility(View.VISIBLE);
        } else {
            holder.currentAccountLabel.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AdminOrdersActivity.class);
            intent.putExtra("USER_ID", user.getIdUsuario());
            intent.putExtra("USER_NAME", fullName);
            intent.putExtra("USER_EMAIL", user.getEmail());
            intent.putExtra("USER_ROLE", user.getRol());
            context.startActivity(intent);
        });

        // Lógica para el botón Desactivar
        Button deactivateButton = holder.itemView.findViewById(R.id.deactivateButton);
        // permisos
        if (!user.isActive()) {
            deactivateButton.setVisibility(View.GONE);
        } else if (user.isSuperuser()) {
            deactivateButton.setVisibility(View.GONE);
        } else if (user.isStaff()) {
            // Es admin
            if (isSuperAdmin) {
                deactivateButton.setVisibility(View.VISIBLE);
            } else {
                deactivateButton.setVisibility(View.GONE);
            }
        } else {
            // Es cliente
            if (isSuperAdmin || isAdmin) {
                deactivateButton.setVisibility(View.VISIBLE);
            } else {
                deactivateButton.setVisibility(View.GONE);
            }
        }

        deactivateButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Desactivar usuario")
                    .setMessage("¿Estás seguro de que deseas desactivar a " + fullName + "?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        if (listener != null) {
                            listener.onDeactivateUser(user, position);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    protected static class UserViewHolder extends RecyclerView.ViewHolder {
        final TextView userName;
        final TextView userEmail;
        final TextView userRole;
        final TextView userStatus;
        final TextView currentAccountLabel;

        UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userRole = itemView.findViewById(R.id.userRole);
            userStatus = itemView.findViewById(R.id.userStatus);
            currentAccountLabel = itemView.findViewById(R.id.currentAccountLabel);
        }
    }
}