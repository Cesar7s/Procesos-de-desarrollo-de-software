
package com.mycompany.pharma.model;


/**
 * Representa un medicamento con su información básica.
 * Incluye identificador, nombre, compuesto activo, precio y cantidad disponible.
 * 
 * @author Angel
 */
public class Medicamento {

    /** Identificador único del medicamento */
    private int id;

    /** Nombre del medicamento */
    private String nombre;

    /** Compuesto activo del medicamento */
    private String compuesto;

    /** Precio del medicamento */
    private double precio;

    /** Cantidad disponible del medicamento */
    private int cantidad;

    /**
     * Constructor vacío de la clase Medicamento.
     */
    public Medicamento() {
    }

    /**
     * Constructor con todos los atributos del medicamento.
     *
     * @param id Identificador único
     * @param nombre Nombre del medicamento
     * @param compuesto Compuesto activo
     * @param precio Precio del medicamento
     * @param cantidad Cantidad disponible
     */
    public Medicamento(int id, String nombre, String compuesto, double precio, int cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.compuesto = compuesto;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el identificador del medicamento.
     *
     * @return id del medicamento
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador del medicamento.
     *
     * @param id Identificador a asignar
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del medicamento.
     *
     * @return nombre del medicamento
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del medicamento.
     *
     * @param nombre Nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el compuesto activo del medicamento.
     *
     * @return compuesto activo
     */
    public String getCompuesto() {
        return compuesto;
    }

    /**
     * Establece el compuesto activo del medicamento.
     *
     * @param compuesto Compuesto activo a asignar
     */
    public void setCompuesto(String compuesto) {
        this.compuesto = compuesto;
    }

    /**
     * Obtiene el precio del medicamento.
     *
     * @return precio del medicamento
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio del medicamento.
     *
     * @param precio Precio a asignar
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene la cantidad disponible del medicamento.
     *
     * @return cantidad disponible
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad disponible del medicamento.
     *
     * @param cantidad Cantidad a asignar
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Retorna una representación en cadena del medicamento.
     *
     * @return String con información del medicamento
     */
    @Override
    public String toString() {
        return "Medicamento{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", compuesto='" + compuesto + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                '}';
    }
}
