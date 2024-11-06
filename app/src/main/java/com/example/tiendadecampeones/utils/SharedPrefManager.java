package com.example.tiendadecampeones.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.tiendadecampeones.models.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.example.tiendadecampeones.models.Product;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPrefManager {

    private static final String SHARED_PREFS_NAME = "cart_shared_prefs";
    private static final String CART_ITEMS_KEY = "cart_items";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private static SharedPrefManager instance;

    private SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefManager(context);
        }
        return instance;
    }

    // Guardar el carrito con CartItems
    public void saveCartItems(List<CartItem> cartItemList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(cartItemList);
        Log.d("SharedPrefManager", "JSON guardado: " + json);
        editor.putString(CART_ITEMS_KEY, json);
        editor.apply();
    }

    // Obtener el carrito con CartItems
    public List<CartItem> getCartItems() {
        String json = sharedPreferences.getString(CART_ITEMS_KEY, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void clearCart() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CART_ITEMS_KEY);
        editor.apply();
    }
}