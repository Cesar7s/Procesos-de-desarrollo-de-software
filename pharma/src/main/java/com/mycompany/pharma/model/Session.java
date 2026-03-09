package com.mycompany.pharma.model;

import java.sql.Connection;

/**
 * Clase estatica usada para guardar valores que deben mostrarse durante la
 * ejecucion del programa.
 *
 * No se requiere un contructor ya que se usan valores estaticos.
 */
public class Session {

    /**
     * Rol de quien accede al sistema.
     */
    private static String rol; // "admin" o "user"

    /**
     * Nombre del usuario que accede al sistema.
     */
    private static String nombre;

    /**
     * Id de la venta de la cual se quiere mostrar la informacion.
     */
    private static int id_Venta;

    /**
     * Conexion activa a la base de datos usada durante la ejecucion del
     * programa.
     */
    private static Connection connection;

    /**
     * Obtiene el rol almacenado del usuario que ingreso.
     *
     * @return El rol del usuario que ingreso.
     */
    public static String getRol() {
        return rol;
    }

    /**
     * Obtiene el nombre almacenado del usuario que ingreso.
     *
     * @return El nombre del usuario que ingreso.
     */
    public static String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el Id de la venta de la cual se quiere mostrar la informacion.
     *
     * @return el Id de la venta de la cual se quiere mostrar la informacion.
     */
    public static int getId_Venta() {
        return id_Venta;
    }

    /**
     * Obtiene la conexion activa a la base de datos.
     *
     * @return La conexion usada durante la ejecucion del programa.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Cambia el rol almacenado del usuario que ingreso.
     *
     * @param r El nuevo rol del usuario.
     */
    public static void setRol(String r) {
        rol = r;
    }

    /**
     * Cambia el nombre almacenado del usuario que ingreso.
     *
     * @param nombre El nuevo nombre del usuario.
     */
    public static void setNombre(String nombre) {
        Session.nombre = nombre;
    }

    /**
     * Cambia el Id de la venta de la cual se quiere mostrar la informacion.
     *
     * @param id_Venta El nuevo id de la venta.
     */
    public static void setId_Venta(int id_Venta) {
        Session.id_Venta = id_Venta;
    }

    /**
     * Cambia la conexion activa a la base de datos.
     *
     * @param connection La nueva conexion usada durante la ejecucion del
     * programa.
     */
    public static void setConnection(Connection connection) {
        Session.connection = connection;
    }

}
