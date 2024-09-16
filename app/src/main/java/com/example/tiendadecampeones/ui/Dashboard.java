package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tiendadecampeones.AboutUs;
import com.example.tiendadecampeones.R;


public class Dashboard extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView searchV;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         EdgeToEdge.enable(this);

        setContentView(R.layout.activity_dashboard);


        searchV = findViewById(R.id.searchV);
        searchV.setOnQueryTextListener(this);


        listView = findViewById(R.id.listView);

        // Datos a mostrar en la lista
        String[][] orders = {
                {"Pedido 123", "Fecha 11/09/2023"},
                {"Pedido 456", "Fecha 27/10/2023"},
                {"Pedido 789", "Fecha 20/12/2023"},
                {"Pedido 1011", "Fecha 24/05/2024"},
                {"Pedido 351", "Fecha 22/01/2024"},
                {"Pedido 3514", "Fecha 25/06/2024"},
                {"Pedido 8511", "Fecha 13/08/2024"}
        };

        // Crear una lista de títulos para mostrar
        String[] orderTitles = new String[orders.length];
        for (int i = 0; i < orders.length; i++) {
            orderTitles[i] = orders[i][0] + " - " + orders[i][1]; // Título y fecha del pedido
        }


        //ArrayAdapter para mostrar los títulos en el ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderTitles);
        listView.setAdapter(adapter);

        // Configuracion de la acción de clic en cada ítem del ListView
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedOrder = orders[position][0]; // Título del pedido seleccionado
                String selectedDate = orders[position][1];  // Fecha del pedido seleccionado

                // Mostrar un Toast con los detalles del pedido
                Toast.makeText(Dashboard.this, "Pedido: " + selectedOrder + "\nFecha: " + selectedDate, Toast.LENGTH_SHORT).show();


                //  Intent para iniciar la nueva actividad
                Intent intent = new Intent(Dashboard.this, Order.class);
                // Pasar los datos del pedido seleccionado a la nueva actividad
                intent.putExtra("ORDER_TITLE", selectedOrder);
                intent.putExtra("ORDER_DATE", selectedDate);
                startActivity(intent);
            }
        });

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
        adapter.getFilter().filter(newText);
        return true;
    }

    // Métodos para manejar los botones

    public void logoutClick(View v) {
        Toast.makeText(this, "Has salido de la aplicación", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        // Intent para iniciar la actividad del dashboard
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void backButton(View v) {
        Toast.makeText(this, "Redireccionando a tu página anterior ", Toast.LENGTH_SHORT).show();
        finish();
    }

//botones de la barra de navegacion inferior
public void webtn(View v) {
    Toast.makeText(this, "¡ Conócenos !", Toast.LENGTH_SHORT).show();

    Intent intent = new Intent(this, AboutUs.class);
    startActivity(intent);
}

public void homeButton(View v) {
    Toast.makeText(this, "¡ Home !", Toast.LENGTH_SHORT).show();

    Intent intent = new Intent(this, Home.class);
    startActivity(intent);
}

public void productsButton(View v) {
    Toast.makeText(this, "¡ Nuestros Productos !", Toast.LENGTH_SHORT).show();

    Intent intent = new Intent(this, ProductsActivity.class);
    startActivity(intent);
}

}

