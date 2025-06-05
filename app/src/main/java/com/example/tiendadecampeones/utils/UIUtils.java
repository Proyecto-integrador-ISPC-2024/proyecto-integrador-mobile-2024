package com.example.tiendadecampeones.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class UIUtils {

    public static void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor("#D32F2F"));

        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(18);
        textView.setPadding(20, 10, 20, 10); // Aumentar espacio interno

        snackbar.show();
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT); // Crear la instancia de Toast
        View toastView = toast.getView();

        if (toastView != null) { // Verificar que la vista no sea nula
            toastView.setBackgroundColor(Color.RED); // Fondo rojo

            TextView toastText = toastView.findViewById(android.R.id.message);
            if (toastText != null) { // Verificar que el TextView existe
                toastText.setTextSize(18);
                toastText.setTextColor(Color.WHITE);
            }
        }

        toast.setGravity(Gravity.CENTER, 0, 0); // Centrar el Toast
        toast.show();
    }
}