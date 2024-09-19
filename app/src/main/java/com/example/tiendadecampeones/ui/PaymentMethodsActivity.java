package com.example.tiendadecampeones.ui;

import android.content.Intent;
import android.os.Bundle;
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

public class PaymentMethodsActivity extends AppCompatActivity {
    private LinearLayout paymentFormContainer;
    private EditText cardNameEditText;
    private EditText cardNumberEditText;
    private EditText cardCVVEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        Spinner paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        paymentFormContainer = findViewById(R.id.paymentFormContainer);
        cardNameEditText = findViewById(R.id.cardNameEditText);
        cardNumberEditText = findViewById(R.id.cardNumberEditText);
        cardCVVEditText = findViewById(R.id.cardCVVEditText);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);

        paymentMethodSpinner.setSelection(0, false);

        paymentMethodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (position == 1) {
                    paymentFormContainer.setVisibility(View.VISIBLE);
                    cardNameEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardCVVEditText.setVisibility(View.VISIBLE);
                } else {

                    paymentFormContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

                paymentFormContainer.setVisibility(View.GONE);
            }
        });


        Button confirmPaymentButton = findViewById(R.id.confirmPaymentButton);
        confirmPaymentButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(PaymentMethodsActivity.this, "Gracias por su compra", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PaymentMethodsActivity.this, Home.class);
                startActivity(intent);
            }
        });
    }
}



