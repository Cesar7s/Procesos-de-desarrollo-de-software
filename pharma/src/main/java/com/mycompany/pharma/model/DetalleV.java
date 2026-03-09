package com.mycompany.pharma.model;


/**
 * Clase que representa el detalle de una venta.
 * Contiene la información del producto vendido,
 * la cantidad y el subtotal generado.
 */
public class DetalleV {

    /** Identificador de la venta */
    private int idVenta;

    /** Identificador del producto */
    private int idProducto;

    /** Cantidad de productos vendidos */
    private int cantidad;

    /** Subtotal correspondiente al detalle */
    private double subtotal;

    /**
     * Constructor vacío.
     */
    public DetalleV() {
    }

    /**
     * Constructor con todos los atributos.
     * 
     * @param idVenta     Identificador de la venta
     * @param idProducto  Identificador del producto
     * @param cantidad    Cantidad vendida
     * @param subtotal    Subtotal del detalle
     */
    public DetalleV(int idVenta, int idProducto, int cantidad, double subtotal) {
        this.idVenta = idVenta;
        this.idProducto = idProducto;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    /**
     * Obtiene el id de la venta.
     * 
     * @return id de la venta
     */
    public int getIdVenta() {
        return idVenta;
    }

    /**
     * Establece el id de la venta.
     * 
     * @param idVenta nuevo id de la venta
     */
    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    /**
     * Obtiene el id del producto.
     * 
     * @return id del producto
     */
    public int getIdProducto() {
        return idProducto;
    }

    /**
     * Establece el id del producto.
     * 
     * @param idProducto nuevo id del producto
     */
    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    /**
     * Obtiene la cantidad vendida.
     * 
     * @return cantidad
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad vendida.
     * 
     * @param cantidad nueva cantidad
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el subtotal.
     * 
     * @return subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal.
     * 
     * @param subtotal nuevo subtotal
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Retorna una representación en cadena del detalle de venta.
     *
     * @return String con información del detalle de venta
     */
    @Override
    public String toString() {
        return "DetalleV{" + "idVenta=" + idVenta + ", idProducto=" + idProducto + ", cantidad=" + cantidad + ", subtotal=" + subtotal + '}';
    }
    
    
}
