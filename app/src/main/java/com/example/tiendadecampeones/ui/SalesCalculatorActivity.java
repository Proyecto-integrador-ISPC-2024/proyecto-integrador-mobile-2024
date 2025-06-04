package com.example.tiendadecampeones.ui;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.SalesResponse;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesCalculatorActivity extends AppCompatActivity {
    private EditText etFechaInicio;
    private EditText etFechaFin;
    private Button btnCalcularVentas;
    private ImageButton backButton;
    private ApiService apiService;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_calculator);

        apiService = RetrofitClient.getClient(this).create(ApiService.class);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        etFechaInicio = findViewById(R.id.etFechaInicio);
        etFechaFin = findViewById(R.id.etFechaFin);
        btnCalcularVentas = findViewById(R.id.btnCalcularVentas);
        backButton = findViewById(R.id.backButton);

        etFechaInicio.setOnClickListener(v -> showDatePicker(etFechaInicio));
        etFechaInicio.setFocusable(false);
        etFechaInicio.setClickable(true);

        etFechaFin.setOnClickListener(v -> showDatePicker(etFechaFin));
        etFechaFin.setFocusable(false);
        etFechaFin.setClickable(true);

        backButton.setOnClickListener(v -> finish());

        btnCalcularVentas.setOnClickListener(v -> {
            String fechaInicio = etFechaInicio.getText().toString();
            String fechaFin = etFechaFin.getText().toString();

            if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
                Toast.makeText(this, "Por favor, complete ambas fechas", Toast.LENGTH_SHORT).show();
                return;
            }

            calcularVentas(fechaInicio, fechaFin);
        });
    }

    private void showDatePicker(EditText editText) {
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editText.setText(dateFormat.format(calendar.getTime()));
        };

        new DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void calcularVentas(String fechaInicio, String fechaFin) {
        Call<SalesResponse> call = apiService.calcularVentas(fechaInicio, fechaFin);

        call.enqueue(new Callback<SalesResponse>() {
            @Override
            public void onResponse(Call<SalesResponse> call, Response<SalesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    double totalVentas = response.body().getTotalVentas();
                    String mensaje = String.format("Total de ventas: $%.2f", totalVentas);
                    showResultDialog(mensaje);
                } else {
                    Toast.makeText(SalesCalculatorActivity.this,
                            "Error al calcular ventas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SalesResponse> call, Throwable t) {
                Toast.makeText(SalesCalculatorActivity.this,
                        "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResultDialog(String mensaje) {
        new AlertDialog.Builder(this)
                .setTitle("Resultado")
                .setMessage(mensaje)
                .setPositiveButton("OK", null)
                .show();
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

    public void profileBtn(View view) {
        Toast.makeText(this, "Redirigiendo a tu perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}