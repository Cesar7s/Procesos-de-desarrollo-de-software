package com.mycompany.pharma.controllers;

import com.mycompany.pharma.model.Usuario;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
 * FXML Controller class.
 * Clase para manejar la administracion de usuarios.
 */
public class VentanaUsuariosController {
    
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

    /**
     * Inicializa el controlador.
     * Aqui se inicializan las propiedades de la tabla 
     * para una mejor manipulacion.
     *
     */
    @FXML
    public void initialize() {
        ColumnaID.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColumnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ColumnaContrasena.setCellValueFactory(new PropertyValueFactory<>("contrasena"));
        ColumnaROl.setCellValueFactory(new PropertyValueFactory<>("rol"));
        
        this.dbUser = new SQLUser();
        
        this.cargarDatos();
    }

    /**
     * Metodo que se activa cuando se hace click en el boton de eliminar.
     */
    @FXML
    private void eliminarUsuario() {
        Usuario selectedItem = TablaUsuarios.getSelectionModel().getSelectedItem();
        //En la lista de usuarios que se elimine aquel cuya id corresponda con la ID de selectedItem
        try {
            if (selectedItem != null) {
                Integer id = selectedItem.getId();
                //Eliminar en la BD
                if (this.dbUser.removeUser(id)) {//Si se elimino correctamente
                    //Actualizar datos en la tabla desde la BD
                    this.cargarDatos();
                    this.mostrarAlerta("ELIMINADO CORRECTAMENTE");
                } else {//Si no se elimino correctamente
                    this.mostrarAlerta("No se pudo eliminar");
                }

            } else {//Si no hay elemento seleccionado en la tabla
                this.mostrarAlerta("Selecciona algun elemento en la Tabla");
            }
        } catch (Exception e) {//Si ocurre alguna excepcion.
            mostrarAlerta("Error" + e.getMessage());
        }

    }

    /**
     * Metodo que se activa cuando se hace click en el boton de ingresar.
     */
    @FXML
    private void ingresarUsuario() {
        //Valida los campos de texto no esten vacios
        if (!entradaNombre.getText().isBlank()
                && !entradaContrasena.getText().isBlank()
                && !entradaRol.getText().isBlank()) {
            try {
                //Almacena los datos en variables
                String nombre = entradaNombre.getText().trim();
                String contrasena = entradaContrasena.getText().trim();
                String rol = entradaRol.getText().trim();
                //Verifica un rol valido
                if (rol.equals("admin") || rol.equals("user")) {
                    if (this.dbUser.createUser(rol, nombre, contrasena)) {//Lo registra en la BD
                        //Si se agrego correctamente
                        this.mostrarAlerta("Creado Exitosamente");
                        
                        //Actualizar datos en la tabla desde la BD
                        this.cargarDatos();
                        //Limpia los campos de ingreso
                        entradaNombre.clear();
                        entradaContrasena.clear();
                        entradaRol.clear();
                    } else {//Si no se creo correctamente
                        this.mostrarAlerta("ERROR de creacion");
                    }

                } else {//Si no se ingresa un rol valido
                    this.mostrarAlerta("Ingresa un Rol Válido (admin/user)");
                    //Si quieres esto hazlo un mensaje popup
                }
            } catch (Exception e) {//Si ocurre alguna excepcion
                this.mostrarAlerta("ERROR-- " + e.getMessage());
                //Si quieres esto hazlo un mensaje popup
            }
        } else {//Si algun campo esta vacio
            this.mostrarAlerta("Los campos no pueden estar vacíos");
        }
    }

    //Se pone en los campos de Nombre, Contraseña y Rol las cosas a modificar,
    //Se pueden dejar campos vacíos, luego se selecciona directamente en la tabla
    //El usuario a modificar y se presiona el botón
    @FXML
    private void modificarUsuario() {
        //Se obtiene el elemeto seleccionado
        Usuario selectedItem = TablaUsuarios.getSelectionModel().getSelectedItem();

        //Si no hay elemento seleccionado en la tabla
        if (selectedItem == null) {
            mostrarAlerta("Selecciona algún elemento en la tabla");
            return;
        }
        //Obtiene el id del elemento a modificar
        Integer id = selectedItem.getId();

        try {
            //Verifica si hay algun valor en el campo nombre
            if (!entradaNombre.getText().isBlank()) {//Actualiza el nombre con el valor en la BD
                if (dbUser.setUsername(id, entradaNombre.getText().trim())) {
                    //Si se actualizo correctamente
                    mostrarAlerta("NOMBRE ACTUALIZADO");
                }
            }

            //Verifica si hay algun valor en el contrasena
            if (!entradaContrasena.getText().isBlank()) {//Actualiza la contrasena con el valor en la BD
                if (dbUser.setUserPassword(id, entradaContrasena.getText().trim())) {
                    mostrarAlerta("CONTRASEÑA ACTUALIZADA");
                }
            }

            //Verifica si hay algun valor en el campo rol
            if (!entradaRol.getText().isBlank()) {

                //Convierte a minusculas
                String rol = entradaRol.getText().trim().toLowerCase();

                //Verifica el rol sea valido
                if (rol.equals("admin") || rol.equals("user")) {

                    if (dbUser.setRol(id, rol)) { //Actualiza el rol con el valor en la BD
                        mostrarAlerta("ROL ACTUALIZADO");
                    }

                } else {//Si no hay un rol valido
                    mostrarAlerta("Escriba un rol válido (admin/user)");
                    return;
                }
            }

            //Actualizar datos en la tabla desde la BD
            this.cargarDatos();

            //Limpia los campos de ingreso.
            entradaNombre.clear();
            entradaContrasena.clear();
            entradaRol.clear();

        } catch (Exception e) {
            mostrarAlerta("ERROR: " + e.getMessage());
        }
    }

    /**
     * Permite regresar a la ventana anterior a esta.
     * @throws IOException Si ocurre un erro en la lectura de los fxml.
     */
    @FXML
    private void salir(ActionEvent event) throws IOException {
        // Nombre del FXML de la ventana anterior
        String fxml = "admin";

        // Cargar el archivo FXML desde recursos
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/scenes/" + fxml + ".fxml")
        );
        Parent root = loader.load();

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

    /**
     * Carga los usuarios desde la BD a la tabla.
     */
    private void cargarDatos() {
        try {
            // 1. Obtener los usuarios desde la BD
            List<Usuario> usuarios = this.dbUser.obtenerUsuarios();

            // 3. Crear un ObservableList y asignarlo a la tabla
            TablaUsuarios.setItems(FXCollections.observableArrayList(usuarios));

        } catch (Exception e) {//Si ocurre alguna excepcion.
            this.mostrarAlerta("ERROR Carga de datos: " + e.getMessage());
        }
    }

}
