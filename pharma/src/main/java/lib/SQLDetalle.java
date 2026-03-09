package lib;

import com.mycompany.pharma.model.DetalleV;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para acceder y manipular toda la informacion almacenada en la BD con
 * respecto a los detalles de cada venta.
 */
public class SQLDetalle {

    private Connection connection;
    
    /**
     * Constructor de la clase.
     * @param connection Coneccion a la BD.
     */
    public SQLDetalle(Connection connection) {
        this.connection = connection;
    }

    public void insertarDetalle(int idVenta, DetalleV detalle) throws SQLException {

        String query = "{ CALL AgregarDetalleVenta(?,?,?,?) }";

        PreparedStatement stmt = connection.prepareCall(query);

        stmt.setInt(1, idVenta);
        stmt.setInt(2, detalle.getIdProducto());
        stmt.setInt(3, detalle.getCantidad());
        stmt.setDouble(4, detalle.getSubtotal());

        stmt.execute();
        stmt.close();
    }
    
    
    /**
     * Obtiene un Arraylist con todos los detalles de una venta.
     *
     * @return la lista de detalles de una venta.
     * @throws Exception Si ocurre un error al ejecutar la consulta.
     */
    public ArrayList<DetalleV> cargarDetalles(int id_Venta) throws Exception {
        ArrayList<DetalleV> detalles = new ArrayList<>();
        String query = "{ CALL ObtenerDetalles(?) }";

        try (PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setInt(1, id_Venta);
            
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                int id_venta_aux = rs.getInt("id_venta_d");
                int id_medicamento_aux = rs.getInt("id_medicamento_d");
                int cantidad_aux = rs.getInt("cantidad_d");
                double subtotal_aux = rs.getDouble("subtotal_d");   

                DetalleV detalle = new DetalleV(id_venta_aux, id_medicamento_aux, cantidad_aux, subtotal_aux);
                detalles.add(detalle);
            }
            }
            

        } catch (SQLException e) {
            throw new Exception("Error al cargar los detalles: " + e.getMessage());
        }

        return detalles;
    }
}
