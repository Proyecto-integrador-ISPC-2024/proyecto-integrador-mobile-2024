package com.example.tiendadecampeones.models;

import java.util.List;

public class PaymentMethods {
    private List<FormaDePago> formas_de_pago;
    private List<Tarjeta> tarjetas;


    public PaymentMethods(List<FormaDePago> formas_de_pago, List<Tarjeta> tarjetas) {
        this.formas_de_pago = formas_de_pago;
        this.tarjetas = tarjetas;
    }

    public PaymentMethods() {
    }

    // Getters y Setters
    public List<FormaDePago> getFormasDePago() {
        return formas_de_pago;
    }

    public void setFormasDePago(List<FormaDePago> formas_de_pago) {
        this.formas_de_pago = formas_de_pago;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    // Clase FormaDePago
    public static class FormaDePago {
        private int id_forma_de_pago;
        private String descripcion;

        // Getters y Setters
        public int getIdFormaDePago() {
            return id_forma_de_pago;
        }

        public void setIdFormaDePago(int id_forma_de_pago) {
            this.id_forma_de_pago = id_forma_de_pago;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }

    // Clase Tarjeta
    public static class Tarjeta {
        private int id_tarjeta;
        private String nombre_tarjeta;

        // Getters y Setters
        public int getIdTarjeta() {
            return id_tarjeta;
        }

        public void setIdTarjeta(int id_tarjeta) {
            this.id_tarjeta = id_tarjeta;
        }

        public String getNombreTarjeta() {
            return nombre_tarjeta;
        }

        public void setNombreTarjeta(String nombre_tarjeta) {
            this.nombre_tarjeta = nombre_tarjeta;
        }
    }
}
