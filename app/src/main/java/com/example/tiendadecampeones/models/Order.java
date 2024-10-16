package com.example.tiendadecampeones.models;
import java.util.List;

public class Order {
    private int id_pedido;
    private String fecha;
    private double total;
    private int id_usuario;
    private String estado;
    private List<OrderDetail> detalles;
    private List<PaymentMethods.FormaDePago> formas_de_pago;
    private List<PaymentMethods.Tarjeta> tarjetas;

    public Order(int id_pedido, String fecha, double total, int id_usuario, String estado, List<OrderDetail> detalles, List<PaymentMethods.FormaDePago> formas_de_pago, List<PaymentMethods.Tarjeta> tarjetas) {
        this.id_pedido = id_pedido;
        this.fecha = fecha;
        this.total = total;
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.detalles = detalles;
        this.formas_de_pago = formas_de_pago;
        this.tarjetas = tarjetas;
    }

    // Getters y Setters
    public int getIdPedido() { return id_pedido; }
    public void setIdPedido(int id_pedido) { this.id_pedido = id_pedido; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public int getIdUsuario() { return id_usuario; }
    public void setIdUsuario(int id_usuario) { this.id_usuario = id_usuario; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<OrderDetail> getDetalles() { return detalles; }
    public void setDetalles(List<OrderDetail> detalles) { this.detalles = detalles; }

    public List<PaymentMethods.FormaDePago> getFormasDePago() { return formas_de_pago; }
    public void setFormasDePago(List<PaymentMethods.FormaDePago> formas_de_pago) { this.formas_de_pago = formas_de_pago; }

    public List<PaymentMethods.Tarjeta> getTarjetas() { return tarjetas; }
    public void setTarjetas(List<PaymentMethods.Tarjeta> tarjetas) { this.tarjetas = tarjetas; }

}
