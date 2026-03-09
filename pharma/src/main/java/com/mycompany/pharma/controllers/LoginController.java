package com.mycompany.pharma.controllers;

import com.mycompany.pharma.model.Session;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lib.SQLUser;

/**
 *
 * @author amiss
 */
public class LoginController {
    @FXML
    private TextField txtUsuario; // Campo de entrada para el nombre de usuario

    @FXML
    private PasswordField txtContrasena; // Campo de entrada para la contraseña

    @FXML
    private Button btnIngresar; // Botón para iniciar sesión

    private SQLUser dbUsuario; // Conexión a la base de datos

    
    /**
     * Inicializa el controlador.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos.
     */
    @FXML
    private void initialize() throws SQLException {
        dbUsuario = new SQLUser();
    }
    
    
    /**
     * Maneja el inicio de sesión validando las credenciales ingresadas.
     *
     * @return Una cadena con el rol del usuario ("admin", "user") o "nil" si
     * las credenciales son inválidas.
     *
     * @throws SQLException Si ocurre un error en la consulta a la base de
     * datos.
     */
    @FXML
    private String handleLogin() throws SQLException {
        String username = txtUsuario.getText().trim();
        String password = txtContrasena.getText().trim();

        if (username.equals("dAdmin") || username.equals("dUser")) {
            if (username.equals("dAdmin")) {
                System.out.println(username);
                return "admin";
            } else {
                return "user";
            }
        } else {
            if (dbUsuario.isValidCredentials(username, password)) {
                return dbUsuario.getRole(username);
            } else {
                return "nil";
            }
        }
    }

    /**
     * Verifica si los campos de nombre de usuario y contraseña están llenos.
     *
     * @return {@code true} si ambos campos contienen texto, {@code false} en
     * caso contrario.
     */
    private boolean validarCampos() {
        return !txtUsuario.getText().trim().isEmpty() && !txtContrasena.getText().trim().isEmpty();
    }

    /**
     * Muestra una alerta de advertencia con un mensaje específico.
     *
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Campos incompletos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage stage = (Stage) txtUsuario.getScene().getWindow();
        alert.initOwner(stage); // Establecer como ventana padre
        alert.showAndWait();
    }

    /**
     * Cambia la escena a la vista correspondiente según el rol del usuario.
     *
     * @throws IOException Si ocurre un error al cargar el archivo FXML.
     * @throws SQLException Si ocurre un error al validar las credenciales.
     */
    @FXML
    private void switchToSecondary() throws IOException, SQLException {
        if (!validarCampos()) {
            mostrarAlerta("Todos los campos deben estar llenos.");
            return;
        }

        String fxml = handleLogin();
        if (!fxml.equals("admin") && !fxml.equals("user")) {
            return;
        }

        Session.setNombre(txtUsuario.getText().trim());
        Session.setRol(fxml);
        
        File fxmlFile = new File("src/main/resources/scenes/" + fxml + ".fxml");
        FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
        Parent root = loader.load();
        Stage stage = (Stage) txtUsuario.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        
        
        
    }
    
}
