package com.mycompany.pharma.controllers;

import com.mycompany.pharma.model.Medicamento;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lib.SQLMedicamento;
import lib.SQLUser;

public class AlmacenController {
    
    //Conexión a la base de datos
    private SQLMedicamento dbMedicamento;

    // Paneles
    @FXML private StackPane SP1;
    @FXML private AnchorPane PEliminar;
    @FXML private AnchorPane PAgregar;
    @FXML private AnchorPane PModificar;

    // Botones
    @FXML private Button btnCrearMedicamento;
    @FXML private Button btnEliminarMedicamento;
    @FXML private Button btnMNombre;
    @FXML private Button btnMCompuesto;
    @FXML private Button btnMPrecio;
    @FXML private Button btnMCantidad;
    @FXML private Button BAgregar;
    @FXML private Button BEliminar;
    @FXML private Button BEditar;
    @FXML private Button BRegreso;

    // TextFields para agregar
    @FXML private TextField txtNombre;
    @FXML private TextField txtCompuesto;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtCantidad;

    // TextFields para eliminar
    @FXML private TextField txtIdEliminar;

    // TextFields para modificar
    @FXML private TextField txtId;
    @FXML private TextField txtNuevoNombre;
    @FXML private TextField txtNuevoCompuesto;
    @FXML private TextField txtNuevoPrecio;
    @FXML private TextField txtNuevaCantidad;

    // TableView y columnas
    @FXML
    private TableView<Medicamento> tabla;
    @FXML
    private TableColumn<Medicamento, Integer> columnaId;
    @FXML
    private TableColumn<Medicamento, String> columnaNombre;
    @FXML
    private TableColumn<Medicamento, String> columnaCompuesto;
    @FXML
    private TableColumn<Medicamento, Double> columnaPrecio;
    @FXML
    private TableColumn<Medicamento, Integer> columnaCantidad;

    // ListView
    @FXML private ListView<?> LV1;
    
    /**
     * Inicializa el controlador.
     *
     * @throws SQLException Si ocurre un error al conectar con la base de datos.
     */
    @FXML
    private void initialize() throws SQLException {
        dbMedicamento = new SQLMedicamento();
        
        columnaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaCompuesto.setCellValueFactory(new PropertyValueFactory<>("compuesto"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        
        this.cargarDatos();
    }

    // =======================
    // MÉTODOS DE PANEL
    // =======================
    @FXML
    private void cambiarAgregar() {
        PAgregar.setVisible(true);
        PEliminar.setVisible(false);
        PModificar.setVisible(false);
    }

    @FXML
    private void cambiarEliminar() {
        PAgregar.setVisible(false);
        PEliminar.setVisible(true);
        PModificar.setVisible(false);
    }

    @FXML
    private void cambiarEditar() {
        PAgregar.setVisible(false);
        PEliminar.setVisible(false);
        PModificar.setVisible(true);
    }

    @FXML
    private void regreso() throws IOException {
        // Nombre del FXML de la ventana anterior
        String fxml = "admin"; 

        // Cargar el archivo FXML desde recursos
        File fxmlFile = new File("src/main/resources/scenes/" + fxml + ".fxml");
        Parent root = FXMLLoader.load(fxmlFile.toURI().toURL());

        // Obtener la ventana actual
        Stage stage = (Stage) BRegreso.getScene().getWindow();

        // Cambiar la escena
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }

    // =======================
    // MÉTODOS CRUD
    // =======================
    @FXML
    private void crearMedicamento() {
        try {
            if (!this.validarCamposIngreso()) {
                throw new Exception("Todos los campos deben estar llenos");
            }
            
            String nombre = txtNombre.getText();
            String compuesto = txtCompuesto.getText();
            String precio = txtPrecio.getText();
            String cantidad = txtCantidad.getText();
            
            Double precioNum = this.validarNumeroPositivoDouble(precio);
            Integer cantidadNum = this.validarNumeroPositivoEntero(cantidad);
            
            if(this.dbMedicamento.agregarMedicamento(nombre, compuesto, precioNum, cantidadNum)){
                this.mostrarAlerta( "Medicamento agregada correctamente");
                
                txtNombre.setText("");
                txtCompuesto.setText("");
                txtPrecio.setText("");
                txtCantidad.setText("");
                
                this.cargarDatos();
            } else {
                this.mostrarAlerta( "ERROR- No se pudo agregar");
            }
        } catch (Exception e) {
            this.mostrarAlerta(e.getMessage());
        }
    }

    @FXML
    private void eliminarMedicamento() {
        String id = txtIdEliminar.getText().trim();
        
        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo");
        }

        try {
            Integer idNum = this.validarNumeroPositivoEntero(id);

            if (this.dbMedicamento.eliminarMedicamento(idNum)) {

                this.mostrarAlerta("Eliminado correctamente");
                txtIdEliminar.setText("");
                
                this.cargarDatos();
            } else {
                this.mostrarAlerta("ERROR- No se pudo eliminar");
            }
        } catch (Exception e) {
            this.mostrarAlerta(e.getMessage());
        }
    }

    @FXML
    private void modificarNombre() {
        String id = txtId.getText().trim();
        String nuevoNombre = txtNuevoNombre.getText().trim();

        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Id");
        }
        if(nuevoNombre.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Nombre");
        }

        try {
            Integer idNum = this.validarNumeroPositivoEntero(id);

            if (this.dbMedicamento.cambiarNombre(idNum, nuevoNombre)) {

                this.mostrarAlerta("Modificado correctamente");
                
                txtIdEliminar.setText("");
                this.txtNuevoNombre.setText("");
                
                this.cargarDatos();
            } else {
                this.mostrarAlerta("ERROR- No se pudo modificar");
            }
        } catch (Exception e) {
            this.mostrarAlerta(e.getMessage());
        }
    }

    @FXML
    private void modificarCompuesto() {
        String id = txtId.getText().trim();
        String nuevoCompuesto = txtNuevoCompuesto.getText().trim();

        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Id");
        }
        if(nuevoCompuesto.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Compuesto");
        }

        try {
            Integer idNum = this.validarNumeroPositivoEntero(id);

            if (this.dbMedicamento.cambiarCompuesto(idNum, nuevoCompuesto)) {

                this.mostrarAlerta("Modificado correctamente");
                
                txtIdEliminar.setText("");
                this.txtNuevoCompuesto.setText("");
                
                this.cargarDatos();
            } else {
                this.mostrarAlerta("ERROR- No se pudo modificar");
            }
        } catch (Exception e) {
            this.mostrarAlerta(e.getMessage());
        }
    }

    @FXML
    private void modificarPrecio() {
        String id = txtId.getText().trim();
        String nuevoPrecio = txtNuevoPrecio.getText().trim();

        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Id");
        }
        if(nuevoPrecio.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Precio");
        }

        try {
            Integer idNum = this.validarNumeroPositivoEntero(id);
            Double precioNum = this.validarNumeroPositivoDouble(nuevoPrecio);

            if (this.dbMedicamento.cambiarPrecio(idNum, precioNum)) {

                this.mostrarAlerta("Modificado correctamente");
                
                txtIdEliminar.setText("");
                this.txtNuevoPrecio.setText("");
                
                this.cargarDatos();
            } else {
                this.mostrarAlerta("ERROR- No se pudo modificar");
            }
        } catch (Exception e) {
            this.mostrarAlerta(e.getMessage());
        }
    }

    @FXML
    private void modificarCantidad() {
        String id = txtId.getText().trim();
        String nuevaCantidad = txtNuevaCantidad.getText().trim();

        if(id.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Id");
        }
        if(nuevaCantidad.isBlank()){
            this.mostrarAlerta("Ingrese un valor en el campo Cantidad");
        }

        try {
            Integer idNum = this.validarNumeroPositivoEntero(id);
            Integer cantidadNum = this.validarNumeroPositivoEntero(nuevaCantidad);

            if (this.dbMedicamento.cambiarCantidad(idNum, cantidadNum)) {

                this.mostrarAlerta("Modificado correctamente");
                
                txtIdEliminar.setText("");
                this.txtNuevaCantidad.setText("");
                
                this.cargarDatos();
            } else {
                this.mostrarAlerta("ERROR- No se pudo modificar");
            }
        } catch (Exception e) {
            this.mostrarAlerta(e.getMessage());
        }
    }
    
    
    // =======================
    // MÉTODOS DE VALIDACION
    // =======================
    
    /**
     * Verifica si los campos para ingresar un medicamento están llenos.
     *
     * @return {@code true} si todos campos contienen texto, {@code false} en
     * caso contrario.
     */
    private boolean validarCamposIngreso() {
        return !this.txtNombre.getText().trim().isEmpty() 
                && !this.txtCompuesto.getText().trim().isEmpty()
                && !this.txtCantidad.getText().trim().isEmpty() 
                && !this.txtPrecio.getText().trim().isEmpty();
    }
    
    /**
     * Convierte un texto en un número de tipo Double y verifica que sea
     * positivo.
     * Lanza excepciones específicas según el tipo de error:
     * 
     * NumberFormatException si el texto no es un número válido.
     * IllegalArgumentException si el número es menor o igual a cero.
     *
     * @param texto El texto que se desea validar y convertir.
     * @return El valor numérico convertido a Double si es mayor que cero.
     * @throws NumberFormatException si el texto no representa un número válido.
     * @throws IllegalArgumentException si el número es menor o igual a cero.
     */
    private Double validarNumeroPositivoDouble(String texto) {
        double valor;
        try {
            valor = Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ingrese un número válido");
        }

        if (valor <= 0) {
            throw new IllegalArgumentException("El número debe ser mayor que cero");
        }

        return valor;
    }

    
    /**
     * Convierte un texto en un número de tipo Entero y verifica que sea
     * positivo.
     * Lanza excepciones específicas según el tipo de error:
     * 
     * NumberFormatException si el texto no es un número válido.
     * IllegalArgumentException si el número es menor o igual a cero.
     *
     * @param texto El texto que se desea validar y convertir.
     * @return El valor numérico convertido a Entero si es mayor que cero.
     * @throws NumberFormatException si el texto no representa un número válido.
     * @throws IllegalArgumentException si el número es menor o igual a cero.
     */
    private Integer validarNumeroPositivoEntero(String texto) {
        Integer valor;
        try {
            valor = Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Ingrese un número válido");
        }

        if (valor <= 0) {
            throw new IllegalArgumentException("El número debe ser mayor que cero");
        }

        return valor;
    }


    /**
     * Muestra una alerta de advertencia con un mensaje específico.
     *
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("EVISO");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage stage = (Stage) this.txtNombre.getScene().getWindow();
        alert.initOwner(stage); // Establecer como ventana padre
        alert.showAndWait();
    }
    
    
    private void cargarDatos() {
        try {
            // 1. Obtener los medicamentos desde la BD
            List<Medicamento> medicamentos = this.dbMedicamento.cargarMedicamentos();

            // 3. Crear un ObservableList y asignarlo a la tabla
            tabla.setItems(FXCollections.observableArrayList(medicamentos));

        } catch (Exception e) {
            this.mostrarAlerta("ERROR Carga de datos: " + e.getMessage());
        }
    }
}
