package com.example.tiendadecampeones.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.tiendadecampeones.models.Product;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefManager {

    private static final String SHARED_PREFS_NAME = "cart_shared_prefs";
    private static final String CART_PRODUCTS_KEY = "cart_products";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveCartProducts(List<Product> productList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String json = gson.toJson(productList);


        Log.d("SharedPrefManager", "JSON guardado: " + json);
        editor.putString(CART_PRODUCTS_KEY, json);
        editor.apply();
    }

    public List<Product> getCartProducts() {
        String json = sharedPreferences.getString(CART_PRODUCTS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Type type = new TypeToken<List<Product>>() {}.getType();
        List<Product> productList = gson.fromJson(json, type);

        return productList;
    }


    public void clearCart() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART_PRODUCTS_KEY);
        editor.apply();
    }
}