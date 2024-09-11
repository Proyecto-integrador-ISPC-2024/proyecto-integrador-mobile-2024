package com.example.tiendadecampeones;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_dashboard extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView searchV;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  EdgeToEdge.enable(this);

        setContentView(R.layout.activity_dashboard);

        // Configurar el SearchView
        searchV = findViewById(R.id.searchV);
        searchV.setOnQueryTextListener(this);

        // Configurar el ListView
        listView = findViewById(R.id.listView);

        // Datos a mostrar en la lista
        String[] datos = {" Pedido 123\n\n 11/09/2023\n ", "\n Pedido 456\n\n 27/10/2023 \n",
                " \nPedido 789\n\n 20/12/2023\n", "\npedido 1011\n\n 24/05/2024\n",
                " \nPedido 351\n\n 22/06/2024\n", "\npedido 8511\n\n 13/08/2024\n"
        };

        // ArrayAdapter para el ListView
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                datos
        );

        // Asigna el adaptador al ListView
        listView.setAdapter(adapter);

        // Ajustar los insets de la ventana
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // Filtrar el contenido del ListView basado en el texto ingresado
        adapter.getFilter().filter(newText);
        return true;
    }

    // Método para manejar el clic en el botón de cierre de sesión
    public void logoutClick(View v) {
        Toast.makeText(this, "Has salido de la aplicación", Toast.LENGTH_SHORT).show();

        // Opcional: finalizar la actividad actual
        finish();
    }
}