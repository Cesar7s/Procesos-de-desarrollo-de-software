package lib;

import com.mycompany.pharma.model.DetalleVenta;
import com.mycompany.pharma.model.Venta;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class SQLVenta extends BaseSQL {

    // AGREGAR VENTA
    public boolean agregarVenta(int productoId, int cantidad) throws Exception {

        String query = "{ CALL AgregarVenta(?, ?) }";

        try (PreparedStatement stmt = connection.prepareCall(query)) {

            stmt.setInt(1, productoId);
            stmt.setInt(2, cantidad);
            stmt.execute();

            return true;

        } catch (SQLException e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    // CANCELAR VENTA
    public boolean cancelarVenta(int idVenta) throws Exception {

        String query = "{ CALL CancelarVenta(?) }";

        try (PreparedStatement stmt = connection.prepareCall(query)) {

            stmt.setInt(1, idVenta);
            stmt.execute();

            return true;

        } catch (SQLException e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    // MÉTODO PARA CARGAR TODAS LAS VENTAS
    public ArrayList<Venta> cargarVentas() throws Exception {
        ArrayList<Venta> ventas = new ArrayList<>();
        String query = "SELECT id_venta, total, fecha, estado FROM venta";

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_venta");
                double total = rs.getDouble("total");
                Date fechaBD = rs.getDate("fecha"); // java.sql.Date
                boolean estadoBD = rs.getBoolean("estado"); // true o false desde la BD

                LocalDate fecha = fechaBD.toLocalDate(); // convierte a LocalDate
                String estado = estadoBD ? "Activa" : "Cancelada";

                Venta venta = new Venta(id, total, fecha, estado);
                ventas.add(venta);
            }

        } catch (SQLException e) {
            throw new Exception("Error al cargar ventas: " + e.getMessage());
        }

        return ventas;
    }
    
    public ArrayList<DetalleVenta> cargarDetalles() throws Exception {
        ArrayList<DetalleVenta> detalles = new ArrayList<>();
        String query = "SELECT id_medicamento, cantidad,subtotal FROM detalle_venta";

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("id_medicamento");
                int cantidad = rs.getInt("cantidad");

                double subtotal = rs.getDouble("subtotal");

                DetalleVenta detalle = new DetalleVenta(id, cantidad,subtotal);
                detalles.add(detalle);
            
            }
        } catch (SQLException e) {
            throw new Exception("Error al cargar detalles: " + e.getMessage());
        }

        return detalles;
    }

}
