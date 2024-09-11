package com.example.tiendadecampeones;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class AboutUs extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        WebView mapWebView = findViewById(R.id.mapWebView);

        // Configuro el WebView
        WebSettings webSettings = mapWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);  // Permitir JavaScript si es necesario

        // Forzar que se abra en el WebView en lugar del navegador
        mapWebView.setWebViewClient(new WebViewClient());

        // Carga la URL del mapa
        String mapUrl = "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d217955.00184323578!2d-64.35902541600714!3d-31.399054706621524!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9432985f478f5b69%3A0xb0a24f9a5366b092!2zQ8OzcmRvYmE!5e0!3m2!1ses!2sar!4v1691437554022!5m2!1ses!2sar";

        mapWebView.loadUrl(mapUrl);

        // Encuentrar el botón de contacto
        Button contactButton = findViewById(R.id.contactButton);

        // Configuro el listener para el botón de contacto
        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamo a la actividad de contacto
                openContactActivity();
            }
        });
    }

    private void openContactActivity() {
        Intent intent = new Intent(this, Contacto.class);
        startActivity(intent);
    }
}
