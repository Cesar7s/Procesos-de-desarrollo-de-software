package lib;

import com.mycompany.pharma.model.Usuario;
import com.mycompany.pharma.model.Venta;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
public class SQLUser extends BaseSQL {

    public SQLUser() {
    }
    
    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param role El rol del usuario.
     * @param username El nombre de usuario.
     * @param password La contraseña del usuario.
     * @return true si el usuario se creó correctamente, false en caso
     * contrario.
     */
    public boolean createUser(String role, String username, String password) throws Exception {
        String query = "{ CALL AgregarUsuario(?, ?, ?) }";
        try (PreparedStatement statement = this.connection.prepareCall(query)) {
            statement.setString(1, role);
            statement.setString(2, username);
            statement.setString(3, password);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }
    
    
    /**
     * Verifica si las credenciales proporcionadas son válidas.
     *
     * @param username El nombre de usuario a verificar.
     * @param password La contraseña a verificar.
     * @return true si las credenciales son válidas, false en caso contrario.
     * @throws SQLException Si ocurre un error al ejecutar la consulta.
     */
    public boolean isValidCredentials(String username, String password) throws SQLException {
        String query = "SELECT contrasena FROM usuario WHERE nombre = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String contra = resultSet.getString("contrasena");
                if (contra.equals(password)) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }
    
    /**
     * Elimina un usuario de la base de datos.
     *
     * @param id El ID del usuario a eliminar.
     * @return true si el usuario se eliminó correctamente, false en caso
     * contrario.
     */
    public boolean removeUser(int id) throws Exception {
        String query = "{ CALL EliminarUsuario(?) }";

        try (PreparedStatement statement = connection.prepareCall(query)) {
            statement.setInt(1, id);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Cambia el nombre de usuario de un usuario existente.
     *
     * @param id El ID del usuario.
     * @param username El nuevo nombre de usuario.
     * @return true si el nombre de usuario se cambió correctamente, false en
     * caso contrario.
     */
    public boolean setUsername(int id, String username) throws Exception {
        String query = "{ CALL CambiarNombreUsuario(?, ?) }";

        try (PreparedStatement statement = connection.prepareCall(query)) {
            statement.setInt(1, id);
            statement.setString(2, username);
            statement.execute();

            return true;
        } catch (SQLException e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Cambia la contraseña de un usuario existente.
     *
     * @param id El ID del usuario.
     * @param password La nueva contraseña.
     * @return true si la contraseña se cambió correctamente, false en caso
     * contrario.
     */
    public boolean setUserPassword(int id, String password) throws Exception {
        String query = "{ CALL CambiarContrasenaUsuario(?, ?) }";
        try (PreparedStatement statement = connection.prepareCall(query)) {
            statement.setInt(1, id);
            statement.setString(2, password);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene el rol de un usuario.
     *
     * @param username El nombre de usuario.
     * @return El rol del usuario, o "nar" si no se encuentra el usuario.
     * @throws SQLException Si ocurre un error al ejecutar la consulta.
     */
    public String getRole(String username) throws SQLException {
        String sql = "SELECT rol FROM usuario WHERE nombre = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("rol");
                return role;
            } else {
                return "nar";
            }
        }
    }

    /**
     * Cambia el rol de un usuario existente.
     *
     * @param id El ID del usuario.
     * @param rol El nuevo rol.
     * @return true si el rol se cambió correctamente, false en caso
     * contrario.
     */
    public boolean setRol(int id, String rol) throws Exception {
        String query = "{ CALL cambiarRolUsuario(?, ?) }";
        try (PreparedStatement statement = connection.prepareCall(query)) {
            statement.setInt(1, id);
            statement.setString(2, rol);
            statement.execute();
            return true;
        } catch (SQLException e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }


// MÉTODO PARA CARGAR TODOS LOS USUARIOS
    public ArrayList<Usuario> obtenerUsuarios() throws Exception {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT id_usuario, nombre, contrasena, rol FROM usuario WHERE estado = TRUE";

        try (PreparedStatement stmt = connection.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nombre = rs.getString("nombre");
                String contrasena = rs.getString("contrasena");
                String rol = rs.getString("rol");

                Usuario usuario = new Usuario(id, nombre, contrasena, rol);
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            throw new Exception("Error al cargar Usuarios: " + e.getMessage());
        }

        return usuarios;
    }
}

