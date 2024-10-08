package com.example.tiendadecampeones.models;
import java.util.List;

public class Pedido {
    private int idPedido;
    private String fecha;
    private double total;
    private int idUsuario;
    private String estado;
    private List<Detalle> detalles;
    private List<FormaDePago> formaDePago;

    public Pedido(int idPedido, String fecha, double total, int idUsuario, String estado, List<Detalle> detalles, List<FormaDePago> formaDePago) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.total = total;
        this.idUsuario = idUsuario;
        this.estado = estado;
        this.detalles = detalles;
        this.formaDePago = formaDePago;
    }
    public int getIdPedido() { return idPedido; }
    public void setIdPedido(int idPedido) { this.idPedido = idPedido; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<Detalle> getDetalles() { return detalles; }
    public void setDetalles(List<Detalle> detalles) { this.detalles = detalles; }

    public List<FormaDePago> getFormaDePago() { return formaDePago; }
    public void setFormaDePago(List<FormaDePago> formaDePago) { this.formaDePago = formaDePago; }
}
