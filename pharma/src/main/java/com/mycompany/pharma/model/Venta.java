package com.mycompany.pharma.model;

import java.time.LocalDate;

/**
 * Representa una venta de un producto.
 * Incluye información sobre el producto vendido, cantidad, precio, total, fecha y estado de la venta.
 * 
 * @author Angel
 */
public class Venta {

    /** Identificador único de la venta */
    private int id;

    /** Identificador del producto vendido */
    private int productoId;

    /** Cantidad de unidades vendidas */
    private int cantidad;

    /** Precio por unidad del producto */
    private double precioUnidad;

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
     * @param productoId Identificador del producto
     * @param cantidad Cantidad de unidades vendidas
     * @param precioUnidad Precio por unidad
     * @param total Total de la venta
     * @param fecha Fecha de la venta
     * @param estado Estado de la venta
     */
    public Venta(int id, int productoId, int cantidad, double precioUnidad, double total, LocalDate fecha, String estado) {
        this.id = id;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnidad = precioUnidad;
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

    /** Obtiene el id del producto */
    public int getProductoId() {
        return productoId;
    }

    /** Establece el id del producto */
    public void setProductoId(int productoId) {
        this.productoId = productoId;
    }

    /** Obtiene la cantidad vendida */
    public int getCantidad() {
        return cantidad;
    }

    /** Establece la cantidad vendida */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /** Obtiene el precio por unidad */
    public double getPrecioUnidad() {
        return precioUnidad;
    }

    /** Establece el precio por unidad */
    public void setPrecioUnidad(double precioUnidad) {
        this.precioUnidad = precioUnidad;
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
                ", productoId=" + productoId +
                ", cantidad=" + cantidad +
                ", precioUnidad=" + precioUnidad +
                ", total=" + total +
                ", fecha=" + fecha +
                ", estado='" + estado + '\'' +
                '}';
    }
}

