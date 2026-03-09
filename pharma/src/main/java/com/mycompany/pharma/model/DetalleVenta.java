/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pharma.model;

/**
 *
 * @author donts
 */
public class DetalleVenta {
    /** Identificador del producto vendido */
    private int productoId;

    /** Cantidad de unidades vendidas */
    private int cantidad;

    /** Precio por unidad del producto */
    private double subtotal;

    public int getProductoId() {
        return productoId;
    }

    public void setProductoId(int productoId) {
        this.productoId = productoId;
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

    public DetalleVenta(int productoId, int cantidad, double subtotal) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    @Override
    public String toString() {
        return "DetalleVenta{" + "productoId=" + productoId + ", cantidad=" + cantidad + ", subtotal=" + subtotal + '}';
    }

}
