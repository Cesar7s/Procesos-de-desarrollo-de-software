package com.mycompany.pharma.model;

/**
 * La clase Usuario representa una entidad que contiene información sobre un usuario.
 * Esta clase incluye atributos como el identificador, nombre, contraseña y rol del usuario.
 * 
 */
public class Usuario {
    private int id;
    private String nombre;
    private String contrasena;
    private String rol;

    /**
     * Constructor de la clase Usuario.
     * 
     * @param id Identificador único del usuario.
     * @param nombre Nombre del usuario.
     * @param contrasena Contraseña del usuario.
     * @param rol Rol o tipo de usuario.
     */
    public Usuario(int id, String nombre, String contrasena, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    // Getters

    /**
     * Obtiene el identificador del usuario.
     * @return El identificador del usuario.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el nombre del usuario.
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene la contrasena del usuario.
     * @return La contrasena del usuario.
     */
    public String getContrasena() {
        return contrasena;
    }

    /**
     * Obtiene el rol del usuario.
     * @return El rol del usuario.
     */
    public String getRol() {
        return rol;
    }

    // Setters (opcional, dependiendo de tu lógica)

    /**
     * Establece el identificador del usuario.
     * @param id El nuevo identificador del usuario.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Establece el nombre del usuario.
     * @param nombre El nuevo nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Establece la contrasena del usuario.
     * @param contrasena La nueva contrasena del usuario.
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Establece el rol del usuario.
     * @param rol El nuevo rol del usuario.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}