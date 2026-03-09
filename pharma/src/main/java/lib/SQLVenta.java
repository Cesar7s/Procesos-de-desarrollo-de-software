package lib;

import com.mycompany.pharma.model.DetalleV;
import com.mycompany.pharma.model.Venta;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para aceder y manipular toda la informacion alamcenada en la BD 
 * con respecto a las ventas.
 */
public class SQLVenta extends BaseSQL {
    
    private SQLDetalle dbDetalle;

    public SQLVenta() {
        this.dbDetalle = new SQLDetalle(this.connection);
    }

    /**
     * Permite agregar una venta a la BD junto con sus detalles.
     * Usando commit permite que se realice todo o nada.
     * @param total el total de cada producto que se vende.
     * @param lista lista de productos que se venderan.
     * @return true si la venta se creó correctamente, false en caso
     *  contrario.
     * @throws Exception Si ocurre un error al ejecutar el procedimiento.
     */
    public boolean agregarVenta(double total, List<DetalleV> lista) throws Exception {

        String query = "{ CALL AgregarVenta(?) }";

        try {

            connection.setAutoCommit(false); // Iniciar transacción

            CallableStatement stmt = connection.prepareCall(query);
            stmt.setDouble(1, total);

            ResultSet rs = stmt.executeQuery();

            int id_venta = 0;

            if (rs.next()) {
                id_venta = rs.getInt("id_venta");
            }

            if (id_venta == 0) {
                throw new Exception("No se pudo registrar la venta");
            }

            
            
            for (DetalleV detalle : lista) {
                this.dbDetalle.insertarDetalle(id_venta, detalle);
            }

            connection.commit(); // Confirma todo
            return true;

        } catch (SQLException e) {

            connection.rollback(); // Si algo falla, deshace todo
            throw new Exception("Error: " + e.getMessage());

        } finally {
            connection.setAutoCommit(true);
        }
    }

    /**
     * Permite cancelar una venta.
     *
     * @param idVenta
     * @return true si la venta se cancelo correctamente, false en caso
     * contrario.
     * @throws Exception Si ocurre un error al ejecutar el procedimiento.
     */
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

    /**
     * Obtiene un Arraylist con todas las ventas.
     *
     * @return la lista de ventas.
     * @throws Exception Si ocurre un error al ejecutar la consulta.
     */
    public ArrayList<Venta> cargarVentas() throws Exception {
        ArrayList<Venta> ventas = new ArrayList<>();
        String query = "SELECT id_venta, total, fecha, estado FROM venta";

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_venta");
                double total = rs.getDouble("total");
                Date fechaBD = rs.getDate("fecha"); // java.sql.Date
                boolean estadoBD = rs.getBoolean("estado"); // true o false desde la BD

                //Convierte la informacion obtenida a una version 
                //para una mejor comprension por el usuario.
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

    /**
     * Calcula y devuelve el subtotal desde la BD.
     * @param id Id de producto que se vendera.
     * @param cantidad cantidad de producto que se vendera.
     * @return el subtotal calculado.
     * @throws Exception Si ocurre alguna excepcion.
     */
    public Double obtenerSubtotal(int id, int cantidad) throws Exception {
        Double subtotal = 0.0;
        String query = "SELECT obtenerTotal(?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, id);
            stmt.setInt(2, cantidad);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                subtotal = rs.getDouble(1);
                return subtotal;
            }

        } catch (SQLException e) {
            throw new Exception("Error: " + e.getMessage());
        }

        return subtotal;

    }

}
