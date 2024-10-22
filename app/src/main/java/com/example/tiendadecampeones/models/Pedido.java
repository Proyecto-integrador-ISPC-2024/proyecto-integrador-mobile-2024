package com.example.tiendadecampeones.models;
import java.util.List;

public class Pedido { // se debería crear una instancia de esta clase en el carro para completar los detalles de los atributos con la información de los productos agregados al carro una vez que se confirma el listado de productos al darle click al botón de "Finalizar compra" ubicado en el carro
    private int id_usuario;
    private double total;
    private List<Detalle> detalles; // Se debería crear una instancia de la clase Detalle para poder completar los atributos de esta clase y así poder empezar a crear el pedido
    private List<FormaDePago> forma_de_pago; // Esta propiedad debería quedar nula por el momento, en otra actividad se debe definir su valor

    public Pedido(int id_usuario, double total, List<Detalle> detalles, List<FormaDePago> forma_de_pago) {
        this.id_usuario = id_usuario;
        this.total = total;
        this.detalles = detalles;
        this.forma_de_pago = forma_de_pago;
    }

    public Pedido() {
    }

    // Getters y Setters
    public int getIdUsuario() {
        return id_usuario;
    }

    public void setIdUsuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }

    public List<FormaDePago> getFormaDePago() {
        return forma_de_pago;
    }

    public void setFormaDePago(List<FormaDePago> forma_de_pago) {
        this.forma_de_pago = forma_de_pago;
    }

    // Clase interna para Detalle del pedido
    public static class Detalle {
        private int id_producto;
        private int id_talle;
        private int cantidad;
        private double subtotal;

        public Detalle(int id_producto, int id_talle, int cantidad, double subtotal) {
            this.id_producto = id_producto;
            this.id_talle = id_talle;
            this.cantidad = cantidad;
            this.subtotal = subtotal;
        }

        public Detalle() {
        }

        // Getters y Setters
        public int getIdProducto() {
            return id_producto;
        }

        public void setIdProducto(int id_producto) {
            this.id_producto = id_producto;
        }

        public int getIdTalle() {
            return id_talle;
        }

        public void setIdTalle(int id_talle) {
            this.id_talle = id_talle;
        }

        public int getCantidad() {
            return cantidad;
        }

        public void setCantidad(int cantidad) {
            this.cantidad = cantidad;
        }

        public double getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }
    }

    // Clase interna para la Forma de Pago
    public static class FormaDePago {
        private Integer id_forma_de_pago;
        private Integer id_tarjeta;

        public FormaDePago(int id_forma_de_pago, Integer id_tarjeta) {
            this.id_forma_de_pago = id_forma_de_pago;
            this.id_tarjeta = id_tarjeta;
        }

        public FormaDePago() {
        }

        // Getters y Setters
        public Integer getIdFormaDePago() {
            return id_forma_de_pago;
        }

        public void setIdFormaDePago(Integer id_forma_de_pago) {
            this.id_forma_de_pago = id_forma_de_pago;
        }

        public int getIdTarjeta() {
            return id_tarjeta;
        }

        public void setIdTarjeta(Integer id_tarjeta) {
            this.id_tarjeta = id_tarjeta;
        }
    }
}