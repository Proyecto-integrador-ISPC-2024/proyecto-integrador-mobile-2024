package com.example.tiendadecampeones.models;
import java.util.List;

public class Pedido {
    private int idPedido;
    private String fecha;
    private double total;
    private int idUsuario;
    private String estado;
    private List<Detalle> detalles;
    private List<FormaDePago> formasDePago;

    public Pedido(int idPedido, String fecha, double total, int idUsuario, String estado, List<Detalle> detalles, List<FormaDePago> formasDePago) {
        this.idPedido = idPedido;
        this.fecha = fecha;
        this.total = total;
        this.idUsuario = idUsuario;
        this.estado = estado;
        this.detalles = detalles;
        this.formasDePago = formasDePago;
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

    public List<FormaDePago> getFormasDePago() { return formasDePago; }
    public void setFormasDePago(List<FormaDePago> formasDePago) { this.formasDePago = formasDePago; }

    public static class FormaDePago {
        private String forma_pago_descripcion;
        private String tarjeta_nombre;

        public FormaDePago(String forma_pago_descripcion, String tarjeta_nombre) {
            this.forma_pago_descripcion = forma_pago_descripcion;
            this.tarjeta_nombre = tarjeta_nombre;
        }

        public String getFormaPagoDescripcion() { return forma_pago_descripcion; }
        public String getTarjetaNombre() { return tarjeta_nombre; }
    }
}


