package lib;

import com.mycompany.pharma.model.Medicamento;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLMedicamento extends BaseSQL {

    public SQLMedicamento() {
    }
    
    // AGREGAR
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

    // ELIMINAR (BORRADO LÓGICO)
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
    
    // MÉTODO PARA CARGAR TODOS LOS MEDICAMENTOS
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

    // EDITAR NOMBRE
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

    // EDITAR COMPUESTO
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

    // EDITAR PRECIO
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

    // EDITAR CANTIDAD
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

//Agregar
//Eliminar
//Editar nombre 
//Editar compuesto
//Editar precio
//Editar cantidad
