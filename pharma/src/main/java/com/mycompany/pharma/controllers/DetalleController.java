package com.mycompany.pharma.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;


import com.mycompany.pharma.model.DetalleV;
import com.mycompany.pharma.model.Session;
import java.util.List;
import static javafx.collections.FXCollections.observableArrayList;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lib.SQLDetalle;

/**
 * Controlador para la vista DetalleV.
 * Se encarga de manejar la tabla de productos
 * y el botón de regreso.
 */
public class DetalleController{

    /** Tabla que muestra los productos del detalle */
    @FXML
    private TableView<DetalleV> tablaDetalles;

    /** Columnas de la tabla */
    @FXML
    private TableColumn<DetalleV, Integer> columnaId;

    @FXML
    private TableColumn<DetalleV, Integer> columnaCantidad;

    @FXML
    private TableColumn<DetalleV, Double> columnaSubtotal;

    /** Botón de regreso */
    @FXML
    private Button btnRegreso;
    
    /**
     * Conexion a BD
     */
    private SQLDetalle dbDetalle;
    

    /**
     * Método que se ejecuta automáticamente
     * al cargar el FXML.
     */
    public void initialize() {
        dbDetalle = new SQLDetalle(Session.getConnection());

        // Asignar propiedades del modelo a las columnas
        columnaId.setCellValueFactory(new PropertyValueFactory<>("idProducto"));
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        this.cargarDatos();
    }

    /**
     * Método que se ejecuta al presionar el botón regreso.
     */
    @FXML
    private void regreso() {
        Stage stage = (Stage)btnRegreso.getScene().getWindow();
        stage.close();
    }
    
    private void cargarDatos(){
        int id_venta = Session.getId_Venta();
        
        try {
            List<DetalleV> detalles = dbDetalle.cargarDetalles(id_venta);
            this.tablaDetalles.setItems(FXCollections.observableArrayList(detalles));
        } catch (Exception e) {
            mostrarAlerta(e.getMessage());
        }
    }
    
    // ===================================
    // ALERTA
    // ===================================
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("AVISO");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
