package com.example.tiendadecampeones.models;

import java.util.List;

public class Order {
    private int id_pedido;
    private String fecha;
    private double total;
    private int id_usuario;
    private String estado;
    private List<OrderDetail> detalle;
    private List<PaymentMethod> formadepago;

    public Order(int id_pedido, String fecha, double total, int id_usuario, String estado, List<OrderDetail> detalle, List<PaymentMethod> formadepago) {
        this.id_pedido = id_pedido;
        this.fecha = fecha;
        this.total = total;
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.detalle = detalle;
        this.formadepago = formadepago;
    }

    // Getters
    public int getIdPedido() {
        return id_pedido;
    }

    public String getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    public int getIdUsuario() {
        return id_usuario;
    }

    public String getEstado() {
        return estado;
    }

    public List<OrderDetail> getDetalle() {
        return detalle;
    }

    public List<PaymentMethod> getFormaDePago() {
        return formadepago;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
