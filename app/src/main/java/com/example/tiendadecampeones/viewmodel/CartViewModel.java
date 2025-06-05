package com.example.tiendadecampeones.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CartViewModel extends ViewModel {

    private final MutableLiveData<Integer> count = new MutableLiveData<>(0);

    /* Observado por las pantallas */
    public LiveData<Integer> getCount() { return count; }

    /* Métodos públicos para modificar */
    public void add()           { count.setValue(count.getValue() + 1); }
    public void remove()        { count.setValue(Math.max(0, count.getValue() - 1)); }
    public void clear()         { count.setValue(0); }
    public void setCount(int n) { count.setValue(n); }
}
