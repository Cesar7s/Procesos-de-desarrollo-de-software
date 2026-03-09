package lib;

import com.mycompany.pharma.model.Medicamento;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase para acceder y manipular toda la informacion alamcenada en la BD 
 * con respecto a los medicamentos.
 */
public class SQLMedicamento extends BaseSQL {

    /**
     * Contructor de la clase.
    */
    public SQLMedicamento() {
    }
    
    /**
     * Permite agregar un medicamento en la BD.
     * 
     * @param nombre Nombre del nuevo medicamento.
     * @param compuesto Compuesto del nuevo medicamento.
     * @param precio Precio del nuevo medicamento.
     * @param cantidad Cantidad en stock del nuevo medicamento.
     * @return true si el medicamento se creó correctamente, false en caso
     * contrario.
     * @throws Exception Si ocurre un error al ejecutar el procedimiento.
     */
    public boolean agregarMedicamento(String nombre, String compuesto, double precio, int cantidad) throws Exception {
        String query = "{ CALL AgregarMedicamento(?, ?, ?, ?) }";

        try (PreparedStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, compuesto);
            stmt.setDouble(3, precio);
            stmt.setInt(4, cantidad);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Permite eliminar un medicamento de la BD.
     * @param id Id del medicamento a eliminar.
     * @return true si el medicamento se elimino correctamente, false en caso
     * contrario.
     * @throws Exception Si ocurre un error al ejecutar el procedimiento.
     */
    public boolean eliminarMedicamento(int id) throws Exception {
        String query = "{ CALL EliminarMedicamento(?) }";

        try (PreparedStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, id);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
    
    /**
     * Obtiene un Arraylist con todos los medicamentos en estado activo.
     * @return la lista de medicamentos.
     * @throws Exception Si ocurre un error al ejecutar la consulta.
     */
    public ArrayList<Medicamento> cargarMedicamentos() throws Exception {
        ArrayList<Medicamento> medicamentos = new ArrayList<>();
        String query = "SELECT id_medicamento, nombre, compuesto, precio, cantidad FROM medicamento WHERE estado = TRUE";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Integer id = rs.getInt("id_medicamento");
                String nombre = rs.getString("nombre");
                String compuesto = rs.getString("compuesto");
                Double precio = rs.getDouble("precio");
                Integer cantidad = rs.getInt("cantidad");
                
                Medicamento medic = new Medicamento(id, nombre, compuesto, precio, cantidad);
                medicamentos.add(medic);
            }

        } catch (SQLException e) {
            throw new Exception("Error al cargar medicamentos: " + e.getMessage());
        }

        return medicamentos;
    }

    /**
     * Permite modificar el nombre un medicamento de la BD.
     * @param id Id del medicamento a modificar.
     * @param nuevoNombre Nuevo nombre del medicamento.
     * @return true si el medicamento se modifico correctamente, false en caso
     * contrario.
     * @throws Exception Si ocurre un error al ejecutar el procedimiento.
     */
    public boolean cambiarNombre(int id, String nuevoNombre) throws Exception {
        String query = "{ CALL CambiarNombreMedicamento(?, ?) }";

        try (PreparedStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, nuevoNombre);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Permite modificar el compuesto un medicamento de la BD.
     * @param id Id del medicamento a modificar.
     * @param nuevoCompuesto Nuevo compuesto del medicamento.
     * @return true si el medicamento se modifico correctamente, false en caso
     * contrario.
     * @throws Exception Si ocurre un error al ejecutar el procedimiento.
     */
    public boolean cambiarCompuesto(int id, String nuevoCompuesto) throws Exception {
        String query = "{ CALL CambiarCompuestoMedicamento(?, ?) }";

        try (PreparedStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, nuevoCompuesto);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Permite modificar el precio de un medicamento de la BD.
     * @param id Id del medicamento a modificar.
     * @param nuevoPrecio Nuevo precio del medicamento.
     * @return true si el medicamento se modifico correctamente, false en caso
     * contrario.
     * @throws Exception Si ocurre un error al ejecutar el procedimiento.
     */
    public boolean cambiarPrecio(int id, double nuevoPrecio) throws Exception {
        String query = "{ CALL CambiarPrecioMedicamento(?, ?) }";

        try (PreparedStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, id);
            stmt.setDouble(2, nuevoPrecio);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * Permite modificar la cantidad/Stock de un medicamento de la BD.
     * @param id Id del medicamento a modificar.
     * @param nuevaCantidad Nueva cantidad/Stock del medicamento.
     * @return true si el medicamento se modifico correctamente, false en caso
     * contrario.
     * @throws Exception Si ocurre un error al ejecutar el procedimiento.
     */
    public boolean cambiarCantidad(int id, int nuevaCantidad) throws Exception {
        String query = "{ CALL CambiarCantidadMedicamento(?, ?) }";

        try (PreparedStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, id);
            stmt.setInt(2, nuevaCantidad);
            stmt.execute();
            return true;

        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }
}

