package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.PaymentMethods;
import com.example.tiendadecampeones.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PaymentMethodsActivity extends AppCompatActivity {
    private LinearLayout paymentFormContainer;
    private EditText cardNumberEditText;
    private EditText cardLast4DigitsEditText;
    private Spinner cardTypeSpinner;
    private Spinner paymentMethodSpinner;
    private EditText aliasEditText;
    private EditText transferNumberEditText;
    private Button confirmPaymentButton;

    // Adaptadores para los métodos de pago
    private ArrayAdapter<PaymentMethods.FormaDePago> paymentMethodAdapter;
    private ArrayAdapter<PaymentMethods.Tarjeta> cardAdapter;

    // Listas para almacenar métodos de pago y tarjetas
    private List<PaymentMethods.FormaDePago> formas_de_pago = new ArrayList<>();
    private List<PaymentMethods.Tarjeta> tarjetas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d("PaymentMethodsActivity", "Mensaje de depuración"); // Correcta forma de imprimir logs en Android

        try {
            setContentView(R.layout.activity_payment_method);
        } catch (Exception e) {
            e.printStackTrace(); // Imprime el error en la consola para más detalles.
            Log.e("PaymentMethodsActivity", "Error al inflar el layout", e); // También puedes registrar el error en Logcat
        }


        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        cardTypeSpinner = findViewById(R.id.cardTypeSpinner);
        paymentFormContainer = findViewById(R.id.paymentFormContainer);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        cardLast4DigitsEditText = findViewById(R.id.cardLast4DigitsEditText);
        aliasEditText = findViewById(R.id.aliasEditText);
        transferNumberEditText = findViewById(R.id.transferNumberEditText);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);


        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
        // Cargo los métodos de pago
        loadPaymentMethods();

        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                handlePaymentMethodSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                paymentFormContainer.setVisibility(View.GONE);
            }
        });

        confirmPaymentButton.setOnClickListener(v -> {
            Toast.makeText(PaymentMethodsActivity.this, "Gracias por su compra", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PaymentMethodsActivity.this, Home.class);
            startActivity(intent);
        });
    }

    private void loadPaymentMethods() {
        ApiService apiService = ApiService.create();
        Call<PaymentMethods> call = apiService.getPaymentMethods();

        call.enqueue(new Callback<PaymentMethods>() {
            @Override
            public void onResponse(Call<PaymentMethods> call, Response<PaymentMethods> response) {
                if (response.isSuccessful() && response.body() != null) {
                    formas_de_pago = response.body().getFormasDePago();
                    tarjetas = response.body().getTarjetas();
                    setupPaymentMethodSpinner(formas_de_pago);
                } else {
                    int errorCode = response.code();
                    Toast.makeText(PaymentMethodsActivity.this, "Error al cargar métodos de pago: Código " + errorCode, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PaymentMethods> call, Throwable t) {
                Toast.makeText(PaymentMethodsActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupPaymentMethodSpinner(List<PaymentMethods.FormaDePago> formas_de_pago) {
        // Crear un adaptador para mostrar solo la descripción de las formas de pago
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                formas_de_pago.stream().map(PaymentMethods.FormaDePago::getDescripcion).collect(Collectors.toList()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);
        paymentMethodSpinner.setSelection(0);

        // Listener para manejar la selección de la forma de pago
        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handlePaymentMethodSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });
    }

    private void handlePaymentMethodSelection(int position) {
        PaymentMethods.FormaDePago selectedMethod = (PaymentMethods.FormaDePago) paymentMethodSpinner.getItemAtPosition(position);

        if (selectedMethod != null) {
            String descripcion = selectedMethod.getDescripcion();
            boolean isDefaultSelection = "Seleccione un método de pago".equals(descripcion);

            if (isDefaultSelection) {
                paymentFormContainer.setVisibility(View.GONE);
                cardTypeSpinner.setVisibility(View.GONE);
                return;
            }

            paymentFormContainer.setVisibility(View.VISIBLE);

            switch (descripcion) {
                case "Credito":
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardLast4DigitsEditText.setVisibility(View.VISIBLE);
                    transferNumberEditText.setVisibility(View.GONE);
                    aliasEditText.setVisibility(View.GONE);

                    // Configurar y mostrar el Spinner de tarjetas
                    setupCardSpinner("Credito");
                    break;

                case "Debito":
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardLast4DigitsEditText.setVisibility(View.VISIBLE);
                    transferNumberEditText.setVisibility(View.GONE);
                    aliasEditText.setVisibility(View.GONE);

                    // Ocultar el Spinner de tarjetas
                    setupCardSpinner("Debito");
                    break;

                case "Transferencia":
                    transferNumberEditText.setVisibility(View.VISIBLE);
                    aliasEditText.setVisibility(View.VISIBLE);
                    transferNumberEditText.setText("000000310009268966941");
                    aliasEditText.setText("devteam.cba");
                    cardNumberEditText.setVisibility(View.GONE);
                    cardLast4DigitsEditText.setVisibility(View.GONE);
                    cardTypeSpinner.setVisibility(View.GONE);
                    break;

                default:
                    paymentFormContainer.setVisibility(View.GONE);
                    break;
            }
        } else {
            paymentFormContainer.setVisibility(View.GONE);
            cardTypeSpinner.setVisibility(View.GONE);
        }
    }

    private void setupCardSpinner(String metodoPago) {
        // Si el método de pago es "Credito", mostrar el Spinner y configurar tarjetas
        if ("Credito".equals(metodoPago)) {
            // Crear un adaptador con los nombres de las tarjetas
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                    tarjetas.stream().map(PaymentMethods.Tarjeta::getNombreTarjeta).collect(Collectors.toList()));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cardTypeSpinner.setAdapter(adapter);

            // Mostrar el Spinner de tarjetas
            cardTypeSpinner.setVisibility(View.VISIBLE);

            // Listener para manejar la selección de la tarjeta
            cardTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Aquí puedes manejar la tarjeta seleccionada si es necesario
                    String tarjetaSeleccionada = tarjetas.get(position).getNombreTarjeta();
                    // Lógica adicional si es necesario
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // No hacer nada si no se selecciona ninguna tarjeta
                }
            });

        } else {
            // Ocultar el Spinner si el método de pago es "Debito" o cualquier otro
            cardTypeSpinner.setVisibility(View.GONE);
        }
    }
}