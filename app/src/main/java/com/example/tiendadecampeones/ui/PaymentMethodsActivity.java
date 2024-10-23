package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tiendadecampeones.R;
import com.example.tiendadecampeones.models.PaymentMethods;
import com.example.tiendadecampeones.models.Pedido;
import com.example.tiendadecampeones.network.ApiService;
import com.example.tiendadecampeones.network.RetrofitClient;
import com.google.gson.Gson;

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
    private TextView transferMessageTextView;
    private TextView cbuLabel;
    private TextView aliasLabel;
    private TextView totalTextView;

    // Listas para almacenar métodos de pago y tarjetas y lo necesario para el pedido
    private List<PaymentMethods.FormaDePago> formas_de_pago = new ArrayList<>();
    private List<PaymentMethods.Tarjeta> tarjetas = new ArrayList<>();
    private Integer selectedPaymentMethodId = null;
    private Integer selectedCardId = null;
    private List<Pedido.Detalle> detalles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        cardTypeSpinner = findViewById(R.id.cardTypeSpinner);
        paymentFormContainer = findViewById(R.id.paymentFormContainer);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        cardLast4DigitsEditText = findViewById(R.id.cardLast4DigitsEditText);
        aliasEditText = findViewById(R.id.aliasEditText);
        transferNumberEditText = findViewById(R.id.transferNumberEditText);
        confirmPaymentButton = findViewById(R.id.confirmPaymentButton);
        transferMessageTextView = findViewById(R.id.transferMessageTextView);
        cbuLabel = findViewById(R.id.cbuLabel);
        aliasLabel = findViewById(R.id.aliasLabel);
        totalTextView = findViewById(R.id.totalTextView);
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        Pedido pedido = cargarPedidoDesdeIntent();
        if (pedido != null) {
            actualizarTotal(pedido.getTotal());
        }

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
        if (pedido != null) {
            // Crear la forma de pago
            Pedido.FormaDePago formaDePago = crearFormaDePago();
            // Establecer la forma de pago en el pedido
            List<Pedido.FormaDePago> formasDePago = new ArrayList<>();
            formasDePago.add(formaDePago);
            pedido.setFormaDePago(formasDePago);

            // Realizar la petición POST con el pedido actualizado
            realizarPedido(pedido);

            // Feedback al usuario
            Toast.makeText(PaymentMethodsActivity.this, "Gracias por su compra", Toast.LENGTH_SHORT).show();
            Intent homeIntent = new Intent(PaymentMethodsActivity.this, Home.class);
            startActivity(homeIntent);
        } else {
            Toast.makeText(PaymentMethodsActivity.this, "No se encontraron datos del pedido.", Toast.LENGTH_SHORT).show();
        }
     });
    }

    private Pedido cargarPedidoDesdeIntent() {
        Intent intent = getIntent();
        String pedidoJson = intent.getStringExtra("pedido");
        Log.d("PaymentMethodsActivity", "JSON recibido: " + pedidoJson);
        if (pedidoJson != null) {
            Gson gson = new Gson();
            return gson.fromJson(pedidoJson, Pedido.class);
        }
        return null;
    }

    private void actualizarTotal(double total) {
        // Actualizar el TextView con el total del pedido
        totalTextView.setText(String.format("Total: $%.2f", total));
    }

    private Pedido.FormaDePago crearFormaDePago() {
        if (esMetodoCredito(selectedPaymentMethodId)) {
            return new Pedido.FormaDePago(selectedPaymentMethodId, selectedCardId);
        } else {
            return new Pedido.FormaDePago(selectedPaymentMethodId, null);
        }
    }

    private void loadPaymentMethods() {
        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String authToken = preferences.getString("accessToken", null);
        int id_usuario = preferences.getInt("id_usuario", -1);

        if (authToken == null) {
            Log.e("AuthTokenDebug", "No se encontró el token de autenticación.");
            Toast.makeText(this, "No autenticado. Inicie sesión.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (id_usuario == -1) {
            Log.e("UserIdDebug", "ID de usuario no válido.");
            System.out.println(id_usuario);
            Toast.makeText(this, "ID de usuario no válido.", Toast.LENGTH_SHORT).show();
            return;
        }
        String fullAuthToken = "Bearer " + authToken;
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<PaymentMethods> call = apiService.getPaymentMethods(fullAuthToken);

        call.enqueue(new Callback<PaymentMethods>() {
            @Override
            public void onResponse(Call<PaymentMethods> call, Response<PaymentMethods> response) {
                if (response.isSuccessful() && response.body() != null) {
                    formas_de_pago = response.body().getFormasDePago();
                    tarjetas = response.body().getTarjetas();

                    if (formas_de_pago != null && !formas_de_pago.isEmpty()) {
                        setupPaymentMethodSpinner(formas_de_pago);
                    } else {
                        Toast.makeText(PaymentMethodsActivity.this, "No se encontraron métodos de pago.", Toast.LENGTH_SHORT).show();
                    }
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
        try {
            // Agregar "Seleccione un método de pago" al inicio de la lista
            List<String> descripcionFormasDePago = formas_de_pago.stream()
                    .map(PaymentMethods.FormaDePago::getDescripcion)
                    .collect(Collectors.toList());
            descripcionFormasDePago.add(0, "Seleccione un método de pago");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, descripcionFormasDePago);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            paymentMethodSpinner.setAdapter(adapter);
            paymentMethodSpinner.setSelection(0);

            // Desactivar el botón de confirmación al inicio
            confirmPaymentButton.setEnabled(false);
        } catch (Exception e) {
            Log.e("SpinnerSetupError", "Error configurando el Spinner de métodos de pago", e);
        }

        // Listener para manejar la selección de la forma de pago
        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handlePaymentMethodSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paymentFormContainer.setVisibility(View.GONE);
            }
        });
    }

    private void handlePaymentMethodSelection(int position) {
        if (position < 0 || position >= formas_de_pago.size() + 1) {
            Log.e("PaymentMethodSelection", "Selección de método de pago inválida: " + position);
            paymentFormContainer.setVisibility(View.GONE);
            confirmPaymentButton.setEnabled(false);
            return;
        }

        if (position == 0) {
            paymentFormContainer.setVisibility(View.GONE);
            cardTypeSpinner.setVisibility(View.GONE);
            confirmPaymentButton.setEnabled(false);
            return;
        }
        cardNumberEditText.setText("");
        cardLast4DigitsEditText.setText("");

        PaymentMethods.FormaDePago selectedMethod = formas_de_pago.get(position - 1);
        selectedPaymentMethodId = selectedMethod.getIdFormaDePago();

        if (selectedMethod != null) {
            String descripcion = selectedMethod.getDescripcion();

            paymentFormContainer.setVisibility(View.VISIBLE);
            confirmPaymentButton.setEnabled(false);
            cardNumberEditText.setVisibility(View.VISIBLE);
            cardLast4DigitsEditText.setVisibility(View.VISIBLE);

            switch (descripcion) {
                case "Credito":
                    setupCardSpinner("Credito"); // Solo para crédito se muestra el spinner de tarjetas
                    cardTypeSpinner.setVisibility(View.VISIBLE);
                    transferNumberEditText.setVisibility(View.GONE);
                    transferMessageTextView.setVisibility(View.GONE);
                    aliasEditText.setVisibility(View.GONE);
                    cbuLabel.setVisibility(View.GONE);
                    aliasLabel.setVisibility(View.GONE);
                    break;

                case "Debito":
                    cardTypeSpinner.setVisibility(View.GONE); // Spinner no se muestra para débito
                    transferMessageTextView.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardLast4DigitsEditText.setVisibility(View.VISIBLE);
                    transferNumberEditText.setVisibility(View.GONE);
                    aliasEditText.setVisibility(View.GONE);
                    cbuLabel.setVisibility(View.GONE);
                    aliasLabel.setVisibility(View.GONE);
                    break;

                case "Transferencia":
                    paymentFormContainer.setVisibility(View.VISIBLE);
                    transferMessageTextView.setVisibility(View.VISIBLE);
                    cbuLabel.setVisibility(View.VISIBLE);
                    transferNumberEditText.setVisibility(View.VISIBLE);
                    transferNumberEditText.setText("000000310009268966941");
                    aliasLabel.setVisibility(View.VISIBLE);
                    aliasEditText.setVisibility(View.VISIBLE);
                    aliasEditText.setText("devteam.cba");
                    cardNumberEditText.setVisibility(View.GONE);
                    cardLast4DigitsEditText.setVisibility(View.GONE);
                    cardTypeSpinner.setVisibility(View.GONE);
                    confirmPaymentButton.setEnabled(true);
                    break;

                default:
                    paymentFormContainer.setVisibility(View.GONE);
                    break;
            }
            addTextWatchers();
        } else {
            paymentFormContainer.setVisibility(View.GONE);
            cardTypeSpinner.setVisibility(View.GONE);
            confirmPaymentButton.setEnabled(false);
        }
    }

    private void addTextWatchers() {
        TextWatcher cardTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateCardInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        cardNumberEditText.addTextChangedListener(cardTextWatcher);
        cardLast4DigitsEditText.addTextChangedListener(cardTextWatcher);
    }

    private void validateCardInputs() {
        String cardNumber = cardNumberEditText.getText().toString();
        String last4Digits = cardLast4DigitsEditText.getText().toString();
        boolean isCardNumberValid = cardNumber.length() == 16 && cardNumber.matches("\\d+");
        boolean isLast4DigitsValid = last4Digits.length() == 4 && last4Digits.matches("\\d+");
        if (!isCardNumberValid) {
            cardNumberEditText.setError("El número de tarjeta debe tener 16 dígitos");
        }
        confirmPaymentButton.setEnabled(isCardNumberValid && isLast4DigitsValid);
    }

    private void setupCardSpinner(String metodoPago) {
        if ("Credito".equals(metodoPago)) {
            List<String> nombresTarjetas = tarjetas.stream()
                    .map(PaymentMethods.Tarjeta::getNombreTarjeta)
                    .collect(Collectors.toList());
            nombresTarjetas.add(0, "Seleccione una tarjeta");

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresTarjetas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cardTypeSpinner.setAdapter(adapter);

            cardTypeSpinner.setVisibility(View.VISIBLE);
            confirmPaymentButton.setEnabled(false);
            cardTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Verificar si se seleccionó la opción por defecto
                    if (position == 0) {
                        selectedCardId = null;
                        // desactivo el boton de confirmar en caso de estar la opcion por defecto
                        confirmPaymentButton.setEnabled(false);
                    } else {
                        selectedCardId = tarjetas.get(position - 1).getIdTarjeta();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    selectedCardId = null;
                }
            });

        } else if ("Transferencia".equals(metodoPago)) {
            cardTypeSpinner.setVisibility(View.GONE);
            confirmPaymentButton.setEnabled(true);
        } else {
            cardTypeSpinner.setVisibility(View.GONE);
            confirmPaymentButton.setEnabled(false);
        }
    }

    private boolean esMetodoCredito(Integer selectedPaymentMethodId) {
        if (selectedPaymentMethodId == null) {
            return false;
        }
        for (PaymentMethods.FormaDePago metodo : formas_de_pago) {
            if (metodo.getIdFormaDePago() == selectedPaymentMethodId) {
                return "Credito".equalsIgnoreCase(metodo.getDescripcion());
            }
        }
        return false;
    }
    private void realizarPedido(Pedido pedido) {

        SharedPreferences preferences = getSharedPreferences("AuthPrefs", MODE_PRIVATE);
        String authToken = preferences.getString("accessToken", null);
        if (authToken == null) {
            Toast.makeText(this, "No se encontró el token de autenticación.", Toast.LENGTH_SHORT).show();
            return;
        }
        String fullAuthToken = "Bearer " + authToken;
        ApiService apiService = RetrofitClient.getClient(this).create(ApiService.class);
        Call<Pedido> call = apiService.realizarPedido(fullAuthToken, pedido);
        call.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(PaymentMethodsActivity.this, "Pedido realizado exitosamente.", Toast.LENGTH_SHORT).show();
                } else {
                    int errorCode = response.code();
                    String errorMessage = response.message();
                    Log.e("PedidoError", "Error al realizar el pedido: Código " + errorCode + ", Mensaje: " + errorMessage);
                    Toast.makeText(PaymentMethodsActivity.this, "Error al realizar el pedido: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Toast.makeText(PaymentMethodsActivity.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}