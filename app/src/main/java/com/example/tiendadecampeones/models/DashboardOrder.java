package com.example.tiendadecampeones.models;

public class DashboardOrder {
    private int id;
    private String fecha;
    private String estado;

    public DashboardOrder(int id, String fecha, String estado) {
        this.id = id;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getIdPedido() {
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }
}
