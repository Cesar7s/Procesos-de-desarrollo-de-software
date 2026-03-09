package com.mycompany.pharma.controllers;

import com.mycompany.pharma.model.Usuario;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lib.SQLUser;

/**
 * FXML Controller class
 *
 * @author aburt
 */
public class VentanaUsuariosController implements Initializable {
    
    // TABLA
    @FXML
    private TableView<Usuario> TablaUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> ColumnaID;
    @FXML
    private TableColumn<Usuario, String> ColumnaNombre;
    @FXML
    private TableColumn<Usuario, String> ColumnaContrasena;
    @FXML
    private TableColumn<Usuario, String> ColumnaROl;
    
    // BOTONES
    @FXML
    private Button botonIngresar;
    @FXML
    private Button botonModificar;
    @FXML
    private Button botonEliminar;
    @FXML
    private Button botonSalir;
   
    // CAMPOS
    @FXML
    private TextField entradaNombre;
    @FXML
    private TextField entradaContrasena;
    @FXML
    private TextField entradaRol;

    private SQLUser dbUser;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ColumnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColumnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColumnaContrasena.setCellValueFactory(new PropertyValueFactory<>("contrasena"));
        ColumnaROl.setCellValueFactory(new PropertyValueFactory<>("rol"));
        
        this.dbUser = new SQLUser();
        
        this.cargarDatos();
    }

    //Primero se selecciona en la tabla el coso a eliminar y luego se presiona el Botón
    @FXML
    private void eliminarUsuario(ActionEvent event) {
        Usuario selectedItem = TablaUsuarios.getSelectionModel().getSelectedItem();
        //En la lista de usuarios que se elimine aquel cuya id corresponda con la ID de selectedItem
        try {
            if (selectedItem != null) {
                Integer id = selectedItem.getId();
                if (this.dbUser.removeUser(id)) {
                    
                    this.cargarDatos();
                    this.mostrarAlerta("ELIMINADO CORRECTAMENTE");
                } else {
                    this.mostrarAlerta("No se pudo eliminar");
                }

            } else {
                this.mostrarAlerta("Selecciona algun elemento en la Tabla");
            }
        } catch (Exception e) {
            mostrarAlerta("Error" + e.getMessage());
        }

    }

    @FXML
    private void ingresarUsuario() {
        if (!entradaNombre.getText().isBlank()
                && !entradaContrasena.getText().isBlank()
                && !entradaRol.getText().isBlank()) {
            try {
                String nombre = entradaNombre.getText().trim();
                String contrasena = entradaContrasena.getText().trim();
                String rol = entradaRol.getText().trim();

                if (rol.equals("admin") || rol.equals("user")) {
                    if (this.dbUser.createUser(rol, nombre, contrasena)) {
                        this.mostrarAlerta("Creado Exitosamente");

                        this.cargarDatos();
                        
                        entradaNombre.clear();
                        entradaContrasena.clear();
                        entradaRol.clear();
                    } else {
                        this.mostrarAlerta("ERROR de creacion");
                    }

                } else {
                    this.mostrarAlerta("Ingresa un Rol Válido (admin/user)");
                    //Si quieres esto hazlo un mensaje popup
                }
            } catch (Exception e) {
                this.mostrarAlerta("ERROR-- " + e.getMessage());
                //Si quieres esto hazlo un mensaje popup
            }
        } else {
            this.mostrarAlerta("Los campos no pueden estar vacíos");
        }
    }

    //Se pone en los campos de Nombre, Contraseña y Rol las cosas a modificar,
    //Se pueden dejar campos vacíos, luego se selecciona directamente en la tabla
    //El usuario a modificar y se presiona el botón
    @FXML
    private void modificarUsuario() {

        Usuario selectedItem = TablaUsuarios.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            mostrarAlerta("Selecciona algún elemento en la tabla");
            return;
        }

        Integer id = selectedItem.getId();

        try {

            if (!entradaNombre.getText().isBlank()) {
                if (dbUser.setUsername(id, entradaNombre.getText().trim())) {
                    mostrarAlerta("NOMBRE ACTUALIZADO");
                }
            }

            if (!entradaContrasena.getText().isBlank()) {
                if (dbUser.setUserPassword(id, entradaContrasena.getText().trim())) {
                    mostrarAlerta("CONTRASEÑA ACTUALIZADA");
                }
            }

            if (!entradaRol.getText().isBlank()) {

                String rol = entradaRol.getText().trim().toLowerCase();

                if (rol.equals("admin") || rol.equals("user")) {

                    if (dbUser.setRol(id, rol)) {
                        mostrarAlerta("ROL ACTUALIZADO");
                    }

                } else {
                    mostrarAlerta("Escriba un rol válido (admin/user)");
                    return;
                }
            }

            cargarDatos();

            entradaNombre.clear();
            entradaContrasena.clear();
            entradaRol.clear();

        } catch (Exception e) {
            mostrarAlerta("ERROR: " + e.getMessage());
        }
    }

    @FXML
    private void salir(ActionEvent event) throws IOException {
        // Nombre del FXML de la ventana anterior
        String fxml = "admin";

        // Cargar el archivo FXML desde recursos
        File fxmlFile = new File("src/main/resources/scenes/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlFile.toURI().toURL());

        // Obtener la ventana actual
        Stage stage = (Stage) botonSalir.getScene().getWindow();

        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Muestra una alerta de advertencia con un mensaje específico.
     *
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("AVISO");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage stage = (Stage) this.entradaNombre.getScene().getWindow();
        alert.initOwner(stage); // Establecer como ventana padre
        alert.showAndWait();
    }

    private void cargarDatos() {
        try {
            // 1. Obtener los usuarios desde la BD
            List<Usuario> usuarios = this.dbUser.obtenerUsuarios();

            // 3. Crear un ObservableList y asignarlo a la tabla
            TablaUsuarios.setItems(FXCollections.observableArrayList(usuarios));

        } catch (Exception e) {
            this.mostrarAlerta("ERROR Carga de datos: " + e.getMessage());
        }
    }
}
