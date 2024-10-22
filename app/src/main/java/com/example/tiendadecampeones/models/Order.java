package com.example.tiendadecampeones.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Order {
    private int id_pedido;
    private String fecha;
    private double total;
    private int id_usuario;
    private String estado;
    private List<OrderDetail> detalles;
    private List<FormaDePago> forma_de_pago;

    // Constructor
    public Order(int id_pedido, String fecha, double total, int id_usuario, String estado, List<OrderDetail> detalles, List<FormaDePago> forma_de_pago) {
        this.id_pedido = id_pedido;
        this.fecha = fecha;
        this.total = total;
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.detalles = detalles;
        this.forma_de_pago = forma_de_pago;
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

    public List<FormaDePago> getFormaDePago() { return forma_de_pago; }
    public void setFormaDePago(List<FormaDePago> forma_de_pago) { this.forma_de_pago = forma_de_pago; }

    public static class FormaDePago {
        private String forma_de_pago_descripcion;
        private String tarjeta_nombre;

        public FormaDePago(String forma_de_pago_descripcion, String tarjeta_nombre) {
            this.forma_de_pago_descripcion = forma_de_pago_descripcion;
            this.tarjeta_nombre = tarjeta_nombre;
        }

        // Getters y Setters
        public String getFormaDePagoDescripcion() { return forma_de_pago_descripcion; }
        public void setFormaDePagoDescripcion(String forma_de_pago_descripcion) { this.forma_de_pago_descripcion = forma_de_pago_descripcion; }

        public String getTarjeta() { return tarjeta_nombre; }
        public void setTarjeta(String tarjeta_nombre) { this.tarjeta_nombre = tarjeta_nombre; }
    }

    public static class OrderDetail {
        private int id_talle;
        private int cantidad;
        private double subtotal;
        private Product producto;

        public OrderDetail(int id_talle, int cantidad, double subtotal, Product producto) {
            this.id_talle = id_talle;
            this.cantidad = cantidad;
            this.subtotal = subtotal;
            this.producto = producto;
        }

        private static final Map<Integer, String> TALLE_MAP = new HashMap<>();

        static {
            TALLE_MAP.put(3, "M");
            TALLE_MAP.put(4, "L");
            TALLE_MAP.put(5, "XL");
        }
        // Getters y Setters
        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }

        public Product getProducto() { return producto; }

        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

        public String getTalleString() {
            return TALLE_MAP.getOrDefault(id_talle,"S");
        }

        @Override
        public String toString() {
            return "Talle: " + getTalleString() + ", Cantidad: " + cantidad + ", Subtotal: $" + subtotal + ", Producto: " + producto;
        }

        public static class Product {
            private int id_producto;
            private String nombre_producto;
            private double precio;
            private String imagen;

            public Product(int id_producto, String nombre_producto, double precio, String imagen) {
                this.id_producto = id_producto;
                this.nombre_producto = nombre_producto;
                this.precio = precio;
                this.imagen = imagen;
            }

            // Getters
            public String getNombreProducto() { return nombre_producto; }
            public double getPrecio() { return precio; }
            public String getImagen() { return imagen; }

            @Override
            public String toString() {
                return nombre_producto + " (ID: " + id_producto + ", Precio: $" + precio + ")";
            }
        }
    }
}
