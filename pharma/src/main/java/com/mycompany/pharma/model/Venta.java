package com.mycompany.pharma.model;

import java.time.LocalDate;

/**
 * Representa una venta de un producto.
 * Incluye información sobre el producto vendido, cantidad, precio, total, fecha y estado de la venta.
 * 
 */
public class Venta {

    /** Identificador único de la venta */
    private int id;

    /** Total de la venta (cantidad * precioUnidad) */
    private double total;

    /** Fecha de la venta */
    private LocalDate fecha;

    /** Estado de la venta (ej. "Pendiente", "Completada", "Cancelada") */
    private String estado;

    /**
     * Constructor vacío de la clase Venta.
     */
    public Venta() {
    }

    /**
     * Constructor con todos los atributos de la venta.
     *
     * @param id Identificador único
     * @param total Total de la venta
     * @param fecha Fecha de la venta
     * @param estado Estado de la venta
     */
    public Venta(int id, double total, LocalDate fecha, String estado) {
        this.id = id;
        this.total = total;
        this.fecha = fecha;
        this.estado = estado;
    }

    /** Obtiene el id de la venta */
    public int getId() {
        return id;
    }

    /** Establece el id de la venta */
    public void setId(int id) {
        this.id = id;
    }

    /** Obtiene el total de la venta */
    public double getTotal() {
        return total;
    }

    /** Establece el total de la venta */
    public void setTotal(double total) {
        this.total = total;
    }

    /** Obtiene la fecha de la venta */
    public LocalDate getFecha() {
        return fecha;
    }

    /** Establece la fecha de la venta */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /** Obtiene el estado de la venta */
    public String getEstado() {
        return estado;
    }

    /** Establece el estado de la venta */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Retorna una representación en cadena de la venta */
    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", total=" + total +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                '}';
    }
}

