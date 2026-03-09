package lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase base que ayuda a establecer conexion a la Base de Datos.
 * 
 * Ayuda a verificar si ya existe una conexion, 
 * para asi no generar una saturacion en el servicio.
 */
public class BaseSQL {

    /**
     * Conexion pertinente a al que podran acceder 
     * las clases que hereden de esta.
     */
    protected Connection connection;

    /**
     * Contructor de la Clase.
     * 
     * Establece la conexion a la BD
     */
    public BaseSQL() {
        connect();
    }

    /**
     * Metodo que establece la conexion a la BD.
     * 
     * Primero verifica si ya existe una conexion establecida.
     * Establece la conexion y maneja la excepcion correspondiente.
     */
    private void connect() {
        if (this.connection == null) {
            try {
                String url = "jdbc:mysql://localhost:3306/farmacia";
                String user = "gerente";
                String password = "1234";

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Conexión unica exitosa");

            } catch (SQLException e) {
                System.out.println("Error de conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene la conexion a la BD.
     * Permite que las clases hijas de esta, 
     * puedan obtener la conexion para hacer uso de la BD.
     * @return La conexion establecida a la BD.
     */
    public Connection getConnection() {
        return connection;
    }
}
